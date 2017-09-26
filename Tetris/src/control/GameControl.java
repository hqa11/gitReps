package control;

import javax.swing.JOptionPane;

import org.jboss.netty.channel.Channel;

import config.Constant;
import config.GameCpHolder;
import dto.Game;
import dto.GameDto;
import dto.Player;
import dto.RequestMessage;
import holder.ServiceHolder;
import service.GameService;
import ui.FrameGame;
import ui.PanelGame;
import ui.PauseButton;
import ui.RoomListFrame;
import ui.StartButton;

public class GameControl {


	private PanelGame pg;

	private GameService gameService;
	
	private GameDto gd;
	
	Game game = (Game) GameCpHolder.get("game");
	Player player = (Player) GameCpHolder.get("player");
	
	public PanelGame getPg() {
		return pg;
	}
	
	public GameService getGameService() {
		return gameService;
	}

	public GameControl() {

		this.pg = (PanelGame) GameCpHolder.get("panelGame");
		this.gameService = 	(GameService) GameCpHolder.get("gameService");
        this.gd = (GameDto) GameCpHolder.get("gameDto");
	}


	public void keyUp() {

		gameService.keyUp();

	}


	public void keyDown() {

		gameService.keyDown(pg,0);
	}

	public void keyLeft() {
		gameService.keyLeft();

	}

	public void keyRight() {

		gameService.keyRight();
		
	}


	/**
	 * �л�����ͼƬ
	 */
	public void changeBackGround() {
		//�������ڸı䳣���صı������ֳ���
		Constant.UI_CONSTANT.changeBgImg();
	}

	/**
	 * �����������ѡ��
	 * @param string
	 */
	public void doEndChoose(String key) {
		gameService.doEndChoose(key);
		
	}

	/**
	 * ������սģʽ
	 */
	public void openMutiModel() {
		
		FrameGame fg =  (FrameGame) GameCpHolder.get("frameGame");
		//��Ϸ������,�޷�����
		if(gd.getIsEnd() != 2) {
			JOptionPane.showMessageDialog(fg,"��������,�޷����� �˲�����","����", JOptionPane.DEFAULT_OPTION);
			return;
		}
		Channel channel = (Channel)GameCpHolder.get("channel");
		if(game.getGameModel() == 1){
			if(!channel.isConnected()){
				JOptionPane.showMessageDialog(fg,"����ʧ�ܣ����������Ƿ�ͨ��","����", JOptionPane.DEFAULT_OPTION);
				//TODO ����
				return;
			}
			//����
			RoomListFrame roomListFrame = new RoomListFrame();
			GameCpHolder.put("roomListFrame", roomListFrame);
			RequestMessage reqMsg = new RequestMessage(1, 3,null, null);
			try {
				ServiceHolder.execute(reqMsg,channel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//���,
			if(player.getUserStatus() !=3 ){
				JOptionPane.showMessageDialog(fg,"�����˳����䣡","����", JOptionPane.DEFAULT_OPTION);
			}else{
				int result = JOptionPane.showConfirmDialog(pg, "��ȷ��Ҫ���˳���սģʽ��");
				if(result != JOptionPane.OK_OPTION)return;
				//�ر��б�,ģʽ����Ϊ��
				RoomListFrame roomListFrame = (RoomListFrame) GameCpHolder.get("roomListFrame");
				if(roomListFrame != null){
					roomListFrame.dispose();
				}
				game.setGameModel(1);
				//����start��ť
				StartButton sb	= (StartButton)GameCpHolder.get("sb");
				sb.reset();
			}
			
		}
		/*FrameGame fg = (FrameGame)GameCpHolder.get("frameGame");
		fg.setSize(1700,fg.getHeight());*/
		
	}

	/**
	 *����Game
	 */
	public void reSetGame() {
		if(game.getGameModel() == 1){
			if(gd.getIsEnd() == 0 || gd.getIsEnd() == 1){
				int result = JOptionPane.showConfirmDialog(pg, "��ȷ��Ҫ������Ϸ��");
				if(result == JOptionPane.OK_OPTION){
					gd.superReSet();
					Constant.BG_MUSIC_PLAYER.loopMusic(Constant.UI_CONSTANT.BG_MUSIC_01_PATH);
					StartButton sb = (StartButton) GameCpHolder.get("sb");
					sb.reset();
					PauseButton pb = (PauseButton) GameCpHolder.get("pb");
					pb.reset();
				}
			}
			
		}else{
			if(gd.getIsEnd() == 2)return;
			if(gd.getIsEnd() == 0 && gd.getResult() == 0 && player.getUserStatus() == 2){
				JOptionPane.showMessageDialog(pg,"��Ϸ���ڽ����У�","����", JOptionPane.DEFAULT_OPTION); 
			}else{
				//�˻ص�����
				RequestMessage reqMsg = new RequestMessage(2, 3, player, null);
				try {
					ServiceHolder.execute(reqMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	
}

