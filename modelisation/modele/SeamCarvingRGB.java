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
	
	private int N;
	private int[][] interest;
	private Graph g;
	private ArrayList<Integer> chemin;
	private File fileName;
	private Pixel[][] image;
	
	public SeamCarvingRGB(File fichier){
		fileName = fichier;
		image = readppm();
	}
	
	public Pixel[][] readppm() {
		try {
			InputStream f = new FileInputStream(fileName);
			BufferedReader d = new BufferedReader(new InputStreamReader(f));
			String magic = d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();
			line = d.readLine();
			s = new Scanner(line);
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
		
		N = image.length * image[0].length+2;
		g = new Graph(N);
		interest = interest3();
		
		//System.out.println("Capacites :");
		
		for(int i=1; i <= image.length;i++){
			g.addEdge(new Edge(0,i,Integer.MAX_VALUE,0));
		}
		
		for(int i = 1; i < N-1; i++){
			
			boolean gauche = 1 <= i && i <= image.length;
			boolean droite = (N-2-image.length) < i && i <= N-2;
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
		for(int i=(N-1)-image.length;i<N-1;i++){
			g.addEdge(new Edge(i,N-1,capacity(i),0));
		}
	}
	
	public int capacity(int N){
		return interest[(N-1)%interest.length][(N-1)/interest.length];
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
        boolean[]tab = new boolean[N];
        coupe.add(0);
        tab[0]=true;
        for(int i = 1;i<=image.length;i++){
        	coupe.add(i);
            tab[i]=true;
        }
        int cmpt = 1;
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
	 * prend un tableau qui contient les numeros des noeuds qui doivent etre coupé et les retires dans le tableau image
	 */
	public void supprCoupe(int[] listeA){
		Pixel[][] newImage = new Pixel[image.length][image[0].length-1];
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
	                }else{
	                        if(supprime) newImage[i][j] = image[i][j+1];
	                        else newImage[i][j] = image[i][j];
	                }
	        }
        }
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
		image = rotationTabDroite();
		toGraph();
		flowMax();
        ArrayList<Integer> s = getNoeudAccessibles();
        int[] coupe = getCoupeFinale(s);
		supprCoupe(coupe);
		image = rotationTabGauche();
	}
	
	public Pixel[][] rotationTabDroite() {
		Pixel[][]res = new Pixel[image[0].length][image.length];
		for (int i = 0; i < image[0].length; i++) {
			for (int j = 0; j < image.length; j++) {
				res[i][j]=image[image.length-j-1][i];
			}
		}
		return res;
	}
	
	public Pixel[][] rotationTabGauche() {
		Pixel[][]res = new Pixel[image[0].length][image.length];
		for (int i = 0; i < image[0].length; i++) {
			for (int j = 0; j < image.length; j++) {
				res[i][j]=image[image.length-j-1][i];
			}
		}
		return res;
	}
	
	public int[] getCoupeFinale(ArrayList<Integer> tab){
		//Ici aux est de la forme largeur / hauteur

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
	
	
	/** Methode interest1 qui renvoie un tableau d'interet de meme taille que le tableau image
	 * Calcul des moyenne des couleurs puis addition des differences entre PixelCourant et PixelAdjacents puis moyenne
	 */

	public int[][] interest1() {
		int hauteur = image.length;
		int[][] rep = null;
		if (hauteur > 0) {
			int largeur = image[0].length;
			rep = new int[hauteur][largeur];
			for (int x = 0; x < largeur; x++) {
				for (int y = 0; y < hauteur; y++) {
					Pixel PixelThis = image[y][x];
					Pixel PixelAvg = new Pixel(0,0,0);
					
					if(x == 0){//pixel de gauche
						PixelAvg = image[y][x+1];
					}
					else if(x == largeur-1){//pixel de droite
						PixelAvg = image[y][x-1];
					}
					else{//autre pixel
						int rAvg = image[y][x+1].getRed() + image[y][x-1].getRed()/2;
						int gAvg = image[y][x+1].getGreen() + image[y][x-1].getGreen()/2;
						int bAvg = image[y][x+1].getBlue() + image[y][x-1].getBlue()/2;
						PixelAvg = new Pixel(rAvg,gAvg,bAvg);
					}
					
					rep[y][x] = (Math.abs(Math.abs(PixelThis.getRed())-Math.abs(PixelAvg.getRed()))+
								 Math.abs(Math.abs(PixelThis.getGreen())-Math.abs(PixelAvg.getGreen()))+
								 Math.abs(Math.abs(PixelThis.getBlue())-Math.abs(PixelAvg.getBlue())))/3;
				}
			}
		}
		
		// Afichage du tableau d'interet
		System.out.println("\n Tableau d'interet 1 :\n");
		for(int i=0; i<rep.length; i++){
			for(int j=0; j<rep[i].length; j++){
				System.out.print(rep[i][j]+" ");
			}
			System.out.println();
		}
		
		return rep;
	}
	
	/** Methode interest2 qui renvoie un tableau d'interet de meme taille que le tableau image
	 * Calcul des moyenne des couleurs puis addition des differences entre PixelCourant et PixelAdjacents
	 */
	
	public int[][] interest2() {
		int hauteur = image.length;
		int[][] rep = null;
		if (hauteur > 0) {
			int largeur = image[0].length;
			rep = new int[hauteur][largeur];
			for (int x = 0; x < largeur; x++) {
				for (int y = 0; y < hauteur; y++) {
					Pixel PixelThis = image[y][x];
					Pixel PixelAvg = new Pixel(0,0,0);
					Pixel PixelInteret = new Pixel(0,0,0);
					
					if(x == 0){//pixel de gauche
						PixelAvg = image[y][x+1];
					}
					else if(x == largeur-1){//pixel de droite
						PixelAvg = image[y][x-1];
					}
					else{//autre pixel
						int rAvg = image[y][x+1].getRed() + image[y][x-1].getRed()/2;
						int gAvg = image[y][x+1].getGreen() + image[y][x-1].getGreen()/2;
						int bAvg = image[y][x+1].getBlue() + image[y][x-1].getBlue()/2;
						PixelAvg = new Pixel(rAvg,gAvg,bAvg);
					}
					 
					PixelInteret = new Pixel(Math.abs(PixelThis.getRed()-PixelAvg.getRed()), Math.abs(PixelThis.getGreen()-PixelAvg.getGreen()), Math.abs(PixelThis.getBlue()-PixelAvg.getBlue()));
					rep[y][x] = PixelInteret.getRed()+PixelInteret.getGreen()+PixelInteret.getBlue();
				}
			}
		}
		
		// Afichage du tableau d'interet
		System.out.println("\n Tableau d'interet 2 :\n");
		for(int i=0; i<rep.length; i++){
			for(int j=0; j<rep[i].length; j++){
				System.out.print(rep[i][j]+" ");
			}
			System.out.println();
		}
		
		return rep;
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
	
	public int[][] interest3(){
        int[][] res = new int[image.length][image[0].length];
        for(int i=0;i<image.length;i++){
                for(int j=0;j<image[0].length;j++){
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
	
	/**
	 * Methode interest4 qui renvoie un tableau d'interet de meme taille que le tableau image
	 * Pas encore comprise mais marche
	 */
	
	public int[][] interest4() {
		int hauteur = image.length;
		int[][] rep = null;
		if (hauteur > 0) {
			int largeur = image[0].length;
			rep = new int[hauteur][largeur];
			for (int x = 0; x < largeur; x++) {
				for (int y = 0; y < hauteur; y++) {
					Pixel PixelThis = image[y][x];
					int interetAdjacent;
					int interetCourant = moyennePixel(PixelThis);
					
					if(x == 0){//pixel de gauche
						interetAdjacent = moyennePixel(image[y][x+1]);
					}
					else if(x == largeur-1){//pixel de droite
						interetAdjacent = moyennePixel(image[y][x-1]);
					}
					else{//autre pixel
						interetAdjacent = (moyennePixel(image[y][x+1])+moyennePixel(image[y][x-1]))/2;
					}
					 
					rep[y][x] = (interetCourant+interetAdjacent)/2;
				}
			}
		}
		
		// Afichage du tableau d'interet
		System.out.println("\n Tableau d'interet 4 :\n");
		for(int i=0; i<rep.length; i++){
			for(int j=0; j<rep[i].length; j++){
				System.out.print(rep[i][j]+" ");
			}
			System.out.println();
		}
		
		return rep;
	}
	
	public void rechercheChemin(){
		chemin = new ArrayList<Integer>();
		boolean fini = false;
		int courant = 0;
		ArrayList<Integer> listeNoeud = new ArrayList<Integer>();
		int[] tabParent = new int[N];
		boolean[] tabMarquer = new boolean[N];
		tabMarquer[0] = true;
		for(int i = 0; i < N; i++){
				tabMarquer[i] = false;
		}
		listeNoeud.add(0);
		while(!fini && courant < listeNoeud.size()){
			int noeudCourant = listeNoeud.get(courant);
			if( noeudCourant == N-1){
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
			getChemin(tabParent);
		else{
			chemin = new ArrayList<Integer>();
			chemin.add(0);
		}
	}
	
	/**
	 * a n'utilisé que si il existe un chemin sinon boucle a l'infini
	 * @param tabParent
	 */
	public void getChemin(int[] tabParent){
		chemin = new ArrayList<Integer>();
		int noeudCourant = N-1;
		do{
			chemin.add(0,noeudCourant);
			noeudCourant = tabParent[noeudCourant];
		}while(chemin.get(0)!=0);
	}
	
	// Methode getArrete qui retourne une arrete entre deux noeuds
	public Edge getArrete(int numeroNoeudSource, int numeroNoeudDestination){
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
			if(!(chemin.get(chemin.size()-1) == N-1)){
				max = true;
			}else{
				nextFlow();
			}
		}
	}
	
	public int[] minFlowMaxByRow(){
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
		boolean droite = N -1-image.length <= noeud && noeud <= N - 2;
		if(noeud==0){
			return 0;
		}
		else if(!droite){
			return noeud+image.length;
		}
		else return N-1;
	}
	
	//Methode initFlow qui attribut un flow initial a la totalité du graph
		public void initFlow(){
			
			int minFlow [] = minFlowMaxByRow();
			
			ArrayList<Edge> arretesNoeudZero = (ArrayList<Edge>) g.adj(0);
			for(int i=0; i<arretesNoeudZero.size(); i++){
				arretesNoeudZero.get(i).used=minFlow[i];
			}
			
			ArrayList<Edge> arretesDernierNoeud = (ArrayList<Edge>) g.adj(N-1);
			for(int i=0; i<arretesDernierNoeud.size(); i++){
				arretesDernierNoeud.get(i).used=minFlow[i];
			}
			
			int compteur = 0;
			
			for(int i=1; i<=N-2-minFlow.length; i++){
				
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
	
	public static void main(String[] args)
	 {
			new SeamCarving();
	 }
}
