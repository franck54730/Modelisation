package modelisation.vue;

import java.awt.Dimension;

import javax.swing.JPanel;

import modelisation.modele.Modele;
import modelisation.modele.Pixel;

public class VueImage extends JPanel{
	
	protected Modele m;
	
	protected Pixel[][] image;
	
	public VueImage(Modele mod) {
        super();
        this.m = mod;
        this.setPreferredSize(new Dimension(500, 500));
    }
	
	public void update(){
		repaint();
	}
	

}
