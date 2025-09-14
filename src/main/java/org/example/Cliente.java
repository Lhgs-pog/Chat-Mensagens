package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static void conectar(){

        //Informações do servidor
        String host = "localhost"; //Endereço
        int port = 12345; //Porta

        try(Socket socket = new Socket(host, port)){

            //Streams
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            new Thread(() -> {
               try{
                   String resposta;
                   while((resposta = entrada.readLine()) != null){
                       System.out.println("[Servidor]: "+resposta);
                   }
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
            }).start();

            //Enviar mensagem
            System.out.println("Escreva uma mensagem: ");
            String mensagem = teclado.readLine();
            saida.println(mensagem);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
