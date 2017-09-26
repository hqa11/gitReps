package service;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jboss.netty.channel.Channel;

import annotions.SocketService;
import config.Constant;
import config.GameCpHolder;
import dto.Game;
import dto.GameDto;
import dto.Player;
import dto.RequestMessage;
import dto.ResponseMessage;
import entity.GameAct;
import ui.OFrameGame;
import ui.OPanelGame;
import ui.ReadyButton;
import ui.RefreshButton;
import ui.RoomDetPanel;
import ui.RoomEntryButton;
import ui.RoomFrame;
import ui.RoomLabel;
import ui.RoomListFrame;
import ui.RoomPanel;
import ui.ShowMessageFrame;
import ui.StartButton;

/**
 * Զ�̲���service
 * @author Administrator
 *
 */
public  class  RemoteService {
	//��һ���̳߳�
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 8, 0L,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100000),
			new ThreadPoolExecutor.CallerRunsPolicy());
	public RemoteService(){

	}

	/**
	 * ���뷿��
	 * @param message
	 */
	@SocketService(module = 1, cmd = 1)
	public void entryRoom(RequestMessage message){

		Channel channel = (Channel) GameCpHolder.get("channel");

		//	channel.write(JSONUtils.toJSON(message));
		channel.write(message);
	}

	/**
	 * ����������뷿��ķ������Ӧ
	 * @param message
	 */
	@SocketService(module = 1, cmd = 2)
	public void entryRoomRsp(ResponseMessage message,Channel channel){
		//����message
		RoomListFrame roomListFrame = (RoomListFrame)GameCpHolder.get("roomListFrame");
		Map<String, Object> requestData = (Map<String, Object>) message.getRequestData();
		String isOk = requestData.get("entryOk") + "";
		String info = requestData.get("info") + "";
		if("1".equals(isOk)){
			Player player =(Player)GameCpHolder.get("player");
			//����Ϊδ׼��״̬
			player.setUserStatus(0);
			//�������б�����
			roomListFrame.setVisible(false);
			//������ʱ����������û����������(���ǵ�ǰ�û�����֮ǰ�ķ���״��)
			Map<Integer, Player> roomUsers = (Map<Integer, Player>) requestData.get("roomUsers");
			Player player2 = null;
			if(roomUsers !=null && !roomUsers.isEmpty()){
				//��Ⱦ����һ����
				for (Integer key : roomUsers.keySet()) {
					player2 = roomUsers.get(key);
					break;
				}
			}

			//��ʾ����
			RoomFrame rf = new RoomFrame(Integer.parseInt(requestData.get("roomNo") + ""));
			GameCpHolder.put("currentRoom", rf);
			System.out.println(GameCpHolder.gameComponets);
			//��Ⱦ����
			RoomDetPanel rdp = (RoomDetPanel)(rf.getContentPane());
			rdp.setPlayer1(player);
			if(player2 != null)rdp.setPlayer2(player2);
			rdp.revalidate();
			//	rdp.repaint();
			//	rdp.repaint(0, 0, rdp.getWidth(), rdp.getHeight()/2);
			//���û���ǰ���ڵķ���������
			Constant.BG_MUSIC_PLAYER.loopMusic(Constant.UI_CONSTANT.IN_ROOMBG_MUSIC); 
			System.out.println(info);
		}else{
			JOptionPane.showMessageDialog(roomListFrame, info,"����", JOptionPane.DEFAULT_OPTION);
			System.out.println(info);
		}
		//����ť���"����"
		RoomEntryButton rb = roomListFrame.getRps()[Integer.parseInt(requestData.get("roomNo") + "") - 1].getReb();
		rb.setFont(new Font("΢���ź�",Font.ITALIC,15));
		rb.setText("����");
		rb.setForeground(null);
		rb.revalidate();
	}

	/**
	 * ��ȡ�����б���Ϣ
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 3)
	public void getRoomInfo(RequestMessage message,Channel channel){
		System.out.println("��ʼ��ȡ�����б���Ϣ��" + message.getRequestData());
		//  channel.write(JSONUtils.toJSON(message));
		channel.write(message);
	}
	
	
	/**
	 * ˢ�·����б�
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 17)
	public void refreshRoomList(RequestMessage message){
		Channel channel = (Channel) GameCpHolder.get("channel");
		channel.write(message);
	}

	/**
	 * ��ȡ�����б���Ӧ����
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 4)
	public void getRoomInfoRsp(ResponseMessage message,Channel channel){
		@SuppressWarnings("unchecked")
		Map<Integer, Map<Integer, Player>> roomInfo = (Map<Integer, Map<Integer, Player>>) ((Map<String, Object>)message.getRequestData()).get("roomInfo");
		System.out.println("���ܵ��˷����б���Ϣ������������>" + roomInfo);
		//��ʼ��Ⱦ�����б�
		RoomListFrame roomListFrame = (RoomListFrame) GameCpHolder.get("roomListFrame");
		//��������
		RoomPanel[] rps = roomListFrame.getRps();
		for(int i = 1;i < 11 ;i ++){
			Map<Integer, Player> room = roomInfo.get(i);
			RoomLabel label = new RoomLabel(i);
			RoomEntryButton rb = new RoomEntryButton("����",i);
			RoomPanel rp = null;
			if(room == null || room.isEmpty()){
				//�������ë�˶�û��
				label.setText("Room0"+i+":�������..");
				label.setToolTipText("Room0"+i+":�������..");
				label.setForeground(Color.green);
				rp = new RoomPanel(i,label, rb);
				//	rp.setBackground(Color.green);
				rp.setRoomState(0);
			}else{
				int size = room.size();
				String desc ="Room0"+i+ ":��ǰ����" + size + "��";
				String toolTip = "��� ";
				rp = new RoomPanel(i,label, rb);
				if(size == 2){
					desc +="(����)";
					rb.setEnabled(false);  
					//		rp.setBackground(Color.red);
					rp.setRoomState(2);
				}else{
					//		rp.setBackground(Color.yellow);
					rp.setRoomState(1);
				}
				label.setText(desc);
				int index = 0;
				for (Integer key : room.keySet()) {
					toolTip += "," + room.get(key).getUserName();
					index ++;
				}
				if(index == 1){
					label.setToolTipText(toolTip + "���ڵȴ���Ϸ...");
					label.setForeground(Color.yellow);
				}else{
					label.setToolTipText(toolTip + "������Ϸ��...");
					label.setForeground(Color.red);
				}

			}
			rps[i-1] = rp;
		}

		//��ȡ�ɹ�����start��ť����
		StartButton sb = (StartButton) GameCpHolder.get("sb");
		sb.setEnabled(false);

		//����һ��ͷ��
		JPanel head = new JPanel();
		RefreshButton rb = new RefreshButton("ˢ���б�") ;
		head.setLayout(null);
		head.add(rb);
		roomListFrame.setRb(rb);
		roomListFrame.setHead(head);
		roomListFrame.add(head);
		roomListFrame.getContentPane().add(head);
		roomListFrame.appendRooms();
		//��Ϸ���뵽��սģʽ
		Game  game = (Game) GameCpHolder.get("game");
		game.setGameModel(2);
	}

	
	/**
	 * ˢ�·����б���Ӧ����
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 18)
	public void refreshRoomInfoRsp(final ResponseMessage message,Channel channel){
		threadPoolExecutor.execute(new Runnable() {
			public void run() {
		
				Map<Integer, Map<Integer, Player>> roomInfo = (Map<Integer, Map<Integer, Player>>) ((Map<String, Object>)message.getRequestData()).get("roomInfo");
				System.out.println("���ܵ��˷����б���Ϣ������������>" + roomInfo);
				//��ʼ��Ⱦ�����б�
				RoomListFrame roomListFrame = (RoomListFrame) GameCpHolder.get("roomListFrame");
				//��������
				RoomPanel[] rps = roomListFrame.getRps();
				//������Ⱦ����
				int i = 1;
				for (RoomPanel rp : rps) {
					Map<Integer, Player> room = roomInfo.get(i);
					RoomLabel label = rp.getRb();
					RoomEntryButton rb = rp.getReb();
					if(room == null || room.isEmpty()){
						//�������ë�˶�û��
						label.setText("Room0"+i+":�������..");
						label.setToolTipText("Room0"+i+":�������..");
						label.setForeground(Color.green);
						rb.setEnabled(true);  
						rp.setRoomState(0);
					}else{
						int size = room.size();
						String desc ="Room0"+i+ ":��ǰ����" + size + "��";
						String toolTip = "��� ";
						if(size == 2){
							desc +="(����)";
							rb.setEnabled(false);  
							rp.setRoomState(2);
						}else{
							rp.setRoomState(1);
							rb.setEnabled(true);  
						}
						label.setText(desc);
						int index = 0;
						for (Integer key : room.keySet()) {
							toolTip += "," + room.get(key).getUserName();
							index ++;
						}
						if(index == 1){
							label.setToolTipText(toolTip + "���ڵȴ���Ϸ...");
							label.setForeground(Color.yellow);
						}else{
							label.setToolTipText(toolTip + "������Ϸ��...");
							label.setForeground(Color.red);
						}

					}
					i ++;
				}
				roomListFrame.getRb().setEnabled(true);
				roomListFrame.getRb().setText("ˢ���б�");
				roomListFrame.getRb().setForeground(Color.black);
				roomListFrame.getContentPane().revalidate();
			}
		});
		
	}

	/**
	 * ���2����/�뿪�˷���
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 9)
	public void theOtherLeave(ResponseMessage message,Channel channel){
		Map<String,Object> ret =(Map<String, Object>) message.getRequestData();
		//��ȡ�û���ǰ���ڷ���
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		if(currentRoom == null)	JOptionPane.showMessageDialog(null,"Room is missing!","����", JOptionPane.DEFAULT_OPTION);
		int come = (int) ret.get("come");
		if(come == 0){
			//�����뿪			
			((RoomDetPanel)currentRoom.getContentPane()).setPlayer2(null);
		}else if(come == 1){
			//���ֽ���
			Player player2 = message.getPlayer();
			((RoomDetPanel)currentRoom.getContentPane()).setPlayer2(player2);
		}
		currentRoom.getContentPane().revalidate();
		currentRoom.getContentPane().repaint();
	}

	/**
	 * ׼��/ȡ����Ϸ
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 5)
	public void readyOrCancelGame(RequestMessage message){
		System.out.println("��ʼ׼����ȡ����" + message.getRequestData());
		Channel channel = (Channel) GameCpHolder.get("channel");
		//  channel.write(JSONUtils.toJSON(message));
		channel.write(message);
	}


	/**
	 * ��ȡ��׼���ɹ�����Ϣ
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 10)
	public void getMyReadyResponse(ResponseMessage message,Channel channel){
		System.out.println("get����׼����Ӧ��" );
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		int flag = (int) ret.get("flag");
		int op = (int) ret.get("op");
		if(flag == 0){
			//ʧ����
			JOptionPane.showMessageDialog(currentRoom, ret.get("info"),"����", JOptionPane.DEFAULT_OPTION);
		}else{
			//׼���ɹ�������
			//����ť��Ϊȡ��
			RoomDetPanel rdp = (RoomDetPanel) currentRoom.getContentPane();
			ReadyButton rb = rdp.getRb();
			rb.setStatus(op == 0 ? 1 : 2);
			rb.setText(op == 0 ? "׼��" : "ȡ��");
			rb.setEnabled(true);
			//��ǰ�û�״̬��Ϊ1(һ׼��)
			Player player = (Player) GameCpHolder.get("player");
			player.setUserStatus(op == 0 ? 0 : 1);
			//ͷ������logo��ready��
			rdp.getPlayer1().setUserStatus(op == 0 ? 0 : 1);
			rdp.validate();
			rdp.repaint();
		}

	}


	/**
	 * ��ȡ�Է��Ѿ�׼������ȡ����֪ͨ
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 11)
	public void getOtherReadyResponse(ResponseMessage message,Channel channel){
		System.out.println("get����׼����Ӧ��" );
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		int op = (int) ret.get("op");
		RoomDetPanel rdp = (RoomDetPanel) currentRoom.getContentPane();
		//ͷ������logo��ready��
		rdp.getPlayer2().setUserStatus(op == 0 ? 0 : 1);
		rdp.validate();
		rdp.repaint();
	}


	/**
	 * ��ȡ���˶��Ѿ�׼���õ�֪ͨ�����Կ�ʼ����ʱ
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 12)
	public void getIsAllReady(ResponseMessage message,final Channel channel){
		System.out.println("get������Ϸ����ʱ��Ӧ��" );
		//JOptionPane.showMessageDialog(null, "׼��׼������������Ϸ������ʼ��","����", JOptionPane.DEFAULT_OPTION);
		final RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		ShowMessageFrame sf = new ShowMessageFrame(5, currentRoom);
		final Player player =(Player)GameCpHolder.get("player");
		new Thread(new  Runnable() {
			public void run() {
				currentRoom.setEnabled(false);
				try {
					Thread.sleep(6000);
					//�������ȷ����Ϸ��ʼ
					Map<String,Object> map = new HashMap<>();
					map.put("roomNo", currentRoom.getRoomNo());
					player.setUserStatus(4);
					channel.write(new RequestMessage(1,7, player, map));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				currentRoom.setEnabled(true);
			}
		}).start();
	}

	/**
	 * һ��׼�����������ڿ��Կ�ʼ��Ϸ��
	 * @param message
	 * @param channel
	 */
	@SocketService(module = 1, cmd = 13)
	public void startGame(ResponseMessage message, Channel channel){
		//1.����ǰ��ROOM����
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		currentRoom.setVisible(false);
		//2.����һ���µ�GAMEFRAME
		//�ȴ���һ��
		GameDto gameDto = new GameDto();
		gameDto.setGameAct(new GameAct());
		OFrameGame og = new OFrameGame(gameDto);
		GameCpHolder.put("currentLookGamePanel",og);
		//3.��ʼ������Ϸ
		GameDto gd = (GameDto) GameCpHolder.get("gameDto");
		//��ʼ��Ϸ
		StartButton sb =  (StartButton) GameCpHolder.get("sb");
		sb.setEnabled(true);
		sb.doClick();
		sb.setEnabled(false);
		Player player =(Player)GameCpHolder.get("player");
		player.setUserStatus(2);
		og.setVisible(true);
	}



	/**
	 * �˳���������
	 * @param reqMsg
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 1,cmd = 6)
	public synchronized  void leaveRoom(RequestMessage reqMsg){
		Channel channel = (Channel) GameCpHolder.get("channel");
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		Player player = reqMsg.getPlayer();
		if(player.getUserStatus() == 1){
			JOptionPane.showMessageDialog(currentRoom, "ϵͳ��⵽������׼��״̬������ȡ����","����", JOptionPane.DEFAULT_OPTION);
			RoomDetPanel rdp = (RoomDetPanel) currentRoom.getContentPane();
			rdp.getRtb().setEnabled(true);
			return;
		}
		channel.write(reqMsg);
	}


	/**
	 * �˳�������Ӧ
	 * @param reqMsg
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 1,cmd = 8)
	public synchronized  void leaveRoomResp(ResponseMessage message, Channel channel){
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		int leaveOk = (int) ret.get("isOk");
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		Player player =	(Player) GameCpHolder.get("player");
		RoomListFrame roomListFrame = (RoomListFrame)GameCpHolder.get("roomListFrame");
		if(leaveOk == 0){
			JOptionPane.showMessageDialog(currentRoom, "�������쳣������ʧ�ܣ�","����", JOptionPane.DEFAULT_OPTION);
		}else{
			//�˻��б����ٷ��䣬�޸����״̬��
			if(currentRoom != null)currentRoom.dispose();
			player.setUserStatus(3);
			roomListFrame.reSetLocation();
			roomListFrame.setVisible(true);
			Constant.BG_MUSIC_PLAYER.loopMusic(Constant.UI_CONSTANT.BG_MUSIC_01_PATH); 
		}
	}




	/**
	 * �򷿼��еĶ�ս�û�������Ϸʵ��
	 * @param reqMsg
	 * @param channel
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 2,cmd = 1)
	public synchronized  void pushGameDto(RequestMessage reqMsg){
		GameDto dto = (GameDto) GameCpHolder.get("gameDto");
		if(dto.getResult() != 0)return;
		Channel channel = (Channel) GameCpHolder.get("channel");
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		((Map<String,Object>)reqMsg.getRequestData()).put("roomNo",currentRoom.getRoomNo());
		channel.write(reqMsg);
	}


	/**
	 * ��Ⱦ��Ҷ���famre��
	 * @param reqMsg
	 * @param channel
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 2,cmd = 2)
	public synchronized void renderLookGame(final ResponseMessage message,final Channel channel){
		System.out.println("��ȡ������ң������ݰ�....");
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		final GameDto dg = (GameDto) ret.get("gameDto");
		//��ȡ��ǰ����ң���Ϸ����
		OFrameGame og = (OFrameGame) GameCpHolder.get("currentLookGamePanel");
		final OPanelGame opg = og.getPanelGame();
		Thread  t = new Thread(new Runnable() {
			@Override
			public void run() {
				//������Ҵ��͹�������Ϸʵ��
				opg.setGameDto(dg);
				opg.resetLayer();
				opg.repaint();
			}
		});
		t.start();
	}

	/**
	 * ���˵�֪ͨ��
	 * @param reqMsg
	 * @param channel
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 2,cmd = 5)
	public synchronized void whoWinNotice(ResponseMessage message,Channel channel){
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		final int win = Integer.parseInt(ret.get("win") + "");

		//��ȡ��ǰ����ң���Ϸ����
		OFrameGame og = (OFrameGame) GameCpHolder.get("currentLookGamePanel");
		final OPanelGame opg = og.getPanelGame();
		//��ǰ�ģ����
		final GameDto dg = opg.getGameDto();
		final GameDto dto = (GameDto) GameCpHolder.get("gameDto");
		Thread  t = new Thread(new Runnable() {
			@Override
			public void run() {
				//������Ҵ��͹�������Ϸʵ��
				if(win == 1){
					//��Ӯ��
					dg.setResult(1);
					dto.setResult(2);
				}else{
					//������
					dg.setResult(2);
					dto.setResult(1);
				}
				dg.setIsEnd(1);
				dto.setIsEnd(1);
				opg.setGameDto(dg);
				opg.resetLayer();
				opg.repaint();
			}
		});

		t.start();

	}




	/**
	 * ����˻ط������桡
	 * @param reqMsg
	 * @param channel
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 2,cmd = 3)
	public synchronized void goBackRoom(RequestMessage message){
		Channel channel = (Channel) GameCpHolder.get("channel");
		//֪ͨ������޸����״̬
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		Map<String,Object> map = new HashMap<>();
		map.put("roomNo", currentRoom.getRoomNo());
		message.setRequestData(map);
		channel.write(message);
	}


	/**
	 * ����˻ط����������Ӧ���
	 * @param reqMsg
	 * @param channel
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@SocketService(module = 2,cmd = 4)
	public synchronized void goBackRoomResp(ResponseMessage message,Channel channel){
		Map<String,Object> ret = (Map<String, Object>) message.getRequestData();
		String isOk = ret.get("backOk") + "";
		RoomFrame currentRoom = (RoomFrame)GameCpHolder.get("currentRoom");
		OFrameGame og = (OFrameGame) GameCpHolder.get("currentLookGamePanel");
		GameDto dto = (GameDto) GameCpHolder.get("gameDto");
		if("1".equals(isOk)){
			//�����ǰ���2����
			og.dispose();
			//�����״̬��Ϊδ׼��
			RoomDetPanel rdp = (RoomDetPanel) currentRoom.getContentPane();
			ReadyButton rb = rdp.getRb();
			currentRoom.setVisible(true);
			rb.doClick();
			dto.superReSet();
			Constant.BG_MUSIC_PLAYER.loopMusic(Constant.UI_CONSTANT.IN_ROOMBG_MUSIC); 
		}else{
			JOptionPane.showMessageDialog(currentRoom, "�������쳣������ʧ�ܣ�","����", JOptionPane.DEFAULT_OPTION);
		}
	}

}
