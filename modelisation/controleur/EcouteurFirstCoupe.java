package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeSelection;

public class EcouteurFirstCoupe implements ActionListener {

	/** Attribut m (Modele). */
	protected Modele m;
	
	public EcouteurFirstCoupe(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		m.setTypeSelection(TypeSelection.FIRST);
	}

}
