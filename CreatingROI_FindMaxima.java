/* 2023-10-06 Ubah steps dialog utk prominence menjadi 1*/
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
public class CreatingROI_FindMaxima implements PlugIn {

	static String title="Maxima";
	int prominence=30;
	GenericDialog gd;

	public void run(String arg) {
		//B. Image
		//B.1. Need Opened Image
		ImagePlus imp;
		imp = IJ.getImage();

		//B.1. Automatically Opened
		IJ.run(imp, "8-bit", "");

		//B.2. Initial
		IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");

		ImageProcessor ip=imp.getProcessor();
		ip.snapshot(); //Makes a copy of this image's pixel data that can be later restored using reset() or reset(mask).

		DialogListener listener = new DialogListener(){
		 public boolean dialogItemChanged(GenericDialog gd, AWTEvent event){
	  		  prominence=(int)gd.getNextNumber();
		          IJ.run(imp, "Find Maxima...", "prominence="+prominence+" exclude output=[Point Selection]");
			  imp.updateAndDraw();
            		  return true;
      	 	  }
		};

		//
      		gd = new NonBlockingGenericDialog("Prominence Adjuster");
		gd.addSlider("Prominence", 1, 99, prominence, 1);
     		gd.addDialogListener(listener);
      		gd.showDialog();
	}
}


