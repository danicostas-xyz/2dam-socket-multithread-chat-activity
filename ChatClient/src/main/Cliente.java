package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
public static final int PUERTO = 2018;
public static final String IP_SERVER = "localhost";

public static void main(String[] args) {
	System.out.println("        APLICACIÓN CLIENTE         ");
	System.out.println("-----------------------------------");

	InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
	
	 try {
         System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
         Socket socketAlServidor = new Socket();
         socketAlServidor.connect(direccionServidor);
         System.out.println("CLIENTE: Conexión establecida... a " + IP_SERVER +
                            " por el puerto " + PUERTO);

         // Hilo para leer mensajes del servidor
         Thread lectorServidor = new Thread(() -> {
             try {
                 InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
                 BufferedReader entradaBuffer = new BufferedReader(entrada);

                 String mensajeServidor = "";
                 while (true) {
                     if(entradaBuffer.ready()) {
                         mensajeServidor = entradaBuffer.readLine();
                         System.out.println(mensajeServidor);
                     }
                 }
             } catch (IOException e) {
                 System.out.println(":::Conexión cerrada por el servidor.");
             }
         });

         // Hilo para escribir mensajes al servidor
         Thread escritorServidor = new Thread(() -> {
        	 Scanner sc = new Scanner(System.in);
             try {
            	 
                 PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
                 String texto;
                 while (true) {
                     texto = sc.nextLine();

                     salida.println(texto); // Enviar mensaje al servidor
                     if ("FIN".equalsIgnoreCase(texto)) {
                         System.out.println(":::Cerrando conexión...");
                         break; // Salir del bucle y cerrar conexión
                     }
                 }
                 // Cerrar el socket al terminar
                 socketAlServidor.close();
                 System.out.println(":::Conexión cerrada por el servidor.");
             } catch (IOException e) {
                 System.out.println("CLIENTE: Error al enviar datos al servidor.");
             }
         });

         // Iniciar ambos hilos
         lectorServidor.start();
         escritorServidor.start();

       

     } catch (UnknownHostException e) {
         System.err.println("CLIENTE: No encuentro el servidor en la dirección " + IP_SERVER);
         e.printStackTrace();
     } catch (IOException e) {
         System.err.println("CLIENTE: Error de entrada/salida");
         e.printStackTrace();
     } catch (Exception e) {
         System.err.println("CLIENTE: Error -> " + e);
         e.printStackTrace();
     }
 }
}
