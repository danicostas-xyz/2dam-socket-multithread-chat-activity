package main;

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
		// TODO Auto-generated method stub
		
	}

}
