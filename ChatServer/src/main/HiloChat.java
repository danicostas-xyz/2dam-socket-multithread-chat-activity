package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class HiloChat implements Runnable{

	private Thread thread;
	private static int num_cliente = 0;
	private Socket socketAlCliente;
	
	
	public HiloChat (Socket socketAlCliente) {
		num_cliente++;
		thread = new Thread(this, "Cliente " + num_cliente);
		this.socketAlCliente = socketAlCliente;
		thread.start();
	}

	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + thread.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			//Salida del servidor al cliente
			salida = new PrintStream(socketAlCliente.getOutputStream());
			//Entrada del servidor al cliente
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			
			String texto = "";
			boolean continuar = true;
			
			//Procesaremos entradas hasta que el texto del cliente sea FIN
			while (continuar) {
				texto = entradaBuffer.readLine();
				if (texto.trim().equalsIgnoreCase("FIN")) {
					//Mandamos la se�al de "OK" para que el cliente sepa que vamos a cortar
					//la comunicacion
					salida.println("OK");
					System.out.println(thread.getName() + " ha cerrado la comunicacion");
					continuar = false;
				} else {
					
					String mensaje = thread.getName() + " dice: " + texto;
					System.out.println(mensaje);
					//Le mandamos la respuesta al cliente
					salida.println(mensaje);
				}
			}
			
			//Cerramos el socket si el cliente escribe FIN y salimos del while
			socketAlCliente.close();

		} catch (IOException e) {
			System.err.println("HiloContadorLetras: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloContadorLetras: Error");
			e.printStackTrace();
		}
		
	}

}
