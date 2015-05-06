package modelisation;
 
import java.io.File;
import java.io.IOException;
 
import javax.swing.JFileChooser;
 
class MainClass {

	private static final int NB_COLONNE_SUPPR = 50;
    public static void main(String[] arg) throws IOException {
        JFileChooser dialogue = new JFileChooser(new File("."));
        File fichier = null;
       
        if (dialogue.showOpenDialog(null)==
            JFileChooser.APPROVE_OPTION) {
            fichier = dialogue.getSelectedFile();
            System.out.println(fichier);
        }
        //seamCarving(fichier);
        seamCarvingRGB(fichier);
    }

        private static void seamCarving(File fichier) {
                // TODO Auto-generated method stub
        		SeamCarving sc = new SeamCarving(fichier);
        		for(int i = 0; i < NB_COLONNE_SUPPR; i++){
        			System.out.print(".");
        			sc.supprColonne();
        		}
        		System.out.println("fini");
        		sc.writepgm("finalPGM.pgm");
        }

        private static void seamCarvingRGB(File fichier) {
            // TODO Auto-generated method stub
    		SeamCarvingRGB sc = new SeamCarvingRGB(fichier);
    		sc.interest1();
    		sc.interest2();
    		sc.interest3();
    		sc.interest4();
    }

}