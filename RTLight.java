
public class RTLight extends Sphere{
	
	public RTLight(Point center, double radius,Color sphereColor,Color specularLight){
		super(center,radius,sphereColor,specularLight);
	}
	
	public Color getAmbient(Point p ){
		return new Color(0,0,0);
	}
	public boolean hasLighting(){
		return false;
	}
	public Color getSpecularWeight(){
		return new Color(0,0,0);
	}
	
	public Point getCenter(){
		return center;
	}
}