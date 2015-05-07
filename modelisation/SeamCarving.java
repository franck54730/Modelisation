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
import java.lang.reflect.Array;
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
	
	public SeamCarving(){
		N = 3;
		interest = new int[3][3];
		ArrayList<Integer> test = new ArrayList<Integer>();
		test.add(0);
		test.add(1);
		test.add(2);
		test.add(3);
		test.add(4);
		test.add(7);
		test.add(5);
		//getCoupeFinale(N,test);
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
	
	public void dontCoupe(int l1, int h1, int l2, int h2){
		for(int i = (h1>h2?h2:h1); i <= (h1>h2?h1:h2); i++){
			for(int j = (l1>l2?l2:l1); j <= (l1>l2?l1:l2); j++){
				int noeud = j*image.length+i+1;
				ArrayList<Edge> leNoeud = (ArrayList<Edge>)g.adj(noeud);
				boolean trouv = false;
				int k = 0;
				while(k < leNoeud.size() && !trouv){
					Edge arrete = leNoeud.get(k);
					if(sortante(arrete, noeud)){
						if(arrete.to>noeud){//si c'est l'arrete qui va vers l'avant
							arrete.capacity = Integer.MAX_VALUE;
						}
					}
				}
			}
		}
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
	
//	public void rechercheChemin(){
//		
//		/* tableau des differents chemins possibles */
//		ArrayList<Integer>[] tabChemin = new ArrayList[interest.length];
//		for (int i = 0; i < tabChemin.length; i++) {
//			tabChemin[i] = new ArrayList<Integer>();
//			tabChemin[i].add(0);
//			tabChemin[i].add(i+1);
//		}
//		/* chemin que l'on va emprunter */
//		chemin = null;
//		/* tableau de "validit�" des differents chemin possibles */
//		boolean[] tabUtilise = new boolean[tabChemin.length];
//		/* on met les cases par d�faut � true */
//		for(int a = 0; a < tabUtilise.length ; a++){
//			tabUtilise[a] = true;
//		}
//		boolean fini = false;
//		
//		/* tant qu'on n'a tout test�  */
//		while(!fini){
//			for(int i=0;i<tabChemin.length;i++){
//				if(tabUtilise[i]){
//					chemin = tabChemin[i];
//					int noeud = chemin.get(chemin.size()-1);
//					boolean trouve = false;
//					ArrayList<Edge> leNoeud = (ArrayList<Edge>)g.adj(noeud);
//					int nbArretes = leNoeud.size();
//					int j = 0;
//					
//					/* tant qu'on n'a pas trouv� de chemin */
//					while(!trouve && j < nbArretes){
//						Edge e = leNoeud.get(j);
//						if(sortante(e,noeud)){
//							if(atteignable(e)){
//								int noeudDest = e.to;
//								if(!parcouru(noeudDest,tabChemin)){
//									chemin.add(noeudDest);
//									trouve = true;
//								}
//							}
//						}
//						j++;
//					}
//					if(!trouve)tabUtilise[i] = false;
//				}
//			}
//			fini = true;
//			for(int b = 0 ; b< tabUtilise.length ; b++){
//				if(tabUtilise[b]){
//					fini = false;
//					b = tabUtilise.length; // on evite de continuer si on a trouv� un chemin 
//				}
//				if(!fini){
//					//si dernier dedans fini
//					if(chemin.contains(N-1))fini=true;
//				}
//			}
//		}
//	}

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
	 * a n'utilis� que si il existe un chemin sinon boucle a l'infini
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
	
	//Methode initFlow qui attribut un flow initial a la totalit� du graph
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
	 * marche bien
	 * prend un tableau qui contient les numeros des noeuds qui doivent etre coup� et les retires dans le tableau image
	 */
	public void supprCoupe(int[] listeA){
		int[][] newImage = new int[image.length][image[0].length-1];
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
		rotationTabDroite();
		toGraph();
		flowMax();
        ArrayList<Integer> s = getNoeudAccessibles();
        int[] coupe = getCoupeFinale(s);
		supprCoupe(coupe);
		rotationTabGauche();
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
	
	public int[][] rotationTabDroite() {
		int[][]res = new int[image[0].length][image.length];
		for (int i = 0; i < image[0].length; i++) {
			for (int j = 0; j < image.length; j++) {
				res[i][j]=image[image.length-j-1][i];
			}
		}
		return res;
	}
	
	public int[][] rotationTabGauche() {
		int[][]res = new int[image[0].length][image.length];
		for (int i = 0; i < image[0].length; i++) {
			for (int j = 0; j < image.length; j++) {
				res[i][j]=image[image.length-j-1][i];
			}
		}
		return res;
	}
	
	public static void main(String[] args)
	 {
			new SeamCarving();
	 }
}
