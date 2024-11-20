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

		try (Scanner sc = new Scanner(System.in)) {

			System.out.println("CLIENTE: Esperando a que el servidor acepte la conexión");
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);
			System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER + " por el puerto " + PUERTO);

			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader entradaBuffer = new BufferedReader(entrada);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			boolean continuar = true;
			
			Thread mensajesEntrantes = new Thread(() -> {
				boolean continuarMensajesEntrantes = true;
				try {
					Socket socketMensajesEntrantes = new Socket();
					
					InputStreamReader inputChat = new InputStreamReader(socketAlServidor.getInputStream());
					BufferedReader chatBufferedReader = new BufferedReader(inputChat);
					
					socketMensajesEntrantes.connect(direccionServidor);
					
					while (continuarMensajesEntrantes) {
						System.out.println(chatBufferedReader.readLine());
					}
					socketMensajesEntrantes.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			});

			mensajesEntrantes.start();

			String mensajeAlServidor = "";
			System.out.println("CLIENTE: Escribe mensaje (FIN para terminar): ");
			
			do {

				mensajeAlServidor = sc.nextLine();
				salida.println(mensajeAlServidor);
				String respuesta = entradaBuffer.readLine();

				if ("OK".equalsIgnoreCase(respuesta)) {
					continuar = false;
				}

			} while (continuar);
			// Cerramos la conexion
			socketAlServidor.close();
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la dirección" + IP_SERVER);
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