package br.com.sicoob.inclusoes.controller;

import java.util.List;
import br.com.sicoob.inclusoes.callback.CallbackContext;

public interface IFileController {

	void remove(String path, List<String> removeFiles, CallbackContext callbackContext);

	void put(String path, List<String> includeFiles, CallbackContext callbackContext);
}
