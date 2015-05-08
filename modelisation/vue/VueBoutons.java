package modelisation.vue;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modelisation.controleur.EcouteurDemarrer;
import modelisation.controleur.EcouteurParcourir;
import modelisation.controleur.EcouteurQuitter;
import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;
import modelisation.modele.Modele.TypeSelection;

public class VueBoutons extends JPanel implements Observer{
	
	/** Attribut demarrer (JButton). */
	protected JButton demarrer;
	
	/** Attribut parcourir (JButton). */
	protected JButton parcourir;
	
	/** Attribut quitter (JButton). */
	protected JButton quitter;
	
	/** Attribut TypeCoupe (JLabel). */
	protected JLabel typeCoupe;
	
	/** Attribut TypeCoupe (JLabel). */
	protected JLabel typeSelection;
	
	protected JLabel occurence;	
	protected JLabel nbClick;
	
	/** Attribut m (Modele). */
	protected Modele m;
	
	protected ImageIcon iconLigne = new ImageIcon("src/supprLigne.png");
	protected ImageIcon iconColonne = new ImageIcon("src/supprColonne.png");

	protected ImageIcon iconNone = new ImageIcon("src/noneCoupe.png");
	protected ImageIcon iconFirst = new ImageIcon("src/firstCoupe.png");
	protected ImageIcon iconDont = new ImageIcon("src/dontCoupe.png");

	public VueBoutons(Modele mod) {
		// TODO Auto-generated constructor stub
		super();
		
		this.m=mod;
		m.addObserver(this);
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
		
		JPanel p1 = new JPanel();
		p1.setBackground(Color.LIGHT_GRAY);
		p1.add(new JLabel("Mode de coupe : "));
		this.typeCoupe = new JLabel();
		p1.add(typeCoupe);
		typeCoupe.setIcon(m.getTypeCoupe()==TypeCoupe.COLONNE ? iconColonne : iconLigne);

		JPanel p2 = new JPanel();
		p2.setBackground(Color.LIGHT_GRAY);
		p2.add(new JLabel("     Mode de selection : "));
		this.typeSelection = new JLabel();
		p2.add(typeSelection);
		typeSelection.setIcon(iconNone);
		
		JPanel p3 = new JPanel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.PAGE_AXIS));
		p3.setBackground(Color.LIGHT_GRAY);
		p3.add(p1);
		p3.add(p2);
		this.add(p3);
		

		JPanel p4 = new JPanel();
		p4.setBackground(Color.LIGHT_GRAY);
		this.occurence = new JLabel("Nombre d'occurence : "+mod.getOccurence());
		p4.add(occurence);
		typeCoupe.setIcon(m.getTypeCoupe()==TypeCoupe.COLONNE ? iconColonne : iconLigne);

		JPanel p5 = new JPanel();
		p5.setBackground(Color.LIGHT_GRAY);
		this.nbClick = new JLabel("Nombre de clic restant : "+(m.getTypeSelection()!=TypeSelection.NONE?(2-mod.getNbClic()):"0"));
		p5.add(nbClick);
		typeCoupe.setIcon(m.getTypeCoupe()==TypeCoupe.COLONNE ? iconColonne : iconLigne);
		
		JPanel p6 = new JPanel();
		p6.setLayout(new BoxLayout(p6, BoxLayout.PAGE_AXIS));
		p6.setBackground(Color.LIGHT_GRAY);
		p6.add(p4);
		p6.add(p5);
		this.add(p6);
		
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		typeCoupe.setIcon(m.getTypeCoupe()==TypeCoupe.COLONNE ? iconColonne : iconLigne);
		switch (m.getTypeSelection()) {
		case DONT:
			typeSelection.setIcon(iconDont);
			break;
		case FIRST:
			typeSelection.setIcon(iconFirst);
			break;
		case NONE:
			typeSelection.setIcon(iconNone);
			break;
		default:
			break;
		}
		this.nbClick.setText("Nombre de clic restant : "+(m.getTypeSelection()!=TypeSelection.NONE?(2-m.getNbClic()):"0"));
		occurence.setText("Nombre d'occurence : "+m.getOccurence());
		demarrer.setEnabled(!m.IsRun() && m.getFichierSelect() != null);
	}

}
