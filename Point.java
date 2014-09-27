public class Point {
	
	private double x;
	private double y;
	private double z;
	public final double MIN = .00001;
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point (Point p){
		this.x=p.getX();
		this.y=p.getY();
		this.z=p.getZ();		
	}
	
	public Point getUnitVector(Point dir){
		double dist=getDistance(dir);
		Point diff=new Point(0,0,0);
		if(dist<=MIN)return new Point(0,0,0);		
		diff=dir.subtract(this);
		
		return diff.divide(dist);		
	}
	
	public Point multiply(double mult){
		return new Point(x*mult,y*mult,z*mult);
	}
	
	public Point bounce(Point normal){
		double bx =(normal.x * (2.0* (normal.x)) - this.x);
		double by =(normal.y * (2.0* (normal.y)) - this.y);
		double bz =(normal.z * (2.0* (normal.z)) - this.z);
		
		return new Point(bx,by,bz);
	}
	
	public void addTo(Point p){
		this.x=this.x+p.x;
		this.y=this.y+p.y;
		this.z=this.z+p.z;		
	}
	
	public Point divide(double divisor){
		double a=x/divisor;
		double b=y/divisor;
		double c=z/divisor;
		
		return new Point (a,b,c);
	}
	public double getDistance(Point point){
		Point diff=subtract(point);
		
		return Math.sqrt(diff.dotProduct(diff));		
	}
	
	public void print() {
		System.out.println(x + ", " + y + ", " + z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public String toString(){
		return x + " "+y+" "+z;
	}

	public Point subtract(Point point) {
		double tx = x - point.x;
		double ty = y - point.y;
		double tz = z - point.z;
		
		return new Point(tx, ty, tz);
	}

	public double dotProduct(Point point) {
		double tx = x * point.x;
		double ty = y * point.y;
		double tz = z * point.z;
		double answer = tx + ty + tz;
		
		return answer;
	}

	public static void main(String[] args) {//this main was used in a lab, and is not to be used for the raytracer assignment
		Point p1 = new Point(.2, .4, 0);
		Point p2 = new Point(3.9, 1.9, 4);
		Point p3 = p1.subtract(p2);
		p3.print();
		Point p4 = p2.subtract(p1);
		p4.print();
		double product1 = p1.dotProduct(p2);
		double product2 = p2.dotProduct(p3);
		System.out.println(product1);
		System.out.println(product2);
	}
}
