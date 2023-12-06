import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


// 21011817 - Ariane Paula Barros
public class Programa
{
    public static void popularAleatoriamente(Vector2<Integer> v) {
        Random generator = new Random(666);      // lucky number
        int novoValor = -1;
        while (v.getOccupied() != v.getTotalSize() && !v.contains(novoValor)) {
            novoValor = generator.nextInt(100);
            v.add(novoValor);
        }
    }
    public static void main (String[] args)
    { 
        try
        { 
            ServerSocket pedido = new ServerSocket(12345);

            while (true) {
                Socket conexao = pedido.accept();
                ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
                ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());
                System.out.println("Conectado!");
                // Receber Pedido
                
                int numberOfProcessors = Runtime.getRuntime().availableProcessors ();
                System.out.printf("Número de processadores (logo, número de threads): %d%n", numberOfProcessors);
                Vector2<Integer> armazenamento = ((Pedido) receptor.readObject()).getV();
                
                System.out.println ("Tecle ENTER para ativar as tarefas e");
                System.out.println ("Tecle novamente ENTER para terminar o programa.");
                Teclado.getUmString();

                System.out.println ("Tecla S se quiser exibir o conteúdo do vetor");
                String opcao = Teclado.getUmString();

                if (opcao.toUpperCase().equals("S")) {
                    System.out.println(armazenamento.toString());
                }
                Vector2<Integer> ordenado = new Vector2<>();
                if (numberOfProcessors >= 2) {
                    Processador.libera();
                    Processador.libera();
                    int metade = armazenamento.getOccupied()/2;
                    Vector2<Integer> chunk2 = new Vector2<>(armazenamento.slice(0, metade));
                    System.out.printf("%nVetor 1: %s%n%n", chunk2);
                    ThreadOrdena t1 = new ThreadOrdena(chunk2);
                    chunk2 = new Vector2<>(armazenamento.slice(metade, armazenamento.getOccupied()));
                    System.out.printf("%nVetor 2: %s%n%n", chunk2);
                    ThreadOrdena t2 = new ThreadOrdena(chunk2);
                    t1.start();
                    t2.start();
                    t1.join();
                    t2.join();
                    Vector2<Integer> v1 = (Vector2<Integer>) t1.armazenamento.clone();
                    Vector2<Integer> v2 = (Vector2<Integer>) t2.armazenamento.clone();
                    System.out.printf("Vetor 1: %s%n", v1.toString());
                    System.out.printf("Vetor 2: %s%n", v2.toString());

                    while(v1.getOccupied() > 0 && v2.getOccupied() > 0) {
                        if (v1.get(0) > v2.get(0)) {//precisamos ordenar
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

                    System.out.printf("FINAL: %s%n", ordenado.toString());

                }
                
                System.out.println ("Execução do programa finalizada.");

                transmissor.writeObject(ordenado);

                // Fechar transmissor, receptor e conexão
                transmissor.close();
                receptor.close();
                conexao.close();
            }
        }
        catch (Exception erro)
        {
            System.out.printf("ERRO %s", erro);
        } // sei que não passei null para o construtor de nenhuma das tarefas
    }
}
