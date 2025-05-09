package simulador.memoria;

/**
 * Simula a gestão de uma heap de memória com alocação e desalocação de requisições.
 */
public class GerenciadorMemoria {
    private HeapMemoria heap;
    private FilaFIFORequisicoes fila;
    private EstatisticasMemoria stats;

    public GerenciadorMemoria(int tamanhoKB) {
        this.heap = new HeapMemoria(tamanhoKB);
        this.fila = new FilaFIFORequisicoes();
        this.stats = new EstatisticasMemoria();
    }

    public void resetarHeap() {
        heap.resetar();
        stats.resetarStats();
    }

    public boolean alocar(RequisicaoMemoria req, boolean comDesfragmentacao) {
        stats.novaRequisicao(req.getTamanho());

        boolean sucesso = heap.alocarFirstFit(req.getId(), req.getTamanho());
        if (sucesso) {
            fila.adicionar(req);
            return true;
        }

        stats.incrementarDesalocadas(fila.liberarMemoria(heap, heap.getTamanho(), 30));

        if (comDesfragmentacao && heap.isFragmentacaoCritica(req.getTamanho())) {
            heap.desfragmentar();
            stats.incrementarDesfragmentacoes();
        }

        sucesso = heap.alocarFirstFit(req.getId(), req.getTamanho());
        if (sucesso) {
            fila.adicionar(req);
            return true;
        }

        stats.incrementarFalhas();
        return false;
    }

    public void setTempoExecucao(long tempo) {
        stats.setTempoExecucao(tempo);
    }

    public void imprimirEstatisticas() {
        stats.imprimir();
    }

    @Override
    public String toString() {
        return heap.representarAscii();
    }

    public double calcularFragmentacaoTotal() {
        return heap.calcularFragmentacao();
    }

    public int tamanhoHeap (){ return heap.getTamanho();}
}
