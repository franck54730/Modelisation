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
	
	public String toString(){
		   return "["+red+" "+green+" "+blue+"]";
	   }
}
