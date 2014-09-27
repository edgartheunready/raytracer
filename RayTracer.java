//Created by Jared Menard
//v3.0
//This is a Raytracing program that "traces" rays of light to produce an image

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class RayTracer {
	
	public final double MIN = .00001;
	private byte image[];
	private int imageWidth,imageHeight;
	private Point eye;
	private Scene scene;
	private String filename,inputFile;
	private double distWeight;
	private boolean isFinished[];
	private int AAF;

	public RayTracer(int width, int heigth, String filename, double topAspect, double sideAspect,int AAF,String inputFile) {
		this.filename=filename;
		this.AAF=AAF;
		this.inputFile=inputFile;
		
		if(topAspect==-1&&sideAspect==-1){
			scene = new Scene(4.0, 3.0, 60.0,inputFile);
			//scene=new Scene(4, 3, 60.0);
		}
		else{
			scene=new Scene((double)topAspect, (double)sideAspect, 60.0,inputFile);
			//scene=new Scene((double)topAspect, (double)sideAspect, 60.0);
		}
		distWeight=.4;//use .1 for lots of spheres(about 30 spheres or so), use .3 for less spheres like around 3 or 4. when reading in from a file use 38
		imageWidth = width;
		imageHeight = heigth;
		image = new byte[imageWidth * imageHeight * 3];
		eye = new Point(0, .3, -4.2);//use 0,0,-6 for the internet file, use for the other0, .3, -4.2
		isFinished=new boolean[10];
		for(int cnt=0;cnt<isFinished.length;cnt++){//sets all values in isFinished to false.
			isFinished[cnt]=false;
		}
	}
	
	public Color calcDiffuse(Intersection intersect){//consolidates most of the code for computing the diffuse light(shadows) in the raytracer.
		Color pixel=new Color(0,0,0);
		Point unitDir;
		double lightAngle;
		double distance=0;
		Point first=intersect.getPoint();
		ArrayList<RTLight> lights=scene.getVisibleLights(first);
		Point  normal= new Point(intersect.getThing().getNormal(first));
		Color diffuse=new Color(intersect.getThing().getDiffuse(first));
		for(RTLight cnt:lights){
			unitDir= first.getUnitVector(cnt.getCenter());
			distance=first.getDistance(cnt.getCenter());
			lightAngle=((normal.dotProduct(unitDir))/distance);
			pixel.addTo(diffuse.multiply(lightAngle));//JMH


		}

		return pixel;
	}
	public Color calcSpecular(Intersection firstIntersect, Point start, Double distanceSoFar){//this method creates the reflections
		Point firstPt=firstIntersect.getPoint();
		Point incomingVector=firstPt.getUnitVector(start);
		Point outgoingVector=incomingVector.bounce(firstIntersect.getThing().getNormal(firstPt));
		outgoingVector.addTo(firstPt);
		distanceSoFar=distanceSoFar+firstIntersect.getDistance();
		Color color=raytrace(firstPt, outgoingVector,distanceSoFar);
		return  color;
	}
	public void createImage(){//this method creates 10 threads which i use to render my image. The main reason that I added threads was to fully utilize my dual core cpu. With ten threads this program could fully use up to 10 different processors fully.
		for(int i=0;i<isFinished.length;i++){
			if(i==0)(new Thread(new RTRunnable(this,0, imageHeight/isFinished.length,i))).start();
			(new Thread(new RTRunnable(this,(imageHeight/isFinished.length)*i, (imageHeight/isFinished.length)*(i+1),i))).start();
		}
	}
	private Color getPixel(int row, int column,int AArow,int AAcolumn){//this method takes in the co-ordinates for one pixel, renders it, and then returns it.
		 double red, green, blue;
		double dist,lightAngle,lightStrength;
		Color curcolor = new Color(0,0,0);
		Thing thing=null;
		Point cur = scene.get3DCoordinate(row+AArow, column+AAcolumn, imageWidth, imageHeight);
		Intersection temp=scene.trace(eye,cur);
			thing=temp.getThing();
			curcolor=new Color(thing.getAmbient(temp.getIntersectionPoint()));
			//curcolor.addTo(calcDiffuse(temp));
			
			if(thing.hasDiffuse()){
				
				ArrayList<RTLight> vlights =scene.getVisibleLights(temp.getPoint());
				
				for(RTLight cnt: vlights){
				Point vector=temp.getPoint().getUnitVector(cnt.getCenter());
				dist=temp.getPoint().getDistance(cnt.getCenter());
				lightAngle=temp.getThing().getNormal(temp.getPoint()).dotProduct(vector);
				lightStrength=(lightAngle/dist);
				curcolor.addTo(calcDiffuse(temp));//.multiply(lightStrength)
				curcolor.addTo(calcSpecular(temp, eye,0.0));//i just changed this from vector to temp.getPt
				curcolor=curcolor.divide(temp.getDistance()*distWeight);
			}
		}    
		
		return curcolor;
	}
		

	public  void print(){//writes out my byte array to a file.
		try{
			Thread.currentThread().sleep(1000);//helps to let the other threads finish what they are doing. Sometimes a black line will show up at the bottom of the image--this is espically probable with larger images. If there is ever a black line at the end of the image simply put a larger number into the sleep method call. 
			PrintStream imageOut = new PrintStream(filename+".ppm");
			imageOut.print("P6 "+ imageWidth+" "+imageHeight+" 255\n");
			imageOut.write(image, 0, image.length);
			imageOut.close();
		}catch(IOException e){
			System.err.println("there is an IO error in the print method in the class RayTracer.");
		}
		catch(InterruptedException e){
			
		}
	}
	public Color raytrace(Point start, Point end, double distance){//one of two methods used to render the specular lighting

		Color specular, specularWeight;
		if(distance>scene.MAX_DEPTH)return new Color(0,0,0);//i did have a x nine infront of the max
		Intersection intersect=scene.trace(start,end);
		Point intersectPt=intersect.getIntersectionPoint();
		Color pixel=new Color(intersect.getThing().getAmbient(intersectPt));
		distance=distance+intersect.getDistance();
		if(intersect.getThing().hasLighting()){
			specularWeight=intersect.getThing().getSpecularWeight();
			pixel.addTo(calcDiffuse(intersect));
			pixel=pixel.divide(distance*distWeight);
			specular=calcSpecular(intersect, start, distance);
			specular.multiplyBy(specularWeight);//need to figure out this whole specular weight thing. maybe i need to add that to the constructor of eact thing in the scene???
			specular.multiplyBy(specularWeight.getNegative());//this doesn't seem to be doing anything any differently than the next line of code, why is that?
			pixel.addTo(specular);
		}
		return pixel;
	}	
	
	public void  threadPixel(int startRow, int endRow,int isFinishedArrayPlace){//this method contains all the loops to render all of the pixels, and calls all the needed methods.
		double red=0;
		double green=0;
		double blue=0;
		Color curcolor = new Color(0,0,0);
		for (int row=startRow; row< endRow; row++) {
			for (int column = 0; column < imageWidth; column = column+1){

					for(int AAFRow=0;AAFRow < AAF;AAFRow++){//these next two for loops are used to add the anti=aliasing. 
						for(int AAFColumn=0;AAFColumn < AAF;AAFColumn++){// AAF is the anti-aliasing factor
							curcolor=getPixel(row, column,AAFRow,AAFColumn);
							red=red+curcolor.getRed();//adds up all the color values from the super-sampled pixels 
							green=green+curcolor.getGreen();
							blue=blue+curcolor.getBlue();
						}
					}
					red=red/(AAF*AAF);//gets the average of the super-sampled pixels.
					green=green/(AAF*AAF);
					blue=blue/(AAF*AAF);

				
					//the red, green, and blue values can get out of hand so this help to keep them in line.
					if(red>1)red=1;
					if(green>1)green=1;
					if(blue>1)blue=1;
					if(red<0)red=0;
					if(green<0)green=0;
					if(blue<0)blue=0;
					red=red*255;
					green=green*255;
					blue=blue*255;
					
					image[(row*imageWidth+column)*3] = (byte)red;
					image[((row*imageWidth+column)*3)+ 1] = (byte)green;
					image[((row*imageWidth+column)*3)+ 2] =(byte) blue;
					red=0;
					green=0;
					blue=0;
					
				}
			}
		isFinished[isFinishedArrayPlace]=true;
		System.out.println("thread "+isFinishedArrayPlace+" of "+isFinished.length+" is done");
	}
	
	public void waitForThreadsToFinish(Thread main){//this method allows the other threads that I just created to finish their jobs, not exactly the most efficient way to do this, but it works.
		
		while(isFinished[0]!=true||isFinished[1]!=true||isFinished[2]!=true||isFinished[3]!=true||isFinished[4]!=true||isFinished[5]!=true||isFinished[6]!=true||isFinished[7]!=true||isFinished[8]!=true||isFinished[9]!=true){
			try{
				main.sleep( 1000);
				//System.out.println("well, here i am awake for a second");
			}catch(Exception e){
				//System.out.println(e);
				//this program throws alot of illegatMonitorStateExceptions so i've decided to comment out the println to speed things up.
			}
		}
		System.out.println("all of the created threads are done");
	}

	public static void main(String[] args){	//the best input in the command line is : 1440 900 filename 6 3.75	
		Thread main = Thread.currentThread();
		System.out.println(main);

			if(args.length!=6&&args.length!=5&&args.length!=3&&args.length!=0&&args.length!=2){ 
				System.err.println("i need either three or five or six or 7 command line arguements 1: image width 2: image height 3: file name 4: aspect ratio width 5: aspect ratio height 6:light weight");
				System.err.println("~note~ you do not need to add a .ppm to your filename");
				System.err.println("the best command line arguements are : 1440 900 filename 6 3.75 : at least it is for me since my screen resolution is 1440*900");
				return;
			} 
			else{
				int width=800;
				int height=600;
				String filenameinput="scene.ssf",filename="ray";
				int AAF=1;//this is the anti aliasing factor. values above 4ish create a very blurred image. for best results use 2 or 3. If you want no AA, then use 1.
				double topAspect=-1;//top aspect and side aspect allow you to change the aspect ratio of your image. This is the same concept as tv screens, standard output is 4:3 and widescreen is 16:9 and for widescreen computer monitors it is 16:10 or 8:5
				double sideAspect=-1;//larger numbers here pan the camera out, while smaller ones zoom it in
				if(args.length==2){
					width=Integer.parseInt(args[0]);
					height = Integer.parseInt(args[1]);
				}
				if(args.length==3){
					width= Integer.parseInt(args[0]);
					height = Integer.parseInt(args[1]);
					filename=args[2];
				}
				if(args.length==5){		
					width= Integer.parseInt(args[0]);
					height = Integer.parseInt(args[1]);
					filename=args[2];
					topAspect=Double.parseDouble(args[3]);
					sideAspect=Double.parseDouble(args[4]);
				}
				if(args.length==6){
					width= Integer.parseInt(args[0]);
					height = Integer.parseInt(args[1]);
					filename=args[2];
					topAspect=Double.parseDouble(args[3]);
					sideAspect=Double.parseDouble(args[4]);
					filenameinput=args[5];
				}
				RayTracer rt = new RayTracer(width, height, filename, topAspect, sideAspect,AAF,filenameinput);
				rt.createImage();
				rt.waitForThreadsToFinish(main);
				rt.print();
			}
		}
	}
