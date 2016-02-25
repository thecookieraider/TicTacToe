
/**
 * Resource manager for audio files
 * 
 * A note about this class: if you remove
 * a clip from the audioFiles hashmap
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/29/2016
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

public class SoundHandler
{
   /** The audioFile hashMap maps Strings to
   * clips. It is the library of the soundhandler */
   private HashMap<String,Clip> audioFiles = new HashMap();
   /** The pauseTimes hasMap maps the names of each
   * clip present in audioFiles to its respective pause time.
   * Every clip has a pause time of zero unless paused
   * at some point. This means that if you try to unpause 
   * a clip that has never been paused, then you will restart
   * the clip */
   private HashMap<String,Long> pauseTimes = new HashMap();
   /** Our audio input stream that is reused whenever
   * we add in a clip to our audioFiles array.
   * We need an AudioInputStream so we can make
   * the leap from file to Clip */
   private AudioInputStream ais;
   /** Static, constant array of valid extensions
   * for files. Not used in the class. */
   public final static String[] sndExts = {
        "wav",
		"aiff",
		"au",
		"snd",
		"aifc"
    };
    /** Returns the extension of a given file.
	* Not used in the class. */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        
        if(i > 0 && i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        
        return ext;
    }
    /** Returns whether or not a clip is currently 
	* playing */
	public boolean isRunning(String name)
	{
		return audioFiles.get(name).isRunning();
	}
	/** Removes all clips and their pause times */
	public void removeAll()
	{
		audioFiles.clear();
		pauseTimes.clear();
	}
	/** Pauses a specified clip for a given amount of 
	* milliseconds. The wait is done through a seperate
	* thread so that main processes don't get held up
	* and so that other songs can be played and stopped
	* as well */
    public void pauseFor(int milliseconds, String name)
    {
		/* Pause the song */
        pause(name);
		/* Make and start a new thread which
		will unpause the song after the specified amount 
		of milliseconds */
		new Thread(new Runnable() {
			public void run()
			{
			    try
			    {
				    Thread.sleep(milliseconds);
				}
				catch(InterruptedException e)
				{
				    e.printStackTrace();
				}
				
				unpause(name);
			}
		}).start();
    }
	/** Stops a given clip for a given amount of
	* milliseconds. The wait is done through a seperate thread
	* so that mainline processes aren't interrupted */
	public void stopFor(int milliseconds, String name)
	{
		stop(name);
		new Thread(new Runnable() {
			public void run()
			{
			    try
			    {
				    Thread.sleep(milliseconds);
				}
				catch(InterruptedException e)
				{
				    e.printStackTrace();
				}
				
				start(name);
			}
		}).start();
	}
	/** Pauses all clips */
    public void pauseAll()
    {
       Set<Map.Entry<String,Clip>> iterable = audioFiles.entrySet();
       
       Iterator<Map.Entry<String,Clip>> iterator = iterable.iterator();
       
       while(iterator.hasNext())
       {
           this.pause(iterator.next().getKey());
       }
    }
   /** Stops all clips */
   public void stopAll()
   {
       Set<Map.Entry<String,Clip>> iterable = audioFiles.entrySet();
       
       Iterator<Map.Entry<String,Clip>> iterator = iterable.iterator();
       
       while(iterator.hasNext())
       {
           iterator.next().getValue().stop();
       }
   }
   /** Sets a clip to loop indefinitely or not
   * If the boolean given is true, the clip will
   * forever. If the boolean given is false, the clip
   * stop looping, but continue playing */
   public void loop(String name, boolean arg)
   {
       if(arg)
       {
           audioFiles.get(name).loop(Clip.LOOP_CONTINUOUSLY);
       }
       else if(!arg)
       {
           audioFiles.get(name).loop(0);
       }
   }
   /** Returns an array of the names of the clips
   * inside of audioFiles, but not the Clips themselves */
   public String[] getNames()
   {
       int counter = 0;
       
       String[] names = new String[audioFiles.size()];
       
       Set<Map.Entry<String,Clip>> iterable = audioFiles.entrySet();
       
       Iterator<Map.Entry<String,Clip>> iterator = iterable.iterator();
       
       while(iterator.hasNext())
       {
           names[counter] = iterator.next().getKey();
           counter++;
       }
       
       return names;
   }
   /** Adds in a new clip with the specified name
   * to our audioFiles hashmap */
   public void add(String fileName, String name)
   {
	   if(audioFiles.containsKey(name))
	   {
		   if(isRunning(name))
		   {
			   stop(name);
		   }
	   }
	   
       try{
		   /* Get the audio input stream from the file */
           ais = AudioSystem.getAudioInputStream(
           new File(fileName).getAbsoluteFile());
        }
        catch(UnsupportedAudioFileException er)
        {
            er.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
       
        try{
		   /* Get an empty clip and attempt
		   to open up our audio input stream and then
		   place the clip into our hashmap with a corresponding
		   pause time in the pauseTimes hashmap */
           Clip clip = AudioSystem.getClip();
           clip.open(ais);
           audioFiles.put(name, clip);
           pauseTimes.put(name, new Long(0));
        }
        catch(LineUnavailableException exc)
        {
            exc.printStackTrace();
        }
        catch(IOException exce)
        {
            exce.printStackTrace();
        }
   }
   /** Pauses a song and sets its current
   * position in playback as the song's new value in 
   * pauseTimes */
   public void pause(String name)
   {
       if(audioFiles.containsKey(name) && pauseTimes.containsKey(name))
       {
            Clip songToPause = audioFiles.get(name);
            if(!songToPause.isRunning())
            {
                return;
            }
            else{
                pauseTimes.put(name, songToPause.getMicrosecondPosition());
                songToPause.stop();
            }
       }
       else
       {
           return;
       }
   }
   /** Starts a clip from where it was paused by looking
   * into pauseTimes to see the exact time it left off on */
   public void unpause(String name)
   {
        if(audioFiles.containsKey(name) && pauseTimes.containsKey(name))
        {
                Clip songToStart = audioFiles.get(name);
                if(!songToStart.isRunning())
                {
                    if(pauseTimes.get(name) < songToStart.getMicrosecondLength())
                    {
                        songToStart.setMicrosecondPosition(pauseTimes.get(name));
                    } else {
                        songToStart.setMicrosecondPosition(0);
                    }
                    
                    songToStart.start();
                }
                else{
                    return;
                }
                
        } else {
            return;
           }
    }  
	/** Removes a single clip from audioFiles 
	* and pauseTimes */
    public void remove(String name)
    {
      if(audioFiles.containsKey(name) && pauseTimes.containsKey(name)) {
      			if(isRunning(name))
      				stop(name);
			
            audioFiles.remove(name);
            pauseTimes.remove(name);
        } else {
            return;
        }
    }
    /** Restarts a song. Different between this
	* function and start() is that restart can be called
	* on clips that are already running 
	*
	* NOTE: restart does erase the pause time
	* for the clip being called upon */
    public void restart(String name)
    {
        if(audioFiles.containsKey(name) && pauseTimes.containsKey(name)) {
            audioFiles.get(name).setMicrosecondPosition(0);
            audioFiles.get(name).start();
            pauseTimes.put(name, new Long(0));
        } else {
            return;
        }
    }
    /** Starts a song. Difference between this
	* method and restart() is that start can only 
	* be called on clips that are stopped
	*
	* NOTE: start does erase the pause time for
	* the clip being called upon */
    public void start(String name)
    {
        if(audioFiles.containsKey(name) && pauseTimes.containsKey(name)) {
            if(audioFiles.get(name).isRunning()) {
                return;
            }
            
            audioFiles.get(name).setMicrosecondPosition(0);
            audioFiles.get(name).start();
            pauseTimes.put(name, new Long(0));
            
        } else {
            return;
        }
    }
    /** Stops a song and erases its pause time */
    public void stop(String name)
    {
        if(audioFiles.containsKey(name) && pauseTimes.containsKey(name)) {
            if(audioFiles.get(name).isRunning()) {
                audioFiles.get(name).stop();
                pauseTimes.put(name, new Long(0));
                return;
            } else {
                return;
            }
            
        } else {
            return;
        }
    }
}
