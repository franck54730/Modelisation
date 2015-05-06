package modelisation;

public class Pixel {

	private final int red;
	private final int green;
	private final int blue;
	
	Pixel(int r, int g, int b){
		this.red=r;
		this.green=g;
		this.blue=b;
	}
	
	public int getRed(){
		return red;
	}
	
	public int getGreen(){
		return green;
	}
	
	public int getBlue(){
		return blue;
	}
	
	public String toString(){
		   return "["+red+" "+green+" "+blue+"]";
	   }
}