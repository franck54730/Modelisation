package modelisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SeamCarvingRGB {

	private File fileName;
	private Pixel[][] image;
	
	SeamCarvingRGB(File fichier){
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
			
			// Affichage de l'image
			System.out.println("Image :\n");
			for(int i=0; i<im.length; i++){
				for(int j=0; j<im[i].length; j++){
					System.out.print(im[i][j]);
				}
				System.out.println();
			}
			
			return im;
		}

		catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
	}
	
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
        
        
        // Afichage du tableau d'interet
     	System.out.println("\n Tableau d'interet 3 :\n");
     	for(int i=0; i<res.length; i++){
     		for(int j=0; j<res[i].length; j++){
     			System.out.print(res[i][j]+" ");
     		}
     		System.out.println();
     	}
        return res;
	}
	
	public int moyennePixel(Pixel p){
		return (p.getRed()+p.getGreen()+p.getBlue())/3;
	}
	
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
	
	public static void main(String[] args)
	 {
			new SeamCarving();
	 }
}
