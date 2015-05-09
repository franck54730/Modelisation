package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeSelection;

public class EcouteurArreter implements ActionListener {
	
	protected Modele m;

	public EcouteurArreter(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		m.setOccurence(1);
		m.setTypeSelection(TypeSelection.NONE);
		m.setArret(true);
		m.miseAJour();

	}

}
