public class Sphere extends Thing{
	protected Point center;
	private double radius;
	private Color sphereColor;
	private Color specularLight;

	public Sphere(Point center, double radius, Color color, Color specularLight) {
		this.center = center;
		this.radius = radius;
		sphereColor = color;
		this.specularLight=specularLight;
	}
	
	public void makeColorProportional(double size){
		sphereColor.changeColor(sphereColor.getRed()*size, sphereColor.getGreen()*size, sphereColor.getBlue()*size);		
	}
	
	public Color getSpecularWeight(){
		return specularLight;
	}
	
	public boolean hasLighting(){
		return true;
	}
	
	public Point getNormal(Point p){
		return center.getUnitVector(p);
	}
	
	public Color getAmbient(Point p ) {
		Color current = sphereColor;
		return current;
	}
	
	public Color getColor(Point p){
		return sphereColor;
	}


	public Intersection getIntersection(Point start, Point end) {
		double distance, A, B, C, disc;
		Point endToStartDiff = end.subtract(start);
		Point startToCenterDiff = start.subtract(this.center);
		A = endToStartDiff.dotProduct(endToStartDiff);
		B = 2.0 * (endToStartDiff.dotProduct(startToCenterDiff));
		C = (startToCenterDiff.dotProduct(startToCenterDiff)) - radius
				* radius;
		disc = B * B - 4.0 * A * C;
		if (disc < 0) {
			return new Intersection(60 + 1, null, null);
		} else {
			distance = (-B - Math.sqrt(disc)) / (2.0 * A);
		}
		if (distance < MIN) {
			distance = (-B + Math.sqrt(disc)) / (2.0 * A);
		}
		if (distance >= MIN) {
			double X, Y, Z;
			X = start.getX() * (1 - distance) + end.getX() * distance;
			Y = start.getY() * (1 - distance) + end.getY() * distance;
			Z = start.getZ() * (1 - distance) + end.getZ() * distance;
			return new Intersection(distance, new Point(X, Y, Z), this);
		}
		return new Intersection(60 + 1, null, null);
	}
	
	public boolean hasDiffuse(){
		return true;
	}
	
	public Color getDiffuse(Point p){
		return getAmbient(p);
	}
}
