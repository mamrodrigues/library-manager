package br.com.sicoob.inclusoes.callback;

import java.util.List;

public interface CallbackContext {

	void onSuccess(List<String> log);

	void onError(String message);
}
