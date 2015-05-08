package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeSelection;

public class EcouteurAucun implements ActionListener{
	
	protected Modele m;

	public EcouteurAucun(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		m.setTypeSelection(TypeSelection.NONE);
	}

}
