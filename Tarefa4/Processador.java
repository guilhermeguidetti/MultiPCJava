public class Processador {
    private static int qtd = Runtime.getRuntime().availableProcessors();

    public static boolean libera() {
        if (qtd > 0) {
            qtd--;
            return true;
        }
        return false;
    }

    public static int getQtd() {
        return qtd;
    }
}
