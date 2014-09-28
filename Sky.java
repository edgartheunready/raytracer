public class Sky extends Thing {
	
	private Color color;
	double MAX;

	public Color getAmbient(Point p) {//gets the base color on a thing
		double intensity =(p.getY()*1.25+color.getGreen())+.05;
		if (intensity<0||intensity>255)intensity=.5;
		Color skycolor = new Color(color.getRed(),  color.getGreen(),(int)intensity) ;
		return skycolor;
	}
	
	public Point getNormal(Point p){//gets the normal of sky(this method should never be called)
		return new Point (0.0,0.0,0.0);
	}
	public boolean hasLighting(){
		return false;
	}
	public Color getSpecularWeight(){
		return new Color(0,0,0);
	}
	public void makeColorProportional(double size){
		color.changeColor(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public Intersection getIntersection(Point start, Point end) {//gets a sky intersection
		double X = start.getX() * (1 - MAX) + end.getX() * MAX;//still need to encorporate vectors into this
		double Y = start.getY() * (1 - MAX) + end.getY() * MAX;
		double Z = start.getZ() * (1 - MAX) + end.getZ() * MAX;
		return new Intersection(MAX, new Point(X, Y, Z), this);
	}

	public Sky(Color color, final double MAX) {
		this.color = color;
		this.MAX = MAX;
	}
	
	public boolean hasDiffuse(){
		return false;
	}
	
	
	public Color getDiffuse(Point p){
		return new Color(0,0,0);
	}
}