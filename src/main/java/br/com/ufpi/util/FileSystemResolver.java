package br.com.ufpi.util;

import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

/**
 * 
 * @author Paulo Sergio
 *
 */
public class FileSystemResolver extends ResourceResolver{
	
	private static final String PATH_TO_FACELETS_FILES_GOES_HERE = "/home/paulo/Documentos/workspace_java/ProjetoAutista/src/main/webapp";

	@Override
	public URL resolveUrl(String path) {
		try {
			return new URL("file", "", PATH_TO_FACELETS_FILES_GOES_HERE + path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
