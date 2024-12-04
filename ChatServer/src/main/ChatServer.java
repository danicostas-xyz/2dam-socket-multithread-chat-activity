package main;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
public static final int PUERTO = 2018;
public static List<Socket> clientes = new ArrayList<Socket>();
	
	public static void main(String[] args) {
		System.out.println("===========================================");
		System.out.println("   🖥️  APLICACIÓN DE SERVIDOR CON HILOS    ");
		System.out.println("===========================================");
		
		
		int peticion = 0;
		
		try (ServerSocket servidor = new ServerSocket()){			
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			
			servidor.bind(direccion);

			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);
			
			while (true) {
				
				Socket socketAlCliente = servidor.accept();
				clientes.add(socketAlCliente);
				System.out.println("SERVIDOR: peticion numero " + ++peticion + " recibida");
				
				new HiloChat(socketAlCliente, clientes);
			}			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}
	
}

