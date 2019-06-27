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

		List<String> logs = new ArrayList<>();

		if (removeFiles.isEmpty()) {
			return;
		}

		try {
			File arquivoBase = new File(path);

			if (!arquivoBase.isFile()) {
				logs.add("O caminho informado não existe!");
				return;
			}

			List<String> listaArquivoBase = buscarListaArquivo(path);

			File arquivo = new File(arquivoBase.getAbsolutePath());
			PrintWriter pw = new PrintWriter(new FileWriter(arquivo));

			listaArquivoBase.forEach(line -> {
				
				if (!removeFiles.contains(line)) {
					pw.println(line);
					
				} else {
					logs.add("Removido: ".concat(line));
				}
			});
			
			if(logs.isEmpty()) {
				logs.add("O(s) arquivo(s) informado(s) para remoção não existe(m) neste documento");
				
			}

			pw.flush();
			pw.close();

			callbackContext.onSuccess(logs);

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

		List<String> logs = new ArrayList<>();

		if (includeFiles.isEmpty()) {
			return;
		}

		try {
			List<String> listaArquivoBase = buscarListaArquivo(path);

			File arquivoBase = new File(path);
			if (!arquivoBase.isFile()) {
				logs.add("O caminho informado não existe!");
				return;
			}

			PrintWriter pw = new PrintWriter(new FileWriter(arquivoBase));

			listaArquivoBase.forEach(item -> pw.println(item.trim()));

			includeFiles.forEach(adicionar -> {
				
				if (!listaArquivoBase.contains(adicionar)) {
		
					pw.println(adicionar.trim());
					logs.add("Adicionado: ".concat(adicionar));
					
				} else {
					
					logs.add("Arquivo ".concat(adicionar).concat(" já existe"));
				}
			});

			pw.flush();
			pw.close();

			callbackContext.onSuccess(logs);

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
