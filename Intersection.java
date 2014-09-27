public class Intersection {	
	
	private double distance;
	private Point placeHit;
	private Thing thingHit;

	public Point getPoint() {
		Point temp = placeHit;
		return temp;
	}

	public Thing getThing() {
		Thing curThing = thingHit;
		return curThing;
	}

	public Point getIntersectionPoint() {
		Point point = placeHit;
		return point;
	}

	public double getDistance() {
		double dist = distance;
		return dist;
	}

	public Intersection(double distance, Point placeHit, Thing thingHit) {
		this.distance = distance;
		this.placeHit = placeHit;
		this.thingHit = thingHit;
	}
}
