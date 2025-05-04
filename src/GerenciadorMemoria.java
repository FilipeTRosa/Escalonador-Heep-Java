import java.util.LinkedList;
import java.util.Queue;

public class GerenciadorMemoria {
    private int[] heap;
    private Queue<RequisicaoMemoria> filaFIFO;

    public GerenciadorMemoria(int tamanhoKB) {
        int totalInteiros = (tamanhoKB * 1024) / 4;
        this.heap = new int[totalInteiros];
        this.filaFIFO = new LinkedList<>();
    }

    public boolean alocar(RequisicaoMemoria requisicao) {
        int tamanho = requisicao.getTamanho();  // Tamanho da requisição em inteiros
        // Percorre a heap para encontrar o primeiro espaço contíguo que seja grande o suficiente
        for (int i = 0; i < heap.length - tamanho; i++) {
            boolean podeAlocar = true;
            // Verifica se o espaço de tamanho necessário está livre
            for (int j = 0; j < tamanho; j++) {
                if (heap[i + j] != 0) { // Se o espaço já estiver ocupado
                    podeAlocar = false;
                    break;
                }
            }
            // Se encontrou um espaço contíguo suficiente
            if (podeAlocar) {
                // Aloca a requisição (marca a área como ocupada)
                for (int j = 0; j < tamanho; j++) {
                    heap[i + j] = requisicao.getId();  // coloca o id nas posicoes ocupadas
                }
                // Adiciona a requisição à fila FIFO para liberar depois
                filaFIFO.offer(requisicao);
                return true; // Retorna verdadeiro indicando que a alocação foi bem-sucedida
            }
        }
        return false;  // Não há espaço suficiente disponível
    }

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

        // Adiciona quebra de linha final se necessário
        if (heap.length % 64 != 0) {
            sb.append("\n");
        }

        return sb.toString();
    }

}
