import java.util.ArrayList;
import java.util.Collections;

public class Vector {
    private static final int TAMANHO_INICIAL = 10;

    private int[] elem;
    private int qtd;

    public Vector() {
        this.elem = new int[TAMANHO_INICIAL];
        this.qtd = 0;
    }

    public Vector(int tamanho) {
        this.elem = new int[tamanho];
        this.qtd = 0;
    }

    public int size() {
        return this.qtd;
    }

    private void redimensioneSe(float taxaDeRedim) {
        int[] novo;
        novo = new int[Math.round(this.elem.length * taxaDeRedim)];

        for (int i = 0; i < this.qtd; i++)
            novo[i] = this.elem[i];

        this.elem = novo;
    }

    public void add(int x) {
        if (this.qtd == this.elem.length)
            this.redimensioneSe(2.0F);

        this.elem[this.qtd] = x;
        this.qtd++;
    }

    public int get(int posicao) {
        if (posicao < 0 || posicao > this.qtd - 1)
            throw new java.lang.ArrayIndexOutOfBoundsException(posicao);

        return this.elem[posicao];
    }

    public void remove(int posicao) throws ArrayIndexOutOfBoundsException {
        if (posicao < 0 || posicao > this.qtd - 1)
            throw new java.lang.ArrayIndexOutOfBoundsException(posicao);

        for (int i = posicao + 1; i < this.qtd; i++)
            this.elem[i - 1] = this.elem[i];

        this.qtd--;
        this.elem[this.qtd] = 0;

        if (this.elem.length > TAMANHO_INICIAL && this.qtd <= Math.round(this.elem.length * 0.25F))
            this.redimensioneSe(0.5F);
    }

    public String toString() {
        String ret = "[";

        for (int i = 0; i < this.qtd; i++)
            ret += this.elem[i] + (i == this.qtd - 1 ? "" : ",");

        return ret + "]";
    }

    public static int[] populateVector(int vectorSize) {
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= vectorSize; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        int[] vector = new int[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            vector[i] = numbers.get(i);
        }

        return vector;
    }
}