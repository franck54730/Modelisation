package modelisation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SeamCarving {
	
	public static int N;
	public static int[][] interest;

	//TODO a degager pour le rendu sert juste a faire des tests
	public SeamCarving(){
		int[][] test = {
				{3,11,24,39},
				{8,21,29,39},
				{74,80,100,200}
		};
		System.out.println("Tableau test :");
		affichageTableau(test);
		Graph g = toGraph(test);
		g.writeFile("src/test.dot");
	}
	
	public static int[][] readpgm(String fn) {
		try {
			InputStream f = ClassLoader.getSystemClassLoader()
					.getResourceAsStream(fn);
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

	public static int[][] interest(int[][] image) {
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
		System.out.println("Tableau d'interets :");
		affichageTableau(rep);
		return rep;
	}
	
	public static void writepgm(int[][] image, String filename){
		   try {
			   FileWriter fw = new FileWriter("src/"+filename+".pgm", true);
			   BufferedWriter output = new BufferedWriter(fw);
			   output.write("P2\n");
			   output.write(image.length+" ");
			   output.write(image[0].length+"\n");
			   output.write("255\n");
			   for(int i=0; i<image.length; i++){
				   for(int j=0; j<image[i].length; j++){
					    output.write(image[i][j]+" ");
				   }
				   output.write("\n");
			   }
			   output.flush();
			   output.close();
			   System.out.println("fichier pgm cree");
		   }
		   catch(IOException ioe){
				System.out.print(System.err);
				ioe.printStackTrace();
				}
	}

	public static void AffichagePgm(String fn) throws IOException{
		   
		InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
	    BufferedReader d = new BufferedReader(new InputStreamReader(f));
	    String line = "";
	    while ((line = d.readLine()) != null) {
	 	   System.out.println(line);
	    }
	}
	
	public static Graph toGraph(int[][]tab){
		
		N = tab.length * tab[0].length+2;
		Graph rep = new Graph(N);
		interest = interest(tab);
		
		System.out.println("Capacites :");
		
		for(int i=1; i <= tab.length;i++){
			rep.addEdge(new Edge(0,i,Integer.MAX_VALUE,0));
		}
		
		for(int i = 1; i < N-1; i++){
			
			boolean gauche = 1 <= i && i <= tab.length;
			boolean droite = (N-2-tab.length) < i && i <= N-2;
			boolean haut = (i-1)%tab.length == 0;
			boolean bas = i%tab.length == 0;
			
			if(!gauche){
				rep.addEdge(new Edge(i, i-tab.length, Integer.MAX_VALUE, 0));
			}
			if(!gauche && !haut){
				rep.addEdge(new Edge(i, i-tab.length-1, Integer.MAX_VALUE, 0));
			}
			if(!gauche && !bas){
				rep.addEdge(new Edge(i, i-tab.length+1, Integer.MAX_VALUE, 0));
			}
			if(!droite){
				rep.addEdge(new Edge(i, i+tab.length, capacity(interest,i), 0));
			}
		}
		for(int i=(N-1)-tab.length;i<N-1;i++){
			rep.addEdge(new Edge(i,N-1,capacity(interest,i),0));
		}
		return rep;
	}
	
	public static int capacity(int [][]tab, int N){
		
		//Affichage capacity
		
		System.out.print(tab[(N-1)%tab.length][(N-1)/tab.length]+" ");
		
		return tab[(N-1)%tab.length][(N-1)/tab.length];
	}
	
	public static void affichageTableau(int [][]tab){
		
		for(int i=0; i<tab.length; i++){
			   for(int j=0; j<tab[i].length; j++){
				    System.out.print(tab[i][j]+" ");
			   }
			   System.out.println();
			}
		System.out.println();
		
	}
	
	public static int[] minFlowMaxByRow(int[][] interest){
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
	public Edge getArrete(int numeroNoeudSource, int numeroNoeudDestination, Graph g){
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
	public int getSuccesseur (Graph g, int Noeud, int h){

		//boolean gauche = 1 <= N && N <= h+1;
		boolean droite = this.N -1-h <= Noeud && Noeud <= this.N - 2;
		if(Noeud==0){
			return 0;
		}
		else if(!droite){
			return Noeud+h;
		}
		else return this.N-1;
	}
	
	//Methode initFlow qui attribut un flow initial a la totalit� du graph
		public void initFlow(Graph g){
			
			int minFlow [] = minFlowMaxByRow(interest);
			
			ArrayList<Edge> arretesNoeudZero = (ArrayList<Edge>) g.adj(0);
			for(int i=0; i<arretesNoeudZero.size(); i++){
				arretesNoeudZero.get(i).used=minFlow[i];
			}
			
			ArrayList<Edge> arretesDernierNoeud = (ArrayList<Edge>) g.adj((this.N)-1);
			for(int i=0; i<arretesDernierNoeud.size(); i++){
				arretesDernierNoeud.get(i).used=minFlow[(minFlow.length)-1];
			}
			
			
			
		}

	//TODO a degager pour le rendu sert juste a faire des tests
	public static void main(String[] args) {
		
		/* Affichage d'un pgm

		try {
			AffichagePgm("ex1.pgm");
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   	 	}*/
		
		new SeamCarving();
	}
	
	/**
	 * prend un tableau representant l'image (les valeurs lu par readpgm)
	 * @param image
	 * @param lesArretes les arretes qui doivent �tre coup�
	 * @return
	 */
	public static int[][] supprCoupe(int[][] image, Edge[] lesArretes){
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
		return rep;
	}

}
