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
	
	public static void main(String[] args)
	 {
			new SeamCarving();
	 }
}
