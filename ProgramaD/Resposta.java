public class Resposta extends Comunicado {
    private Integer resultado;

    public Resposta(Integer resultado) {
        this.resultado = resultado;
    }

    public Integer getResultado() {
        return resultado;
    }
}
