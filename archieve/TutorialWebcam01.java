import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class TutorialWebcam01 implements PlugIn {

	public void run(String arg) {

		Webcam webcam = Webcam.getDefault();
		Webcam.open();
		BufferedImage image = webcam.getImage();

		ImagePlus imp = new ImagePlus("Title");
		imp.setImage(image);
		imp.show();
		//IJ.run(imp, "Invert", "");
		//IJ.wait(1000);
		//IJ.run(imp, "Invert", "");
	}

}
