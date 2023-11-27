/* 2023-10-06 Tambah Fire
 * 2023-10-10 Ubah menjadi JET
 */
import ij.IJ; 				//Static Utility Methods
import ij.ImagePlus;				//An ImagePlus contain an ImageProcessor (2D image) or an ImageStack (3D, 4D or 5D image).
import ij.ImageStack;			//This class represents an expandable array of images.
import ij.Macro;				//This class contains static methods that perform macro operations.
import ij.Prefs;				//This class contains the ImageJ preferences, which are loaded from the "IJ_Props.txt" and "IJ_Prefs.txt" files.
import ij.WindowManager;			//This class consists of static methods used to manage ImageJ's windows.
import ij.gui.GenericDialog;		//This class is a customizable modal dialog box. Here is an example GenericDialog with one string field and two numeric fields:
import ij.gui.ImageRoi;

import ij.macro.Interpreter;
import ij.measure.Calibration;

import ij.plugin.PlugIn;

import ij.process.ColorProcessor;
import ij.process.ShortProcessor;
import ij.process.ByteProcessor;

import ij.process.FHT;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import java.util.Calendar;

import com.github.sarxos.webcam.Webcam;				//https://github.com/sarxos/webcam-capture
import com.github.sarxos.webcam.WebcamDiscoveryService;

import ij.LookUpTable;

/*******************************************************************************
 * @author Jerome Mutterer
 * @date 2014 12 15 first release
 * @date 2015 01 14 added macro support and custom resolutions thanks to Jarom
 *       Jackson
 * @date 2015 02 03 added proper macro support using GenericDialog for options
 *       thanks to Wayne Rasband
 * @modif Fran  ois Gannier
 * @date 2015 06 01 added calibration
 * @date 2015 06 01 added saving prefs
 * @date 2015 06 21 added fps on statusbar
 * @date 2015 06 24 fixed error with no webcam plugged
 * @date 2015 08 18 added acquiring timelapse sequences thanks to Chris Elliott
 * @date 2015 09 14 fixed a bug when timelapse would start despite of user
 *       setting
 * @date 2016 05 18 added an option to display live FFT spectrum with image
 *       inset
 * @modif Stephanus Tandjung
 * @date 2023 09 25 
* 640x480um (640x480);RGB;1.2MB
 ******************************************************************************/

public class thermal_jirou_plugin_02 implements PlugIn {

	/////////////// Variables/////////////////////////
	Webcam camera;
	BufferedImage image;

	ImagePlus imp;
	ImagePlus imp2;
	ImagePlus outputImage;

	ImageProcessor ip;

	int camID = 0;
	int width = 0;
	int height = 0;
	int interval = 1;
	int  nFrames = 1;
	float calib = 1;

	String unit = "pixel";

	private String macro;

	boolean grab = false;
	boolean customSize = false;
	boolean doTimelapse = false;
	boolean shiftToStart = false;
	//boolean doFFT = false;
	boolean doMacro = false;
	boolean displayFPS = true;

	private FHT fht;

	private GenericDialog gd;

	int prominence = 30;
	//////////////////////////////////////////////////////


