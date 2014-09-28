public class Intersection {

  private double distance;
  private Point placeHit;
  private Thing thingHit;

  public Point getPoint() {
    Point temp = placeHit;
    return temp;
  }

  public Thing getThing() {
    return thingHit;
  }

  public Point getIntersectionPoint() {
    return placeHit;
  }

  public double getDistance() {
    return distance;
  }

  public Intersection(double distance, Point placeHit, Thing thingHit) {
    this.distance = distance;
    this.placeHit = placeHit;
    this.thingHit = thingHit;
  }
}
