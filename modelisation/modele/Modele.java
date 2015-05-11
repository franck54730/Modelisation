package modelisation.modele;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Modele extends Observable implements Runnable{
	
	private int N;
	private int[][] interest;
	private int[][] interestModif;
	private Graph graphe;
	private ArrayList<Integer> chemin;
	private SeamCarving seamCarving;
	private SeamCarvingRGB seamCarvingRGB;
	private int avancement = 0;
	private int nbClick = 0;
	private int height;
	private boolean interetModifier = false;
	
	public boolean isInteretModifier() {
		return interetModifier;
	}

	public void setInteretModifier(boolean interetModifier) {
		this.interetModifier = interetModifier;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	private int width;
	private int posX1;
	private int posX2;
	private int posY1;
	private int posY2;
	
	public enum TypeCoupe {LIGNE,COLONNE};
	private TypeCoupe typeCoupe = TypeCoupe.COLONNE;
	
	public enum TypeSelection {FIRST,DONT,NONE};
	private TypeSelection typeSelection = TypeSelection.NONE;
	
	private boolean run = false;
	
	protected boolean arret = false;
	
	public TypeCoupe getTypeCoupe(){
		return typeCoupe;
	}
	
	public void setTypeCoupe(TypeCoupe t){
		typeCoupe = t;
		miseAJour();
	}
	
	public TypeSelection getTypeSelection(){
		return typeSelection;
	}
	
	public void setTypeSelection(TypeSelection t){
		typeSelection = t;
		miseAJour();
	}
	
	/**
	 * Nombre de fois que le traitement doit etre effectué (nombre de pixel à supprimer)
	 */
	private int occurence = 1;
	
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
	 * Methode fileChooserOpen() qui permet la selection du fichier + settage de choix du fichier
	 * @throws IOException
	 */
	public void fileChooserOpen() throws IOException {
		JFileChooser dialogue = new JFileChooser(new File("."));//"~"));  a décommenté version final
		dialogue.setDialogTitle("Ouvrir un fichier PGM ou PPM");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "PGM & PPM Images", "ppm", "pgm");
		dialogue.setFileFilter(filter);
        File fichier = null;
       
        if (dialogue.showOpenDialog(null)==
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            setFichierSelect(fichier);
            String fichierSelect = dialogue.getSelectedFile().toString();
            
            //System.out.println("Nom du fichier : " + fichierSelect);
            
            if (fichierSelect.lastIndexOf(".") > 0) {
                // On récupère l'extension du fichier
                String ext = fichierSelect.substring(fichierSelect.lastIndexOf("."));
                // Si le fichier est un pgm
                if (ext.equals(".pgm")) {
                    //System.out.println("extension: " + ext);
                    setChoixSeamCarving(typeChoix.PGM);
                    seamCarving.lireFichier();
                    //seamCarving(fichier);
                }
                // si le fichier est un ppm
                else if(ext.equals(".ppm")) {
                	//System.out.println("extension: " + ext);
                	setChoixSeamCarving(typeChoix.PPM);
                	seamCarvingRGB.lireFichier();
                	//seamCarvingRGB(fichier);
                }else {
                	//System.out.println("Choisissez un fichier ppm ou pgm.");
                }
                miseAJour();
            }
            else{
            	//System.out.println("Aucun fichier selectionné.");
            }
        }
	}
	
	/**
	 * Methode fileChooserSave() qui permet la selection du fichier + settage de choix du fichier
	 * @throws IOException
	 */
	public void fileChooserSave() throws IOException {
		JFileChooser dialogue = new JFileChooser(fichierSelect);
		dialogue.setDialogTitle("Sauvegarder votre fichier");  		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PGM & PPM Images", "ppm", "pgm");
		dialogue.setFileFilter(filter); 
		dialogue.setSelectedFile(fichierSelect);
        File fichier = null;
		if (dialogue.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		    fichier = dialogue.getSelectedFile();
		    
		    String fichierSelect = dialogue.getSelectedFile().toString();
		    int index = fichierSelect.lastIndexOf(".");
	    	if(index != fichierSelect.length()-1){
			    if (index != -1) {
	                // On récupère l'extension du fichier
	                String ext = fichierSelect.substring(fichierSelect.lastIndexOf("."));
	                // Si le fichier est un pgm
	                if (ext.equals(".pgm")) {
	                	if(choixSeamCarving != typeChoix.PGM){
	                		//on change pour lui et on l'avertie
	                		JOptionPane.showMessageDialog(null,
	                			    "L'extention que vous avez choisi ne correspond pas a celle de votre fichier\n" +
	                			    "initiale, le programme l'a changé pour vous.",
	                			    "Attention",
	                			    JOptionPane.WARNING_MESSAGE);
	        			    index = fichierSelect.lastIndexOf(".");
	        	    		fichierSelect = fichierSelect.substring(0, index);
	        	    		fichierSelect = fichierSelect+".ppm";
	                	}
	                }
	                // si le fichier est un ppm
	                else if(ext.equals(".ppm")) {
	                	if(choixSeamCarving != typeChoix.PPM){
	                		//on change pour lui et on l'avertie
	                		JOptionPane.showMessageDialog(null,
	                			    "L'extention que vous avez choisi ne correspond pas a celle de votre fichier\n" +
	                			    "initiale, le programme l'a changé pour vous.",
	                			    "Attention",
	                			    JOptionPane.WARNING_MESSAGE);	 
            				index = fichierSelect.lastIndexOf(".");
	        	    		fichierSelect = fichierSelect.substring(0, index);
	        	    		fichierSelect = fichierSelect+".pgm";
	                	}
	                }else {
	                	//on change pour lui 
	                	JOptionPane.showMessageDialog(null,
                			    "L'extention que vous avez choisi ne correspond pas a celle de votre fichier\n" +
                			    "initiale, le programme l'a changé pour vous.",
                			    "Attention",
                			    JOptionPane.WARNING_MESSAGE);
        				index = fichierSelect.lastIndexOf(".");
        	    		fichierSelect = fichierSelect.substring(0, index);
        	    		fichierSelect = fichierSelect+(choixSeamCarving==typeChoix.PPM? ".ppm" : ".pgm");
	                }
	            }else{//pas d'extentions on l'a rajoute
    	    		fichierSelect = fichierSelect+(choixSeamCarving==typeChoix.PPM? ".ppm" : ".pgm");
	            }
	    	}else{// juste un point a la fin
	    		fichierSelect = fichierSelect.substring(0, fichierSelect.length()-2);
	    		fichierSelect = fichierSelect+(choixSeamCarving==typeChoix.PPM? ".ppm" : ".pgm");
	    	}
	    	//System.out.println(fichierSelect);
            //sauvegarde du fichier
	    	if(choixSeamCarving == typeChoix.PGM){
		        seamCarving.writepgm(fichierSelect);
	    	}else{
		        seamCarvingRGB.writepgm(fichierSelect);
	    	}
            miseAJour();
		}
	}
	
	/**
	 * Methode seamCarving() qui permet le traitement d'une image PGM
	 * @param fichier
	 */
    private void seamCarving() {
    	// TODO Auto-generated method stub
        int boucle = 0;

    	if(typeCoupe == TypeCoupe.LIGNE)
    		seamCarving.rotationTabDroite();
        while(!arret){
        	for(int i = 0; i < occurence; i++){
            	avancement = (boucle*100)/occurence;
            	seamCarving.supprColonne();
            	boucle++;
            	miseAJour();
            }
        	arret = true;
        }
    	if(typeCoupe == TypeCoupe.LIGNE)
    		seamCarving.rotationTabGauche();
        avancement = 100;
        setRun(false);
        resetInterestModif();
        miseAJour();
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
    	while(!arret){
    		for(int i = 0; i < occurence; i++){
        		avancement = (boucle*100)/occurence;
            	if(typeCoupe == TypeCoupe.COLONNE)
            		seamCarvingRGB.supprColonne();
            	else
            		seamCarvingRGB.supprLigne();
        		boucle++;
        		miseAJour();
        	}
    		arret = true;
    	}
    	avancement = 100;
    	setRun(false);
        resetInterestModif();
    	miseAJour();
    }
    
    /**
     * echange hauteur et largeur
     */
    public void switchImage(){
    	int tmp = width;
    	width = height;
    	height = tmp;
    }

    /**
     * Methode run() qui permet de lancer le traitement de l'image
     */
	public void run() {
		// TODO Auto-generated method stub
		//seamCarving.firstCoupe(18, 0, 22, 4);
		
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
	
	public void miseAJour(){
		setChanged();
		notifyObservers();
	}
	
	public void setAvancement(int a){
		avancement = a;
	}
	
	public int getAvancement(){
		return avancement;
	}
	
	public boolean IsRun(){
		return run;
	}
	
	public void setRun(boolean b){
		run = b;
	}

	public void doClick(MouseEvent arg0) {
		if(typeSelection != TypeSelection.NONE && fichierSelect != null){
			if(arg0.getY() > interestModif.length || arg0.getX() > interestModif[0].length){
				JOptionPane.showMessageDialog(null,
        			    "Veuillez cliquer sur l'image pour la selection.",
        			    "Attention",
        			    JOptionPane.WARNING_MESSAGE);	
				nbClick = 0;
			}else{
				nbClick++;
		        if(nbClick==1){
		                posX1 = arg0.getX();
		                posY1 = arg0.getY();
		        }
		        if(nbClick==2){
	                posX2 = arg0.getX();
	                posY2 = arg0.getY();
	                nbClick = 0;
	                if(typeSelection == TypeSelection.FIRST){
	                	firstCoupe(posX1, posY1, posX2, posY2);
	                }else {
	                	dontCoupe(posX1, posY1, posX2, posY2);
	                }
	                interetModifier = true;
	                typeSelection = TypeSelection.NONE;
		        }
			}
		}
		miseAJour();
	}
	
	
	public void resetInterestModif(){
		interetModifier = false;
		interestModif = new int[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				interestModif[i][j] = -1;
	}
	
	public void dontCoupe(int l1, int h1, int l2, int h2){
		for(int i = (h1>h2?h2:h1); i <= (h1>h2?h1:h2); i++){
			for(int j = (l1>l2?l2:l1); j <= (l1>l2?l1:l2); j++){
				interestModif[i][j] = Integer.MAX_VALUE;
			}
		}
	}
	
	public void firstCoupe(int l1, int h1, int l2, int h2){
		for(int i = (h1>h2?h2:h1); i <= (h1>h2?h1:h2); i++){
			for(int j = (l1>l2?l2:l1); j <= (l1>l2?l1:l2); j++){
				interestModif[i][j] = 0;
			}
		}
	}
	
	public void setOccurence(int o){
		occurence = o;
		miseAJour();
	}
	
	public int getOccurence(){
		return occurence;
	}
	
	public int getNbClic(){
		return nbClick;
	}
	
	public void setArret(boolean a){
		arret = a;
	}
	
	public boolean getArret(){
		return arret;
	}
}