	/****************************************************/
	/* A. run                                           */
	/* 512 x 392um (256x196)
	/****************************************************/
	public void run(String s) {
		IJ.beep();
		//1. Get the Camera
		camera = Webcam.getDefault();

		//2. Check availability 
		if (null == camera) {
	  	  return;
		}

		//3. Preferences files.
		// - public class Prefs extends java.lang.Object: This class contains the ImageJ preferences, 
		//   which are loaded from the "IJ_Props.txt" and "IJ_Prefs.txt" files. 
		// - static void set?(java.lang.String key, boolean value): Saves the value of the boolean value 
		//   in the preferences file using the keyword key. 
		Prefs.set("Cam.newImage", false);

		//4. Running Dialog and get the camID. 
		//   boolean showDialog()
		if (!showDialog())
		  return;	
		 							
		camera = Webcam.getWebcams().get(camID);

		//IJ.log(Integer.toString(camID));

		//5. Jika kamera ada, maka dilanjutkan
		if (null != camera) {
			//IJ.log("Camera tidak Null");
			Dimension[] sizes = camera.getViewSizes();
			Dimension s1 = sizes[sizes.length - 1];		
			camera.close();
			camera.setViewSize(s1);
			camera.open();

			//Images
			ip = new ByteProcessor(s1.width, s1.height);
			imp = new ImagePlus("", ip);
			IJ.run(imp, "16-bit", "");
			imp.setDefault16bitRange(10);

			//Calibration
			Calibration cal = imp.getCalibration();

			cal.setUnit(unit);
			cal.pixelWidth = calib;
			cal.pixelHeight = calib;
			imp.show();
			
			long frames = 0;
			double frameRate;
			long currentTime, initialTime = Calendar.getInstance().getTimeInMillis(), diff, frameTime;
			frameTime = initialTime;
			String framerateString;
			frameTime = Calendar.getInstance().getTimeInMillis();
			String times = "<timestamps>";
			boolean timelapseFired = false;
			
			//Loop
			while (!(IJ.escapePressed() || null == imp.getWindow())) 
			{
				if (camera.isImageNew()) {					
					image = camera.getImage();
					imp.setImage(image);
					//IJ.run(imp, "16-bit", "");
					//IJ.run(imp, "Jet", "");

					//Prefs.set("Cam.newImage", true);						
				}//if (camera.isImageNew())
			}while (!(IJ.escapePressed() || null == imp.getWindow()))

			//imp.setTitle("Snap");

			//frameTime = Calendar.getInstance().getTimeInMillis();
			//times += "\n" + frameTime;

			//imp.setProperty("Info", times + "\n</timestamps>");
			camera.close();

			Prefs.set("Webcam.width", width);
			Prefs.set("Webcam.height", height);
			Prefs.set("Webcam.interval", interval);
			Prefs.set("Webcam.nFrames", nFrames);
			Prefs.set("Webcam.doTimelapse", doTimelapse);
			Prefs.set("Webcam.shiftToStart", shiftToStart);
			Prefs.set("Webcam.displayFPS", displayFPS);
			Prefs.set("Webcam.customSize", customSize);
			Prefs.set("Webcam.doMacro", doMacro);
			Prefs.set("Webcam.macro", macro);

			cal = imp.getCalibration();

			if ((cal.pixelWidth != calib) || (cal.getUnit() != unit)) {
				if (IJ.showMessageWithCancel("Preferences", "Calibration changed, replace?")) {
					Prefs.set("Webcam.calib", cal.pixelWidth);
					Prefs.set("Webcam.calUnit", cal.getUnit());
				}
			} else {
				Prefs.set("Webcam.calib", calib);
				Prefs.set("Webcam.calUnit", unit);
			}

		}
	}//End of run
	/////////////////////////////////////////////////////////////////////////////

