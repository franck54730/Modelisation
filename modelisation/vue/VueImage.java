package modelisation.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import modelisation.modele.Modele;
import modelisation.modele.Pixel;

public class VueImage extends JPanel implements Observer{
	
	protected Modele m;
	
	protected Pixel[][] image;
	
	public VueImage(Modele mod) {
        super();
        this.m = mod;
        m.addObserver(this);
        this.setPreferredSize(new Dimension(500, 500));
    }

	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		image = m.getImage();
		repaint();
	}
	
	 protected void paintComponent(Graphics g) {
		 
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D) g;
         if(image != null){
        	 for (int i = 0; i < image.length; i++) {
                 for (int j = 0; j < image[0].length; j++) {
                	 g2.setColor(new Color(image[i][j].getRed(),image[i][j].getGreen(),image[i][j].getBlue()));
                	 g2.drawLine(j,30+i,j,30+i);
                 }
             }
             repaint();
         }
	 }

}
