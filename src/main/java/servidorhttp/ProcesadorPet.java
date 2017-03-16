package servidorhttp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.commons.io.filefilter.AgeFileFilter;

public class ProcesadorPet implements Runnable {

	private Socket con;

	public ProcesadorPet(Socket con) {
		super();
		this.con = con;
	}

	public void run() {

		try {
			
			/*  MASOMENOS ASI VA EL PUNTO 3
			Properties pro = new Properties();
			pro.load(new FileInputStream("C:\\Users\\loter\\Desktop\\Universidad\\Semestre 8\\Sistemas Distribuidos\\"
					+ "corte 2\\servidorhttp\\src\\main\\java\\properties\\Prop.properties"));
			
			String nuevaURl = pro.getProperty("NuevaURL");
			String estadoHttp = pro.getProperty("EstadoHTTPPermanente");
			*/

			try {
				System.out.println("procesando peticion peticion!!!!!!");

				PrintStream salida = new PrintStream(con.getOutputStream());
				BufferedReader entrada = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String lin = entrada.readLine();
				System.out.println("peticion:" + lin);

				String partes[] = lin.split(" ");
				// no se requiere el slash (/)
				String recurso = partes[1].substring(1);

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

					String rutaError = "GET /paginas/404/index.html HTTP/1.1";

					String partess[] = rutaError.split(" ");
					// no se requiere el slash (/)
					String recursos = partess[1].substring(1);

					System.err.println("No existe el recuros");
					salida.println("HTTP/1.1 404 OK");

					String contentType = getCOntentType(recursos);
					salida.println("Content-type: " + contentType);
					salida.println("Content-length: " + new File(recursos).length());

					salida.println();

					// salida.println("Content-type: "+"text/html");
					// salida.println("Content-length: "+cad.length());
					// salida.println();
					// salida.println(cad);

					FileUtil fu = new FileUtil();
					fu.descargar(recursos, salida);

				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
