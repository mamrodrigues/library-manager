package br.com.sicoob.inclusoes.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import br.com.sicoob.inclusoes.callback.CallbackContext;

public class FileController implements IFileController {

	private static BufferedReader buffer;

	@Override
	public void remove(String path, List<String> removeFiles, CallbackContext callbackContext) {

		if (removeFiles.isEmpty()) {
			System.out.println("Nenhum item a remover");
			return;
		}

		System.out.println("Itens a serem removidos:");
		for (String item : removeFiles) {
			System.out.println(" - " + item);
		}

		try {
			File arquivoBase = new File(path);

			if (!arquivoBase.isFile()) {
				System.out.println("O caminho informado não existe!");
				return;
			}

			List<String> listaArquivoBase = buscarListaArquivo(path);
			List<String> listaArquivosRealmenteRemovidos = new ArrayList<String>();

			File arquivo = new File(arquivoBase.getAbsolutePath());
			PrintWriter pw = new PrintWriter(new FileWriter(arquivo));

			for (String line : listaArquivoBase) {
				if (!removeFiles.contains(line)) {
					pw.println(line);
				} else {
					listaArquivosRealmenteRemovidos.add(line);
				}
			}

			System.out.println("Itens que foram removidos:");
			for (String item : listaArquivosRealmenteRemovidos) {
				System.out.println(" - " + item);
			}

			pw.flush();
			pw.close();
			
			callbackContext.onSuccess();

		} catch (FileNotFoundException arq) {
			arq.printStackTrace();
			callbackContext.onError("FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			callbackContext.onError("IOException");
		}
	}

	@Override
	public void put(String path, List<String> includeFiles, CallbackContext callbackContext) {

		if (includeFiles.isEmpty()) {
			System.out.println("Nenhum item a adicionar");
			return;
		}

		System.out.println("Itens a serem adicionados:");
		for (String item : includeFiles) {
			System.out.println(" + " + item);
		}

		try {
			List<String> listaArquivoBase = buscarListaArquivo(path);

			File arquivoBase = new File(path);
			if (!arquivoBase.isFile()) {
				System.out.println("O caminho informado não existe!");
				return;
			}

			PrintWriter pw = new PrintWriter(new FileWriter(arquivoBase));

			for (String item : listaArquivoBase) {
				pw.println(item.trim());
			}

			List<String> listaArquivosDuplicados = new ArrayList<String>();

			for (String adicionar : includeFiles) {
				if (!listaArquivoBase.contains(adicionar)) {
					pw.println(adicionar.trim());
				} else {
					listaArquivosDuplicados.add(adicionar);
				}
			}

			pw.flush();
			pw.close();

			if (!listaArquivosDuplicados.isEmpty()) {
				System.out.println("Itens já existentes anteriormente:");
				for (String item : listaArquivosDuplicados) {
					System.out.println(" ++ " + item);
				}
			}
			
			callbackContext.onSuccess();

		} catch (FileNotFoundException arq) {
			arq.printStackTrace();
			callbackContext.onError("FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			callbackContext.onError("IOException");
		}
	}

	public List<String> buscarListaArquivo(String caminhoArquivo) throws IOException {
		buffer = new BufferedReader(new FileReader(caminhoArquivo));

		List<String> listaRetorno = new ArrayList<>();
		String line = null;

		while ((line = buffer.readLine()) != null) {
			listaRetorno.add(line.trim());
		}
		return listaRetorno;
	}

}
