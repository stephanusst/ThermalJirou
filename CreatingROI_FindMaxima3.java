/****************************************************** 
Thermal Jirou Plug In.
Find Maxima 
2022-10-16 Lookup Table  JET
2022-10-08 Ditambahkan AutoThreshold 
2023-10-06 Ubah steps dialog utk prominence menjadi 1
2023/10/03 Tambah Lookup table "Fire"
*******************************************************/
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.filter.*;

//Trial
import ij.text.TextPanel;

import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.*;

public class CreatingROI_FindMaxima3 implements PlugIn {

	static String title="Maxima";
	GenericDialog gd;
	TextPanel tp;
	String macro="Kosong";
	int prominence = 14;
	int u, v, p;
	ImagePlus imp;

	public void run(String arg) {

		////////////////B. Image /////////////////////////////

		//B.1. Need Opened Image

		imp = IJ.getImage();

		//B.1. Automatically Opened
		//IJ.run(imp, "8-bit", "");
		//IJ.run("Jet");

	  	//IJ.setAutoThreshold(imp, "Default dark no-reset");
		//IJ.run(imp, "Create Selection", "");

		//B.2. Initial
	
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		IJ.resetThreshold(imp);

		ImageProcessor ip= imp.getProcessor();
		ip.snapshot(); //Makes a copy of this image's pixel data that can be later restored using reset() or reset(mask).

		BeeperControl a_beep = new BeeperControl();
		a_beep.beepForAnHour();
		
		DialogListener listener = new DialogListener(){
		 public boolean dialogItemChanged(GenericDialog gd, AWTEvent event){


			  //IJ.setAutoThreshold(imp, "Default dark no-reset");
   			  //IJ.run(imp, "Create Selection", "");

	  		  prominence=(int)gd.getNextNumber();
		              IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
			  //IJ.resetThreshold(imp);

			  //ip.drawString(String.valueOf(prominence),30,40);
			  //u=30;v=30;
			  //p=ip.getPixel(u,v);
		          //IJ.log(String.valueOf(p));

			  IJ.run(imp, "16-bit", "");
			  IJ.run("Jet");
			  imp.updateAndDraw();
            		  return true;
      	 	  }
		};

      		gd = new NonBlockingGenericDialog("Prominence Adjuster");
		gd.addSlider("Prominence", 1, 99, prominence, 1);
     		gd.addDialogListener(listener);
		gd.showDialog();	

		//IJ.log("Run End");
		
		/*
		//C. Threshold
		//D. Selection
		IJ.run(imp, "Select None", "");
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		imp.updateAndDraw();
	
		//E. Find Maxima
		//A. Dialog
		//GenericDialog gd;
		gd = new GenericDialog("Find Maxima");
		gd.addStringField("Title: ", title);
		gd.addSlider("Prominence", 1,99, 10);
		gd.showDialog();
		if (gd.wasCanceled()) return;
		prominence=(int)gd.getNextNumber();
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
  
		//F. ImageProcessor
		//ImageProcessor ip = imp.getProcessor(); 
		// fill a rectangular region with 255 (on grayscale this is white color):  
		//Roi roi = new Roi(30, 40, 100, 100); // x, y, width, height of the rectangle  
		//ip.setRoi(roi);  
		//ip.setValue(255);  
		//ip.fill(); 	

		imp.updateAndDraw();

		while (!gd.wasCanceled()) {
	  	  gd.showDialog();
		  prominence=(int)gd.getNextNumber();
		  IJ.run(imp, "Select None", "");
		  IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		}
		*/
	}


 	class BeeperControl {
   		private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		public void beepForAnHour() {
			final Runnable beeper = new Runnable() {
       			public void run() { 
					System.out.println("beep"); 	
					IJ.beep();
					IJ.log("beep");
					IJ.run(imp, "Select None", "");
		          		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
			  		IJ.run(imp, "16-bit", "");
			  		IJ.run("Jet");
					imp.updateAndDraw();
				}
			};

     			final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 2, SECONDS);
     			scheduler.schedule(new Runnable() {
       			public void run() { 
					beeperHandle.cancel(true); 
				}
     			}, 60 * 60, SECONDS);
   		}
 	}
}


