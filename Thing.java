public abstract class Thing {
	public final double MIN = .00001;
	Scene curScene;

	abstract public Point getNormal(Point spot);
	abstract public Intersection getIntersection(Point start, Point end);
	abstract public Color getDiffuse(Point p);
	abstract public Color getAmbient(Point p );
	abstract public Color getSpecularWeight();
	abstract public boolean hasLighting();
	abstract public boolean hasDiffuse();
	abstract public void makeColorProportional(double size);
}
