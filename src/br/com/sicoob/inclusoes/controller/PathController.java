package br.com.sicoob.inclusoes.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathController {

	public List<String> getFolders(String dir) {

		Path pathDir = Paths.get(dir);

		try (Stream<Path> walk = Files.walk(pathDir)) {
			List<String> result = walk.filter(p -> Files.isDirectory(p) && p.getParent().equals(pathDir))
					.map(path -> path.toString()).collect(Collectors.toList());

			return result;
		} catch (IOException e) {
			return null;
		}
	}

	public List<String> getFiles(String dir) {
		Path pathDir = Paths.get(dir);

		try (Stream<Path> walk = Files.walk(pathDir)) {

			List<String> result = walk.filter(p -> Files.isRegularFile(p) && p.getParent().equals(pathDir))
					.map(x -> x.toString()).collect(Collectors.toList());

			return result;
		} catch (IOException e) {
			return null;
		}
	}

}
