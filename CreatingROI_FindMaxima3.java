/****************************************************** 
2023-10-06 Ubah steps dialog utk prominence menjadi 1
2022-10-08 Ditambahkan AutoThreshold 
*******************************************************/
import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.filter.*;

/******************************************************
 Find Maxima
2023/10/03 Tambah Lookup table "Fire"
*******************************************************/
public class CreatingROI_FindMaxima3 implements PlugIn {

	static String title="Maxima";
	int prominence=10;
	GenericDialog gd;

	public void run(String arg) {
		//B. Image
		//B.1. Need Opened Image
		ImagePlus imp;
		imp = IJ.getImage();

		//B.1. Automatically Opened
		IJ.run(imp, "8-bit", "");

	  	IJ.setAutoThreshold(imp, "Default dark no-reset");
		IJ.run(imp, "Create Selection", "");

		//B.2. Initial
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		IJ.resetThreshold(imp);

		ImageProcessor ip=imp.getProcessor();
		ip.snapshot(); //Makes a copy of this image's pixel data that can be later restored using reset() or reset(mask).

		DialogListener listener = new DialogListener(){
		 public boolean dialogItemChanged(GenericDialog gd, AWTEvent event){
			  IJ.setAutoThreshold(imp, "Default dark no-reset");
   			  IJ.run(imp, "Create Selection", "");

	  		  prominence=(int)gd.getNextNumber();
		          IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
			  imp.updateAndDraw();
            		  return true;
      	 	  }
		};

      		gd = new NonBlockingGenericDialog("Prominence Adjuster");
		gd.addSlider("Prominence", 10, 99, prominence, 1);

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
}

