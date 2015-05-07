package modelisation.vue;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import modelisation.controleur.EcouteurDemarrer;
import modelisation.controleur.EcouteurParcourir;
import modelisation.controleur.EcouteurQuitter;
import modelisation.modele.Modele;

public class VueBoutons extends JPanel implements Observer{
	
	/** Attribut demarrer (JButton). */
	protected JButton demarrer;
	
	/** Attribut parcourir (JButton). */
	protected JButton parcourir;
	
	/** Attribut quitter (JButton). */
	protected JButton quitter;
	
	/** Attribut m (Modele). */
	protected Modele m;

	public VueBoutons(Modele mod) {
		// TODO Auto-generated constructor stub
		super();
		this.m=mod;
		this.setBackground(Color.LIGHT_GRAY);
		
		this.parcourir = new JButton();
		this.add(parcourir);
		parcourir.setIcon(new ImageIcon("src/parcourir.png"));
		parcourir.addActionListener(new EcouteurParcourir(m));

		
		this.demarrer = new JButton();
		this.add(demarrer);
		demarrer.setIcon(new ImageIcon("src/demarrer.png"));
		demarrer.addActionListener(new EcouteurDemarrer(m));
		
		this.quitter = new JButton();
		this.add(quitter);
		quitter.setIcon(new ImageIcon("src/quitter.png"));
		quitter.addActionListener(new EcouteurQuitter(m));
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		// Si changement de bouton : m.notifyObservers();
	}

}
