public class Main {
    public static void main(String[] args) {
        GerenciadorMemoria gerenteMemoria = new GerenciadorMemoria(10);
        GeradorRequisicoes gerador = new GeradorRequisicoes();
        RequisicaoMemoria req1 = gerador.gerar();
        String string = req1.toString();
        System.out.println(string);

        RequisicaoMemoria req2 = gerador.gerar();
        String string2 = req2.toString();
        System.out.println(string2);

        gerenteMemoria.alocar(req1);
        gerenteMemoria.alocar(req2);
        String gerente = gerenteMemoria.toString();
        System.out.println(gerente);
    }
}