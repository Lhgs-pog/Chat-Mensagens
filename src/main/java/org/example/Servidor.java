package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String[] args) {

        //Porta do servidor
        int port = 12345;

        //Executor que gerencia os threads(melhor prática já que vc não precisa criar manualmente)
        ExecutorService pool = Executors.newCachedThreadPool();

        try(ServerSocket serve = new ServerSocket(port)){
            System.out.println("Servidor iniciado na porta: "+port);

            while (true){
                //Espera um cliente se conectar
                Socket socket = serve.accept();
                System.out.println("Cliente conectado:" +socket.getInetAddress());

                pool.execute(new AtenderCliente(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class AtenderCliente implements Runnable{

    public Socket socket;

    public AtenderCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {


        try (
            //Entrada
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            //Saida
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String mensagem;
            while ((mensagem = input.readLine()) != null){
                System.out.println("["+socket.getInetAddress()+"]: "+mensagem);
                output.println("Eco: "+mensagem);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
