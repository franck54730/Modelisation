package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;

public class EcouteurDemarrer implements ActionListener {

	/** Attribut m (Modele). */
	protected Modele m;
	
	public EcouteurDemarrer(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Thread t = new Thread((Runnable)m, "Traitement-image");
		t.start() ;
		m.setAvancement(0);
	}

}
