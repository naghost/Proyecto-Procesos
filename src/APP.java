import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class APP {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		boolean salir = false;
		
		Semaphore semaphore = new Semaphore(1);
		Cajas[] cajas = new Cajas[11];
		
		for (int i = 0; i < cajas.length; i++) {
			cajas[i] = new Cajas(semaphore);
		}
		do {
			System.out.println("1.Añadir cliente\n" + "2. Salir");
			// Solo tenemos 2 opciones 1 para añadir clientes 2 para cerrar las cajas
			// al cerrar las cajas tendran que esperar a que terminen el trabajo las cajeras

			switch (Teclado(sc)) {
			case 1:
				AnadirCliente(sc, cajas);
				break;
			case 2:
				salir = ApagarSistema(cajas);
				break;
			default:
				System.out.println("Error");
				break;
			}

		} while (salir == false);

	}

	private static boolean ApagarSistema(Cajas[] cajas) {
		boolean salir;
		for (int i = 0; i < cajas.length; i++) {
			cajas[i]._stop();
			// paramos las cajas
		}
		salir = true;
		// Terminamos programa
		return salir;
	}

	private static void AnadirCliente(Scanner sc, Cajas[] cajas) {
		int caja = 0;
		System.out.println("¿A que caja quieres enviar el cliente?(Cajas 0-10)");
		do {
			// comprobamos que selecciona una caja valida
			caja = Teclado(sc);
			if (caja > 10 || caja < 0) {
				System.out.println("Caja no valida");
			}
		} while (caja > 11 || caja < 0);

		if (cajas[caja].isInitialized()) {
			// si ya esta iniciado la caja añadimos un cliente
			cajas[caja].addClient();
		} else {
			// asignamos un numero de caja para identificarlas
			cajas[caja].buildBox(caja);
			cajas[caja].start();
		}
	}

	public static int Teclado(Scanner sc) {

		String Introducido = "";
		int numero = 0;

		boolean error = false;
		do {
			Introducido = sc.nextLine();
			try {
				numero = Integer.parseInt(Introducido);
				error = false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("No se ha introducido un numero vuelve a intentarlo");
				error = true;
			}

		} while (error == true);
		return numero;
	}

}
