import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

public class Distribute {

    public static void popularAleatoriamente(Vector2<Integer> v) {
        Random generator = new Random(666);      // lucky number
        int novoValor = -1;
        while (v.getOccupied() != v.getTotalSize() && !v.contains(novoValor)) {
            novoValor = generator.nextInt(1000);
            v.add(novoValor);
        }
    }

    public static Vector2<Integer> ordenadarPar(Vector2<Integer> v1, Vector2<Integer> v2) {
        Vector2<Integer> ordenado = new Vector2<>();
        while(v1.getOccupied() > 0 && v2.getOccupied() > 0) {
            if (v1.get(0) > v2.get(0)) {
                ordenado.add(v2.get(0));
                v2.remove(0);
            } else {
                ordenado.add(v1.get(0));
                v1.remove(0);
            }
        }
        // os 2 vetores devem estar ordenados internamente
        while (v1.getOccupied() > 0) {
            ordenado.add(v1.get(0));
            v1.remove(0);
        }

        while (v2.getOccupied() > 0) {
            ordenado.add(v2.get(0));
            v2.remove(0);
        }

        return ordenado;
    }
    public static void main(String[] args) {
        String[] ips = {"172.17.0.1", }; // ips dos pc daqui do lado
        Vector2<Integer> vetor = new Vector2<>();
        popularAleatoriamente(vetor);

        int numPartes = ips.length;

        int tamanhoParte = vetor.getOccupied() / numPartes;

        try (BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {
            Vector2<Object> desordenado = new Vector2<>();
            System.out.print("Distribuindo...");
            
            for (int i = 0; i < numPartes; i++) {
                try {
                    int inicio = i * tamanhoParte;
                    int fim = (i == numPartes - 1) ? vetor.getOccupied() : (i + 1) * tamanhoParte;

                    Vector2<Integer> parteVetor = vetor.slice(inicio, fim);

                    Socket conexao = new Socket(ips[i], 12345);
                    ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
                    ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());

                    // enviar pedido
                    Pedido pedido = new Pedido(parteVetor);
                    transmissor.writeObject(pedido);
                    System.out.println("Pedido enviado para " + ips[i]);

                    // pegar resposta
                    desordenado.add((Vector2<Integer>)receptor.readObject());

                    // fecha tudo
                    transmissor.close();
                    receptor.close();
                    conexao.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Vector2<Integer> ordenado = new Vector2<>();
            for (int i = 0; i < desordenado.getOccupied(); i++) {
                Vector2<Integer> v1 = ((Vector2<Integer>)ordenado.clone());
                Vector2<Integer> v2 = ((Vector2<Integer>)desordenado.get(i));
                ordenado = ordenadarPar(v1, v2);
            }
            
            System.out.printf("FINAL: %s%n", ordenado.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}