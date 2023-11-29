import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Distribute {

    public static void main(String[] args) {
        String[] ips = {"172.16.20.139", "172.16.21.132"}; // ips dos pc daqui do lado

        int[] vetor = Vector.populateVector(10);

        int numPartes = ips.length;

        int tamanhoParte = vetor.length / numPartes;

        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Qual numero vai procurar: ");
            int numeroProcurado = Integer.parseInt(teclado.readLine());

            for (int i = 0; i < numPartes; i++) {
                try {
                    int inicio = i * tamanhoParte;
                    int fim = (i == numPartes - 1) ? vetor.length : (i + 1) * tamanhoParte;

                    int[] parteVetor = new int[fim - inicio];
                    for (int j = inicio; j < fim; j++) {
                        parteVetor[j - inicio] = vetor[j];
                    }

                    Socket conexao = new Socket(ips[i], 25565);
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
                        System.out.println("Número encontrado em " + ips[i]);
                    }

                    // fecha tudo
                    transmissor.close();
                    receptor.close();
                    conexao.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
