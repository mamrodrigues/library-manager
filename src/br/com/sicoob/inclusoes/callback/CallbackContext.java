package br.com.sicoob.inclusoes.callback;

public interface CallbackContext {
	
	void onSuccess();
	void onError(String message);
}
