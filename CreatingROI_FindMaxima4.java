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

public class CreatingROI_FindMaxima4 implements PlugIn {

	static String title="Maxima";
	GenericDialog gd;
	TextPanel tp;
	String macro="Kosong";
	int prominence = 14;
	int u, v, p;
	ImagePlus imp;

	public void run(String arg) {
		////////////////A. Image /////////////////////////////

		//1. Get Image
		imp = IJ.getImage();

		//2. Initial	
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		IJ.resetThreshold(imp);

		ImageProcessor ip= imp.getProcessor();
		ip.snapshot(); //Makes a copy of this image's pixel data that can be later restored using reset() or reset(mask).

		BeeperControl a_beep = new BeeperControl();
		a_beep.beepForAnHour();
		
		/****************************************************************************************************************
		// Interface DialogListener
		// PlugIns or PlugInFilters that want to listen to changes in a GenericDialog without adding listeners 
		// for each dialog field should implement this method.
		/ /https://imagej.net/ij/ij/developer/api/ij/ij/gui/DialogListener.html
		****************************************************************************************************************/
		DialogListener listener = new DialogListener(){
		public boolean dialogItemChanged(GenericDialog gd, AWTEvent event){
		  prominence=(int)gd.getNextNumber();
		  IJ.run(imp, "Select None", "");	
		  IJ.run(imp, "16-bit", "");
	  	  IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		  IJ.run("Fire");
		  imp.updateAndDraw();
		  a_beep.beepForAnHour();
            	  return true;
      	 	  }
		};

      		gd = new NonBlockingGenericDialog("Prominence Adjuster");
		gd.addSlider("Prominence", 1, 99, prominence, 1);
     		gd.addDialogListener(listener);
		gd.showDialog();	
	}

 	class BeeperControl {
	  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);  //cannot be extended or inherited
	    public void beepForAnHour() {
	      final Runnable beeper = new Runnable() {
	        public void run() { 
		//IJ.beep();
		//IJ.log("beep dalam function");
		IJ.run(imp, "Select None", "");
          		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
	  	IJ.run(imp, "16-bit", "");
	  	IJ.run("Fire");
		imp.updateAndDraw();
		}
	        };//Runnable
     	    final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 1, SECONDS);

     	    scheduler.schedule(new Runnable() {
       	      public void run() { 
	        beeperHandle.cancel(true); 
	        IJ.log("Stop Find Maxima");
	      }
     	    }, 6* 1, SECONDS);//scheduler
   	  }//ScheduleExecuterService
	}//Beeper Control
}

