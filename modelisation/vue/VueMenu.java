package modelisation.vue;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import modelisation.controleur.EcouteurAnnuler;
import modelisation.controleur.EcouteurAucun;
import modelisation.controleur.EcouteurColonne;
import modelisation.controleur.EcouteurDontCoupe;
import modelisation.controleur.EcouteurFirstCoupe;
import modelisation.controleur.EcouteurLigne;
import modelisation.controleur.EcouteurOccurrence;
import modelisation.controleur.EcouteurParcourir;
import modelisation.controleur.EcouteurQuitter;
import modelisation.controleur.EcouteurSauvegarder;
import modelisation.modele.Modele;
import modelisation.modele.Modele.TypeCoupe;
import modelisation.modele.Modele.TypeSelection;


@SuppressWarnings("serial")
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
	
	protected ImageIcon iconParcourirMenu = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/parcourir-menu.png"));
	protected ImageIcon iconEnregistrer = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/enregistrer.png"));
	protected ImageIcon iconQuitterMenu = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/quitter-menu.png"));
	protected ImageIcon iconSupprColonne = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/supprColonne.png"));
	protected ImageIcon iconSupprLigne = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/supprLigne.png"));
	protected ImageIcon iconDontCoupe = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/dontCoupe.png"));
	protected ImageIcon iconFirstCoupe = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/firstCoupe.png"));
	protected ImageIcon iconOccurence = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/occurence.png"));
	protected ImageIcon iconNoneCoupe = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/noneCoupe.png"));
	protected ImageIcon iconAnnulerCoupe = new ImageIcon(VueMenu.class.getResource("/modelisation/folder/annulerCoupe.png"));
	
	
	public VueMenu(Modele mod) {
		this.m=mod;
		m.addObserver(this);
		JMenu jMenu1 = new JMenu("Fichier");
		JMenu jMenu2 = new JMenu("Options");
		
		JMenu jMenu21 = new JMenu("Coupe");
		jMenu2.add(jMenu21);
		
		JMenu jMenu22 = new JMenu("Sélection");
		jMenu2.add(jMenu22);
		
		jMenuItemOuvrir = new JMenuItem("Ouvrir");
		jMenuItemOuvrir.setIcon(iconParcourirMenu);
		jMenuItemOuvrir.addActionListener(new EcouteurParcourir(m));
		
		jMenuItemEnregistrer = new JMenuItem("Enregistrer-sous");
		jMenuItemEnregistrer.setIcon(iconEnregistrer);
		jMenuItemEnregistrer.addActionListener(new EcouteurSauvegarder(m));
		
		jMenuItemQuitter = new JMenuItem("Quitter");
		jMenuItemQuitter.setIcon(iconQuitterMenu);
		jMenuItemQuitter.addActionListener(new EcouteurQuitter(m));
		
		jMenuIemColonne = new JMenuItem("Sup-colonne");
		jMenuIemColonne.setIcon(iconSupprColonne);
		jMenuIemColonne.addActionListener(new EcouteurColonne(m));
		
		jMenuItemLigne = new JMenuItem("Sup-ligne");
		jMenuItemLigne.setIcon(iconSupprLigne);
		jMenuItemLigne.addActionListener(new EcouteurLigne(m));
		
		jMenuItemDontCoupe = new JMenuItem("Garder");
		jMenuItemDontCoupe.setIcon(iconDontCoupe);
		jMenuItemDontCoupe.addActionListener(new EcouteurDontCoupe(m));
		
		jMenuItemFirstCoupe = new JMenuItem("Supprimer");
		jMenuItemFirstCoupe.setIcon(iconFirstCoupe);
		jMenuItemFirstCoupe.addActionListener(new EcouteurFirstCoupe(m));
		
		jMenuItemOccurence = new JMenuItem("Occurrence");
		jMenuItemOccurence.setIcon(iconOccurence);
		jMenuItemOccurence.addActionListener(new EcouteurOccurrence(m));
		
		jMenuItemAucun = new JMenuItem("Aucun");
		jMenuItemAucun.setIcon(iconNoneCoupe);
		jMenuItemAucun.addActionListener(new EcouteurAucun(m));
		
		jMenuItemAnnuler = new JMenuItem("Annuler");
		jMenuItemAnnuler.setIcon(iconAnnulerCoupe);
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
