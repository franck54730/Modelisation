package modelisation.vue;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import modelisation.controleur.EcouteurAnnuler;
import modelisation.controleur.EcouteurArreter;
import modelisation.controleur.EcouteurAucun;
import modelisation.controleur.EcouteurColonne;
import modelisation.controleur.EcouteurDontCoupe;
import modelisation.controleur.EcouteurFirstCoupe;
import modelisation.controleur.EcouteurLigne;
import modelisation.controleur.EcouteurOccurence;
import modelisation.controleur.EcouteurParcourir;
import modelisation.controleur.EcouteurQuitter;
import modelisation.controleur.EcouteurSauvegarder;
import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;
import modelisation.modele.Modele.TypeSelection;


public class VueMenu extends JMenuBar implements Observer {
	
	/** Attribut m (Modele). */
	protected Modele m;
	protected JMenuItem jMenuItemOuvrir;
	protected JMenuItem jMenuItemEnregistrer;
	protected JMenuItem jMenuItemQuitter;
	protected JMenuItem jMenuIemColonne;
	protected JMenuItem jMenuItemLigne;
	protected JMenuItem jMenuItemDontCoupe;
	protected JMenuItem jMenuItemAucun;
	protected JMenuItem jMenuItemFirstCoupe;
	protected JMenuItem jMenuItemOccurence;
	protected JMenuItem jMenuItemAnnuler;
	protected JMenu jMenuArreter;
	
	
	public VueMenu(Modele mod) {
		this.m=mod;
		m.addObserver(this);
		JMenu jMenu1 = new JMenu("Fichier");
		JMenu jMenu2 = new JMenu("Options");
		
		JMenu jMenu21 = new JMenu("Coupe");
		jMenu2.add(jMenu21);
		
		JMenu jMenu22 = new JMenu("Selection");
		jMenu2.add(jMenu22);
		
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
		
		jMenuItemDontCoupe = new JMenuItem("Garder");
		jMenuItemDontCoupe.setIcon(new ImageIcon("src/dontCoupe.png"));
		jMenuItemDontCoupe.addActionListener(new EcouteurDontCoupe(m));
		
		jMenuItemFirstCoupe = new JMenuItem("Supprimer");
		jMenuItemFirstCoupe.setIcon(new ImageIcon("src/firstCoupe.png"));
		jMenuItemFirstCoupe.addActionListener(new EcouteurFirstCoupe(m));
		
		jMenuItemOccurence = new JMenuItem("Occurence");
		jMenuItemOccurence.setIcon(new ImageIcon("src/occurence.png"));
		jMenuItemOccurence.addActionListener(new EcouteurOccurence(m));
		
		jMenuItemAucun = new JMenuItem("Aucun");
		jMenuItemAucun.setIcon(new ImageIcon("src/noneCoupe.png"));
		jMenuItemAucun.addActionListener(new EcouteurAucun(m));
		
		jMenuItemAnnuler = new JMenuItem("Annuler");
		jMenuItemAnnuler.setIcon(new ImageIcon("src/annulerCoupe.png"));
		jMenuItemAnnuler.addActionListener(new EcouteurAnnuler(m));
		
		jMenu1.add(jMenuItemOuvrir);
		jMenu1.add(jMenuItemEnregistrer);
		jMenu1.add(jMenuItemQuitter);
		
		jMenu21.add(jMenuIemColonne);
		jMenu21.add(jMenuItemLigne);
		jMenu21.add(jMenuItemOccurence);
		
		jMenu22.add(jMenuItemDontCoupe);
		jMenu22.add(jMenuItemFirstCoupe);
		jMenu22.add(jMenuItemAucun);
		jMenu22.add(jMenuItemAnnuler);
		
		this.add(jMenu1);
		this.add(jMenu2);
		
		//jMenuItemPerso.addActionListener(new EcouteurPerso(m));
		//jMenuItemTest.addActionListener(new EcouteurTest(m));
		//jMenuItemBulle.addActionListener(new EcouteurBulles(m));
		
	}

	@SuppressWarnings("deprecation")
	public void update(Observable arg0, Object arg1) {
		// TODO Stub de la méthode généré automatiquement

		jMenuItemEnregistrer.setEnabled(m.getFichierSelect() != null && !m.IsRun());
		jMenuIemColonne.setEnabled(m.getTypeCoupe() != TypeCoupe.COLONNE && !m.IsRun());
		jMenuItemLigne.setEnabled(m.getTypeCoupe() != TypeCoupe.LIGNE & !m.IsRun());
		jMenuItemDontCoupe.setEnabled(m.getTypeSelection() != TypeSelection.DONT && !m.IsRun() && m.getFichierSelect() != null);
		jMenuItemFirstCoupe.setEnabled(m.getTypeSelection() != TypeSelection.FIRST && !m.IsRun() && m.getFichierSelect() != null);
		jMenuItemAucun.setEnabled((m.getTypeSelection() == TypeSelection.DONT || m.getTypeSelection() == TypeSelection.FIRST) && !m.IsRun());
		jMenuItemOccurence.setEnabled(!m.IsRun());
		jMenuItemOuvrir.setEnabled(!m.IsRun());
		jMenuItemAnnuler.setEnabled(!m.IsRun() && m.getFichierSelect() != null && m.isInteretModifier());

//		jMenuItemEnregistrer.setEnabled(m.getFichierSelect() != null);
//		jMenuIemColonne.setEnabled(m.getTypeCoupe() != TypeCoupe.COLONNE);
//		jMenuItemLigne.setEnabled(m.getTypeCoupe() != TypeCoupe.LIGNE);
//		
//		jMenuItemDontCoupe.setEnabled(m.getTypeSelection() != TypeSelection.DONT && m.getFichierSelect() != null);
//		jMenuItemFirstCoupe.setEnabled(m.getTypeSelection() != TypeSelection.FIRST && m.getFichierSelect() != null);
//		jMenuItemAnnuler.setEnabled(m.getTypeSelection() == TypeSelection.DONT || m.getTypeSelection() == TypeSelection.FIRST);

	}
	
}
