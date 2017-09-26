package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import config.Constant;
import config.GameConfig;
import config.GameCpHolder;
import config.UiCons;
import control.GameControl;
import control.PlayerControl;
import dto.Game;
import dto.GameDto;
import dto.MapCell;
import service.GameService;

public class PanelGame extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GameConfig gameConfig= Constant.GAME_CONFIG;
	private List<Layer> lays = new ArrayList<>();
	private GameDto gameDto;
	private   int PAUSE_IMG_WIDTH = Constant.UI_CONSTANT.PAUSE_IMG.getWidth(null);
	private   int PAUSE_IMG_HEIGHT = Constant.UI_CONSTANT.PAUSE_IMG.getHeight(null);
	Game game = (Game) GameCpHolder.get("game");
	public PanelGame() {
		
		this.setOpaque(false);
		
		this.setBackground(null);

		this.gameDto = (GameDto) GameCpHolder.get("gameDto");

		this.initCompont();	

		this.initLayer();

		this.initMusicPlayer();
		
		for (Layer layer : lays) {

			//������Ϸ���ݶ���
			layer.setJp(this);

		}

	}

	/**
	 * ��ʼ��layer
	 */
	public void initLayer(){

		this.lays = gameConfig.getLayers();

		for (Layer layer : lays) {

			//������Ϸ���ݶ���
			layer.setGameDto(this.gameDto);

		}

	}

	/**
	 * ��ʼ�����ֲ�����
	 */
	public void initMusicPlayer(){

	new Thread(new  Runnable() {
		public void run() {
			try {
				Thread.sleep(1500);
					Constant.BG_MUSIC_PLAYER.loopMusic(Constant.UI_CONSTANT.BG_MUSIC_01_PATH); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}).start();

	}


	/**
	 * ��ʼ�����
	 */
	public void initCompont(){



	}

	/**
	 * ������Ʒ���
	 */
	@Override
	protected synchronized void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		//��ý���(���ڼ��������¼�)
		this.requestFocus();
		Layer layGame = null;
		for (Layer lay : lays) {
			lay.paint(g);
			if(lay instanceof LayerGame){
				layGame = lay;
			}
		}
		if(this.gameDto.getIsEnd() == 2){
			doBeginGame(g,layGame);
			return;
		}
		
		//��ͣ��
		if(this.gameDto.getIsEnd() == 3){
			System.out.println("pause-----------------");
			doPauseGame(g,layGame);
			return;
		}
		
		
		if(this.gameDto.getIsEnd() == 1){
			
			if(game.getGameModel() == 2){
				
				if(this.gameDto.getResult() == 1){
					doLoseGame(g,layGame);
					return;
				}
				
				if(this.gameDto.getResult() == 2){
					doWinGame(g,layGame);
					return;
				}
			}
			
			doEndGame(g,layGame);
			return;
		}
		
		

		for (Layer lay : lays) {
			lay.paint(g);
		}
	
	}

	
	private void doLoseGame(Graphics g, Layer layGame) {
		//����Ļ�������еķ������ѵ�
				MapCell[][] map = gameDto.getGameMap();
				for (int i = 0; i< map.length;i++) {
					for(int j =0;j < map[i].length;j++){
						if(map[i][j].isSt()){
							g.drawImage(Constant.UI_CONSTANT.GAME_IMG,
									layGame.x + j * 32 +7 ,
									layGame.y + i * 32 +7 ,
									layGame.x + j * 32 +7 + 32,
									layGame.y + i * 32 +7 + 32,
									32*8,0,32*9 ,32, null);
						}						
					}
				}
				g.drawImage(Constant.UI_CONSTANT.LOSE_IMG,
						510,260,664,360,0,0,154,100,null);
				g.setColor(Color.red);
				g.drawString("��R�˻���Ϸ����", layGame.x + 70, layGame.y + 360);
	}

	private void doWinGame(Graphics g, Layer layGame) {
		g.drawImage(Constant.UI_CONSTANT.WIN_IMG,
				510,260,664,360,0,0,154,100,null);
		g.setColor(Color.green);
		g.drawString("��R�˻���Ϸ����", layGame.x + 70, layGame.y + 360);
		//����ʤ������
		Constant.ACT_MUSIC_PLAYER.playMusic(Constant.UI_CONSTANT.WIN_MUSIC);
	}

	/**
	 * ��ͣ��Ϸ��
	 * @param g
	 * @param layGame
	 */
	private void doPauseGame(Graphics g, Layer layGame) {
		g.drawImage(Constant.UI_CONSTANT.PAUSE_IMG,
				layGame.x + 6,
				layGame.y + 6,
				layGame.x + layGame.w - 7,
				layGame.y + layGame.h - 7,
				0,0,PAUSE_IMG_WIDTH ,PAUSE_IMG_HEIGHT, this);
	}

	/**
	 * ׼����ʼ��Ϸ
	 * @param g
	 * @param layGame 
	 */
	private void doBeginGame(Graphics g, Layer layGame) {
		g.drawImage(Constant.UI_CONSTANT.COVER_IMG,
				layGame.x + 6,
				layGame.y + 6,
				layGame.x + layGame.w - 6,
				layGame.y + layGame.h - 7,
				0,0,Constant.UI_CONSTANT.COVER_IMG.getWidth(null) ,Constant.UI_CONSTANT.COVER_IMG.getHeight(null), null);
	}

	/**
	 * gameOver��
	 * @param g
	 * @param layGame 
	 */
	private void doEndGame(Graphics g, Layer layGame) {
		//����Ļ�������еķ������ѵ�
		MapCell[][] map = gameDto.getGameMap();
		for (int i = 0; i< map.length;i++) {
			for(int j =0;j < map[i].length;j++){
				if(map[i][j].isSt()){
					g.drawImage(Constant.UI_CONSTANT.GAME_IMG,
							layGame.x + j * 32 +7 ,
							layGame.y + i * 32 +7 ,
							layGame.x + j * 32 +7 + 32,
							layGame.y + i * 32 +7 + 32,
							32*8,0,32*9 ,32, null);
				}						
			}
		}
		g.drawImage(Constant.UI_CONSTANT.GAME_OVER_IMG,
				430,260,730,360,0,0,300,100,null);
		g.setColor(Color.green);
		g.drawString("�Ƿ������Ϸ��(��Y/N)", layGame.x + 70, layGame.y + 360);
	}

	public void setPlayerControl(PlayerControl pc) {

		this.addKeyListener(pc);

	}


}
