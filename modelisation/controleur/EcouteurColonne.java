package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;

public class EcouteurColonne implements ActionListener {

	/** Attribut m (Modele). */
	protected Modele m;
	
	public EcouteurColonne(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		m.setTypeCoupe(TypeCoupe.COLONNE);
		m.setOccurence(1);
	}

}
