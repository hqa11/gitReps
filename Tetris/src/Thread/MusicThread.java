package Thread;

import control.MusicControl;

public class MusicThread  extends Thread{
	
	private MusicControl mp;
	//0.���� 1.ѭ������ 
	private int tag;

	public MusicThread(MusicControl mp,int tag){
		this.mp = mp;
		this.tag = tag;
		
		
	}
	
	@Override
	public void run() {
		
		if(tag == 0){
			
			this.mp.getAc().start();
			
		}else if(tag == 1){
			
			this.mp.getAc().loop(10000);
			
		}
		
	}
	
}
