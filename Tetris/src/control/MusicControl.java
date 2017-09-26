package control;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Thread.MusicThread;

public class MusicControl {

	private Clip  ac; 
	//����ظ������������Ŀ�������
	private Map<String,Clip> audioMap = new HashMap<>();
	
	private Thread currentThread;
	
	private static volatile MusicControl bgmp;
	
	private static volatile MusicControl actmp;
	
	
	private MusicControl(){}
	
	public static MusicControl getBgInstance(){
		
		if(bgmp == null){
			
			bgmp = new MusicControl();
			
		}
		
		return bgmp;
	}
	
public static MusicControl getActInstance(){
		
		if(actmp == null){
			
			actmp = new MusicControl();
		}
		
		return actmp;
	}
	
	
	/**
	 * ��������
	 * @param path
	 */
	public void playMusic(String path){

		//��ֹ��ǰ�߳�
		this.stopMusic();
		this.operateMusic(path, 0);

	}
	
	
	
	/**
	 * ѭ����������
	 * @param path
	 */
	public void loopMusic(String path){
		//��ֹ��ǰ�߳�
		this.stopMusic();
		this.operateMusic(path,1);

	}

	/**
	 * ֹͣ����
	 */
	public void stopMusic() {
		//�رղ�����������ֹ�����߳�
		if(ac != null)this.ac.stop();
		if(currentThread != null){
			
			currentThread.interrupt();
			currentThread = null;
			
		}
		
	}

	private void operateMusic(String path,int operate){

		try {
			
			Clip audioClip = audioMap.get(path);
			
			if(audioClip != null){
				
				this.setAc(audioClip);
				
			}else{
				
				
				URL url = this.getClass().getClassLoader().getResource(path);
				
				Clip newAudioClip = AudioSystem.getClip();
				
				AudioInputStream stream = AudioSystem.getAudioInputStream(url);
				newAudioClip.open(stream );
				
				this.setAc(newAudioClip);
				
				audioMap.put(path, newAudioClip);
				
				
			}
			
				
			//����һ���µ��߳������Ʋ�����
			MusicThread mt = new MusicThread(this,operate);
			this.setCurrentThread(mt);
			mt.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public Clip getAc() {
		return ac;
	}

	public void setAc(Clip ac) {
		FloatControl gainControl = 
			    (FloatControl) ac.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(6.0f); // Reduce volume by 10 decibels.
		
		this.ac = ac;
	}

	public Thread getCurrentThread() {
		return currentThread;
	}

	public void setCurrentThread(Thread currentThread) {
		this.currentThread = currentThread;
	}

}
