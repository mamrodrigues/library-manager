package inclusoes;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.sicoob.inclusoes.view.MainView;

public class MainClass {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainView mainView = new MainView();
				mainView.exec();
			}
		});
	}
	
}
