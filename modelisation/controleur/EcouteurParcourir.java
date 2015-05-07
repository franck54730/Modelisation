package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import modelisation.modele.Modele;

public class EcouteurParcourir implements ActionListener {
	
	/** Attribut m (Modele). */
	protected Modele m;

	public EcouteurParcourir(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			m.fileChooser();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
