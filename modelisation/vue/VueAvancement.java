package modelisation.vue;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import modelisation.modele.Modele;

public class VueAvancement extends JPanel implements Observer{
	
	protected Modele m;
	protected JLabel jTexte;

	public VueAvancement(Modele mod) {
		// TODO Auto-generated constructor stub
		super();
		this.m=mod;
		m.addObserver(this);
		this.setBackground(Color.LIGHT_GRAY);
		
		this.jTexte = new JLabel("Traitement de l'image : 0%");
		this.add(jTexte);
		
	}

	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(m.getAvancement()!=100){
			jTexte.setText("Traitement de l'image : "+m.getAvancement()+"%");
		}
		else{
			jTexte.setText("Traitement de l'image effectué");
		}
		
	}

}
