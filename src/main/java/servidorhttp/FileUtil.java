package servidorhttp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public class FileUtil {
	/**
	 * Metodo para descargar en el flujo de salida un recurso.
	 * @param rutaFile, ruta del archivo
	 * @param output, flujo de salida.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void descargar(String rutaFile, OutputStream output) throws FileNotFoundException, IOException{

		FileInputStream entrada=new FileInputStream(rutaFile);
		byte[] bite=new byte[1024];
		int b=0;
		while((b=entrada.read(bite))>0){
			System.out.println(new String(bite));
			output.write(bite,0,b);
		}
	}

}
