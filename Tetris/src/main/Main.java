package main;

import holder.ServiceHolder;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jboss.netty.channel.Channel;
import service.GameService;
import socket.ClientFactory;
import ui.EffectLabel;
import ui.FrameGame;
import ui.PanelGame;
import ui.PauseButton;
import ui.StartButton;
import util.DataUtil;
import Thread.DownThread;
import Thread.NettyThread;
import config.GameCpHolder;
import control.GameControl;
import control.PlayerControl;
import dto.Game;
import dto.GameDto;
import dto.Player;

public class Main {

	public static void main(String[] args){
		//����MAC�������
		try {
			System.setProperty( "Quaqua.tabLayoutPolicy","wrap");
			JFrame.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
			//����Զ��ҵ�� 
			ServiceHolder.loadServiceMethods();
			//Ϊ�û�����һ��token���ǳ�
			Player player = new Player(UUID.randomUUID().toString(), DataUtil.getChineseName(),3);
			GameCpHolder.put("player",player);
			//Ĭ�ϣ���ͨģʽ
			GameCpHolder.put("game",new Game());
			//����socketChannel
			Channel channel = ClientFactory.getChannel();
			GameCpHolder.put("channel", channel);

			GameDto gameDto = new GameDto();
			//��Ϸʵ�����
			GameCpHolder.put("gameDto", gameDto);
			PanelGame panelGame=new PanelGame(); 
			//�������
			GameCpHolder.put("panelGame", panelGame);
			EffectLabel el = new EffectLabel();
			//��Ч��Ⱦ���
			GameCpHolder.put("el", el);
			GameService gs =new GameService();
			//ҵ���߼�ִ����
			GameCpHolder.put("gameService",gs);
			GameControl gc = new GameControl();
			PauseButton pb = new PauseButton();
			//��ͣ���
			GameCpHolder.put("pb", pb);
			//��Ϸ������
			GameCpHolder.put("gameController",gc);
			PlayerControl pc =new PlayerControl();
			panelGame.setPlayerControl(pc);
			StartButton sb = new StartButton();
			//��ʼ���
			GameCpHolder.put("sb", sb);
			FrameGame frameGame = new FrameGame();
			//����Ϸ���װ������
			GameCpHolder.put("frameGame", frameGame);
			//���������߳�
			Thread.sleep(1000);
			new DownThread().start();
			new NettyThread().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
