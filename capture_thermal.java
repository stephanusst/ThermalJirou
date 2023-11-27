import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

import java.awt.image.BufferedImage;
import com.github.sarxos.webcam.Webcam;				//https://github.com/sarxos/webcam-capture
import com.github.sarxos.webcam.WebcamDiscoveryService;


public class capture_thermal implements PlugIn {

	public void run(String arg) {
		ImagePlus imp = IJ.getImage();
		IJ.run(imp, "Invert", "");
		IJ.wait(2000);
		IJ.run(imp, "Invert", "");



		Webcam webcam = Webcam.getDefault();
		webcam.open();
		BufferedImage image = webcam.getImage();
		ImagePlus imp = new ImagePlus("Title");
		imp.setImage(image);

	}

}
