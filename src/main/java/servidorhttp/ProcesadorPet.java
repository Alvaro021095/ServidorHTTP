package servidorhttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class ProcesadorPet implements Runnable {

	private Socket con;

	public ProcesadorPet(Socket con) {
		super();
		this.con = con;
	}

	public void run() {

		try {

			Properties pro = new Properties();
			pro.load(new FileInputStream(
					"C:\\Users\\loter\\git\\ServidorHTTP\\src\\" + "main\\java\\properties\\prop.properties"));

			String nuevaURL = pro.getProperty("NuevaURL");
			String estHTTP301 = pro.getProperty("EstadoHTTPPermanente");
			String url = pro.getProperty("url");

			System.out.println("procesando peticion peticion!!!!!!");

			PrintStream salida = new PrintStream(con.getOutputStream());
			BufferedReader entrada = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String lin = entrada.readLine();
			System.out.println("peticion:" + lin);

			String partes[] = lin.split(" ");
			// no se requiere el slash (/)
			String recurso = partes[1].substring(1);

			if (recurso.equals(url)) {

				salida.println("HTTP/1.1 301 Moved Permanently");
				salida.println("Location: "+ nuevaURL);
				salida.print("Content-Type: text/html; charset=UTF-8");
				salida.println("Content-Length: 219");				
				salida.println();
				
			} else {

				boolean existe = new File(recurso).exists();
				if (existe) {
					System.out.println("existe el recurso!!!!");
					salida.println("HTTP/1.1 200 OK");
					String contentType = getCOntentType(recurso);
					salida.println("Content-type: " + contentType);
					salida.println("Content-length: " + new File(recurso).length());
					salida.println("Cache-Control: public, s-maxage=1800");
					salida.println();
					FileUtil f = new FileUtil();
					f.descargar(recurso, salida);

				} else {

					String rutaError = "GET /paginas/car/error_404.html HTTP/1.1";

					String partess[] = rutaError.split(" ");
					// no se requiere el slash (/)
					String recursos = partess[1].substring(1);

					System.err.println("No existe el recuros");
					salida.println("HTTP/1.1 404 OK");

					String contentType = getCOntentType(recursos);
					salida.println("Content-type: " + contentType);
					salida.println("Content-length: " + new File(recursos).length());

					salida.println();

					FileUtil fu = new FileUtil();
					fu.descargar(recursos, salida);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				con.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getCOntentType(String nombre) {
		if (nombre.endsWith(".jpg") || nombre.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		if (nombre.endsWith(".png")) {
			return "image/png";
		}

		if (nombre.endsWith(".css")) {
			return "text/css";
		}

		if (nombre.endsWith(".js")) {
			return "text/css";
		}
		if (nombre.endsWith(".html")) {
			return "text/html";
		}
		return "";

	}

}
