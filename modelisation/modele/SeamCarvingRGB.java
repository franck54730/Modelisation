package modelisation.modele;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class SeamCarvingRGB {
	
	private Pixel[][] image;
	private Modele model;
	
	public SeamCarvingRGB(Modele m){
		model = m;
	}
	
	public void lireFichier(){
		image = readppm();
	}
	
	public Pixel[][] readppm() {
		try {
			File test = model.getFichierSelect();
			InputStream f = new FileInputStream(test);
			BufferedReader d = new BufferedReader(new InputStreamReader(f));
			@SuppressWarnings("unused")
			String magic = d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			@SuppressWarnings("resource")
			Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();
			model.setWidth(width);
			model.setHeight(height);
			model.resetInterestModif();
			line = d.readLine();
			s = new Scanner(line);
			@SuppressWarnings("unused")
			int maxVal = s.nextInt();
			Pixel[][] im = new Pixel[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height * width) {
				int r = s.nextInt();
				int g = s.nextInt();
				int b = s.nextInt();
				im[count / width][count % width] = new Pixel(r,g,b);
				count++;
			}
			/*
			// Affichage de l'image
			System.out.println("Image :\n");
			for(int i=0; i<im.length; i++){
				for(int j=0; j<im[i].length; j++){
					System.out.print(im[i][j]);
				}
				System.out.println();
			}
			*/
			return im;
		}

		catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
	}
	
	public void writepgm(String test){
//		   try {
//			   FileWriter fw = new FileWriter(test);
//			   PrintWriter output = new PrintWriter(new BufferedWriter(fw));
//			   output.println("P2");
//			   output.print(image.length+" ");
//			   output.print(image[0].length+"");
//			   output.println("255");
//			   for(int i=0; i<image.length; i++){
//				   for(int j=0; j<image[i].length; j++){
//					    output.print(image[i][j]+" ");
//				   }
//				   output.println("\n");
//			   }
//			   output.flush();
//			   output.close();
//			   System.out.println("fichier pgm cree");
//		   }
//		   catch(IOException ioe){
//				System.out.print(System.err);
//				ioe.printStackTrace();
//			}
		
		int width = image[0].length;
	    int height = image.length;
	    try {
	            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(test)));
	            pw.println("P3");
	            pw.print(width);
	            pw.print(" ");
	            pw.println(height);
	            pw.println("255");
	            for(int i = 0; i< height; i++){
	                    for(int j = 0; j<width; j++){
	                            pw.print(image[i][j].getRed()+" "+image[i][j].getGreen()+" "+image[i][j].getBlue());
	                            pw.print("    ");
	                    }
	                    pw.println("\n");
	            }
	            pw.close();
	    } catch (IOException e) {
	           
	            e.printStackTrace();
	    }
	}

	public void toGraph(){
		
		model.setN(image.length * image[0].length+2);
		model.setGraph(new Graph(model.getN()));
		model.setInterest(interest());
		Graph g = model.getGraphe();
		//System.out.println("Capacites :");
		
		for(int i=1; i <= image.length;i++){
			g.addEdge(new Edge(0,i,Integer.MAX_VALUE,0));
		}
		
		for(int i = 1; i < model.getN()-1; i++){
			
			boolean gauche = 1 <= i && i <= image.length;
			boolean droite = (model.getN()-2-image.length) < i && i <= model.getN()-2;
			boolean haut = (i-1)%image.length == 0;
			boolean bas = i%image.length == 0;
			
			if(!gauche){
				g.addEdge(new Edge(i, i-image.length, Integer.MAX_VALUE, 0));
			}
			if(!gauche && !haut){
				g.addEdge(new Edge(i, i-image.length-1, Integer.MAX_VALUE, 0));
			}
			if(!gauche && !bas){
				g.addEdge(new Edge(i, i-image.length+1, Integer.MAX_VALUE, 0));
			}
			if(!droite){
				g.addEdge(new Edge(i, i+image.length, capacity(i), 0));
			}
		}
		for(int i=(model.getN()-1)-image.length;i<model.getN()-1;i++){
			g.addEdge(new Edge(i,model.getN()-1,capacity(i),0));
		}
	}
	
	public int capacity(int N){
		return model.getInterest()[(N-1)%image.length][(N-1)/image.length];
	}

	// Methode capacity qui retourne la capacity d'une arrete
	public int capacity(Edge e){
		return e.capacity-e.used;
	}
	
	/**
	 * renvoi une arraylist qui contient tous les noeuds qui sont accessible de puis la source
	 */
	public ArrayList<Integer> getNoeudAccessibles(){
		ArrayList<Integer> coupe = new ArrayList<Integer>();
        boolean[]tab = new boolean[model.getN()];
        coupe.add(0);
        tab[0]=true;
        for(int i = 1;i<=image.length;i++){
        	coupe.add(i);
            tab[i]=true;
        }
        int cmpt = 1;
        Graph g = model.getGraphe();
        while(cmpt<coupe.size()){
                ArrayList<Edge> arretes = (ArrayList<Edge>) g.adj(coupe.get(cmpt));
                for(Edge e : arretes){
                        //Edge e = getArrete(j, getSuccesseur(g, j, tabCapMin.length), g);
                        if(sortante(e, coupe.get(cmpt))){
                                if(atteignable(e) ){
                                        if(tab[e.to]==false){
                                        	coupe.add(e.to);
                                            tab[e.to]=true;
                                        }
                                }
                        }      
                }
                //System.out.println(cmpt);
                cmpt++;
        }
		return coupe;
	}
	

	public boolean sortante(Edge e, int noeudDestination){
		return e.from == noeudDestination;
	}
	
	//Methode atteignable qui retourne un boolean permettant de savoir si un noeud est atteignable
	public boolean atteignable (Edge e){
		if(capacity(e) > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * marche bien
	 * prend un tableau qui contient les numeros des noeuds qui doivent etre coup� et les retires dans le tableau image
	 */
	public void supprCoupe(int[] listeA){
		Pixel[][] newImage = new Pixel[image.length][image[0].length-1];
		int[][] interetModif = new int[image.length][image[0].length-1];
        for(int i = 0; i < listeA.length; i++){
	        boolean supprime = false;
	        int source = listeA[i];
	        int height = (source-1)%image.length;
	        int width = (source-1)/image.length;
	       // System.out.println(width+"= width"+height+"=height");
	        for(int j = 0; j < newImage[0].length; j++){
	                if((height == i) && (width == j)){
	                        supprime = true;
	                        //System.out.println(source);
	                        newImage[i][j] = image[i][j+1];
	                        interetModif[i][j] = model.getInterestModif()[i][j+1];
	                }else{
	                        if(supprime){
	                        	newImage[i][j] = image[i][j+1];
	                        	interetModif[i][j] = model.getInterestModif()[i][j+1];
	                        }else{
	                        	newImage[i][j] = image[i][j];
	                        	interetModif[i][j] = model.getInterestModif()[i][j];
	                        }
	                }
	        }
        }
        model.setInterestModif(interetModif);
        image = newImage;
	}
	
	
	public void supprColonne(){
		toGraph();
		flowMax();
        ArrayList<Integer> s = getNoeudAccessibles();
        int[] coupe = getCoupeFinale(s);
		supprCoupe(coupe);
	}
	
	public void supprLigne(){
		image = rotationTabDroite(image);
		model.switchImage();
		toGraph();
		flowMax();
        ArrayList<Integer> s = getNoeudAccessibles();
        int[] coupe = getCoupeFinale(s);
		supprCoupe(coupe);
		image = rotationTabGauche(image);
		model.switchImage();
	}
	
	public Pixel[][] rotationTabDroite(Pixel[][] tableau) {
		Pixel[][]res = new Pixel[tableau[0].length][tableau.length];
        int[][]interetModif = new int[tableau[0].length][tableau.length];
        for (int i = 0; i < tableau[0].length; i++) {
                for (int j = 0; j < tableau.length; j++) {
                        res[i][j]=tableau[tableau.length-j-1][i];
                        interetModif[i][j]=model.getInterestModif()[tableau.length-j-1][i];
                }
        }
        model.setInterestModif(interetModif);
        return res;
	}

	public Pixel[][] rotationTabGauche(Pixel[][] tableau) {
		Pixel[][]res = new Pixel[tableau[0].length][tableau.length];
        int[][]interetModif = new int[tableau[0].length][tableau.length];
        for (int i = 0; i < tableau[0].length; i++) {
                for (int j = 0; j < tableau.length; j++) {
                        res[i][j]=tableau[j][tableau[0].length-i-1];
                        interetModif[i][j]=model.getInterestModif()[j][tableau[0].length-i-1];
                }
        }
        model.setInterestModif(interetModif);
        return res;
	}
	
	public int[] getCoupeFinale(ArrayList<Integer> tab){
		//Ici aux est de la forme largeur / hauteur

		@SuppressWarnings("unchecked")
		ArrayList<Integer>[] aux = new ArrayList[image.length];
		for(int i=0; i<aux.length; i++){
			aux[i] = new ArrayList<Integer>();
		}
		
		for(int i=1; i<tab.size(); i++){
			int ligne = tab.get(i)%image.length;
			ligne = ligne == 0?image.length:ligne;
			ligne--;
			aux[ligne].add(tab.get(i));
		}
		
		
		int retour[];
		retour = new int[image.length];
		
		for(int i=0; i<aux.length; i++){
			retour[i]=aux[i].get(aux[i].size()-1);
			//System.out.println(retour[i]);
		}
		
		return retour;
	}
	
	public int getAverage(int a, int b){
        return (a+b)/2;
	}

	public int getAverageRGB(int a, int b, int c){
        return (a+b+c)/3;
	}
	
	/**
	 * Methode interest3 qui renvoie un tableau d'interet de meme taille que le tableau image
	 * Moyenne des moyennes des RGB Adjacents avec la moyenne RGB courante 
	 */
	
	public int[][] interest(){
        int[][] res = new int[image.length][image[0].length];
        for(int i=0;i<image.length;i++){
                for(int j=0;j<image[0].length;j++){
                	if(model.getInterestModif()[i][j] == -1){
                        if(j != 0 && j != image[0].length-1){ //pixel normal
                                int moyg = getAverageRGB(image[i][j-1].getRed(), image[i][j-1].getGreen(), image[i][j-1].getBlue());
                                int moyd = getAverageRGB(image[i][j+1].getRed(), image[i][j+1].getGreen(), image[i][j+1].getBlue());
                                int moycourant = getAverageRGB(image[i][j].getRed(), image[i][j].getGreen(), image[i][j].getBlue());
                               
                                res[i][j] = Math.abs(moycourant - getAverage(moyg, moyd));
                        }else if(j==0){ //pixel en debut de ligne
                                int moyd = getAverageRGB(image[i][j+1].getRed(), image[i][j+1].getGreen(), image[i][j+1].getBlue());
                                int moycourant = getAverageRGB(image[i][j].getRed(), image[i][j].getGreen(), image[i][j].getBlue());

                                res[i][j] = Math.abs(moycourant - moyd);
                        }else if(j==image[0].length-1){ //pixel en bout de ligne
                                int moyg = getAverageRGB(image[i][j-1].getRed(), image[i][j-1].getGreen(), image[i][j-1].getBlue());
                                int moycourant = getAverageRGB(image[i][j].getRed(), image[i][j].getGreen(), image[i][j].getBlue());

                                res[i][j] = Math.abs(moycourant - moyg);
                        }
                	}else{
                		res[i][j] = model.getInterestModif()[i][j];
                	}
                }
        }
        
        /*
        // Afichage du tableau d'interet
     	System.out.println("\n Tableau d'interet 3 :\n");
     	for(int i=0; i<res.length; i++){
     		for(int j=0; j<res[i].length; j++){
     			System.out.print(res[i][j]+" ");
     		}
     		System.out.println();
     	}
     	*/
        return res;
	}
	

	public int moyennePixel(Pixel p){
		return (p.getRed()+p.getGreen()+p.getBlue())/3;
	}

	public void rechercheChemin(){
		Graph g = model.getGraphe();
		ArrayList<Integer> chemin = new ArrayList<Integer>();
		boolean fini = false;
		int courant = 0;
		ArrayList<Integer> listeNoeud = new ArrayList<Integer>();
		int[] tabParent = new int[model.getN()];
		boolean[] tabMarquer = new boolean[model.getN()];
		tabMarquer[0] = true;
		for(int i = 0; i < model.getN(); i++){
				tabMarquer[i] = false;
		}
		listeNoeud.add(0);
		while(!fini && courant < listeNoeud.size()){
			int noeudCourant = listeNoeud.get(courant);
			if( noeudCourant == model.getN()-1){
				fini = true;
			}else{
				ArrayList<Edge> leNoeud = (ArrayList<Edge>)g.adj(noeudCourant);
				int j = 0;
				//pour chaque noeud fils
				while(j<leNoeud.size()){
					Edge e = leNoeud.get(j);
					if(sortante(e,noeudCourant)){
						if(atteignable(e)){
							if(!tabMarquer[e.to]){
								tabParent[e.to] = e.from;
								listeNoeud.add(e.to);
								tabMarquer[e.to] = true;
							}
						}
					}
					j++;
				}
			}
			courant++;
		}
		if(fini)
			chemin = getChemin(tabParent);
		else{
			chemin = new ArrayList<Integer>();
			chemin.add(0);
		}
		model.setChemin(chemin);
	}
	
	/**
	 * a n'utilis� que si il existe un chemin sinon boucle a l'infini
	 * @param tabParent
	 */
	public ArrayList<Integer> getChemin(int[] tabParent){
		ArrayList<Integer> chemin = new ArrayList<Integer>();
		int noeudCourant = model.getN()-1;
		do{
			chemin.add(0,noeudCourant);
			noeudCourant = tabParent[noeudCourant];
		}while(chemin.get(0)!=0);
		return chemin;
	}
	
	// Methode getArrete qui retourne une arrete entre deux noeuds
	public Edge getArrete(int numeroNoeudSource, int numeroNoeudDestination){
		Graph g = model.getGraphe();
		Edge rep = null;
		ArrayList<Edge> lesArretes = (ArrayList<Edge>) g.adj(numeroNoeudSource);
		for(int i=0; i<lesArretes.size(); i++){
			if(lesArretes.get(i).from == numeroNoeudSource && lesArretes.get(i).to == numeroNoeudDestination){
				rep = lesArretes.get(i);
			}
		}
		return rep;
	}
	
	public void nextFlow(){
		ArrayList<Integer> chemin = model.getChemin();
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < chemin.size()-1; i++) {
			int source = chemin.get(i);
			int destination = chemin.get(i+1);
			Edge e = getArrete(source, destination);
			if(capacity(e)<min) min = capacity(e);
		}
		for (int i = 0; i < chemin.size()-1; i++) {
			int source = chemin.get(i);
			int destination = chemin.get(i+1);
			Edge e = getArrete(source, destination);
			e.used += min;
		}
	}
	
	public void flowMax(){
		initFlow();
		boolean max = false;
		while(!max){
			rechercheChemin();
			ArrayList<Integer> chemin = model.getChemin();
			if(!(chemin.get(chemin.size()-1) == model.getN()-1)){
				max = true;
			}else{
				nextFlow();
			}
		}
	}
	
	public int[] minFlowMaxByRow(){
		int[][] interest = model.getInterest();
		int[] rep = new int[interest.length];
		for(int i = 0; i < interest.length; i++){
			int min = Integer.MAX_VALUE;
			for(int j = 0; j < interest[0].length; j++){
				min = interest[i][j]<min? interest[i][j]:min;
			}
			rep[i] = min;
		}
		return rep;
	}
	
	
	//Methode getSuccesseur qui retourne le successeur d'un noeud
	public int getSuccesseur (int noeud){

		//boolean gauche = 1 <= N && N <= h+1;
		boolean droite = model.getN() -1-image.length <= noeud && noeud <= model.getN() - 2;
		if(noeud==0){
			return 0;
		}
		else if(!droite){
			return noeud+image.length;
		}
		else return model.getN()-1;
	}
	
	//Methode initFlow qui attribut un flow initial a la totalit� du graph
		public void initFlow(){
			Graph g = model.getGraphe();
			int minFlow [] = minFlowMaxByRow();
			
			ArrayList<Edge> arretesNoeudZero = (ArrayList<Edge>) g.adj(0);
			for(int i=0; i<arretesNoeudZero.size(); i++){
				arretesNoeudZero.get(i).used=minFlow[i];
			}
			
			ArrayList<Edge> arretesDernierNoeud = (ArrayList<Edge>) g.adj(model.getN()-1);
			for(int i=0; i<arretesDernierNoeud.size(); i++){
				arretesDernierNoeud.get(i).used=minFlow[i];
			}
			
			@SuppressWarnings("unused")
			int compteur = 0;
			
			for(int i=1; i<=model.getN()-2-minFlow.length; i++){
				
				int nextNoeud = getSuccesseur(i);
				
//				if(compteur == minFlow.length){
//					compteur=1;
//				}
//				else{
//					compteur ++;
//				}
//				
				int ligneCourant = (i-1)%minFlow.length;
				getArrete(i,nextNoeud).used=minFlow[ligneCourant];
				
			}
			
		}

		public Pixel[][] getImage() {
			// TODO Auto-generated method stub
			return image;
		}
}
