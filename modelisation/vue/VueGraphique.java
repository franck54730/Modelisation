package modelisation.vue;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import modelisation.modele.Modele;

public class VueGraphique extends JPanel implements Observer{

	protected Modele m;
	
	protected VueImage vi;
	
	public VueGraphique(Modele mod) {
		// TODO Auto-generated constructor stub
		this.m=mod;
		m.addObserver(this);
		vi = new VueImage(m);
		this.add(vi);
	}
	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Runnable code = new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				//vi.
			}
		};
		if (SwingUtilities.isEventDispatchThread())
			code.run() ;
		else
			try {
				SwingUtilities.invokeAndWait(code) ;
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
