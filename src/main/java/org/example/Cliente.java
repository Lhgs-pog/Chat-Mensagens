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

            //Enviar mensagem
            System.out.println("Escreva uma mensagem: ");
            String mensagem = teclado.readLine();
            saida.println(mensagem);

            //Receber resposta
            String resposta = entrada.readLine();
            System.out.println("O servidor respondeu: "+resposta);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
