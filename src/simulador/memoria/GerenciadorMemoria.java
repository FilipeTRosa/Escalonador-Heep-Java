package simulador.memoria;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Simula a gestão de uma heap de memória com alocação e desalocação de requisições.
 */

public class GerenciadorMemoria {
    private int[] heap;
    private Queue<RequisicaoMemoria> filaFIFO;
    private int totalRequisicoesGeradas = 0;
    private long somaTamanhosRequisicoes = 0;
    private int totalRequisicoesDesalocadas = 0;
    private long somaTamanhosDesalocadas = 0;
    private long tempoExecucaoMs = 0;
    private int totalAlocacoesComFalha = 0;


    /**
     * Cria um gerenciador de memória com uma heap de tamanho especificado em kilobytes.
     * @param tamanhoKB Tamanho da heap em kilobytes.
     */
    public GerenciadorMemoria(int tamanhoKB) {
        int totalInteiros = (tamanhoKB * 1024) / 4;
        this.heap = new int[totalInteiros];
        this.filaFIFO = new LinkedList<>();
    }

    public void resetarHeap(){
        for (int i = 0; i < heap.length; i++) {
            heap[i] = 0;
        }
        totalRequisicoesGeradas = 0;
        somaTamanhosRequisicoes = 0;
        totalRequisicoesDesalocadas = 0;
        somaTamanhosDesalocadas = 0;
        tempoExecucaoMs = 0;
        totalAlocacoesComFalha = 0;
    }
    public void getTamanho(){
        System.out.println("\n O tamanho da heap em inteiros é:" + heap.length + "\n");
    }

    /**
     * Tenta alocar a requisição na heap. Caso não haja espaço, libera 30% da heap e tenta novamente.
     */
    public boolean alocarFirstFit(RequisicaoMemoria requisicao) {
        totalRequisicoesGeradas++;
        somaTamanhosRequisicoes += requisicao.getTamanho();

        int tamanho = requisicao.getTamanho();
        int id = requisicao.getId();

        // Primeira tentativa
        for (int i = 0; i <= heap.length - tamanho; i++) {
            boolean podeAlocar = true;
            for (int j = 0; j < tamanho; j++) {
                if (heap[i + j] != 0) {
                    podeAlocar = false;
                    break;
                }
            }

            if (podeAlocar) {
                for (int j = 0; j < tamanho; j++) {
                    heap[i + j] = id;
                }
                filaFIFO.offer(requisicao);
                return true;
            }
        }

        // Libera 30% e tenta de novo
        liberar30PorCento();

        // Segunda tentativa
        for (int i = 0; i <= heap.length - tamanho; i++) {
            boolean podeAlocar = true;
            for (int j = 0; j < tamanho; j++) {
                if (heap[i + j] != 0) {
                    podeAlocar = false;
                    break;
                }
            }

            if (podeAlocar) {
                for (int j = 0; j < tamanho; j++) {
                    heap[i + j] = id;
                }
                filaFIFO.offer(requisicao);
                return true;
            }
        }
        totalAlocacoesComFalha++;
        return false;
    }

    /**
     * Tenta alocar a requisição na heap. Caso não haja espaço, libera 30% da heap e tenta novamente.
     * Após liberar 30% desfragmenta a memoria
     */
    public boolean alocarFirstFitDesfragmentando(RequisicaoMemoria requisicao) {
        totalRequisicoesGeradas++;
        somaTamanhosRequisicoes += requisicao.getTamanho();

        int tamanho = requisicao.getTamanho();
        int id = requisicao.getId();

        // Primeira tentativa
        for (int i = 0; i <= heap.length - tamanho; i++) {
            boolean podeAlocar = true;
            for (int j = 0; j < tamanho; j++) {
                if (heap[i + j] != 0) {
                    podeAlocar = false;
                    break;
                }
            }

            if (podeAlocar) {
                for (int j = 0; j < tamanho; j++) {
                    heap[i + j] = id;
                }
                filaFIFO.offer(requisicao);
                return true;
            }
        }

        // Libera 30% e tenta de novo
        liberar30PorCento();
        desfragmentar();

        // Segunda tentativa
        for (int i = 0; i <= heap.length - tamanho; i++) {
            boolean podeAlocar = true;
            for (int j = 0; j < tamanho; j++) {
                if (heap[i + j] != 0) {
                    podeAlocar = false;
                    break;
                }
            }

            if (podeAlocar) {
                for (int j = 0; j < tamanho; j++) {
                    heap[i + j] = id;
                }
                filaFIFO.offer(requisicao);
                return true;
            }
        }
        totalAlocacoesComFalha++;
        return false;
    }
    /**
     * Define o tempo de execução calculado na classe Main
     */
    public void setTempoExecucao(long tempoMs) {
        this.tempoExecucaoMs = tempoMs;
    }

