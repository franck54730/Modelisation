package modelisation.vue;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import modelisation.controleur.EcouteurColonne;
import modelisation.controleur.EcouteurDontCoupe;
import modelisation.controleur.EcouteurFirstCoupe;
import modelisation.controleur.EcouteurLigne;
import modelisation.controleur.EcouteurParcourir;
import modelisation.controleur.EcouteurQuitter;
import modelisation.controleur.EcouteurSauvegarder;
import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;


public class VueMenu extends JMenuBar implements Observer {
	
	/** Attribut m (Modele). */
	protected Modele m;
	protected JMenuItem jMenuItemOuvrir;
	protected JMenuItem jMenuItemEnregistrer;
	protected JMenuItem jMenuItemQuitter;
	protected JMenuItem jMenuIemColonne;
	protected JMenuItem jMenuItemLigne;
	protected JMenuItem jMenuItemDontCoupe;
	protected JMenuItem jMenuItemFirstCoupe;
	
	
	public VueMenu(Modele mod) {
		this.m=mod;
		m.addObserver(this);
		JMenu jMenu1 = new JMenu("Fichier");
		JMenu jMenu2 = new JMenu("Options");
		
		jMenuItemOuvrir = new JMenuItem("Ouvrir");
		jMenuItemOuvrir.setIcon(new ImageIcon("src/parcourir-menu.png"));
		jMenuItemOuvrir.addActionListener(new EcouteurParcourir(m));
		
		jMenuItemEnregistrer = new JMenuItem("Enregistrer-sous");
		jMenuItemEnregistrer.setIcon(new ImageIcon("src/enregistrer.png"));
		jMenuItemEnregistrer.addActionListener(new EcouteurSauvegarder(m));
		
		jMenuItemQuitter = new JMenuItem("Quitter");
		jMenuItemQuitter.setIcon(new ImageIcon("src/quitter-menu.png"));
		jMenuItemQuitter.addActionListener(new EcouteurQuitter(m));
		
		jMenuIemColonne = new JMenuItem("Sup-colonne");
		jMenuIemColonne.setIcon(new ImageIcon("src/supprColonne.png"));
		jMenuIemColonne.addActionListener(new EcouteurColonne(m));
		
		jMenuItemLigne = new JMenuItem("Sup-ligne");
		jMenuItemLigne.setIcon(new ImageIcon("src/supprLigne.png"));
		jMenuItemLigne.addActionListener(new EcouteurLigne(m));
		
		jMenuItemDontCoupe = new JMenuItem("Dont-coupe");
		jMenuItemDontCoupe.setIcon(new ImageIcon("src/dontCoupe.png"));
		jMenuItemDontCoupe.addActionListener(new EcouteurDontCoupe(m));
		
		jMenuItemFirstCoupe = new JMenuItem("First-coupe");
		jMenuItemFirstCoupe.setIcon(new ImageIcon("src/firstCoupe.png"));
		jMenuItemFirstCoupe.addActionListener(new EcouteurFirstCoupe(m));
		
		jMenu1.add(jMenuItemOuvrir);
		jMenu1.add(jMenuItemEnregistrer);
		jMenu1.add(jMenuItemQuitter);
		
		jMenu2.add(jMenuIemColonne);
		jMenu2.add(jMenuItemLigne);
		jMenu2.add(jMenuItemDontCoupe);
		jMenu2.add(jMenuItemFirstCoupe);
		
		this.add(jMenu1);
		this.add(jMenu2);
		
		//jMenuItemPerso.addActionListener(new EcouteurPerso(m));
		//jMenuItemTest.addActionListener(new EcouteurTest(m));
		//jMenuItemBulle.addActionListener(new EcouteurBulles(m));
		
	}

	public void update(Observable arg0, Object arg1) {
		// TODO Stub de la méthode généré automatiquement
		jMenuItemEnregistrer.setEnabled(m.getFichierSelect() != null);
		jMenuIemColonne.setEnabled(m.getTypeCoupe() != TypeCoupe.COLONNE);
		jMenuItemLigne.setEnabled(m.getTypeCoupe() != TypeCoupe.LIGNE);
	}
	
}
