import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador de Memória Simulado
 *
 * Este projeto simula a alocação de memória usando uma política First-Fit em uma heap representada por um vetor de inteiros.
 * Quando a memória está cheia, 30% da heap é liberada com uma política FIFO.
 *
 * Funcionalidades:
 * - Geração de requisições de memória de tamanho aleatório
 * - Alocação First-Fit
 * - Liberação de memória usando FIFO (30%)
 * - Estatísticas detalhadas: requisições, desalocações, tempo de execução, fragmentação
 * - Visualização gráfica simples do estado da heap
 *
 * Desenvolvido por: [Bianca Durgante, Davi Lemos e Filipe Teixeira]
 * Data: [Em andamento]
 * Curso: Engenharia da Computação – UNIPAMPA
 */

/**
 * Classe principal para testar o Gerenciador de Memória.
 * Gera um lote de requisições, tenta alocar, e imprime estatísticas ao final.
 */
public class Main {
    public static void main(String[] args) {
        GerenciadorMemoria gerenteMemoria = new GerenciadorMemoria(200);
        GeradorRequisicoes gerador = new GeradorRequisicoes();
      /*  RequisicaoMemoria req1 = gerador.gerar();
        String string = req1.toString();
        System.out.println(string);

        RequisicaoMemoria req2 = gerador.gerar();
        String string2 = req2.toString();
        System.out.println(string2);

        gerenteMemoria.alocar(req1);
        gerenteMemoria.alocar(req2);
        String gerente = gerenteMemoria.toString();
        System.out.println(gerente);
        */
        gerenteMemoria.getTamanho();
        List<RequisicaoMemoria> lote = gerador.gerarLote(10000);
        long inicio = System.currentTimeMillis();
        for (RequisicaoMemoria r : lote) {
            gerenteMemoria.alocarFirstFit(r);
        }
        long fim = System.currentTimeMillis();
        gerenteMemoria.setTempoExecucao(fim - inicio);

        String gerente = gerenteMemoria.toString();
        System.out.println(gerente);
        gerenteMemoria.imprimirEstatisticas();
        System.out.printf("Fragmentação final da heap: %.2f%%\n", gerenteMemoria.calcularFragmentacaoTotal());
    }
}