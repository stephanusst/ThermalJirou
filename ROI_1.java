import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

public class ROI_1 implements PlugIn {

	public void run(String arg) {
		ImagePlus imp = IJ.getImage();
		IJ.setAutoThreshold(imp, "Mean Dark");
   		IJ.run(imp, "Create Selection", "");
		RoiManager rm = RoiManager.getRoiManager();
	}

}
