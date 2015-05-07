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
            String fichierSelect = dialogue.getSelectedFile().toString();
            
            System.out.println("Nom du fichier : " + fichierSelect);
            
            if (fichierSelect.lastIndexOf(".") > 0) {
                // On récupère l'extension du fichier
                String ext = fichierSelect.substring(fichierSelect.lastIndexOf("."));
                // Si le fichier est un pgm
                if (ext.equals(".pgm")) {
                    System.out.println("extension: " + ext);
                    seamCarving(fichier);
                }
                // si le fichier est un ppm
                else if(ext.equals(".ppm")) {
                	System.out.println("extension: " + ext);
                	seamCarvingRGB(fichier);
                }
                else {
                	System.out.println("Choisissez un fichier ppm ou pgm.");
                }
            }
            else{
            	System.out.println("Aucun fichier selectionné.");
            }
        }
                
        //seamCarving(fichier);
        //seamCarvingRGB(fichier);
    }

        private static void seamCarving(File fichier) {
                // TODO Auto-generated method stub
        		SeamCarving sc = new SeamCarving(fichier);
        		int boucle = NB_COLONNE_SUPPR;
        		for(int i = 0; i < NB_COLONNE_SUPPR; i++){
        			System.out.print(boucle+" ");
        			sc.supprColonne();
        			boucle--;
        		}
        		System.out.println("fini");
        		sc.writepgm("finalPGM.pgm");
        }

        private static void seamCarvingRGB(File fichier) {
            // TODO Auto-generated method stub
    		SeamCarvingRGB sc = new SeamCarvingRGB(fichier);
    		//sc.interest1();
    		//sc.interest2();
    		//sc.interest3();
    		//sc.interest4();
    		
    		int boucle = NB_COLONNE_SUPPR;
    		for(int i = 0; i < NB_COLONNE_SUPPR; i++){
    			System.out.print(boucle+" ");
    			sc.supprColonne();
    			boucle--;
    		}
    		System.out.println("fini");
    		sc.writepgm("finalPPM.ppm");
    }

}