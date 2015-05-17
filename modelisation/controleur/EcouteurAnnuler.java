package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;

public class EcouteurAnnuler implements ActionListener{
	
	protected Modele m;

	public EcouteurAnnuler(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		m.resetInterestModif();
		m.miseAJour();
	}

}
