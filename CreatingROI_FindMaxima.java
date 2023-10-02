import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;

public class CreatingROI_FindMaxima2 implements PlugIn {

	static String title="Maxima";
	int prominence=10;

	public void run(String arg) {
		//B. Image
		//B.1. Need Opened Image
		ImagePlus imp = IJ.getImage();

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
		  	prominence=(int)gd.getNextNumber();
		  	IJ.run(imp, "Select None", "");
		  	IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
            		  //double gamma = gd.getNextNumber();
            		  //ip.reset();
            		  //ip.gamma(gamma);
            		  //imp.setProcessor(ip);
            		  //gd.repaint();
            		  return true;
      	 	  }
		};
		
      		GenericDialog gd = new GenericDialog("Titik Terpanas");
      		//gd.addImage(imp);
      		gd.addSlider("Prominence:", 10, 99, 1);
     		gd.addDialogListener(listener);
      		gd.showDialog();
		

		/*
		//C. Threshold
		//IJ.setAutoThreshold(imp, "Default dark");
		//D. Selection
		//IJ.run(imp, "Create Selection", "");
		IJ.run(imp, "Select None", "");
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
		imp.updateAndDraw();

		//E. Find Maxima
		//A. Dialog
		GenericDialog gd;
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


