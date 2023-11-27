import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;

import ij.plugin.frame.*;
import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryService;

public class Tutorial_Webcam_02 implements PlugIn {
	Webcam camera;
	BufferedImage image;
	ImagePlus imp;
	ImageProcessor ip;
	int camID=0;
	public void run(String arg) {
		Webcam camera = Webcam.getDefault();
		if (null == camera) {
	  	  return;
		}
		
		camera = Webcam.getWebcams().get(camID);
		IJ.log(Integer.toString(camID));

		Dimension[] sizes = camera.getViewSizes();	//640x480
		Dimension s1 = sizes[sizes.length - 1];
		camera.close();

		camera.setViewSize(s1);
		camera.open();

		ip= new ColorProcessor(s1.width, s1.height);

		imp=new ImagePlus("", ip);
		WindowManager.addWindow(imp.getWindow());

		imp.show();

		//camera.close();
		
	}

}
