package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {

        //Porta do servidor
        int port = 12345;

        try(ServerSocket serve = new ServerSocket(port)){
            System.out.println("Servidor iniciado na porta: "+port);

            //Espera um cliente se conectar
            Socket socket = serve.accept();
            System.out.println("Cliente conectado:" +socket.getInetAddress());

            //Entrada
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            //Saida
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);


            //Recebendo a mensagem do cliente
            String mensagem = input.readLine();
            System.out.println("Mensagem do cliente: "+mensagem);

            //Respondendo a menssagem do cliente
            output.println("Mensagem recebida: "+ mensagem);


            socket.close(); //Encerra a conexão
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}