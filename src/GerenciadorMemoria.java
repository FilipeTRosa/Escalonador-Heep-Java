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

    /**
     * Cria um gerenciador de memória com uma heap de tamanho especificado em kilobytes.
     * @param tamanhoKB Tamanho da heap em kilobytes.
     */
    public GerenciadorMemoria(int tamanhoKB) {
        int totalInteiros = (tamanhoKB * 1024) / 4;
        this.heap = new int[totalInteiros];
        this.filaFIFO = new LinkedList<>();
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

        return false;
    }

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
                (totalRequisicoesGeradas > 0 ? somaTamanhosRequisicoes / totalRequisicoesGeradas : 0));
        System.out.println("Requisições desalocadas (FIFO): " + totalRequisicoesDesalocadas);
        System.out.println("Tamanho médio das desalocações: " +
                (totalRequisicoesDesalocadas > 0 ? somaTamanhosDesalocadas / totalRequisicoesDesalocadas : 0));
        System.out.println("Tempo total de execução: " + tempoExecucaoMs + " ms");
    }

}
