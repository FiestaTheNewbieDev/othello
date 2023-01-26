package fr.hrudyfiesta.othello.utils;

/**
 Représente un ensemble de coordonnées x, y.
*/
public class Coords {
	public final int x;
	public final int y;

	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Coords other) {
		return this.x == other.x && this.y == other.y;
	}
}
