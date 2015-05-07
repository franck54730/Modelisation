package modelisation.modele;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFileChooser;

public class Modele extends Observable implements Runnable{
	
	private int N;
	private int[][] interest;
	private int[][] interestModif;
	private Graph graphe;
	private ArrayList<Integer> chemin;
	private SeamCarving seamCarving;
	private SeamCarvingRGB seamCarvingRGB;
	
	/**
	 * Nombre de fois que le traitement doit etre effectué (nombre de pixel à supprimer)
	 */
	private static final int NB_COLONNE_SUPPR = 5;
	
	/**
	 * Choix seamCarving à utiliser
	 */
	public enum typeChoix{PGM,PPM};
	
	private typeChoix choixSeamCarving;
	
	/**
	 * Choix du fichier à utiliser
	 */
	private File fichierSelect;
	
	/**
	 * Constructeur de Modele
	 */
	public Modele(){
    	seamCarvingRGB = new SeamCarvingRGB(this);
        seamCarving = new SeamCarving(this);
	}

	/**
	 * Setter pour le choix de seamCarving
	 * @param typeChoix c
	 */
	public void setChoixSeamCarving(typeChoix c){
		choixSeamCarving = c;
	}
	
	/**
	 * Getter pour le choix de seamCarving
	 * @return choixSeamCarving
	 */
	public typeChoix getChoixSeamCarving(){
		return choixSeamCarving;
	}
	
	/**
	 * Setter pour le choix du fichier
	 * @param File f
	 */
	public void setFichierSelect(File f){
		fichierSelect = f;
	}
	
	/**
	 * Getter pour le choix du fichier
	 * @return fichierSelect
	 */
	public File getFichierSelect(){
		return fichierSelect;
	}
	
	/**
	 * Methode fileChooser() qui permet la selection du fichier + settage de choix du fichier
	 * @throws IOException
	 */
	public void fileChooser() throws IOException {
		JFileChooser dialogue = new JFileChooser(new File("."));
        File fichier = null;
       
        if (dialogue.showOpenDialog(null)==
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            setFichierSelect(fichier);
            String fichierSelect = dialogue.getSelectedFile().toString();
            
            System.out.println("Nom du fichier : " + fichierSelect);
            
            if (fichierSelect.lastIndexOf(".") > 0) {
                // On récupère l'extension du fichier
                String ext = fichierSelect.substring(fichierSelect.lastIndexOf("."));
                // Si le fichier est un pgm
                if (ext.equals(".pgm")) {
                    System.out.println("extension: " + ext);
                    setChoixSeamCarving(typeChoix.PGM);
                    seamCarving.lireFichier();
                    //seamCarving(fichier);
                }
                // si le fichier est un ppm
                else if(ext.equals(".ppm")) {
                	System.out.println("extension: " + ext);
                	setChoixSeamCarving(typeChoix.PPM);
                	seamCarvingRGB.lireFichier();
                	//seamCarvingRGB(fichier);
                }
                else {
                	System.out.println("Choisissez un fichier ppm ou pgm.");
                }
            }
            else{
            	System.out.println("Aucun fichier selectionné.");
            }
        }
	}
	
	/**
	 * Methode seamCarving() qui permet le traitement d'une image PGM
	 * @param fichier
	 */
    private void seamCarving() {
    	// TODO Auto-generated method stub
        int boucle = 0;
        for(int i = 0; i < NB_COLONNE_SUPPR; i++){
        	System.out.println((boucle*100)/NB_COLONNE_SUPPR+"%");
        	seamCarving.supprColonne();
        	boucle++;
        }
        System.out.println("100% \nTraitement effectué.");
        seamCarving.writepgm("finalPGM.pgm");
    }

    /**
     * Methode seamCarvingRgb() qui permet le traitement d'une image PPM
     * @param fichier
     */
    private void seamCarvingRGB() {
    	// TODO Auto-generated method stub
    	//sc.interest1();
    	//sc.interest2();
    	//sc.interest3();
    	//sc.interest4();
    		
    	int boucle = 0;
    	for(int i = 0; i < NB_COLONNE_SUPPR; i++){
    		System.out.println((boucle*100)/NB_COLONNE_SUPPR+"%");
    		seamCarvingRGB.supprColonne();
    		boucle++;
    	}
    	System.out.println("100% \nTraitement effectué.");
    	seamCarvingRGB.writepgm("finalPPM.ppm");
    }

    /**
     * Methode run() qui permet de lancer le traitement de l'image
     */
	public void run() {
		// TODO Auto-generated method stub
		seamCarving.firstCoupe(3, 0, 12, 6);
		switch(choixSeamCarving){
			case PGM : seamCarving();
			break;
			case PPM : seamCarvingRGB();
			break;
		}
	}

	/**
	 * fonction qui set N ou N est le nombre du noeud du graph
	 * @param i
	 */
	public void setN(int i) {
		N = i;
	}

	public int getN() {
		return N;
	}

	public void setGraph(Graph g) {
		graphe = g;
	}

	public void setInterest(int[][] inter) {
		interest = inter;
	}

	public Graph getGraphe() {
		return graphe;
	}

	public int[][] getInterest() {
		return interest;
	}

	public void setChemin(ArrayList<Integer> arrayList) {
		chemin = arrayList;
	}

	public ArrayList<Integer> getChemin() {
		return chemin;
	}

	public void setInterestModif(int[][] is) {
		interestModif = is;
	}

	public int[][] getInterestModif() {
		// TODO Stub de la méthode généré automatiquement
		return interestModif;
	}
	
	public Pixel[][] getImage(){
		return choixSeamCarving == typeChoix.PGM ? seamCarving.getImage() : seamCarvingRGB.getImage();
	}
	
}
