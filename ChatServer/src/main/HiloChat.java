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
	private String nombreCliente;
	
	
	public HiloChat (Socket socketAlCliente, List<Socket> clientes) {
		num_cliente++;
		thread = new Thread(this, "Cliente " + num_cliente);
		this.socketAlCliente = socketAlCliente;
		this.clientes = clientes;
		thread.start();
	}


	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + thread.getName());

		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			
			// Aqui hacemos la entrada al servidor
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			
			entradaBuffer = new BufferedReader(entrada);
			 salida = new PrintStream(socketAlCliente.getOutputStream());

	            // Solicitar y registrar el nombre del cliente
	            salida.println("Por favor, introduce tu nombre:");
	            nombreCliente = entradaBuffer.readLine();
	            System.out.println("Cliente conectado: " + nombreCliente);
	            enviarATodos("🔔 " + nombreCliente + " se ha unido al chat.");
			
			String texto ="";
			boolean continuar = true;
			
			while(continuar) {
				
				texto = entradaBuffer.readLine();
				
				if(texto.trim().equalsIgnoreCase("FIN")) {
					
					salida.println("OK");
					
					System.out.println(thread.getName() + " hemos cerrado el chat hasta nueva orden");
					
					continuar = false;
					
				}else {
					System.out.println(thread.getName() + " dice: " + texto);
					
					for (Socket socket : clientes) {
						if(socket != socketAlCliente) {
						salida = new PrintStream(socket.getOutputStream());
						salida.println(thread.getName() + " dice: " + texto);
						}
					}
				}
			}
			socketAlCliente.close();

		} catch (IOException e) {
			System.err.println("HiloContadorLetras: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloContadorLetras: Error");
			e.printStackTrace();
		}
		
	}


	private void enviarATodos(String mensaje) {
        
            for (Socket cliente : clientes) {
                try {
                    PrintStream salida = new PrintStream(cliente.getOutputStream());
                    salida.println(mensaje);
                } catch (IOException e) {
                    System.out.println("Error al enviar mensaje a un cliente.");
                }
            }
        }
    }


