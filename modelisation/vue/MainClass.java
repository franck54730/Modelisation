package modelisation.vue;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import modelisation.modele.*;
 
class MainClass extends JFrame{
	
	public MainClass(){
		super("Projet Modelisation - Traitements d'images");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Modele m = new Modele();
	    
	    VueGraphique vg = new VueGraphique(m);
	    
	    VueBoutons vb = new VueBoutons(m);
	    
	    this.add(vg, BorderLayout.CENTER);
        this.add(vb, BorderLayout.SOUTH);
	    
	    pack() ;
        setVisible(true);
	}
	
	public static void main(String[] args) {
        new MainClass() ;
    }
	
}