package fr.hrudyfiesta.othello;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveManager {
	/**
	 Constante indicant le chemin et le nom du fichier de sauvegarde.
	*/
	private static final String PATH = "./last.sav";

	/**
	 Supprime le fichier de sauvegarde

	 @return Si l'opération a réussie ou non.
	*/
	public static boolean clear() {
		Path path = Paths.get(PATH);

		try {
			Files.delete(path);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	/**
	 Rajoute une ligne au fichier de sauvegarde

	 @param line La ligne à rajouter
	 @return Si l'opération a réussie ou non.
	*/
	public static boolean write(String line) {
		Path path = Paths.get(PATH);

		try {
			if (!Files.exists(path)) Files.createFile(path);

			Files.write(path, Arrays.asList(line), StandardCharsets.UTF_8, StandardOpenOption.APPEND);

			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	/**
	 Lis le fichier sauvegarde et renvoit une liste de chaque ligne.

	 @return La liste des lignes du fichier.
	*/
	public static ArrayList<String> read() {
		Path path = Paths.get(PATH);

		try {
			return (ArrayList<String>) Files.readAllLines(path);
		}
		catch (IOException e) {
			return new ArrayList<>();
		}
	}
}
