import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProgramaR {

    public static void main(String[] args) {
        try {
            ServerSocket pedido = new ServerSocket(12345);

            while (true) {
                Socket conexao = pedido.accept();
                ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
                ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());
                System.out.println("Conectado!");
                // Receber Pedido
                Pedido pedidoRecebido = (Pedido) receptor.readObject();
                // Processar Pedido e criar Resposta
                Integer resultado = pedidoRecebido.getNumeroPosicao();
                Resposta resposta = new Resposta(resultado);
                // Enviar Resposta
                transmissor.writeObject(resposta);

                // Fechar transmissor, receptor e conexão
                transmissor.close();
                receptor.close();
                conexao.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}