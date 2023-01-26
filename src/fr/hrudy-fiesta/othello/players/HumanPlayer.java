package fr.hrudyfiesta.othello.players;

import fr.hrudyfiesta.othello.utils.Coords;

/**
 Classe représentant un joueur humain.
*/
public class HumanPlayer implements IPlayer {
	// Score du joueur
	private int score = 0;

	public volatile int playX = -1;
	public volatile int playY = -1;

	/**
	 Comportement à utiliser pour jouer un coup.
	 Renvoie un ensemble de coordonnées x, y représentant le coup choisi.
	*/
	@Override
	public Coords play() {
		Coords result = new Coords(this.playX, this.playY);
		return result;
	}

	/**
	 @return Le score du joueur
	 */
	@Override
	public int getScore() {
		return this.score;
	}

	/**
	 Définis le score du joueur.

	 @param score Valeur à utiliser
	 */
	@Override
	public void setScore(int score) {
		this.score = score;
	}


	/**
	 Augmente le score du joueur de n.

	 @param n Valeur à ajouter au score du joueur.
	 */
	@Override
	public void increaseScore(int n) {
		this.score = this.score + n;
	}

	/**
	 Diminue le score du joueur de n.

	 @param n Valeur à soustraire au score du joueur.
	 */
	@Override
	public void decreaseScore(int n) {
		this.score = score - n;
	}
}
