package fr.hrudyfiesta.othello;

import fr.hrudyfiesta.othello.gui.CustomButton;
import fr.hrudyfiesta.othello.players.HumanPlayer;
import fr.hrudyfiesta.othello.players.IPlayer;
import fr.hrudyfiesta.othello.players.bot.BotDifficulty;
import fr.hrudyfiesta.othello.players.bot.BotPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu {

	private static int menuType;

	private static JButton button1;
	private static JButton button2;
	private static JButton button3;
	private static JButton button4;

	private static volatile boolean shouldLaunchGame = false;
	private static volatile int gridSize = 8;
	private static volatile IPlayer player1;
	private static volatile IPlayer player2;
	private static volatile ArrayList<String> moves = new ArrayList<>();

	/**
	 Initialise le jeu.

	 @param args Arguments donnés au lancement du programme.
	*/
	public static void main(String[] args) {
		// Charge la texture depuis les fichiers du jeu.
		File file = new File("./tileset.png");

		try {
			Game.texture = ImageIO.read(new File("./tileset.png"));
		}
		catch (Exception ignore) {
			System.out.println("Erreur de lecture, impossible de lire le fichier de textures.");
			return;
		}

		// Paramètre certaines propriétés de la fenêtre de base.
		Game.WINDOW.setVisible(true);
		Game.WINDOW.pack();

		// Initialise le menu
		init(args);
	}

	/**
	 Initialise le menu, utile pour pouvoir rappeler cette fonction par la suite.

	 @param args Arguments donnés au lancement du programme.
	*/
	public static void init(String[] args) {
		// Passe l'écran actif sur celui de l'écran principal.
		switchToPanel(createMainMenu(args));

		// Puisque Swing est multithreadé, on demande au thread principal d'attendre le feu vert du thread de l'interface
		// pour lancer la partie.
		while (!shouldLaunchGame) {}

		// Réinitialise l'indicateur pour lancer le jeu,
		shouldLaunchGame = false;

		// Lance la partie avec les paramètres donnés.
		Game.init(player1, player2, gridSize, moves);
	}

	/**
	 Fonction permettant de demander à Swing de changer l'écran actif (les éléments de la fenêtre).

	 @param panel L'écran (JPanel) à utiliser.
	*/
	public static void switchToPanel(JPanel panel) {
		// Appelle le garbage collector au changement de menu
		System.gc();

		// Change le menu avec Swing.
		SwingUtilities.invokeLater(() -> {
			Game.WINDOW.remove(Game.currentPanel);
			Game.currentPanel = panel;
			Game.WINDOW.add(Game.currentPanel);
			Game.WINDOW.invalidate();
			Game.WINDOW.revalidate();
		});
	}

	/**
	 Crée le JPanel de l'écran principal.

	 @param args Arguments donnés au lancement du programme.
	 @return L'écran avec tout les composants.
	*/
	private static JPanel createMainMenu(String[] args) {
		Game.WINDOW.setMinimumSize(new Dimension(520, 480));
		Game.WINDOW.setTitle("Othello - Main menu");
		Game.WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(Game.BACKGROUND);
		panel.setLayout(new GridLayout(2, 1));

		JLabel title = new JLabel("OTHELLO");
		title.setFont(new Font("Sans-Serif", Font.BOLD, 69));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.CENTER);
		panel.add(title);

		JPanel buttonList = new JPanel();
		buttonList.setBackground(Game.BACKGROUND);
		buttonList.setLayout(new GridLayout(1, 3, 30, 0));

		ClickListener clickListener = new ClickListener();

		button1 = new CustomButton("Joueur vs Joueur", new ImageIcon(Game.texture.getSubimage(32, 16, 16, 16).getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
		button1.addActionListener(clickListener);

		button2 = new CustomButton("Joueur vs Bot", new ImageIcon(Game.texture.getSubimage(48, 16, 16, 16).getScaledInstance(64,64, Image.SCALE_SMOOTH)));
		button2.addActionListener(clickListener);


		button3 = new CustomButton("Sauvegarde", new ImageIcon(Game.texture.getSubimage(0, 32, 16, 16).getScaledInstance(64,64, Image.SCALE_SMOOTH)));
		button3.addActionListener(clickListener);

		buttonList.add(button1);
		buttonList.add(button2);
		buttonList.add(button3);

		panel.add(buttonList);

		return panel;
	}


	/**
	 Crée le JPanel de l'écran pour choisir la difficulté de l'ordinateur.

	 @return L'écran avec tout les composants.
	*/
	private static JPanel createBotMenu() {
		Game.WINDOW.setMinimumSize(new Dimension(520, 480));
		Game.WINDOW.setTitle("Othello - Difficulté du bot");
		Game.WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(Game.BACKGROUND);
		panel.setLayout(new BorderLayout());

		ClickListener clickListener = new ClickListener();

		button4 = new CustomButton("Revenir à l'écran précédent");
		button4.addActionListener(clickListener);
		button4.setPreferredSize(new Dimension(32, 64));
		button4.setHorizontalTextPosition(SwingConstants.RIGHT);
		button4.setVerticalTextPosition(SwingConstants.CENTER);

		panel.add(button4, BorderLayout.NORTH);

		JLabel title = new JLabel("Choisissez une difficulté:");
		title.setFont(new Font("Sans-Serif", Font.BOLD, 32));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.CENTER);
		panel.add(title, BorderLayout.CENTER);

		JPanel buttonList = new JPanel();
		buttonList.setBackground(Game.BACKGROUND);
		buttonList.setLayout(new GridLayout(1, 3, 30, 0));
		buttonList.setPreferredSize(new Dimension(32, 146));


		button1 = new CustomButton("Facile", new ImageIcon(Game.texture.getSubimage(16, 32, 16, 16).getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
		button1.addActionListener(clickListener);

		button2 = new CustomButton("Moyen", new ImageIcon(Game.texture.getSubimage(32, 32, 16, 16).getScaledInstance(64,64, Image.SCALE_SMOOTH)));
		button2.addActionListener(clickListener);

		button3 = new CustomButton("DIfficile", new ImageIcon(Game.texture.getSubimage(48, 32, 16, 16).getScaledInstance(64,64, Image.SCALE_SMOOTH)));
		button3.addActionListener(clickListener);

		buttonList.add(button1);
		buttonList.add(button2);
		buttonList.add(button3);

		panel.add(buttonList, BorderLayout.SOUTH);

		return panel;
	}


	/**
	 Classe gérant l'action de clique des boutons Swing.
	*/
	private static class ClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			// Bouton 1
			if (e.getSource() == button1) {
				switch (menuType) {
					case 0:
						menuType = 0;
						launchNewGame(0);
						break;

					case 1:
						menuType = 0;
						launchNewGame(1);
						break;
				}
			}

			// Bouton 2
			if (e.getSource() == button2) {
				switch (menuType) {
					case 0:
						menuType = 1;
						switchToPanel(createBotMenu());
						break;

					case 1:
						menuType = 0;
						launchNewGame(2);
						break;
				}
			}

			// Bouton 3
			if (e.getSource() == button3) {
				switch (menuType) {
					case 0:
						ArrayList<String> save = SaveManager.read();

						if (save.size() < 1) {
							JOptionPane.showMessageDialog(null, "Il n'y a pas de sauvegarde enregistrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
							break;
						}

						String[] parameters = save.get(0).trim().split("\\s");

						int mode = -1;
						int size = -1;
						try {
							mode = Integer.parseInt(parameters[0]);
							size = Integer.parseInt(parameters[1]);
						}
						catch (Exception x) {
							JOptionPane.showMessageDialog(null, "Fichier de sauvegarde incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
							break;
						}

						if (mode < 0 || mode > 3) {
							JOptionPane.showMessageDialog(null, "Fichier de sauvegarde incorrect (mode inexistant).", "Erreur", JOptionPane.ERROR_MESSAGE);
							break;
						}

						if (size < 3) {
							JOptionPane.showMessageDialog(null, "Fichier de sauvegarde incorrect (taille incorrecte).", "Erreur", JOptionPane.ERROR_MESSAGE);
							break;
						}

						gridSize = size;

						player1 = new HumanPlayer();

						switch (mode) {
							case 0:
								player2 = new HumanPlayer();
								System.out.println("Joueur contre joueur (Taille " + gridSize + "):");
								break;

							case 1:
								player2 = new BotPlayer(BotDifficulty.EASY);
								System.out.println("Joueur contre bot facile (Taille " + gridSize + "):");
								break;

							case 2:
								player2 = new BotPlayer(BotDifficulty.MEDIUM);
								System.out.println("Joueur contre bot moyen (Taille " + gridSize + "):");
								break;

							case 3:
								player2 = new BotPlayer(BotDifficulty.HARD);
								System.out.println("Joueur contre bot difficile (Taille " + gridSize + "):");
								break;
						}

						moves = new ArrayList<>();
						moves.addAll(save.subList(1, save.size()));

						shouldLaunchGame = true;

						break;

					case 1:
						menuType = 0;
						launchNewGame(3);
						break;
				}
			}

			// Button 4
			if (e.getSource() == button4) {
				menuType = 0;
				switchToPanel(createMainMenu(null));
			}
		}


		/**
		 Lance une nouvelle partie sans coups joués.

		 @param mode Le mode de la partie
		*/
		private void launchNewGame(int mode) {
			int optionSize = 0;
			while (optionSize < 3) {
				try {
					optionSize = Integer.parseInt(JOptionPane.showInputDialog("Veuillez indiquer la taille des côtés de la grille (3 à infini):"));
				}
				catch (Exception ignore) {}
			}

			gridSize = optionSize;

			player1 = new HumanPlayer();

			switch (mode) {
				case 0:
					player2 = new HumanPlayer();
					break;

				case 1:
					player2 = new BotPlayer(BotDifficulty.EASY);
					break;

				case 2:
					player2 = new BotPlayer(BotDifficulty.MEDIUM);
					break;

				case 3:
					player2 = new BotPlayer(BotDifficulty.HARD);
					break;
			}

			SaveManager.clear();
			SaveManager.write(mode + " " + optionSize);

			shouldLaunchGame = true;

			switch (mode) {
				case 0:
					System.out.println("Joueur contre joueur (Taille " + gridSize + "):");
					break;

				case 1:
					System.out.println("Joueur contre bot facile (Taille " + gridSize + "):");
					break;

				case 2:
					System.out.println("Joueur contre bot moyen (Taille " + gridSize + "):");
					break;

				case 3:
					System.out.println("Joueur contre bot difficile (Taille " + gridSize + "):");
					break;
			}
		}

	}
}
