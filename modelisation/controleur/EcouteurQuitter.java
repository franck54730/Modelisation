package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;

public class EcouteurQuitter implements ActionListener {
	
	/** Attribut m (Modele). */
	protected Modele m;

	public EcouteurQuitter(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

}
