package fr.hrudyfiesta.othello.utils;

import fr.hrudyfiesta.othello.Game;
import fr.hrudyfiesta.othello.players.HumanPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 Représente une case du plateau.
*/
public class Case extends JButton implements ActionListener {
	// Représente le type de la case. 0: Aucun pion n'est dessus. 1: Le pion dessus est celui du joueur 1. 2: Le pion dessus est celui du joueur 2.
	private volatile int type;

	private final int x;
	private final int y;

	/**
	 Booléen qui représente si la case doit être affichée comme valide ou non.
	*/
	public volatile boolean isPossible;

	public Case(int x, int y) {
		super();

		this.addActionListener(this);

		this.x = x;
		this.y = y;

		this.setPreferredSize(new Dimension(32, 32));
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setOpaque(false);
	}

	/**
	 Définit le type actuel de la case.

	 @param type Le type de la case. (0: Aucun pion n'est dessus. 1: Le pion dessus est celui du joueur 1. 2: Le pion dessus est celui du joueur 2)
	*/
	public void setType(int type) {
		if (type >= 0 && type < 3) this.type = type;
	}

	/**
	 Renvoie le type actuel de la case. (0: Aucun pion n'est dessus. 1: Le pion dessus est celui du joueur 1. 2: Le pion dessus est celui du joueur 2)
	 */
	public int getType() {
		return type;
	}


	/**
	 Fonction responsable de l'affichage de la case dans la console et du rafraîchissement de l'interface graphique.
	*/
	public void draw() {
		if (isPossible) System.out.print("[.]");
		else if (this.type == 0) System.out.print("[ ]");
		else System.out.print("[" + this.type + "]");


		this.paintComponent(this.getGraphics());
	}

	/**
	 Gère l'affichage de la case dans l'interface graphique.

	 @param g L'objet Graphics donné par Swing / AWT.
	*/
	@Override
	protected void paintComponent(Graphics g) {
		try {
			if (Game.texture == null) return;

			g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 0, 0, 16, 16, null);

			if (this.isPossible && Game.getCurrentPlayer() == Game.getPlayer1()) g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 48, 0, 64, 16, null);
			else if (this.isPossible) g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 32, 0, 48, 16, null);
			else if (this.type == 1) g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 0, 16, 16, 32, null);
			else if (this.type == 2) g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 16, 16, 32, 32, null);

			if (this.getModel().isRollover() && this.isPossible && Game.getCurrentPlayer() instanceof HumanPlayer) g.drawImage(Game.texture, 0, 0, this.getWidth(), this.getHeight(), 16, 0, 32, 16, null);
		}
		catch (Exception ignore) {}
	}

	/**
	 Gère le clique de la case avec Java Swing

	 @param e L'évènement donné par Swing / AWT.
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() != this || !this.getModel().isRollover()) return;

		if (Game.getCurrentPlayer() instanceof HumanPlayer) {
			HumanPlayer player = (HumanPlayer) Game.getCurrentPlayer();

			player.playX = this.x;
			player.playY = this.y;
		}
	}
}
