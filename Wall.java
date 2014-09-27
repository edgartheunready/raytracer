public class Wall extends Thing {
	
	private double floorWidth;
	private Color dark, bright;
	private Color specularLight;
	
	public Wall(Color dark,Color bright, double floorWidth, Color specularLight) {
		this.dark=dark;
		this.bright=bright;
		this.specularLight=specularLight;
		this.floorWidth = floorWidth;
	}
	
	public boolean hasLighting(){
		return true;
	}
	
	public void makeColorProportional(double size){
		dark.changeColor(dark.getRed()*size, dark.getGreen()*size, dark.getBlue()*size);
		bright.changeColor(bright.getRed()*size, bright.getGreen()*size, bright.getBlue()*size);
	}
	
	public Color getSpecularWeight(){//what does specular light need to be?
		return specularLight;
	}

	
	public Point getNormal(Point spot){
		return new Point(0.0,1.0,0.0);
	}
	
	public Color getAmbient(Point p) {
		double cD=4;
		double x=p.getX();
		double z=p.getZ();
		x=x%cD;
		z=z%cD;		
		
		if(z>-1&&z<=2){
			if(x>=0&&x<=2){
				return dark;
			}
			if(x>=2&&x<4){
				return bright;
			}
			if(x<0&&x>=-2){
				return bright;
			}
			if(x<=-2&&x>-4){
				return dark;
			}
		}
		if(z>=2&&z<4){
			if(x>=0&&x<=2){

				return bright;
			}
			if(x>=2&&x<4){
				return dark;
			}	
			if(x<0&&x>=-2){
				return dark;
			}
			if(x<=-2&&x>-4){
				return bright;
			}
		}		
		return dark;
	}

	public Intersection getIntersection(Point start, Point end) {
		double distance = (floorWidth - start.getX()) / (end.getX()- start.getX());
		if (Math.abs(end.getY() - start.getY()) < this.MIN) {
			return new Intersection(60 + 1, null, null);
		}
		if (distance < MIN) {
			return new Intersection(60 + 1, null, null);
		}
		double x =floorWidth;
		double y =  start.getY() * (1.0 - distance) + end.getY() * distance;
		double z = start.getZ() * (1.0 - distance) + end.getZ() * distance;
		return new Intersection(distance, new Point(x, y, z), this);
	}
	
	public boolean hasDiffuse(){
		return true;
	}
	
	public Color getDiffuse(Point p){
		return getAmbient(p);
	}
}
