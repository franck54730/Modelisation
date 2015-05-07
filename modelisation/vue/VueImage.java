package modelisation.vue;

import java.awt.Dimension;

import javax.swing.JPanel;

import modelisation.modele.Modele;

public class VueImage extends JPanel{
	
	protected Modele m;
	
	public VueImage(Modele mod) {
        super();
        this.m = mod;
        this.setPreferredSize(new Dimension(500, 500));
        
        // Dessiner image
        repaint();
    }

}
