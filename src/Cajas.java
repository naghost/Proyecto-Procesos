import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Semaphore;

public class Cajas extends Thread {
	private int cajero = 0;
	private  int cliente = 0;
	private final String[] articulo = { "Agua", "Fruta", "Desayuno", "Meriendas", "Cenas" };
	private boolean parar = false;
	private boolean iniciada = false;
	private int contador = 0;
	private final Semaphore semaphore;
	
	public Cajas(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public boolean isInitialized() {
		return iniciada;
	}
	
	public void addClient() {
		cliente++;
	}
	
	public void buildBox(int boxId) {
		cajero = boxId;
		addClient();
	}
	
	public void _stop() {
		parar = true;
	}
	
	public void run() {
		iniciada = true;

		while (!parar) { // deja activada la caja en el primer inicio
			
				
				// Intentamos acceder al recurso
				
				while (contador != cliente) { // recorre los clientes y los añade al fichero
					try {
					semaphore.acquire();
					String cadenaBase = "Cajero " + cajero + " cliente " + contador + ": ";
					Collections.shuffle(Arrays.asList(articulo));
					FileWriter fichero = null;
					PrintWriter pw = null;
					
					try { // escribe en el fichero los articulos
						fichero = new FileWriter("caja", true);
						pw = new PrintWriter(fichero);
						pw.print(cadenaBase);

						for (int i = 0; i < articulo.length; i++) {
							pw.print(articulo[i] + " ");
							try {
								this.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						pw.print("\n");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						if(pw!=null){pw.close();}
						if (fichero!=null) {try{fichero.close();}catch(IOException ignored) {}}
					}
				} catch (InterruptedException e1) {
					// Excepcion de semaforo
					e1.printStackTrace();
				} finally {				
					semaphore.release();

				}
					contador++;
				}
				



			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} // Fin bucle caja
		
		closeBox();

	}

	private void closeBox() {
		FileWriter fichero = null;
		PrintWriter pw = null;
		// Cerramos la caja
		try {
			
			semaphore.acquire();
			
			fichero = new FileWriter("caja", true);
			pw = new PrintWriter(fichero);
			pw.print("\n \n CAJA CERRADA "+cajero+" \n \n");
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			semaphore.release();
			if(pw != null)pw.close();
			if(fichero != null) {
				try {
					fichero.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void parar() {
		this.parar = true;
	}
}
