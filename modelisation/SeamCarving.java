package modelisation;

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
import java.util.Random;
import java.util.Scanner;

public class SeamCarving {
	
	private int N;
	private int[][] interest;
	private Graph g;
	private int[][] image;
	private File fileName;
	private ArrayList<Integer> chemin;

	//TODO a degager pour le rendu sert juste a faire des tests
	public SeamCarving(File fichier){
		fileName = fichier;
		image = readpgm();
		//g.writeFile("src/test.dot");
	}
	
	public int[][] readpgm() {
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
			int[][] im = new int[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = s.nextInt();
				count++;
			}
			return im;
		}

		catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
	}

	public int[][] interest() {
		int hauteur = image.length;
		int[][] rep = null;
		if (hauteur > 0) {
			int largeur = image[0].length;
			rep = new int[hauteur][largeur];
			for (int x = 0; x < largeur; x++) {
				for (int y = 0; y < hauteur; y++) {
					int valeurThis = image[y][x];
					int valeurAvg = 0;
					if(x == 0){//pixel de gauche
						valeurAvg = image[y][x+1];
					}else if(x == largeur-1){//pixel de droite
						valeurAvg = image[y][x-1];
					}else{//autre pixel
						valeurAvg = (image[y][x+1]+image[y][x-1])/2;
					}
					rep[y][x] =  Math.abs(Math.abs(valeurThis)-Math.abs(valeurAvg));
				}
			}
		}
		
		// Afichage du tableau d'interet
		//affichageTableau(rep);
		return rep;
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
	            pw.println("P2");
	            pw.print(width);
	            pw.print(" ");
	            pw.println(height);
	            pw.println("255");
	            for(int i = 0; i< height; i++){
	                    for(int j = 0; j<width; j++){
	                            pw.print(image[i][j]);
	                            pw.print(" ");
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
		interest = interest();
		
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
		
		//Affichage capacity
		
		//System.out.print(interest[(N-1)%interest.length][(N-1)/interest.length]+" ");
		
		return interest[(N-1)%interest.length][(N-1)/interest.length];
	}
	
	public void affichageTableau(int [][]tab){
		
		for(int i=0; i<tab.length; i++){
			   for(int j=0; j<tab[i].length; j++){
				    System.out.print(tab[i][j]+" ");
			   }
			   System.out.println();
			}
		System.out.println();
		
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
	
	// Methode capacity qui retourne la capacity d'une arrete
	public int capacity(Edge e){
		return e.capacity-e.used;
	}
	
	//Methode atteignable qui retourne un boolean permettant de savoir si un noeud est atteignable
	public boolean atteignable (Edge e){
		if(capacity(e) > 0){
			return true;
		}
		return false;
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
	
	public Edge[] getCoupe(){
		Edge[] rep = new Edge[image.length];
        boolean[] tabTrouve = new boolean[image.length];
        int cmptIterations = 0;
        for (int i = 0; i < tabTrouve.length; i++) {
                tabTrouve[i]=false;
        }
        ArrayList<Edge>[] lesNoeuds = g.getAdj();
        boolean fini = false;
        int ligneCourante = 0;
        int i = 1;
        while(!fini && i<lesNoeuds.length-1){
       
                if(!tabTrouve[ligneCourante]){
                        Edge e = getArrete(i, getSuccesseur(i));
                        //int succ = getSuccesseur(i);
                        //System.out.println(i+" "+succ);
                        if(capacity(e) == 0){
                                cmptIterations++;
                                rep[ligneCourante] = e;
                               
                                tabTrouve[ligneCourante]=true;
                        }
                        //System.out.println(tabTrouve.length+" "+cmptIterations);
                        fini = (tabTrouve.length == cmptIterations);
                       
                        //ligneCourante++;
                        if(ligneCourante == image.length)ligneCourante=0;
                }
                ligneCourante=(i)%tabTrouve.length;
                i++;
        }
        return rep;
	}
	/*
	public Edge[] getCoupeProfondeur(){
		Edge[] rep = new Edge[image.length];
        ArrayList[] alArrete = new ArrayList[image.length];
        for(int i = 0; i < image.length; i++){
        	alArrete[i] = new ArrayList<Edge>();
        }
        for(int i = 0; i < image.length;i++ ){//pour chaque ligne
        	ArrayList<Edge> allMinArrete = alArrete[i];
        	int courant = i+1;
        	int succ = getSuccesseur(courant);
    		int minCapa = Integer.MAX_VALUE;
        	while(succ != N-1){
        		Edge arrete = getArrete(courant, succ);
        		if(capacity(arrete) == 0){
        			if(arrete.capacity < minCapa){
        				minCapa = arrete.capacity;
        				allMinArrete.clear();
        				allMinArrete.add(arrete);
        			}else if(arrete.capacity == minCapa){
        				allMinArrete.add(arrete);
        			}
        		}
        		courant = succ;
        		succ = getSuccesseur(courant);
        	}
        }
    	for(int i = 0; i < alArrete.length; i++){
    		Random r = new Random();
    		int index = r.nextInt(alArrete[i].size());
    		Edge choisi = (Edge)alArrete[i].get(index);
    		rep[i] = choisi;
    	}
        /*
        int ligneCourante = 0;
        int i = 1;
        while(!fini && i<lesNoeuds.length-1){
       
                if(!tabTrouve[ligneCourante]){
                        Edge e = getArrete(i, getSuccesseur(i));
                        //int succ = getSuccesseur(i);
                        //System.out.println(i+" "+succ);
                        if(capacity(e) == 0){
                                cmptIterations++;
                                rep[ligneCourante] = e;
                               
                                tabTrouve[ligneCourante]=true;
                        }
                        //System.out.println(tabTrouve.length+" "+cmptIterations);
                        fini = (tabTrouve.length == cmptIterations);
                       
                        //ligneCourante++;
                        if(ligneCourante == image.length)ligneCourante=0;
                }
                ligneCourante=(i)%tabTrouve.length;
                i++;
        }
        return rep;
	}
	
	public Edge[] getCoupeContigu(){
		Edge[] rep = new Edge[image.length];
    	ArrayList<Edge> arreteMin = new ArrayList<Edge>();
    	int courant = 1;
    	int succ = getSuccesseur(courant);
		int minCapa = Integer.MAX_VALUE;
    	while(succ != N-1){
    		Edge arrete = getArrete(courant, succ);
    		if(capacity(arrete) == 0){
    			if(arrete.capacity < minCapa){
    				minCapa = arrete.capacity;
    				arreteMin.clear();
    				arreteMin.add(arrete);
    			}else if(arrete.capacity == minCapa){
    				arreteMin.add(arrete);
    			}
    		}
    		courant = succ;
    		succ = getSuccesseur(courant);
        }
		Random r = new Random();
		int index = r.nextInt(arreteMin.size());
		Edge choisi = arreteMin.get(index);
		System.out.println(choisi);
		rep[0] = choisi;
		for(int ligne = 1; ligne < image.length; ligne++){
			minCapa = Integer.MAX_VALUE;
			ArrayList<Edge> suivants = new ArrayList<Edge>();//toto(choisi);
			ArrayList<Edge> meilleurEdgeCapa = new ArrayList<Edge>();
			for(int i = 0; i < suivants.size();i++){
				Edge suivant = suivants.get(i);
				int capacity = capacity(suivant); 
				if(capacity < minCapa){
					minCapa = capacity;
					meilleurEdgeCapa.clear();
					meilleurEdgeCapa.add(suivant);
				}else if(capacity == minCapa){
					meilleurEdgeCapa.add(suivant);
				}
			}
			Edge good = null;
			if(meilleurEdgeCapa.size() == 1){
				
			}else{
				minCapa = Integer.MAX_VALUE;
				for(int i = 0; i < meilleurEdgeCapa.size();i++){
					Edge suivant = meilleurEdgeCapa.get(i);
					if(suivant.capacity < minCapa){
	    				minCapa = suivant.capacity;
	    				good =suivant;
	    			}
				}
			}
			rep[1]=good;
		}
        /*
        int ligneCourante = 0;
        int i = 1;
        while(!fini && i<lesNoeuds.length-1){
       
                if(!tabTrouve[ligneCourante]){
                        Edge e = getArrete(i, getSuccesseur(i));
                        //int succ = getSuccesseur(i);
                        //System.out.println(i+" "+succ);
                        if(capacity(e) == 0){
                                cmptIterations++;
                                rep[ligneCourante] = e;
                               
                                tabTrouve[ligneCourante]=true;
                        }
                        //System.out.println(tabTrouve.length+" "+cmptIterations);
                        fini = (tabTrouve.length == cmptIterations);
                       
                        //ligneCourante++;
                        if(ligneCourante == image.length)ligneCourante=0;
                }
                ligneCourante=(i)%tabTrouve.length;
                i++;
        }
        return rep;
	}
	*/
	
	public ArrayList<Edge> suiveur(Edge e){
		ArrayList<Edge> rep = new ArrayList<Edge>();
		int noeudBase = e.from;
		int[] noeudSuivants = new int[3];
		noeudSuivants[0] = noeudBase+1-image.length;
		noeudSuivants[1] = noeudBase+1;
		noeudSuivants[2] = noeudBase+1+image.length;
		
		for(int i = 0; i < 3; i++){
			int courant = noeudSuivants[i];
			if(courant>0 && courant < N-1-image.length){
				int succ = getSuccesseur(courant);
				rep.add(getArrete(courant, succ));
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

	public boolean parcouru(int noeud, ArrayList<Integer>[] tab){
		boolean res = false;
		for(int i = 0 ; i < tab.length ; i++){
			if(tab[i].contains(noeud))res = true;
		}
		return res;
	}

	public boolean sortante(Edge e, int noeudDestination){
		return e.from == noeudDestination;
	}
	
	public void rechercheChemin(){
		
		/* tableau des differents chemins possibles */
		ArrayList<Integer>[] tabChemin = new ArrayList[interest.length];
		for (int i = 0; i < tabChemin.length; i++) {
			tabChemin[i] = new ArrayList<Integer>();
			tabChemin[i].add(0);
			tabChemin[i].add(i+1);
		}
		/* chemin que l'on va emprunter */
		chemin = null;
		/* tableau de "validité" des differents chemin possibles */
		boolean[] tabUtilise = new boolean[tabChemin.length];
		/* on met les cases par défaut à true */
		for(int a = 0; a < tabUtilise.length ; a++){
			tabUtilise[a] = true;
		}
		boolean fini = false;
		
		/* tant qu'on n'a tout testé  */
		while(!fini){
			for(int i=0;i<tabChemin.length;i++){
				if(tabUtilise[i]){
					chemin = tabChemin[i];
					int noeud = chemin.get(chemin.size()-1);
					boolean trouve = false;
					ArrayList<Edge> leNoeud = (ArrayList<Edge>)g.adj(noeud);
					int nbArretes = leNoeud.size();
					int j = 0;
					
					/* tant qu'on n'a pas trouvé de chemin */
					while(!trouve && j < nbArretes){
						Edge e = leNoeud.get(j);
						if(sortante(e,noeud)){
							if(atteignable(e)){
								int noeudDest = e.to;
								if(!parcouru(noeudDest,tabChemin)){
									chemin.add(noeudDest);
									trouve = true;
								}
							}
						}
						j++;
					}
					if(!trouve)tabUtilise[i] = false;
				}
			}
			fini = true;
			for(int b = 0 ; b< tabUtilise.length ; b++){
				if(tabUtilise[b]){
					fini = false;
					b = tabUtilise.length; // on evite de continuer si on a trouvé un chemin 
				}
				if(!fini){
					//si dernier dedans fini
					if(chemin.contains(N-1))fini=true;
				}
			}
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
			
//			if(compteur == minFlow.length){
//				compteur=1;
//			}
//			else{
//				compteur ++;
//			}
//			
			int ligneCourant = (i-1)%minFlow.length;
			getArrete(i,nextNoeud).used=minFlow[ligneCourant];
			
		}
		
	}
	
	/**
	 * prend un tableau representant l'image (les valeurs lu par readpgm)
	 * @param image
	 * @param lesArretes les arretes qui doivent être coupé
	 * @return
	 */
	public void supprCoupe(Edge[] lesArretes){
		int[][] rep = new int[image.length][image[0].length-1];
		for(int indexI = 0; indexI < image.length; indexI++){
			boolean suppr = false;
			Edge arrete = lesArretes[indexI];
			int src = arrete.from;
			int h = (src-1)%image.length;
			int l = (src-1)/image[0].length;
			for(int indexJ = 0; indexJ < rep[0].length; indexJ++){
				if((h == indexI) && (l == indexJ)){
					suppr = true;
					rep[indexI][indexJ] = image[indexI][indexJ+1];
				}else{
					if(suppr)
						rep[indexI][indexJ] = image[indexI][indexJ+1];
					else
						rep[indexI][indexJ] = image[indexI][indexJ];
				}
			}
		}
		image = rep;
	}
	
	public void supprColonne(){
		toGraph();
		flowMax();
		Edge[] coupe = getCoupe();
		supprCoupe(coupe);
	}
	

	public String toStringGraph(){
		StringBuilder sb = new StringBuilder();
			int ligne = 0;
			for(int i = 0; i < image.length; i++){
				Edge e = getArrete(0, i+1);
				sb.append("("+e.from+")"+"--"+e.used+"/"+Math.round(e.capacity)+"->"+"("+e.to+")");
				int succ = i+1;
				while(succ != N-1){
					int succ2 = getSuccesseur(succ);
					e = getArrete(succ, succ2);
					sb.append("--"+e.used+"/"+Math.round(e.capacity)+"->"+"("+e.to+")");
					succ = succ2;
				}
				sb.append("\n");
			}
			return sb.toString();
	}
}
