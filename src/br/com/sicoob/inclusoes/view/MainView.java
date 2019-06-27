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

import br.com.sicoob.inclusoes.callback.CallbackContext;
import br.com.sicoob.inclusoes.controller.FileController;
import br.com.sicoob.inclusoes.controller.PathController;

public class MainView {

	JFrame frame;
	JTextField diretorio = new JTextField("", 30);
	JButton executar = new JButton("Executar");

	PathController pathController;
	FileController fileController;

	public void exec() {
		pathController = new PathController();
		fileController = new FileController();

		buildFrame();
		buildTabs(frame.getContentPane());

		frame.setVisible(true);
	}

	private void buildFrame() {
		frame = new JFrame("ARQUIVOS DE INCLUSÃO E EXCLUSÃO");
		frame.setDefaultCloseOperation(3);
		frame.setLocation(20, 20);
		frame.setResizable(false);
		frame.setSize(new Dimension(650, 380));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel versao = new JLabel("Versão 1.0");
		versao.setBounds(30, 310, 80, 30);
		frame.add(versao);

		JLabel inclusaoLabel = new JLabel("Adicionar");
		JTextArea inclusao = new JTextArea("");
		inclusaoLabel.setBounds(30, 50, 200, 50);
		inclusao.setBounds(30, 100, 250, 200);
		frame.add(inclusaoLabel);
		frame.add(inclusao);

		JLabel exclusaoLabel = new JLabel("Remover");
		JTextArea exclusao = new JTextArea();
		exclusaoLabel.setBounds(350, 50, 200, 50);
		exclusao.setBounds(350, 100, 250, 200);
		frame.add(exclusaoLabel);
		frame.add(exclusao);

		executar.setBounds(500, 310, 100, 30);
		frame.add(executar);

		executar.addActionListener((ActionEvent ae) -> {

			if (diretorio.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Favor informar o arquivo", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (inclusao.getText().isEmpty() && exclusao.getText().isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Favor informar a lista de arquivos para adicionar ou remover",
						"Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			executarInclusao(inclusao.getText());
			executarExclusao(exclusao.getText());
		});
	}

	private void executarExclusao(String exclusao) {
		if (!exclusao.isEmpty()) {
			List<String> removeFiles = buildListFile(exclusao);

			fileController.remove(diretorio.getText(), removeFiles, new CallbackContext() {

				@Override
				public void onSuccess(List<String> logs) {

					StringBuilder messageLog = new StringBuilder();
					logs.stream().forEach(log -> messageLog.append(log).append("\n"));

					JOptionPane.showMessageDialog(frame, messageLog, "Sucesso ao remover",
							JOptionPane.INFORMATION_MESSAGE);
				}

				@Override
				public void onError(String message) {
					JOptionPane.showMessageDialog(frame, message, "Erro ao remover", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	private void executarInclusao(String inclusao) {
		if (!inclusao.isEmpty()) {
			List<String> includeFiles = buildListFile(inclusao);

			fileController.put(diretorio.getText(), includeFiles, new CallbackContext() {

				@Override
				public void onSuccess(List<String> logs) {

					StringBuilder messageLog = new StringBuilder();
					logs.stream().forEach(log -> messageLog.append(log).append("\n"));

					JOptionPane.showMessageDialog(frame, messageLog, "Sucesso ao incluir",
							JOptionPane.INFORMATION_MESSAGE);
				}

				@Override
				public void onError(String message) {
					JOptionPane.showMessageDialog(frame, message, "Erro ao incluir", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
	}

	public void buildTabs(Container pane) {

		JTabbedPane tabs = new JTabbedPane();
		tabs.add(getFirstPanel(), "Selecione o arquivo de Inclusão ou Exclusão");

		pane.add(tabs, BorderLayout.CENTER);
	}

	public JPanel getFirstPanel() {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Arquivo"));

		JButton procurar = new JButton("Procurar");
		panel.add(procurar);

		JButton limpar = new JButton("Limpar");
		panel.add(limpar);

		panel.add(diretorio);

		procurar.addActionListener(((ActionEvent ae) -> {
			JFileChooser fchooser = new JFileChooser();
			// fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fchooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fchooser.enableInputMethods(true);
			fchooser.showSaveDialog(frame);

			diretorio.setText(fchooser.getSelectedFile().getAbsolutePath());
		}));

		limpar.addActionListener((ActionEvent ae) -> diretorio.setText(""));

		return panel;
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
