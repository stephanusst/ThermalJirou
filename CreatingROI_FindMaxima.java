import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.filter.*;

public class CreatingROI_FindMaxima implements PlugIn {

	static String title="Maxima";
	int prominence=30;
	GenericDialog gd;

	public void run(String arg) {
		//B. Image
		//B.1. Need Opened Image
		ImagePlus imp;
		//IJ.run(imp, "IJ webcam plugin", "camera=[S0-40 0] width=1280 height=960 unit=�m pixel_size=2.00000000 interval=1000 frames=3 process");
		imp = IJ.getImage();

		//B.1. Automatically Opened
		//ImagePlus imp = IJ.openImage("I:/ImageJ/plugins/Workspace/_images/SnapDada.tif");
		//imp.show();
		IJ.run(imp, "16-bit", "");

		//B.2. Initial
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		ImageProcessor ip=imp.getProcessor();
		ip.snapshot(); //Makes a copy of this image's pixel data that can be later restored using reset() or reset(mask).

		DialogListener listener = new DialogListener(){
		 public boolean dialogItemChanged(GenericDialog gd, AWTEvent event){
			  //IJ.setAutoThreshold(imp, "Mean Dark");
   			  //IJ.run(imp, "Create Selection", "");
			  //RoiManager rm = RoiManager.getRoiManager();
			  //ThresholdToSelection ts= new ThresholdToSelection(ip);

			  //rm.addRoi(roi);
			  //rm.select(0);
			  //roiManager("Select", 0);

	  		  prominence=(int)gd.getNextNumber();
	  		  IJ.run(imp, "Select None", "");
		          IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
			  imp.updateAndDraw();
			  IJ.log("listener");
            		  return true;
      	 	  }
		};

      		gd = new NonBlockingGenericDialog("Prominence Adjuster");
      		//gd.addImage(imp);
		gd.addSlider("Prominence", 10, 99, 30, 2);
     		gd.addDialogListener(listener);
      		gd.showDialog();
		IJ.log("Run End");
		
		/*
		//C. Threshold
		//IJ.setAutoThreshold(imp, "Default dark");
		//D. Selection
		IJ.run(imp, "Select None", "");
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		imp.updateAndDraw();

		//E. Find Maxima
		//A. Dialog
		//GenericDialog gd;
		gd = new GenericDialog("Find Maxima");
		gd.addStringField("Title: ", title);
    		//gd.addImage(imp);
		//gd.addNumericField("Prominence: ", prominence,0);
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
}


