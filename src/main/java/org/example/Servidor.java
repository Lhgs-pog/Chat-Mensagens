package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {

    //Lista de todos os clientes conectados
    private static final Set<PrintWriter> clientes = ConcurrentHashMap.newKeySet();

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

    static class AtenderCliente implements Runnable{

        private Socket socket;
        private PrintWriter saida;

        public AtenderCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                //Entrada
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
            ) {

                //Saida
                saida = new PrintWriter(socket.getOutputStream(), true);
                clientes.add(saida);

                String mensagem;
                while ((mensagem = entrada.readLine()) != null){
                    System.out.println("["+socket.getInetAddress()+"]: "+mensagem);
                    enviarPAraTodos(mensagem);
                }

            } catch (IOException e) {
                System.out.println("Cliente desconectado: "+socket.getInetAddress());
            } finally {
                if (saida != null){
                    clientes.remove(saida);
                }
                try{
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        public void enviarPAraTodos(String mensagem){
            for(PrintWriter cliente : clientes){
                cliente.println(mensagem);
            }
        }
    }
}

