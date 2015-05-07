package modelisation.vue;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import modelisation.modele.*;
 
class MainClass extends JFrame{
	
	public MainClass(){
		super("Projet Modelisation - Logiciel de traitement d'image");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Modele m = new Modele();
	    
	    VueGraphique vg = new VueGraphique(m);
	    
	    VueBoutons vb = new VueBoutons(m);
	    
	    VueAvancement va = new VueAvancement(m);
	    
	    this.add(va, BorderLayout.NORTH);
	    this.add(vg, BorderLayout.CENTER);
        this.add(vb, BorderLayout.SOUTH);
        this.setJMenuBar(new VueMenu(m));
	    
        m.miseAJour();
	    pack() ;
        setVisible(true);
	}
	
	public static void main(String[] args) {
        new MainClass() ;
    }
	
}