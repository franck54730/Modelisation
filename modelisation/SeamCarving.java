package modelisation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SeamCarving {

	//TODO a degager pour le rendu sert juste a faire des tests
	public SeamCarving(){
		int[][] test = {
				{3,11,24,39},
				{8,21,29,39},
				{74,80,100,200}
		};
		int[][] interrest = interest(test);
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

	public static void Affichage(String fn) throws IOException{
		   
		   InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
	    BufferedReader d = new BufferedReader(new InputStreamReader(f));
	    String line = "";
	    while ((line = d.readLine()) != null) {
	 	   System.out.println(line);
	    }
	}
	
	public static Graph toGraph(int[][]tab){
		
		int N = tab.length * tab[0].length+2;
		Graph rep = new Graph(tab.length * (tab[0].length+2));
		
		for(int i=1; i <= tab.length;i++){
			rep.addEdge(new Edge(0,i,Integer.MAX_VALUE,0));
		}
		
		for(int i = 1; i < N-1; i++){
			
			boolean gauche = 1 <= i && i <= tab.length;
			boolean droite = (N-2-tab.length) < i && i < N-2;
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
				int aux[][] = interest(tab);
				rep.addEdge(new Edge(i, i+tab.length, capacity(aux,N), 0));
			}
		}
		return rep;
	}
	
	public static int capacity(int [][]tab, int N){
		N=N-1;
		/*System.out.println("tab.length = "+tab[0].length);
		System.out.println("N = "+N);
		System.out.println("(N / tab.length) - 1 = "+N/tab[0].length);
		System.out.println("N % tab.length = "+N%tab[0].length);
		System.out.println(tab[N/tab[0].length][N%tab[0].length]);*/
		
		return tab[N/tab[0].length][N%tab[0].length];
	}

	//TODO a degager pour le rendu sert juste a faire des tests
	public static void main(String[] args) {
		
		/*try {
			Affichage("ex1.pgm");
   		} catch (IOException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   	 	}*/
		
		//new SeamCarving();
   	
		int tab[][] = { {8,1,5}, {3,2,9}, {4,6,7} };
		//writepgm(tab, "test");
		
		
		capacity(tab,1);
	}

}

