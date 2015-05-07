package modelisation.vue;

import javax.swing.JFrame;

import modelisation.modele.*;
 
class MainClass extends JFrame{
	
	public MainClass(){
		super("Projet Modelisation - Traitements d'images");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Modele m = new Modele();
	    m.run();
	}
	
	public static void main(String[] args) {
        new MainClass() ;
    }
	
}