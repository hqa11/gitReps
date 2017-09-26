package Thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import config.GameCpHolder;
import dto.Game;
import dto.GameDto;
import dto.RequestMessage;
import holder.ServiceHolder;

public class NettyThread extends Thread{
private volatile GameDto gameDto;
	
	private RequestMessage rms = new RequestMessage(2,1,null, null) ;
	
	private Map<String,Object> map = new ConcurrentHashMap<>(); 
	
	private Game game;
	
	public NettyThread(){
		
		this.gameDto = (GameDto) GameCpHolder.get("gameDto");
		this.game = (Game) GameCpHolder.get("game");
		map.put("gameDto", gameDto);
	}
	
	@Override
	public void run() {
		while (true) {
			if(gameDto.getIsEnd() != 2  && gameDto.getResult() == 0){
				if(game.getGameModel()  == 2){
					//��������ģʽ����ô������Ϸ���󵽷�����
					rms.setRequestData(map);
					try {
						//�˴���Ҫ  ��һ��������ֹ�ڴ�������ж���ֵ���ı�
					synchronized (gameDto) {
						ServiceHolder.execute(rms);
					}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
//����߳����ڴ��䵱ǰ��gameDto������Ƶ�ʷǳ�֮��
	
	
	
}
