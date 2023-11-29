import java.io.Serializable;
 
public class Pedido extends Comunicado {
    private int[] vetorInt;
    private int numero;

    public Pedido(int[] vetorInt, int numero) {
        this.vetorInt = vetorInt;
        this.numero = numero;
        System.out.println("Novo Pedido!");
    }
 
    public Integer getNumeroPosicao() {
        for (int i = 0; i < vetorInt.length; i++) {
            if (vetorInt[i] == numero) {
                return i;
            }
        }
        return null;
    }
}