
/**
 * IconGetter is a utility class
 * that simply returns an imageicon
 * using an image path
 * 
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/28/2016
 */
 
 import java.io.File;
 import javax.swing.ImageIcon;
 
public class IconGetter{
	
	public static ImageIcon getImageIcon(String path) {
		
		return new ImageIcon(path);
	
	}
}
