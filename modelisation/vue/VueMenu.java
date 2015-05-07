package modelisation.vue;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import modelisation.modele.Modele;


public class VueMenu extends JMenuBar {
	
	/** Attribut m (Modele). */
	protected Modele m;
	
	public VueMenu(Modele mod) {
		this.m=mod;
		JMenu jMenu1 = new JMenu("Fichier");
		JMenu jMenu2 = new JMenu("Options");
		
		JMenuItem jMenuItemOuvrir = new JMenuItem("Ouvrir");
		jMenuItemOuvrir.setIcon(new ImageIcon("src/parcourir-menu.png"));
		
		JMenuItem jMenuItemEnregistrer = new JMenuItem("Enregistrer-sous");
		jMenuItemEnregistrer.setIcon(new ImageIcon("src/enregistrer.png"));
		
		JMenuItem jMenuItemQuitter = new JMenuItem("Quitter");
		jMenuItemQuitter.setIcon(new ImageIcon("src/quitter-menu.png"));
		
		JMenuItem jMenuIemColonne = new JMenuItem("Sup-colonne");
		jMenuIemColonne.setIcon(new ImageIcon("src/supprColonne.png"));
		
		JMenuItem jMenuItemLigne = new JMenuItem("Sup-ligne");
		jMenuItemLigne.setIcon(new ImageIcon("src/supprLigne.png"));
		
		JMenuItem jMenuItemDontCoupe = new JMenuItem("Dont-coupe");
		jMenuItemDontCoupe.setIcon(new ImageIcon("src/dontCoupe.png"));
		
		JMenuItem jMenuItemFirstCoupe = new JMenuItem("First-coupe");
		jMenuItemFirstCoupe.setIcon(new ImageIcon("src/firstCoupe.png"));
		
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
	
}
