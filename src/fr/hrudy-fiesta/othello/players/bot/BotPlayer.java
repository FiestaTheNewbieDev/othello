package fr.hrudyfiesta.othello.players.bot;

import fr.hrudyfiesta.othello.Game;
import fr.hrudyfiesta.othello.players.IPlayer;
import fr.hrudyfiesta.othello.utils.Coords;

import java.util.ArrayList;
import java.util.Random;

/**
 Classe représentant un joueur IA.
*/
public class BotPlayer implements IPlayer {
	private int score = 0;
	private BotDifficulty difficulty;

	public BotPlayer (BotDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 Comportement à utiliser pour jouer un coup.
	 Renvoie un ensemble de coordonnées x, y représentant le coup choisi.
	*/
	@Override
	public Coords play() {
		if (this.difficulty == BotDifficulty.EASY) return randomAI();
		else if (this.difficulty == BotDifficulty.MEDIUM) return minmaxAI(3);
		else return minmaxAI(5);
	}

	/**
	 Joue un coup avec l'IA aléatoire.

	 @return Coordonnées jouées
	*/
	private Coords randomAI(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException exception) {
			System.out.println(exception);
		}

		Coords[] possibleMoves = Game.getPossibleMoves();
		int max = possibleMoves.length - 1;
		int min = 0;
		int random = (new Random()).nextInt(max - min + 1) + min;

		for (Coords c : possibleMoves) {
			System.out.println("|_ x: " + c.x + " y: " + c.y);
		}

		return possibleMoves[random];
	}

	/**
	 Joue un coup avec MinMax.

	 @param maxDepth La profondeur de recherche à utiliser
	 @return Coordonnées jouées
	*/
	private Coords minmaxAI(int maxDepth) {
		ArrayList<GameState> states = new ArrayList<>();

		for (Coords c : Game.getPossibleMoves()) states.add(new GameState(c.x, c.y, maxDepth));

		int finalIndex = 0;
		int scoreMax = states.get(0).getScore();
		for (int i = 0; i < states.size(); i += 1) {
			GameState s = states.get(i);

			int score = s.getScore();
			if (score >= scoreMax) {
				finalIndex = i;
				scoreMax = score;
			}

			// Désactivé pour ne pas flooder la console
			//s.draw();
		}

		GameState state = states.get(finalIndex);

		return new Coords(state.getX(), state.getY());
	}

	/**
	 @return Le score du bot
	 */
	@Override
	public int getScore() {
		return this.score;
	}

	/**
	 Définis le score du bot.

	 @param score Valeur à utiliser
	 */
	@Override
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 Augmente le score du bot de n.

	 @param n Valeur à ajouter au score du joueur.
	 */
	@Override
	public void increaseScore(int n) {
		this.score = this.score + n;
	}

	/**
	 Diminue le score du bot de n.

	 @param n Valeur à soustraire au score du joueur.
	 */
	@Override
	public void decreaseScore(int n) {
		this.score = this.score - n;
	}

	/**
	 @return La difficulté du bot.
	*/
	public BotDifficulty getDifficulty() {
		return this.difficulty;
	}
}
