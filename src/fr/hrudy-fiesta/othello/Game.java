package fr.hrudyfiesta.othello;

import fr.hrudyfiesta.othello.gui.AspectRatioKeeperLayout;
import fr.hrudyfiesta.othello.gui.CustomButton;
import fr.hrudyfiesta.othello.players.HumanPlayer;
import fr.hrudyfiesta.othello.players.IPlayer;
import fr.hrudyfiesta.othello.players.bot.BotDifficulty;
import fr.hrudyfiesta.othello.players.bot.BotPlayer;
import fr.hrudyfiesta.othello.utils.Case;
import fr.hrudyfiesta.othello.utils.Coords;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

@SuppressWarnings("unused")
public class Game {
	// Taille du tableau, gérée en paramètre par le Menu.
	private static int boardSize;

	// Tableau de cases
	private static Case[][] board;

	// Instance des joueurs
	private static IPlayer player1;
	private static IPlayer player2;

	// Joueur actuel, toujours une copie de soit player1, soit player2.
	private static IPlayer currentPlayer;

	// Liste des coups de la sauvegarde;
	private static ArrayList<String> moves = new ArrayList<>();

	// Liste des coups possibles (coordonnées), mise à jour à chaque tour.
	private static Coords[] possibleMoves = new Coords[0];

	// Nombre de tours joués
	private static int round = 0;

	// Fenêtre du jeu, pour l'interface graphique, constante.
	public static final JFrame WINDOW = new JFrame();

	// Ecran actif, utilisé pour les menus.
	public static JPanel currentPanel = new JPanel();

	// Texture contenant tout les sprites, initialisée par le menu.
	public static BufferedImage texture = null;

	// Couleur de fond, constante qui ne change pas.
	public static final Color BACKGROUND = Color.BLACK;

	private static JLabel roundText;
	private static JLabel scoresText;

	private static CustomButton menuButton;
	private static volatile boolean shouldExitToMainMenu;

	public static IPlayer getPlayer1() {
		return player1;
	}

	public static IPlayer getPlayer2() {
		return player2;
	}

	public static int getBoardSize() {return boardSize;}
	public static Case[][] getBoard() {
		return board;
	}
	public static IPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	public static Coords[] getPossibleMoves() {
		return possibleMoves;
	}
	public static int getRound() {
		return round;
	}


	/**
	 Initialise le jeu en prenant les différents paramètres

	 @param firstPlayer Une instance d'IPlayer qui représente le premier joueur.
	 @param secondPlayer Une instance d'IPlayer qui représente le deuxième joueur.
	 @param boardSize La taille du tableau
	 @param moves La liste des coups à préjouer, pour le système de sauvegarde.
	*/
	public static void init(IPlayer firstPlayer, IPlayer secondPlayer, int boardSize, ArrayList<String> moves) {
		// Définis les variables de la classe sur leurs objets respectifs.
		player1 = firstPlayer;
		player2 = secondPlayer;

		// Définis le joueur actuel sur le joueur 1.
		currentPlayer = player1;

		// Définis les scores des joueurs
		player1.setScore(2);
		player2.setScore(2);

		// Mets le numéro de manche à 0
		round = 0;

		// Désactive le clique du bouton
		shouldExitToMainMenu = false;

		// Charge la sauvegarde
		if (moves != null) Game.moves = moves;


		// Paramètre la fenêtre
		WINDOW.setMinimumSize(new Dimension(480, 520));

		// Définis le titre
		if (player2 instanceof HumanPlayer) WINDOW.setTitle("Othello - Joueur vs Joueur (" + boardSize + "x" + boardSize + ")");
		else {
			BotPlayer bot = (BotPlayer) player2;

			if (bot.getDifficulty() == BotDifficulty.EASY) WINDOW.setTitle("Othello - Joueur vs Bot Facile (" + boardSize + "x" + boardSize + ")");
			else if (bot.getDifficulty() == BotDifficulty.MEDIUM) WINDOW.setTitle("Othello - Joueur vs Bot Moyen (" + boardSize + "x" + boardSize + ")");
			else WINDOW.setTitle("Othello - Joueur vs Bot Difficile (" + boardSize + "x" + boardSize + ")");
		}

		WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Organisation de la fenêtre
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// Panel gardant un ratio de carré pour la grille
		JPanel ratioKeeper = new JPanel();
		ratioKeeper.setLayout(new AspectRatioKeeperLayout());
		ratioKeeper.setBackground(BACKGROUND);

		// Grille de jeu
		JPanel grille = new JPanel();
		grille.setLayout(new GridLayout(boardSize, boardSize));
		grille.setBackground(BACKGROUND);
		grille.setBorder(new LineBorder(Color.GRAY, 2));
		ratioKeeper.add(grille);

		// Rajoute le panel à la fenêtre
		panel.add(ratioKeeper, BorderLayout.CENTER);

		// Taille du tableau
		Game.boardSize = boardSize;
		board = new Case[Game.boardSize][Game.boardSize];

		// Initialise chaque case du tableau.
		for (int i = 0; i < Game.boardSize; i += 1) {
			for (int j = 0; j < Game.boardSize; j += 1) {
				board[i][j] = new Case(i, j);
			}
		}

		// Définis les 4 premiers pions du tableau
		addPawn(new Coords(Game.boardSize / 2 - 1, Game.boardSize / 2 - 1), 2);
		addPawn(new Coords(Game.boardSize / 2, Game.boardSize / 2 - 1), 1);
		addPawn(new Coords(Game.boardSize / 2 - 1, Game.boardSize / 2), 1);
		addPawn(new Coords(Game.boardSize / 2, Game.boardSize / 2), 2);

		// Ajoute les cases dans la grille de l'interface.
		for (int i = 0; i < Game.boardSize; i += 1) {
			for (int j = 0; j < Game.boardSize; j += 1) {
				grille.add(board[j][i]);
			}
		}

		// Barre sur le côté de la fenêtre
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new GridLayout(5, 1,3 ,0));
		sidebar.setBorder(new LineBorder(Color.GRAY));
		sidebar.setBackground(new Color(24,24,24));

