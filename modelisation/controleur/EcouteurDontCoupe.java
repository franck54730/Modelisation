package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;
import modelisation.modele.Modele.TypeSelection;

public class EcouteurDontCoupe implements ActionListener {

	/** Attribut m (Modele). */
	protected Modele m;
	
	public EcouteurDontCoupe(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		m.setTypeSelection(TypeSelection.DONT);
	}

}
