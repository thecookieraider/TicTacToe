
/**
 * Filter for our file chooser for tictactoe
 * Only allows for images to be chosen
 * Image types are determined by what is allowed 
 * to be a javax.swing.ImageIcon
 * 
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/28/2016
 */

/* Include the FileFilter class 
so we can create our own */
import javax.swing.filechooser.FileFilter;
/* Include Arrays so we can convert
our primitive array to a list check 
if it contains a valid file extension */
import java.util.Arrays;
/* Include io.File since that
is what is accepted by a FileFilter */
import java.io.File;

public class MusicFilter extends FileFilter
{
	/* Music files that can be played
	by java */
	public final static String[] sndExts = {
		"wav",
		"aiff",
		"au",
		"snd",
		"aifc"
	};
	
	/* This method is how this filter
	decides what files to show in a
	JFileChooser */
    public boolean accept(File f) {
		/* Get the extension of the file */
		String extension = getExtension(f);
		/* If the file given is a directory,
		then we need to go ahead and return true,
		which means that it should be shown in the
		JFileChooser */
		if(f.isDirectory())
		{
			return true;
		}
		/* If we did get an extension from
		the file, and if it is not a directory */
		if(extension != null)
		{
			/* Convert the primitive array to a list,
			and check if it contains the extension that
			we got */
			if(Arrays.asList(sndExts).contains(extension))
			{
				return true;
			}
			/* If the extension is not in our 
			array, it is invalid, so we return false,
			which means the file shouldn't be shown */
			else
			{
				return false;
			}
		}
		/* If the file has no extension, and is
		not a directory. Then it should not be 
		shown */
		return false;
	}
	/** Retrieves the extension of a file */
	public static String getExtension(File f) {
		String ext = null; /* Extension that will be returned */
		String s = f.getName(); /* Name of the file */
		int i = s.lastIndexOf('.'); /* Get the period that should denote the beginning
									   of the extension of the file */
		
		if(i > 0 && i < s.length() - 1) { /* If the index is not at the beginning
										     of the file, and it is within the filename
											 then we can substring the extension */
											 
			ext = s.substring(i+1).toLowerCase(); /* Grab a substring of anything past the last period */
		}
		
		return ext;
	}
	/** getDescription() is what is displayed in the JFileChooser
	* in the filter box. It should adequately decide what file
	* extensions this filter accepts */
	public String getDescription()
	{
		return "Audio Files (.wav, .aiff, .snd, .aifc, .au)";
	}
	
}
