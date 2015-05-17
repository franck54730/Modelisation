package modelisation.controleur;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import modelisation.modele.Modele;

public class EcouteurCoupe implements MouseListener {

	private Modele model;
	
	public EcouteurCoupe(Modele m){
		model = m;
	}
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Stub de la méthode généré automatiquement
		model.doClick(arg0);
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Stub de la méthode généré automatiquement

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Stub de la méthode généré automatiquement

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Stub de la méthode généré automatiquement

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Stub de la méthode généré automatiquement

	}

}