	/***************************************************************************/
	/* B. Show Dialog()                                                                          */
	/***************************************************************************/
	boolean showDialog() {
		int n = 0;
		String[] cameraNames = new String[Webcam.getWebcams().size()];

		//a. Display available Webcam
		for (Webcam c : Webcam.getWebcams()) {
			cameraNames[n] = c.getName();
			n++;
		}

		//b. Variables
		customSize = (boolean) Prefs.get("Webcam.customSize", false);
		width = (int) Prefs.get("Webcam.width", width);
		height = (int) Prefs.get("Webcam.height", height);
		calib = (float) Prefs.get("Webcam.calib", 1.0);
		unit = (String) Prefs.get("Webcam.calUnit", "\u00B5m");
		macro = (String) Prefs.get("Webcam.macro", "run('8-bit Color', 'number=4');");
		displayFPS = (boolean) Prefs.get("Webcam.displayFPS", displayFPS);
		shiftToStart = (boolean) Prefs.get("Webcam.shiftToStart", false);
		doTimelapse = (boolean) Prefs.get("Webcam.doTimelapse", false);
		doMacro = (boolean) Prefs.get("Webcam.doMacro", false);
		interval = (int) Prefs.get("Webcam.interval", interval);
		nFrames = (int) Prefs.get("Webcam.nFrames", nFrames);

		//c. Display
		gd = new GenericDialog("Thermal Jirou...");

		gd.addChoice("Camera name", cameraNames, cameraNames[0]);
		gd.addCheckbox("Show FPS in status bar", displayFPS);
		gd.addCheckbox("Grab and return (Find Maxima)", false);
		gd.addCheckbox("Custom size", customSize);

		gd.addNumericField("Width", width, 0, 5, "pixels");
		gd.addNumericField("Height", height, 0, 5, "pixels");

		//gd.addMessage("\nCalibration");
		//gd.addStringField("Unit", unit);
		//gd.addNumericField("Pixel_size", calib, 8, 12, "units/px");

		//gd.addCheckbox("Do timelapse", doTimelapse);
		gd.addCheckbox("Press_Shift to start", shiftToStart);

		gd.addNumericField("Interval", interval, 0, 6, "msecs");
		gd.addNumericField("Frames", nFrames, 0, 6, "");
		
		gd.addCheckbox("Process Live Image!", doMacro);
		gd.addTextAreas(macro,null, 1, 40);

		gd.showDialog();

		//d. Response
		if (gd.wasCanceled())
			return false;

		camID = (int) gd.getNextChoiceIndex();
		displayFPS = gd.getNextBoolean();
		grab = (boolean) gd.getNextBoolean();
		customSize = (boolean) gd.getNextBoolean();
		width = (int) gd.getNextNumber();
		height = (int) gd.getNextNumber();
		//unit = (String) gd.getNextString();
		//calib = (float) gd.getNextNumber();
		//doTimelapse = gd.getNextBoolean();
		shiftToStart = gd.getNextBoolean();
		interval = (int) gd.getNextNumber();
		nFrames = (int) gd.getNextNumber();
		doMacro = gd.getNextBoolean();
		macro = gd.getTextArea1().getText();
		return true;
	}

	/********************************************************************************************/
	// C. pad
	//    Taken from the FFT.java class
	/********************************************************************************************/
	ImageProcessor pad(ImageProcessor ip) {
		int originalWidth = ip.getWidth();
		int originalHeight = ip.getHeight();
		int maxN = Math.max(originalWidth, originalHeight);
		int i = 2;

		while (i < maxN)
		  i *= 2;
		  if (i == maxN && originalWidth == originalHeight) {
		    return ip;
		}
		maxN = i;

		ImageStatistics stats = ImageStatistics.getStatistics(ip, ImageStatistics.MEAN, null);
		ImageProcessor ip2 = ip.createProcessor(maxN, maxN);

		ip2.setValue(stats.mean);
		ip2.fill();
		ip2.insert(ip, 0, 0);

		return ip2;
	}

	/********************************************************************************************/
	// D. runMacro
	//    taken from the batch processor
	/********************************************************************************************/	
	private boolean runMacro(String macro, ImagePlus imp) {
		WindowManager.setTempCurrentImage(imp);
		Interpreter interp = new Interpreter();
		try {
			outputImage = interp.runBatchMacro(macro, imp);
		} catch(Throwable e) {
			interp.abortMacro();
			String msg = e.getMessage();
			if (!(e instanceof RuntimeException && msg!=null && e.getMessage().equals(Macro.MACRO_CANCELED)))
				IJ.handleException(e);
			return false;
		} finally {
			WindowManager.setTempCurrentImage(null);
		}
		return true;
	}

}//public class IJ_webcam_plugin implements PlugIn
///////////////////////////////////////////////////////////////////////////////////////////////////////
