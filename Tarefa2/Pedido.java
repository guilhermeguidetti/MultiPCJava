public class Pedido extends Comunicado {
    private Vector2<Integer> vetor;

    public Pedido(Vector2<Integer> vetor) {
        this.vetor = vetor;
    }

    public Vector2<Integer> getV() {
        return (Vector2<Integer>)this.vetor.clone();
    }
}