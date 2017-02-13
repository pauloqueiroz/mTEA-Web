package br.com.ufpi.util;

import java.io.File;

/**
 * 
 * @author Paulo Sergio
 *
 */
public class ArquivoUtil {

	/**
	 * Metodo que busca um diretorio local para criacao de arquivos.
	 * 
	 * @return diretorio local da maquina
	 */
	public static String getDiretorio() {
		String sistemaOperacional = System.getProperty("os.name");
		String diretorio;
		if (sistemaOperacional.toUpperCase().equals("LINUX")) {
			diretorio = System.getProperty("java.io.tmpdir") + "/";
		} else {
			diretorio = System.getProperty("java.io.tmpdir");
		}
		return diretorio;
	}

	/**
	 * Metodo que apaga um arquivo do diretorio do servidor.
	 * 
	 * @param nomeArquivo
	 *            nome do arquivo que se deseja apagar.
	 * @return true se o arquivo foi apagado com sucesso e false caso contrário.
	 */
	public static boolean apagaArquivo(String nomeArquivo) {
		String dirTmp = ArquivoUtil.getDiretorio();
		File arquivo = new File(dirTmp + "" + nomeArquivo);
		boolean sucess = arquivo.delete();
		return sucess;
	}

	/**
	 * Retorna tamanho do arquivo em MB
	 * 
	 * @param arquivo
	 *            Arquivo que se deseja calcular tamanho
	 * @return tamanho do arquivo em MB ou 0 caso o arquivo seja null.
	 */
	public static double calculaTamanhoArquivoEmMegaBytes(File arquivo) {
		if (arquivo != null && arquivo.exists()) {
			long tamanhoArquivoEmBytes = arquivo.length();
			return tamanhoArquivoEmBytes / 1048576D;
		}
		return -1D;
	}

	public static double bytesTo(double quantidadeDeBytes, char unidadeDeMedida) {
		double bytes = quantidadeDeBytes;
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);
		double terabytes = (gigabytes / 1024);
		double petabytes = (terabytes / 1024);
		double exabytes = (petabytes / 1024);
		double zettabytes = (exabytes / 1024);
		double yottabytes = (zettabytes / 1024);
		switch (unidadeDeMedida) {
		case 'b':
			return bytes;
		case 'k':
			return kilobytes;
		case 'm':
			return megabytes;
		case 'g':
			return gigabytes;
		case 't':
			return terabytes;
		case 'p':
			return petabytes;
		case 'e':
			return exabytes;
		case 'z':
			return zettabytes;
		case 'y':
			return yottabytes;
		default:
			return 0.0;
		}
	}

}
