package fr.hrudyfiesta.othello.players;

import fr.hrudyfiesta.othello.utils.Coords;

/**
 Interface représentant un joueur générique.
*/
public interface IPlayer {

	/**
	Comportement à utiliser pour jouer un coup.
	Renvoie un ensemble de coordonnées x, y représentant le coup choisi.
	*/
	Coords play();

	/**
	 Gestion du score du joueur
	 */
	public int getScore();
	public void setScore(int score);
	public void increaseScore(int n);
	public void decreaseScore(int n);
}
