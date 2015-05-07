package modelisation.vue;

import javax.swing.JPanel;

import modelisation.modele.Modele;

public class VueGraphique extends JPanel{

	protected Modele m;
	
	protected VueImage vi;
	
	public VueGraphique(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
		vi = new VueImage(m);
		this.add(vi);
	}
	
}
