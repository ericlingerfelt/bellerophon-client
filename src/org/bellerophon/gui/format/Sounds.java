package org.bellerophon.gui.format;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Frame;
import java.net.URL;
import java.util.Random;
import org.bellerophon.data.MainData;
import org.bellerophon.enums.ResourceType;

public class Sounds {

	public static void playRandomMrTSound(){
		int[] array = new int[]{10, 100, 25, 29, 32, 42, 45, 48, 51, 54, 57
								, 6, 60, 66, 70, 73, 79, 82, 85, 88, 91, 94, 97};
		Random random = new Random();
		int randomIndex = random.nextInt(array.length);
		AudioClip ac = createAudioClip("/resources/sounds/MrT-" + array[randomIndex] + ".wav");
		ac.play();
	}
	
	public static AudioClip createAudioClip(String path) {
		AudioClip audioClip = null;
		if(MainData.getResourceType()==ResourceType.LOCAL){
			audioClip = getAudioClipFromDisk(path);
		}else if (MainData.getResourceType()==ResourceType.REMOTE){
			audioClip = getAudioClipFromJar(path);
		}
		return audioClip;
    }

	private static AudioClip getAudioClipFromDisk(String path){
		java.net.URL url = Frame.class.getResource(path);
		//System.out.println(url);
		if (url != null) {
			//String urlString = url.toString(); 
			//urls=urls.replaceFirst("file:/", "file:///");
			return Applet.newAudioClip(url);
		}
		return null;
	}
	
	private static AudioClip getAudioClipFromJar(String path){
		try{
			Frame frame = new Frame();
			URL url = frame.getClass().getResource(path);
			String urls=url.toString(); 
			urls=urls.replaceFirst("file:/", "file:///");
			return Applet.newAudioClip(new URL(urls));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