		// Bouton du menu principal
		menuButton = new CustomButton("  Retourner au menu principal  ");
		menuButton.addActionListener(new ClickListener());
		sidebar.add(menuButton);

		// Texte indiquant la manche actuelle
		roundText = new JLabel("", SwingConstants.CENTER);
		roundText.setHorizontalAlignment(SwingConstants.CENTER);
		roundText.setVerticalAlignment(SwingConstants.CENTER);
		roundText.setForeground(Color.WHITE);
		roundText.setFont(new Font("Sans-Serif", Font.BOLD, 24));
		sidebar.add(roundText);

		// Texte indiquant les scores des joueurs
		scoresText = new JLabel("", SwingConstants.CENTER);
		scoresText.setHorizontalAlignment(SwingConstants.CENTER);
		scoresText.setVerticalAlignment(SwingConstants.CENTER);
		scoresText.setForeground(Color.WHITE);
		scoresText.setFont(new Font("Sans-Serif", Font.PLAIN, 18));
		sidebar.add(scoresText);

		// Rajoute la sidebar à la fenêtre
		panel.add(sidebar,  BorderLayout.EAST);


		// Modifie l'écran actuel pour être celui de la grille de jeu.
		Menu.switchToPanel(panel);

		// Démarre la boucle
		loop();
	}

	/**
	 Définis le type d'une case à une position donnée. Permet d'ignorer les erreurs potentielles.

	 @param x Coordonnée x de la case
	 @param y Coordonnée y de la case
	 @param player Le type de case
	 */
	private static void addPawn(int x, int y, int player){
		try {
			board[x][y].setType(player);
		}
		catch (Exception ignore) {}
	}

	public static int getPawn(int x, int y) {
		return board[x][y].getType();
	}

	/**
	 Définis le type d'une case à une position donnée. Permet d'ignorer les erreurs potentielles.

	 @param coords Les coordonnées de la case
	 @param player Le type de case
	*/
	private static void addPawn(Coords coords, int player){
		addPawn(coords.x, coords.y, player);
	}

	public static int getPawn(Coords coords) {
		return getPawn(coords.x, coords.y);
	}

	/**
	 Fonction interne qui gère la boucle du jeu.
	*/
	private static void loop() {
		// Génère les coups possible pour le tout premier coup.
		updatePossibleMoves();

		// Nombre de joueurs qui ont passé ce tour
		int toursPasses = 0;

		// Tant qu'il n'y a pas de gagnants (que les deux joueurs n'ont pas passé le même tour) ou qu'on ne clique pas pour revenir au menu principal.
		while (toursPasses < 2 && !shouldExitToMainMenu) {
			// Dessine le plateau pour ce tour
			draw();

			// Si il n'y a aucun coup possible, le tour est passé.
			if (possibleMoves.length == 0) {
				toursPasses += 1;

				// Change le joueur actuel sur l'autre joueur.
				if (currentPlayer == player1) currentPlayer = player2;
				else currentPlayer = player1;

				// Mets à jour les coups possibles pour l'autre joueur.
				updatePossibleMoves();

				continue;
			}

			// Le jouer actuel peut jouer, on réinitialise le compteur de sauts de tours.
			toursPasses = 0;

			// Joue le tour
			play();

			// Mets à jour les coups possibles pour le tour suivant
			updatePossibleMoves();
		}

		// Une fois la partie terminée, on mets à jour l'affichage pour le afficher le dernier coup.
		draw();

		// Si l'on est sorti de la boucle parce qu'on a cliqué sur le bouton. On revient au menu principal.
		if (shouldExitToMainMenu) {
			Menu.init(null);
			return;
		}

		// Indique dans la console que la partie est terminée.
		System.out.println("Partie terminée!");

		// Si le score du joueur 1 est supérieur à celui du joueur 2
		if (player1.getScore() > player2.getScore()) {

			// Définis la couleur du texte de round en bleu
			roundText.setForeground(new Color(0,130,230));

			// Si le joueur 2 est humain, on affiche les messages pour le mode joueur vs joueur.
			if (player2 instanceof HumanPlayer) {
				roundText.setText("<html><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Victoire !</br><br>Le joueur 1 gagne.</br></html>");
				System.out.println("Le joueur 1 gagne.");
			}
			// Sinon on affiche les messages pour le mode contre l'ordinateur
			else {
				roundText.setText("<html><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Victoire !</br><br>Le joueur gagne.</br></html>");
				System.out.println("Le joueur gagne.");
			}
		}

		// Si le score du joueur 2 est supérieur à celui du joueur 1
		else if (player2.getScore() > player1.getScore()) {

			// Définis la couleur du texte de round en rouge
			roundText.setForeground(new Color(200,40,40));

			// Si le joueur 2 est humain, on affiche les messages pour le mode joueur vs joueur.
			if (player2 instanceof HumanPlayer) {
				roundText.setText("<html><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Victoire !</br><br>Le joueur 2 gagne.</br></html>");
				System.out.println("Le joueur 2 gagne.");
			}
			// Sinon on affiche les messages pour le mode contre l'ordinateur
			else {
				roundText.setText("<html><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Défaite !</br><br>L'ordinateur gagne.</br></html>");
				System.out.println("L'ordinateur gagne.");
			}
		}

		// Sinon si il y a égalité, on affiche l'égalité.
		else {
			roundText.setText("Égalité !");
			System.out.println("Égalité!");
		}

		// Boucle utilisée si l'on clique sur le bouton du menu principal lorsqu'une partie est déjà terminée.
		while (!shouldExitToMainMenu) {}

		// Si l'on clique sur le bouton, revient au menu.
		Menu.init(null);
	}

	/**
	 Fonction interne qui joue un coup.
	*/
	private static void play() {
		// Appelle le garbage collector à chaque coup joué
		System.gc();

		// Demande au joueur actuel de jouer.
		Coords play = new Coords(-1, -1);

		// Sécurité qui vérifie si le coup est valide pour le serveur. Sors de la boucle si l'on clique sur le bouton.
		boolean isActuallyValid = false;
		while (!isActuallyValid && !shouldExitToMainMenu) {

			// Indique si le coup de la sauvegarde est valide (doit être joué)
			boolean shouldPlaySaveMove = false;

			// Si il y a encore un coup potentiel à jouer, on essaye de le jouer.
			if (moves.size() > 0) {
				int x = -1;
				int y = -1;

				// Récupère les coordonnées du coup
				try {
					String[] move = moves.remove(0).split("\\s");
					x = Integer.parseInt(move[0]);
					y = Integer.parseInt(move[1]);
				}
				catch (Exception ignore) {}

				play = new Coords(x, y);

				// Vérifie la liste des coups possibles pour ce tour,
				for (Coords possibleMove : possibleMoves) {
					if (play.equals(possibleMove)) {  // Si le coup est dans cette liste, le coup est réellement valide et on le joue.
						isActuallyValid = true;
						shouldPlaySaveMove = true;

						break;
					}
				}
			}

			// Si le coup n'est pas valide ce tour, on joue le tour normalement
			if (!shouldPlaySaveMove) {

				// Récupère le coup du joueur
				play = currentPlayer.play();

				// Vérifie la liste des coups possibles pour ce tour,
				for (Coords possibleMove : possibleMoves) {
					if (play.equals(possibleMove)) {  // Si le coup est dans cette liste, le coup est réellement valide et on sort de la boucle.

						//  Le coup est maintenant valide
						isActuallyValid = true;

						// On sauvegarde le coup
						SaveManager.write(play.x + " " + play.y);

						break;
					}
				}
			}
		}

		// Mets à jour le tableau, maintenant que le coup est valide.
		updateBoard(play.x, play.y);

		// Augmente le compteur de round de 1.
		round += 1;

		// Change le joueur actuel sur l'autre joueur.
		if (currentPlayer == player1) currentPlayer = player2;
		else currentPlayer = player1;
	}

	/**
	 Mets à jour la liste des coups possibles à ce tour, utilisée par différentes fonctions pour accéder à tous les coups.
	*/
	private static void updatePossibleMoves() {
		// Liste qui stocke les coups possibles
		ArrayList<Coords> list = new ArrayList<>();

		// Ne lance pas la boucle si l'on quitte pour le menu principal (évite des crashs).
		if (!shouldExitToMainMenu) {
			// Vide les coups actuellement possibles
			for (Coords c : possibleMoves) {
				if (shouldExitToMainMenu) break;

				Case theCase = board[c.x][c.y];
				if (theCase != null) theCase.isPossible = false;
			}

			// Vérifie toutes les cases du tableau pour les coups possibles
			for (int x = 0; x < boardSize; x += 1) {
				for (int y = 0; y < boardSize; y += 1) {
					if (shouldExitToMainMenu) break;

					if (isValidMove(x, y)) {
						board[x][y].isPossible = true;
						list.add(new Coords(x, y));
					}
				}
			}
		}

		// Convertit l'ArrayList en tableau
		possibleMoves = list.toArray(new Coords[0]);
	}


	/**
	 Fonction interne qui vérifie si des coordonnées données représentent un coup valide pour ce tour.
	 Vérifie si les coordonnées données sont dans le tableau puis appelle checkDirection pour les 8 directions possibles.

	 @param x Coordonnée en X
	 @param y Coordonnée en Y
	 @return Si le coup est valide ou non
	*/
	private static boolean isValidMove(int x, int y) {
		// Si la case est déjà occupée, le coup ne peut pas être valide.
		if (getPawn(x, y) != 0) return false;

		// Si le coup est en dehors des limites du tableau, le coup n'est pas possible.
		if (x < 0 || y < 0 || x >= boardSize || y >= boardSize) return false;

		// Vérifie pour chaque direction si le coup remplis toutes les conditions (haut, bas, gauche, droite, diagonales)
		for (int dx = -1; dx <= 1; dx += 1) {
			for (int dy = -1; dy <= 1; dy += 1) {
				if (dx == 0 && dy == 0) continue;  // Ignore la case actuelle.
				if (checkDirection(x, y, dx, dy)) return true;  // Un coup valide existe dans une direction.
			}
		}

		return false; // Le coup n'est pas valide.
	}

	/**
	 Fonction interne qui vérifie si un point de départ et une direction donnée remplissent les conditions requises pour qu'un coup soit valide.

	 Cette fonction vérifie à la fois si le point est adjacent à un point adverse et si la direction donnée permet à ce point
	 de prendre d'autres points en sandwich (si il y a un point du joueur actuel dans la direction donnée).

	 @param x Coordonnée du point de départ en X
	 @param y Coordonnée du point de départ en Y
	 @param dx Direction (inertie à rajouter au point) en X. Soit -1, 0, 1.
	 @param dy Direction (inertie à rajouter au point) en Y Soit -1, 0, 1.
	 @return Si les paramètres donnés représentent un coup valide ou non.
	*/
	private static boolean checkDirection(int x, int y, int dx, int dy) {
		// Récupère l'id du joueur actuel.
		int player = 1;
		if (currentPlayer == player2) player = 2;

		// Récupère l'id de l'opposant.
		int opponent = player == 1? 2 : 1;

		// Compteur de pions valides.
		int count = 0;

		// On avance dans la direction.
		x += dx;
		y += dy;

		// Tant que l'on n'atteint pas la limite du tableau et qu'on n'a pas rencontré une case du joueur actuel.
		while (x >= 0 && y >= 0 && x < boardSize && y < boardSize && board[x][y] != null && getPawn(x, y) == opponent) {
			count += 1;
			x += dx;
			y += dy;
		}

		// Si les coordonnées actuelles sont toujours valide, que l'on a rencontré au moins une case qui n'est pas du joueur actuel,
		// et que l'on termine sur une case du joueur, cette direction sera modifiée par la suite et le coup est valide.
		return x >= 0 && y >= 0 && x < boardSize && y < boardSize && count > 0 && board[x][y] != null && getPawn(x, y) == player;
	}

	/**
	 Mets à jour le tableau, pour retourner les pions adéquats par rapport aux coordonnées
	 Les coordonnées données sont assumées valides.

	 @param x Coordonnées du coup en X
	 @param y Coordonnées du coup en Y
	*/
	private static void updateBoard(int x, int y) {
		// Récupère l'id du joueur actuel.
		int player = 1;
		if (currentPlayer == player2) player = 2;

		// Pour toutes les directions possibles,
		for (int dx = -1; dx <= 1; dx += 1) {
			for (int dy = -1; dy <= 1; dy += 1) {

				// Si la direction en question est valide,
				if (checkDirection(x, y, dx, dy)) {

					// On sauvegarde la position actuelle.
					int originX = x;
					int originY = y;

					// On change toutes les cases qui ne sont pas du joueur dans cette direction.
					while (x >= 0 && y >= 0 && x < boardSize && y < boardSize && board[x][y] != null && ((x == originX && y == originY) || getPawn(x, y) != player)) {

						// La case actuelle dans la direction.
						Case currentCase = board[x][y];

						// Modifie les scores respectifs
						if (currentCase.getType() != player) {
							currentPlayer.increaseScore(1);

							if (currentCase.getType() > 0) {
								if (player == 1) player2.decreaseScore(1);
								else player1.decreaseScore(1);
							}
						}

						// Change le type de la case pour être celui du joueur actuel.
						currentCase.setType(player);

						// On passe à la prochaine case de la direction.
						x += dx;
						y += dy;
					}

					// On restaure la position de base, pour les autres directions.
					x = originX;
					y = originY;
				}

			}
		}
	}

	/**
	 Fonction interne responsable de l'affichage du tableau.
	 Appelle la fonction adéquate pour chaque case.
	*/
	private static void draw() {

		// Mets à jour les cases et gère l'affichage dans la console
		for (int j = 0; j < boardSize; j += 1) {
			for (int i = 0; i < boardSize; i += 1) board[i][j].draw();

			System.out.println();
		}

		// Saute une ligne dans la console pour le coup d'après.
		System.out.println();

		// Modifie le texte de round pour indiquer le joueur qui joue
		String playerText = "&nbsp;&nbsp;&nbsp;&nbsp;Bot";
		if (currentPlayer == player1) playerText = "Joueur 1";
		else if (currentPlayer instanceof HumanPlayer) playerText = "Joueur 2";

		roundText.setText("<html><br>Manche " + (round + 1) + ": </br><br>&nbsp;" + playerText + "</br></html>");

		// Modifie le texte de score avec les scores à jours
		// Affiche les messages respectifs pour un joueur/bot.
		if (player2 instanceof HumanPlayer) {
			System.out.println("Joueur 1: " + player1.getScore() + "  Joueur 2: " + player2.getScore());
			scoresText.setText("<html><br>Scores:</br><br>Joueur 1: " + player1.getScore() + "</br><br>Joueur 2: " + player2.getScore() + "</br></html>");
		}
		else {
			System.out.println("Joueur: " + player1.getScore() + "  Bot: " + player2.getScore());
			scoresText.setText("<html><br>Scores:</br><br>Joueur: " + player1.getScore() + "</br><br>Bot: " + player2.getScore() + "</br></html>");
		}
	}

	/**
	 Gère le clique du bouton pour revenir à l'écran titre.
	*/
	private static class ClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == menuButton) shouldExitToMainMenu = true;
		}
	}
}
