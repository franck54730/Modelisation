package modelisation.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;

public class EcouteurOccurence implements ActionListener {
	
	protected Modele m;

	public EcouteurOccurence(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean bon = false;
		Object retour = JOptionPane.showInputDialog(null, "Donnez une occurence","Occurence pour le traitement de l'image", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/occurence.png"),null,"");

		while(!bon){
			try {
				if(retour != null){
					int occurence = Integer.parseInt(retour.toString());
					if(m.getTypeCoupe() == TypeCoupe.COLONNE && m.getWidth()>= occurence || m.getTypeCoupe() == TypeCoupe.LIGNE && m.getHeight() >= occurence){
						m.setOccurence(occurence);
						bon = true;
					}else{
						retour = JOptionPane.showInputDialog(null, "Donnez une occurence valide\nLe nombre d�passe la taille de l'image","Occurence pour le traitement de l'image", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/occurence.png"),null,"");
						bon = false;
					}
				}
				
			}
			catch(Exception ex){
				retour = JOptionPane.showInputDialog(null, "Donnez une occurence valide\nCeci n'est pas un nombre","Occurence pour le traitement de l'image", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("src/occurence.png"),null,"");
				bon = false;
			}
		}
		
	}

}
