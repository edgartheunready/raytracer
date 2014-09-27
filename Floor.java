public class Floor extends Thing {
	
	private double floorHeight;
	private Color dark, bright;
	private Color specularLight;
	
	public Floor(Color dark,Color bright, double floorHeight,Color specularLight) {
		this.dark=dark;
		this.bright=bright;
		this.floorHeight = floorHeight;
		this.specularLight=specularLight;
	}
	
	public boolean hasLighting(){
		return true;
	}
	
	public Color getSpecularWeight(){//what does specular light need to be?
		return specularLight;
	}
	
	public void makeColorProportional(double size){//keeps the original colors proportional to what you chose, and prepares them for recieving diffuse lighting and shadows
		dark.changeColor(dark.getRed()*size, dark.getGreen()*size, dark.getBlue()*size);
		bright.changeColor(bright.getRed()*size, bright.getGreen()*size, bright.getBlue()*size);
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
		return new Color(255,25,25);//you should never see this color
	}

	public Intersection getIntersection(Point start, Point end) {
		double distance = (floorHeight - start.getY()) / (end.getY()- start.getY());
		if (Math.abs(end.getX() - start.getY()) < this.MIN) {
			return new Intersection(60 + 1, null, null);
		}
		if (distance < MIN) {
			return new Intersection(60 + 1, null, null);
		}
		double x = start.getX() * (1.0 - distance) + end.getX() * distance;
		double y = floorHeight;
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
