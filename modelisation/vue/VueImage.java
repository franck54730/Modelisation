package modelisation.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import modelisation.controleur.EcouteurCoupe;
import modelisation.modele.Modele;
import modelisation.modele.Pixel;

@SuppressWarnings("serial")
public class VueImage extends JPanel implements Observer{
	
	protected Modele m;
	
	protected Pixel[][] image;
	protected int[][] interestModif;
	
	public VueImage(Modele mod) {
        super();
        this.m = mod;
        m.addObserver(this);
        this.setPreferredSize(new Dimension(600, 500));
        this.addMouseListener(new EcouteurCoupe(mod));
    }

	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		image = m.getImage();
		interestModif = m.getInterestModif();
		repaint();
	}
	
	 protected void paintComponent(Graphics g) {
		 
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D) g;
         if(image != null){
        	 for (int i = 0; i < image.length; i++) {
                 for (int j = 0; j < image[0].length; j++) {
                	 int interest = interestModif[i][j];
                	 if(interest == -1){
	                	 g2.setColor(new Color(image[i][j].getRed(),image[i][j].getGreen(),image[i][j].getBlue()));
	                	 g2.drawLine(j,i,j,i);
                	 }else if(interest == Integer.MAX_VALUE){
                		 g2.setColor(new Color(0,255,0));
	                	 g2.drawLine(j,i,j,i);
                	 }else if(interest == 0){
                		 g2.setColor(new Color(255,0,0));
	                	 g2.drawLine(j,i,j,i);
                	 }
                 }
             }
             repaint();
         }
	 }

}
