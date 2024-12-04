package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class HiloChat implements Runnable{

	private Thread thread;
	private static int num_cliente = 0;
	private Socket socketAlCliente;
	private List<Socket> clientes;
	
	PrintStream salida = null;
	InputStreamReader entrada = null;
	BufferedReader entradaBuffer = null;
	
	
	public HiloChat (Socket socketAlCliente, List<Socket> clientes) {
		++num_cliente;
		thread = new Thread(this, "Cliente " + num_cliente);
		this.socketAlCliente = socketAlCliente;
		this.clientes = clientes;
		thread.start();
	}


	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + thread.getName());

		try {
			
			enviarCantidadUsuariosActuales();
			nuevoUsuarioConectado();
			
			
			// Aqui hacemos la entrada al servidor
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			
			entradaBuffer = new BufferedReader(entrada);
			
			String texto ="";
			boolean continuar = true;
			
			
			while(continuar) {
				
				texto = entradaBuffer.readLine();
				
				if(texto.isBlank() || texto == null) {
					continue;
				}
				
				if(texto.trim().equalsIgnoreCase("FIN")) {
					System.out.println(thread.getName() + " ha abandonado el chat.");
					
					
					enviarMensajes(thread.getName() + " ha abandonado el chat.");
					clientes.remove(socketAlCliente);
					enviarCantidadUsuariosActuales();
					continuar = false;
					
				}else {
					System.out.println(thread.getName() + ": " + texto);
					
					enviarMensajes(thread.getName() + ": " + texto);
					}
				}

		} catch (IOException e) {
			System.err.println("HiloContadorLetras: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloContadorLetras: Error");
			e.printStackTrace();
		}
		
	}
	
	public void enviarMensajes(String texto) throws IOException {
		for (Socket socket : clientes) {
			if(socket != socketAlCliente) {
			salida = new PrintStream(socket.getOutputStream());
			salida.println(texto);
			}
		}
	}
	
	public void enviarCantidadUsuariosActuales() throws IOException {
			for (Socket socket : clientes) {
				salida = new PrintStream(socket.getOutputStream());
				salida.println("Usuarios conectados: " + clientes.size());
			}
		}
	public void nuevoUsuarioConectado() throws IOException {
		for (Socket socket : clientes) {
			if(socket != socketAlCliente){
			salida = new PrintStream(socket.getOutputStream());
			salida.println(thread.getName() + " se ha conectado. ");
			}
		}
	}

}
