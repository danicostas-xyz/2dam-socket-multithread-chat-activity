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
	System.out.println("===================================");
	System.out.println("       🖥️  APLICACIÓN CLIENTE      ");
	System.out.println("===================================");
	
	InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
	
	 try {
         System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
         Socket socketAlServidor = new Socket();
         socketAlServidor.connect(direccionServidor);
         System.out.println("===================================");
         System.out.println("        📡 CONEXIÓN ESTABLECIDA        ");
         System.out.println("===================================");
         System.out.println("🔗 Servidor: " + IP_SERVER);
         System.out.println("🔌 Puerto: " + PUERTO);
         System.out.println("-----------------------------------");
         
       
         
        

         // Hilo para leer mensajes del servidor
         Thread lectorServidor = new Thread(() -> {
             try {
                 InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
                 BufferedReader entradaBuffer = new BufferedReader(entrada);

                 String mensajeServidor = "";
                 while (true) {
                     System.out.println(entradaBuffer.readLine());
                 }
             } catch (IOException e) {
            	 System.out.println("===================================");
            	 System.out.println("   ❌ CLIENTE: CONEXIÓN CERRADA   ");
            	 System.out.println("===================================");
            	 System.out.println("💔 El servidor ha cerrado la conexión.");
             }
         });

         // Hilo para escribir mensajes al servidor
         Thread escritorServidor = new Thread(() -> {
        	 Scanner sc = new Scanner(System.in);
             try {
            	 
                 PrintStream salida1 = new PrintStream(socketAlServidor.getOutputStream());
                 String texto;
                 while (true) {
                     texto = sc.nextLine();

                     salida1.println(texto); // Enviar mensaje al servidor
                     if ("FIN".equalsIgnoreCase(texto)) {
                    	 System.out.println("=====================================");
                    	 System.out.println("   🔒 CLIENTE: CERRANDO CONEXIÓN   ");
                    	 System.out.println("=====================================");
                    	 System.out.println("🔌 Desconectando del servidor...");
                         break; // Salir del bucle y cerrar conexión
                     }
                 }
                 socketAlServidor.close(); // Cerrar el socket al terminar
             } catch (IOException e) {
            	 System.out.println("===============================================");
            	 System.out.println("   ⚠️ CLIENTE: ERROR AL ENVIAR DATOS AL SERVIDOR   ");
            	 System.out.println("===============================================");
            	 System.out.println("❌ Hubo un problema al intentar enviar los datos.");
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

     System.out.println("CLIENTE: Fin del programa");
 }
}
