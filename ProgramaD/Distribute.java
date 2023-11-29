import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Distribute {

    public static void main(String[] args) {
        String[] ips = {"172.16.20.139", "172.16.21.132"}; // ips dos pc daqui do lado

        // Supondo que você já tenha um vetor criado
        int[] vetor = Vector.populateVector(10);

        // Número de partes para dividir o vetor
        int numPartes = ips.length;

        // Tamanho de cada parte
        int tamanhoParte = vetor.length / numPartes;

        // Usar Scanner para obter o número a ser procurado
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o número que deseja procurar: ");
        int numeroProcurado = scanner.nextInt();

        for (int i = 0; i < numPartes; i++) {
            try {
                // Calcula o índice inicial e final para a parte atual
                int inicio = i * tamanhoParte;
                int fim = (i == numPartes - 1) ? vetor.length : (i + 1) * tamanhoParte;

                // Obtém a parte atual do vetor
                int[] parteVetor = Arrays.copyOfRange(vetor, inicio, fim);

                // Estabelecer conexão com o programa R no computador de IP "ip"
                Socket conexao = new Socket(ips[i], 12345);
                ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
                ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());

                // enviar pedido
                Pedido pedido = new Pedido(parteVetor, numeroProcurado);
                transmissor.writeObject(pedido);
                System.out.println("Pedido enviado para " + ips[i]);

                // pegar resposta
                Resposta resposta = (Resposta) receptor.readObject();

                // ver e achou e printar
                Integer achou = resposta.getResultado();
                if (achou != null) {
                    System.out.println("Número encontrado" + " em " + ips[i]);
                }

                // fecha tudo
                transmissor.close();
                receptor.close();
                conexao.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Fechar o Scanner
        scanner.close();
    }
}
