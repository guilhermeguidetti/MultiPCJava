public class Resposta extends Comunicado {
    private Vector2<Integer> resultado;

    public Resposta(Vector2<Integer> resultado) {
        this.resultado = resultado;
    }

    public Object getResultado() {
        return resultado.clone();
    }
}