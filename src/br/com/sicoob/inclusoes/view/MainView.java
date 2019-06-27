package br.com.sicoob.inclusoes.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import br.com.sicoob.inclusoes.callback.CallbackContext;
import br.com.sicoob.inclusoes.controller.FileController;
import br.com.sicoob.inclusoes.controller.PathController;

public class MainView {

	JPanel aba;

	JLabel selecioneLabel = new JLabel("Diretorio");
	JTextField diretorio = new JTextField("", 30);
	JButton procurar = new JButton("Procurar");
	JButton limpar = new JButton("Limpar");
	JButton carregar = new JButton("Executar");

	JLabel inclusaoLabel = new JLabel("Arquivos de Inclusão");
	JTextArea inclusao = new JTextArea();
	
	JLabel exclusaoLabel = new JLabel("Arquivos de Exclusão");
	JTextArea exclusao = new JTextArea();
	
	JFrame frame;
	JFileChooser fchooser;
	
	PathController pathController;
	FileController fileController;

	public void run() {

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
				exec();
			}
		});
	}

	private void exec() {
		pathController = new PathController();
		fileController = new FileController();
		
		buildFrame();
		buildPanel(frame.getContentPane());

		frame.setVisible(true);
	}
	
	private void buildFrame() {
		frame = new JFrame(":: ARQUIVOS DE INCLUSÃO E EXCLUSÃO ::");
		frame.setDefaultCloseOperation(3);
		frame.setLocation(20, 20);
		frame.setResizable(false);
		frame.setSize(new Dimension(650, 380));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		inclusaoLabel.setBounds(30,50,200,50);
		inclusao.setBounds(30,100,250,200);
		inclusao.setLayout(null);  
		inclusao.setVisible(true); 
		frame.add(inclusaoLabel);
		frame.add(inclusao);
		
		carregar.setBounds(550,310,80,30);
		frame.add(carregar);
		
		exclusaoLabel.setBounds(350,50,200,50);
		exclusao.setBounds(350,100,250,200);  
		exclusao.setLayout(null); 
		exclusao.setVisible(true);
		frame.add(exclusaoLabel);
		frame.add(exclusao);
		
		carregar.addActionListener((ActionEvent ae) -> {
			
			List<String> includeFiles = buildListFile(inclusao.getText());
			List<String> removeFiles = buildListFile(exclusao.getText());
			
			fileController.remove(diretorio.getText(), removeFiles, new CallbackContext() {
				
				@Override
				public void onSuccess() {
				    JOptionPane.showMessageDialog(frame, "Remoção feita com sucesso");
				}
				
				@Override
				public void onError(String message) {
					
				}
			});
			
			fileController.put(diretorio.getText(), includeFiles, new CallbackContext() {
				
				@Override
				public void onSuccess() {
				    JOptionPane.showMessageDialog(frame, "Inclusão feita com sucesso");					
				}
				
				@Override
				public void onError(String message) {
					
				}
			});
			
		});
	}

	public void buildPanel(Container pane) {

		JTabbedPane tabs = new JTabbedPane();
		final String TAB1 = "Selecione o diretório dos Arquivos de Inclusão e Exclusão";

		JPanel panel1 = new JPanel();
		panel1.add(selecioneLabel);
		panel1.add(diretorio);
		panel1.add(procurar);
		panel1.add(limpar);

		tabs.add(panel1, TAB1);

		pane.add(tabs, BorderLayout.CENTER);

		procurar.addActionListener(((ActionEvent ae) -> {
			fchooser = new JFileChooser();
			//fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fchooser.enableInputMethods(true);
			fchooser.showSaveDialog(frame);

			diretorio.setText(fchooser.getSelectedFile().getAbsolutePath());
		}));

		limpar.addActionListener((ActionEvent ae) -> diretorio.setText(""));
	}
	
	public List<String> buildListFile(String filesString) {
		
		List<String> result = new ArrayList<>();
		
		final Pattern pattern = Pattern.compile("\\S+", Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(filesString);

		while (matcher.find()) {
		    result.add(matcher.group(0));
		}
		
		return result;
	}

}
