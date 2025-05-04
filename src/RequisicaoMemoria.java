import java.util.concurrent.atomic.AtomicInteger;


public class RequisicaoMemoria {
    private static final AtomicInteger contadorId = new AtomicInteger(1);

    private int id;
    private int tamanho;
    private long timestamp;

    public RequisicaoMemoria(int tamanho) {
        this.id = contadorId.getAndIncrement();
        this.tamanho = tamanho;
        this.timestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public int getTamanho() {
        return tamanho;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "RequisicaoMemoria{" +
                "id=" + id +
                ", tamanho=" + tamanho +
                ", timestamp=" + timestamp +
                '}';
    }

}