package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import modelisation.modele.Modele;

public class EcouteurOccurence implements ActionListener {
	
	protected Modele m;

	public EcouteurOccurence(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean bon = false;
		while(!bon){
			Object retour = JOptionPane.showInputDialog(null, "Donnez l'occurence","Occurence pour le traintement de l'image", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/occurence.png"),null,"");
			try {
				if(retour != null){
					int occurence = Integer.parseInt(retour.toString());
					m.setOccurence(occurence);
				}
				bon = true;
			}
			catch(Exception ex){
				bon = false;
			}
		}
		
	}

}
