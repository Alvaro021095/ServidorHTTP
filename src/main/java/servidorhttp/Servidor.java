package servidorhttp;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Servidor implements Runnable {

	private Executor pool;

	public void run() {
		pool = Executors.newFixedThreadPool(100);
		try {
			ServerSocket server = new ServerSocket(9000);
			while (true) {
				System.out.println("esperando peticion!!!!!!");
				Socket con=server.accept();
				System.out.println(" peticion aceptada!!!!!!");

				ProcesadorPet proc=new ProcesadorPet(con);
				pool.execute(proc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		new Thread(new Servidor()).start();
	}

}