    /**
     * Libera 30% da heap, desalocando requisições da fila FIFO.
     */
    private void liberar30PorCento() {
        int totalParaLiberar = (int) (heap.length * 0.3);
        int liberados = 0;

        while (!filaFIFO.isEmpty() && liberados < totalParaLiberar) {
            RequisicaoMemoria req = filaFIFO.poll();
            int id = req.getId();
            int liberadosPorReq = 0;

            for (int i = 0; i < heap.length; i++) {
                if (heap[i] == id) {
                    heap[i] = 0;
                    liberados++;
                    liberadosPorReq++;
                    if (liberados >= totalParaLiberar) break;
                }
            }

            totalRequisicoesDesalocadas++;
            somaTamanhosDesalocadas += liberadosPorReq;
        }
    }

    /**
     * Desfragmenta a heap reorganizando os blocos ocupados para o início do vetor.
     * Elimina os espaços vazios entre requisições desalocadas, tornando a ocupação
     * da memória contígua e aumentando a chance de alocar requisições maiores.
     *
     * O método cria uma nova heap auxiliar, copia os IDs das posições ocupadas
     * da heap original para as próximas posições livres, e ao final substitui a
     * heap original pela versão desfragmentada.
     */
    private void desfragmentar() {
        int[] novaHeap = new int[heap.length];
        int index = 0; // próxima posição livre na nova heap

        // Percorre a heap atual
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] != 0) {
                novaHeap[index] = heap[i];
                index++;
            }
        }

        heap = novaHeap;
        //System.out.println("Heap desfragmentada com sucesso.");
    }

    /**
     * Retorna a porcentagem da heap que está livre (fragmentada).
     */
    public double calcularFragmentacaoTotal() {
        int livres = 0;
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] == 0) {
                livres++;
            }
        }
        return (livres * 100.0) / heap.length;
    }

    /**
     * Imprime um mapa ASCII da heap, usando '.' para posições livres e o 'ID' para ocupadas.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heap.length; i++) {
            if (heap[i] == 0) {
                sb.append(". ");
            } else {
                sb.append(heap[i]).append(" ");
            }

            if ((i + 1) % 64 == 0) {
                sb.append("\n");
            }
        }
        // Quebra a linha no fim
        if (heap.length % 64 != 0) {
            sb.append("\n");
        }

        return sb.toString();
    }
    /**
     * Imprime as estatísticas de execução do programa.
     */
    public void imprimirEstatisticas() {
        System.out.println("=== Estatísticas ===");
        System.out.println("Total de requisições geradas: " + totalRequisicoesGeradas);
        System.out.println("Tamanho médio das requisições: " +
                (totalRequisicoesGeradas > 0 ? somaTamanhosRequisicoes / totalRequisicoesGeradas : 0) + " inteiros");
        System.out.println("Requisições desalocadas (FIFO): " + totalRequisicoesDesalocadas);
        System.out.println("Tamanho médio das desalocações: " +
                (totalRequisicoesDesalocadas > 0 ? somaTamanhosDesalocadas / totalRequisicoesDesalocadas : 0) + " inteiros");
        System.out.println("Tempo total de execução: " + tempoExecucaoMs + " ms");
        System.out.println("Total de alocações que falharam: " + totalAlocacoesComFalha);
    }
}
