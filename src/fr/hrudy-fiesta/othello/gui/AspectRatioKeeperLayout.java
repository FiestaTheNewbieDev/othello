package fr.hrudyfiesta.othello.gui;

import javax.swing.*;
import java.awt.*;

/**
 Un layout personnalisé pour Swing qui permet de garder le ratio de dimension d'un objet donné.
 Il ne supporte qu'un seul objet à la fois, sinon une Exception est renvoyée.

 Adapté du travail de @fmarot sur Github:
 https://gist.github.com/fmarot/f04346d0e989baef1f56ffd83bbf764d
*/
public class AspectRatioKeeperLayout implements LayoutManager {

	// Utilisé pour les calculs du ratio si aucun autre objet n'est présent.
	private static Component fakeComponent = new JPanel();

	public AspectRatioKeeperLayout() {
		fakeComponent.setPreferredSize(new Dimension(0, 0));
	}

	@Override
	public void layoutContainer(Container parent) {
		Component component = getSingleComponent(parent);
		Insets insets = parent.getInsets();
		int maxWidth = parent.getWidth() - (insets.left + insets.right);
		int maxHeight = parent.getHeight() - (insets.top + insets.bottom);

		Dimension prefferedSize = component.getPreferredSize();
		Dimension targetDim = getScaledDimension(prefferedSize, new Dimension(maxWidth, maxHeight));

		double targetWidth = targetDim.getWidth();
		double targetHeight = targetDim.getHeight();

		double hgap = (maxWidth - targetWidth) / 2;
		double vgap = (maxHeight - targetHeight) / 2;

		// Définit la taille et la position de l'objet.
		component.setBounds((int) hgap, (int) vgap, (int) targetWidth, (int) targetHeight);
	}

	private Component getSingleComponent(Container parent) {
		int parentComponentCount = parent.getComponentCount();

		if (parentComponentCount > 1) throw new IllegalArgumentException(this.getClass().getSimpleName() + " ne supporte pas plus d'un élément.");

		if (parentComponentCount == 1) return parent.getComponent(0);
		else return fakeComponent;
	}

	private Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {
		double widthRatio = boundary.getWidth() / imageSize.getWidth();
		double heightRatio = boundary.getHeight() / imageSize.getHeight();
		double ratio = Math.min(widthRatio, heightRatio);

		return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getSingleComponent(parent).getPreferredSize();
	}


	// Fonctions inutiles puisque l'objet ne peut contenir qu'un seul élément.
	@Override
	public void addLayoutComponent(String arg0, Component arg1) {}

	@Override
	public void removeLayoutComponent(Component parent) {}

}