package fr.hrudyfiesta.othello.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 Un bouton personnalisé avec des paramètres par défaut et un affichage personnalisé, sans le bleu moche de Swing de base.
*/
@SuppressWarnings("unused")
public class CustomButton extends JButton {
	private Color backgroundPressed = Color.WHITE;
	private Color backgroundHovered = new Color(42, 42, 42);
	private Color backgroundNormal = new Color(42, 42, 42);

	private Color foregroundPressed = Color.BLACK;
	private Color foregroundHovered = Color.WHITE;
	private Color foregroundNormal = Color.WHITE;

	private Border borderPressed = new LineBorder(Color.BLACK, 1);
	private Border borderHovered = new LineBorder(Color.WHITE, 3);
	private Border borderNormal = new LineBorder(Color.GRAY, 1);

	public CustomButton() {
		this(null, null);
	}

	public CustomButton(String text) {
		this(text, null);
	}

	public CustomButton(String text, ImageIcon icon) {
		super(text, icon);
		super.setContentAreaFilled(false);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		if (icon != null) this.setVerticalTextPosition(SwingConstants.BOTTOM);

		this.setFont(new Font("Sans-Serif", Font.BOLD, 16));

		this.setBackground(backgroundNormal);
		this.setForeground(foregroundNormal);
		this.setBorder(borderNormal);
		this.setFocusable(false);
	}

	public Color getBackgroundPressed() {
		return backgroundPressed;
	}

	public void setBackgroundPressed(Color backgroundPressed) {
		this.backgroundPressed = backgroundPressed;
	}

	public Color getBackgroundHovered() {
		return backgroundHovered;
	}

	public void setBackgroundHovered(Color backgroundHovered) {
		this.backgroundHovered = backgroundHovered;
	}

	public Color getBackgroundNormal() {
		return backgroundNormal;
	}

	public void setBackgroundNormal(Color backgroundNormal) {
		this.backgroundNormal = backgroundNormal;
	}

	public Color getForegroundPressed() {
		return foregroundPressed;
	}

	public void setForegroundPressed(Color foregroundPressed) {
		this.foregroundPressed = foregroundPressed;
	}

	public Color getForegroundHovered() {
		return foregroundHovered;
	}

	public void setForegroundHovered(Color foregroundHovered) {
		this.foregroundHovered = foregroundHovered;
	}

	public Color getForegroundNormal() {
		return foregroundNormal;
	}

	public void setForegroundNormal(Color foregroundNormal) {
		this.foregroundNormal = foregroundNormal;
	}

	public Border getBorderPressed() {
		return borderPressed;
	}

	public void setBorderPressed(Border borderPressed) {
		this.borderPressed = borderPressed;
	}

	public Border getBorderHovered() {
		return borderHovered;
	}

	public void setBorderHovered(Border borderHovered) {
		this.borderHovered = borderHovered;
	}

	public Border getBorderNormal() {
		return borderNormal;
	}

	public void setBorderNormal(Border borderNormal) {
		this.borderNormal = borderNormal;
	}

	/**
	 Modifie la manière dont le composant est affiché, permet de désactiver la texture moche de Java Swing de base en désactivant le fond,
	 puis le redessinant manuellement.

	 @param g L'objet Graphics utilisé pour dessiner, donné par Swing / AWT.
	*/
	@Override
	protected void paintComponent(Graphics g) {
		if (this.getModel().isPressed()) {
			this.setBackground(this.backgroundPressed);
			this.setForeground(this.foregroundPressed);
			this.setBorder(this.borderPressed);
		}
		else if (this.getModel().isRollover()) {
			this.setBackground(this.backgroundHovered);
			this.setForeground(this.foregroundHovered);
			this.setBorder(this.borderHovered);
		}
		else {
			this.setBackground(this.backgroundNormal);
			this.setForeground(this.foregroundNormal);
			this.setBorder(this.borderNormal);
		}

		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		super.paintComponent(g);
	}

	// Désactive la fonction setContentAreaFilled, vu qu'elle ne doit plus être utilisée.
	@Override
	public void setContentAreaFilled(boolean b) {}
}
