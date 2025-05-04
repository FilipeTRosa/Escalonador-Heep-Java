import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorRequisicoes {
    private final Random random = new Random();

    // Gera uma única requisição com tamanho entre 4 e 256 inteiros
    public RequisicaoMemoria gerar() {
        int tamanho = 4 + random.nextInt(253); // [4, 256]
        return new RequisicaoMemoria(tamanho);
    }

    // Gera um lote com 'n' requisições
    public List<RequisicaoMemoria> gerarLote(int n) {
        List<RequisicaoMemoria> lote = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            lote.add(gerar());
        }
        return lote;
    }
}