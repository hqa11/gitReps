package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import config.Constant;
import config.GameConfig;
import config.GameCpHolder;
import dto.GameDto;

/**
 * ��Ϸ������
 * @author Administrator
 *
 */
public class OFrameGame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	
	private volatile OPanelGame panelGame;
	public OPanelGame getPanelGame() {
		return panelGame;
	}
	public OFrameGame(GameDto gameDto){
		FrameGame fg= (FrameGame) GameCpHolder.get("frameGame");
		panelGame = new OPanelGame(gameDto);
		this.setTitle("��ң���Ϸ���");
		this.setIconImage(Constant.UI_CONSTANT.COVER_IMG);
		//�޷�ֱ�ӹر�
		this.setDefaultCloseOperation(JFrame. DO_NOTHING_ON_CLOSE);
		//���󴰿������ƶ�һ��
		fg.setLocation(fg.getX()-250, fg.getY());
		this.setSize(fg.getWidth()-750,fg.getHeight() + 160);
		this.setResizable(false);
		this.setLocation(fg.getX()+fg.getWidth()+5, (int)fg.getY());
		this.setContentPane(panelGame);
		this.setLayout(null);
		/*EffectLabel el = (EffectLabel) GameCpHolder.get("el");
		el .setOb(this);
		this.getContentPane().add(el);*/
		this.setVisible(false);
		//��ֹ��������С��
		fg.setExtendedState(JFrame.NORMAL);
		fg.requestFocusInWindow();
		fg.requestFocus();
		
	}

}
