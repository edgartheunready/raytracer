import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Scene {
	private double scene_width;
	private double scene_height;
	private ArrayList <Thing> thing;
	private ArrayList <RTLight>lights;
	public static double MAX_DEPTH;
	private String filename;
	
	public ArrayList<Thing> getList(){
		return thing;
	}
	public Scene(double width, double height, double MAX_DEPTH, String filename){
		this.scene_width = width;
		this.scene_height = height;
		Scene.MAX_DEPTH=MAX_DEPTH;		
		this.thing=new ArrayList <Thing>();
		this.lights=new ArrayList<RTLight>();
		this.filename=filename;
		createScene( width, height,MAX_DEPTH,filename);
	}


	public Point Get3DObject(int column, int row, int image_width,int image_height) {
		double image_proportion, scene_proportion, x, y;		
		image_proportion = column / (double) (image_width - 1);
		scene_proportion = image_proportion * scene_width;
		x = scene_proportion - this.scene_width / 2.0;
		image_proportion = row / (double) (image_height - 1);
		scene_proportion = image_proportion * scene_height;
		y = this.scene_height / 2.0 - scene_proportion;

		return new Point(x, y, 0.0);
	}
	
	public ArrayList<RTLight> getVisibleLights(Point p){//finds the visible lights for each point 

		ArrayList<RTLight> visibleLights=new ArrayList<RTLight>();
		for(RTLight temp: lights){
			
		
			Intersection lightInt = trace(p,temp.getCenter());
			
			if(lightInt.getThing() == temp){
				visibleLights.add((RTLight)lightInt.getThing());
			}
			
		}
		
		return visibleLights;
	}
	public void editColors(){
		for(Thing temp: thing){
			int size=thing.size();//i use this size variable to proportionally adapt the amount i dub down the colors based on the number of lights in the scene. So, this lets you change the number of lights in the scene without changeing alot of code to make sure the colors don't get too dark or too light. 
			if(size<=0)size=1;
			temp.makeColorProportional(.5);
		}
	}
	public Intersection trace(Point start, Point end) {//finds the nearest intersection with the light rays
		double curDist, closeDist;
		Intersection closestIntersection= new Intersection(60+1, null, null);
		for(Thing thing:thing){
		  Intersection thingIntersection = thing.getIntersection(start, end);
			curDist = thingIntersection.getDistance();
			closeDist = closestIntersection.getDistance();
			if (curDist < closeDist) {
				closestIntersection = thingIntersection;
			}
		}
		for(Thing light:lights){
		  Intersection thingIntersection = light.getIntersection(start, end);
			curDist = thingIntersection.getDistance();
			closeDist = closestIntersection.getDistance();
			if (curDist < closeDist) {
				closestIntersection = thingIntersection;
			}
		}
		return closestIntersection;
	}

	public Point get3DCoordinate(int row, int col, int width, int height) {//maps pixels to co-ordinates
		double x, y;
		x = scene_width * (col / ((double) (width - 1))) - scene_width / 2.0;
		y = scene_height / 2.0 - scene_height * row	/ ((double) (height - 1));
		return new Point(x, y, 0);
	}
		
	public  void createScene(double width, double height, double MAX_DEPTH,String filename){
		
		
		String SCenter="", SRadius="",SColor="",floorHeight="",floorWidth="",color1="",color2="",specular="";
		double temp=0,pointX=0,pointY=0,pointZ=0,red=0,green=0,blue=0,redAlt=0,greenAlt=0,blueAlt=0,rS=0,bS=0,gS=0,rad=0,floorY=0,floorX=0;
		int cnt=0;
		String ts;
		try{
			Scanner s = new Scanner(new FileInputStream(filename));
			while(s.hasNext()){
				cnt++;
				
				ts=s.next();
				if(ts.equalsIgnoreCase("sky")){
					thing.add(new Sky(new Color( .156, .156, 0), MAX_DEPTH));
				}
				if(ts.equalsIgnoreCase("sphere")){
					if(s.hasNext()){
						SCenter=s.next();
						if(SCenter.equalsIgnoreCase("center")){
							pointX=s.nextDouble();
							pointY=s.nextDouble();
							pointZ=s.nextDouble();
						}
					}
					if(s.hasNext()){
						SRadius=s.next();
						if(SRadius.equalsIgnoreCase("radius")){
							rad=s.nextDouble();
						}
					}
					if(s.hasNext()){
						SColor=s.next();
						if(SColor.equalsIgnoreCase("color")||SColor.equalsIgnoreCase("color1")){
							red=s.nextDouble();
							green=s.nextDouble();
							blue=s.nextDouble();

						}
					}
					if(s.hasNext()){
						specular=s.next();
						if(specular.equalsIgnoreCase("specular")||specular.equalsIgnoreCase("color")){
							rS=s.nextDouble();
							gS=s.nextDouble();
							bS=s.nextDouble();

						}
					}
					thing.add(new Sphere(new Point(pointX, pointY, pointZ), rad, new Color(red,green,blue),new Color(rS,gS,bS)));//note to self, it seems that my color constructor that takes doubles isn't working correctly
				   // editColors();
				}
				if(ts.equalsIgnoreCase("wall")){
					if(s.hasNext()){
						floorHeight=s.next();
						if(floorHeight.equalsIgnoreCase("width")){
							floorX=s.nextDouble();
						}

					}
					if(s.hasNext()){
						color1=s.next();
						if(color1.equalsIgnoreCase("color1")||color1.equalsIgnoreCase("color")){
							red=s.nextDouble();
							green=s.nextDouble();
							blue=s.nextDouble();

						}
					}
					if(s.hasNext()){
						color2=s.next();
						if(color2.equalsIgnoreCase("color2")||color2.equalsIgnoreCase("color")){
							redAlt=s.nextDouble();
							greenAlt=s.nextDouble();
							blueAlt=s.nextDouble();

						}
					}
					if(s.hasNext()){
						specular=s.next();
						if(specular.equalsIgnoreCase("specular")||specular.equalsIgnoreCase("color")){
							rS=s.nextDouble();
							gS=s.nextDouble();
							bS=s.nextDouble();

						}
					}
					thing.add(new Wall(new Color(red,green,blue),new Color (redAlt,greenAlt,blueAlt),floorX,new Color(rS,gS,bS)));
				    //editColors();
				}
				if(ts.equalsIgnoreCase("floor")){
					if(s.hasNext()){
						floorHeight=s.next();
						if(floorHeight.equalsIgnoreCase("height")){
							floorY=s.nextDouble();
						}

					}
					if(s.hasNext()){
						color1=s.next();
						if(color1.equalsIgnoreCase("color1")||color1.equalsIgnoreCase("color")){
							red=s.nextDouble();
							green=s.nextDouble();
							blue=s.nextDouble();

						}
					}
					if(s.hasNext()){
						color2=s.next();
						if(color2.equalsIgnoreCase("color2")||color2.equalsIgnoreCase("color")){
							redAlt=s.nextDouble();
							greenAlt=s.nextDouble();
							blueAlt=s.nextDouble();

						}
					}
					if(s.hasNext()){
						specular=s.next();
						if(specular.equalsIgnoreCase("specular")||specular.equalsIgnoreCase("color")){
							rS=s.nextDouble();
							gS=s.nextDouble();
							bS=s.nextDouble();

						}
					}
					thing.add(new Floor(new Color(red,green,blue),new Color (redAlt,greenAlt,blueAlt),floorY,new Color(rS,gS,bS)));
				   // editColors();
				}
				if(ts.equalsIgnoreCase("light")){
					if(s.hasNext()){
						SCenter=s.next();
						if(SCenter.equalsIgnoreCase("center")){
							pointX=s.nextDouble();
							pointY=s.nextDouble();
							pointZ=s.nextDouble();
						}
					}
					if(s.hasNext()){
						SRadius=s.next();
						if(SRadius.equalsIgnoreCase("radius")){
							rad=s.nextDouble();
						}
					}
					lights.add(new RTLight(new Point(pointX,pointY,pointZ),rad,new Color( 1,.75,.75),new Color(0,0,0)));
				   // editColors();
				}
			}

		}catch(InputMismatchException e){
			System.out.println(cnt+" "+SCenter+" "+ SRadius+" "+SColor+" "+floorHeight+" "+floorWidth+" "+color1+" "+color2+" "+specular+" "+temp+" "+pointX+" "+pointY+" "+pointZ+" "+red+" "+green+" "+blue+" "+redAlt+" "+greenAlt+" "+blueAlt+" "+rS+" "+bS+" "+gS+" "+rad+" "+floorY+" "+floorX  );
		}catch(IOException e){
			System.out.println(e+" error in scene.ssf");
		}


	}

}
