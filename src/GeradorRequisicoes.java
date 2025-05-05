import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gera requisições de memória com tamanho aleatório dentro de um intervalo.
 */
public class GeradorRequisicoes {
    private final Random random = new Random();
    /**
     * Gera uma nova requisição de memória com tamanho aleatório.
     */
    public RequisicaoMemoria gerar() {
        int tamanho = 4 + random.nextInt(253); // [4, 256]
        return new RequisicaoMemoria(tamanho);
    }
    /**
     * Gera um lote de requisições de memória com tamanho aleatório. A quantidade de requisições
     * por lote será informada pelo usuario.
     */
    public List<RequisicaoMemoria> gerarLote(int n) {
        List<RequisicaoMemoria> lote = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lote.add(gerar());
        }
        return lote;
    }
}