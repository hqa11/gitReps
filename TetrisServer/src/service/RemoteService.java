package service;

import holder.RoomHolder;
import holder.SocketHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.util.internal.StringUtil;

import util.JSONUtils;

import annotions.SocketService;
import dto.GameDto;
import dto.Player;
import dto.RequestMessage;
import dto.ResponseMessage;

/**
 * ����ҵ���߼���service
 * @author Administrator
 *
 */
public class RemoteService {

	/**
	 * ����ͻ�������
	 * @param msg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 1)// ������뷿��
	public void entryRoom(RequestMessage reqMsg, Channel channel) {
		if(reqMsg == null || channel == null) System.out.println("params is illegal!");
		@SuppressWarnings({ "unchecked" })
		Map<String,Object> map= (Map<String,Object>)reqMsg.getRequestData();
		//�鿴��������
		Integer roomNo = Integer.parseInt(map.get("roomNo") + "");
		Map<Integer, Player> roomPlayers = RoomHolder.get(roomNo);
		//������
		Map<String, Object> responseData = new HashMap<String, Object>();
		if(roomPlayers == null || roomPlayers.size() == 0 || roomPlayers.size() < 2){
			responseData.put("currentNum",roomPlayers == null ? "0" : roomPlayers.size());
			//�鿴���û��ǲ�����������
			if(roomPlayers != null){
				Player palyer  = roomPlayers.get(channel.getId());
				if(palyer != null){
					responseData.put("entryOk","0");
					responseData.put("info","���Ѿ��������ˣ�������");
					ResponseMessage respMsg = new ResponseMessage(1,2,null, responseData );
					//	channel.write(JSONUtils.toJSON(respMsg));
					channel.write(respMsg);
					return;
				}
			}
			//����δ����ҵ���߼�
			responseData.put("entryOk","1");
			responseData.put("roomNo",roomNo);
			responseData.put("info","��ӭ������˷��䣡");
			//��������˷���
			Map<Integer, Player> beforeRoomPlayers = new HashMap<>();
			if(roomPlayers != null)beforeRoomPlayers.putAll(roomPlayers);
			responseData.put("roomUsers",beforeRoomPlayers);

			//���������仹�������ˣ���ô֪ͨ������������
			Map<String,Object> ret = new HashMap<>();
			if(roomPlayers != null && roomPlayers.size() > 0 ){
				for(Integer cId : roomPlayers.keySet()){
					ret.put("come", 1);
					//��Ϊ�����е���һ�����
					Channel vsChannel = SocketHolder.get(cId);
					//֪ͨ�Է���һ�����뿪��
					vsChannel.write(new ResponseMessage(1,9, reqMsg.getPlayer(), ret));
				}
			}
			RoomHolder.put(roomNo, reqMsg.getPlayer(),channel.getId());
			//��¼�������ڵ�λ��
			RoomHolder.getRoomChannelHolder().put(channel.getId(), roomNo);

		}else if(roomPlayers.size() >= 2){
			//TODO ����������ҵ���߼�
			responseData.put("currentNum",roomPlayers.size());
			responseData.put("info","������������Ѿ����ˣ�");
			responseData.put("roomNo",roomNo);
			responseData.put("entryOk","0");
		}
		ResponseMessage respMsg = new ResponseMessage(1, 2, null, responseData );
		//д����Ϣ
		//	channel.write(JSONUtils.toJSON(respMsg));
		channel.write(respMsg);
	}

	/**
	 * ���ͷ����б�
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 3)
	public void pushRoomList(RequestMessage reqMsg, Channel channel){
		Map<Integer, Map<Integer, Player>> roomInfo = RoomHolder.getRoomHolder();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("roomInfo",roomInfo);
		data.put("flag",true);
		ResponseMessage respMsg = new ResponseMessage(1,4,null,data);
		//	channel.write(JSONUtils.toJSON(respMsg));
		channel.write(respMsg);
	}
	
	
	/**
	 * ˢ�·����б�
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 17)
	public void refreshRoomList(RequestMessage reqMsg, Channel channel){
		Map<Integer, Map<Integer, Player>> roomInfo = RoomHolder.getRoomHolder();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("roomInfo",roomInfo);
		data.put("flag",true);
		ResponseMessage respMsg = new ResponseMessage(1,18,null,data);
		channel.write(respMsg);
	}
	

	/**
	 * ׼������ȡ��׼��
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 5)
	public  void readyOrCancelGame(RequestMessage reqMsg, Channel channel){
		System.out.println("������յ���׼������=========================>");
		Map<String,Object> map= (Map<String,Object>)reqMsg.getRequestData();
		//0.ȡ�� //1. ��ʼ
		Integer op = (Integer) map.get("op");
		Integer roomNo = (Integer) map.get("roomNo");
		Map<Integer, Map<Integer, Player>> roomInfo = RoomHolder.getRoomHolder();
		Map<Integer, Player> roomPlayers= roomInfo.get(roomNo);
		Player player = roomPlayers.get(channel.getId());
		//������
		Map<String,Object> ret = new HashMap<>();
		ret.put("op", op);
		int states =  player.getUserStatus();
		//У�鵱ǰ�û�״̬
		if(states == op){
			ret.put("flag", 0);
			ret.put("info", "���Ѿ��ڴ�״̬�ˣ������ظ�����!");
			channel.write(new ResponseMessage(1,10, null, ret));
			return;
		}
		//isAllReady
		if(op == 0){
			//ȡ��
			player.setUserStatus(0);
		}else{
			//׼��
			player.setUserStatus(1);

		}
		//�򷿼��е�����������׼����Ϣ
		if(roomPlayers.isEmpty()){
			ret.put("flag", 0);
			ret.put("info", "��ǰʱ���޷�׼��!");
			channel.write(new ResponseMessage(1,10, null, ret));
			return;
		}
		int hasAllReady = 1;
		Channel vsChannel = null;
		for(Integer cId : roomPlayers.keySet()){
			Integer vsCid = channel.getId();
			if(cId != vsCid){
				//��Ϊ�����е���һ�����
				vsChannel = SocketHolder.get(cId);
				//֪ͨ�Է���׼������Ϣ֪ͨ
				ret.put("vsPlayer", player);
				vsChannel.write(new ResponseMessage(1,11, null, ret));
			}
			Player p = roomPlayers.get(cId);
			if(p.getUserStatus() != 1)hasAllReady = 0;
		}
		//֪ͨ����׼��ok
		ret.put("flag", 1);
		channel.write(new ResponseMessage(1,10, null, ret));

		//���鿴һ���������ǲ��Ƕ��Ѿ�׼���ˣ������׼������ô�����Ϳ�ʼ��Ϸ��֪ͨ
		if(roomPlayers!= null && hasAllReady == 1 && roomPlayers.size() > 1){
			ret.put("isAllReady", 1);
			channel.write(new ResponseMessage(1,12, null, ret));
			vsChannel.write(new ResponseMessage(1,12, null, ret));
		}

	}

	/**
	 * �뿪��������
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 6)
	public void leaveRoom(RequestMessage reqMsg, Channel channel){
		@SuppressWarnings("unchecked")
		Map<String,Object> map= (Map<String,Object>)reqMsg.getRequestData();
		Integer roomNo = Integer.parseInt(map.get("roomNo") + "");
		Map<String,Object> ret = new HashMap<>();
		Map<Integer, Player> roomPlayers = RoomHolder.get(roomNo);
		Integer id = channel.getId();
		if(roomPlayers == null || roomPlayers.get(channel.getId()) == null){
			ret.put("isOk", 0);
		}else{
			//�Ƴ�����
			roomPlayers.remove(channel.getId());
			ret.put("isOk", 1);
			//���������仹�������ˣ���ô֪ͨ�����Ѿ��뿪����Ϣ
			if(roomPlayers.size() > 0 ){
				for(Integer cId : roomPlayers.keySet()){
					if(cId != id){
						ret.put("come",0);
						//��Ϊ�����е���һ�����
						Channel vsChannel = SocketHolder.get(cId);
						//֪ͨ�Է���һ�����뿪��
						vsChannel.write(new ResponseMessage(1,9, null, ret));
					}
				}
			}
		}
		//�����뿪���ˣ��뿪�ɹ�/ʧ����
		channel.write(new ResponseMessage(1,8, null, ret));
	}


	/**
	 * �û�����ʱ���ȷ�ϣ������ok����ô֪ͨ�ͻ�������Game
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 1,cmd = 7)
	public void confirmReadyOk(RequestMessage reqMsg, Channel channel){
		@SuppressWarnings("unchecked")
		Map<String,Object> map= (Map<String,Object>)reqMsg.getRequestData();
		Integer roomNo = Integer.parseInt(map.get("roomNo") + "");
		Map<Integer, Player> roomPlayers = RoomHolder.get(roomNo);
		Integer cId = channel.getId();
		Player player1 = roomPlayers.get(cId);
		player1.setUserStatus(4);
		//�鿴��һ���˵�״̬�Ƿ�Ϊ4
		Map<String,Object> ret = new HashMap<>();
		if(roomPlayers.size() > 0 ){
			for(Integer id : roomPlayers.keySet()){
				if(cId != id){
					//��Ϊ�����е���һ�����
					Player player2 = roomPlayers.get(id);
					if(player2.getUserStatus() == 4){
						ret.put("isAllReady", 1);
						channel.write(new ResponseMessage(1,13, null, ret));
						SocketHolder.get(id).write(new ResponseMessage(1,13, null, ret));
					}
				}
			}
		}
	}
	

	/**
	 * �򷿼��еĶ�ս�û�������Ϸʵ��
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 2,cmd = 1)
	public void pushGameDto(RequestMessage reqMsg, Channel channel){
		Map<String,Object> map = (Map<String,Object>) reqMsg.getRequestData();
		GameDto dto = (GameDto) map.get("gameDto");
		if(dto.getResult() !=0 )return;
		//�����������͸������
		Integer roomNo = Integer.parseInt(map.get("roomNo") + "");
		Map<Integer, Player> roomPlayers = RoomHolder.get(roomNo);
		Integer cId = channel.getId();
		Map<String,Object> ret = new HashMap<>();
		Channel channel2 = null;
		if(roomPlayers.size() > 0 ){
			for(Integer id : roomPlayers.keySet()){
				if(cId != id){
					//��Ϊ�����е���һ�����
					ret.put("gameDto", dto);
					channel2 = SocketHolder.get(id);
					channel2.write(new ResponseMessage(2, 2,null, ret));
				}

			}
		}

		//�ж�˭����
		Map<String,Object> ret2 = new HashMap<>();
		//��Ȼ��һ������ȹ�
		int isEnd = dto.getIsEnd();
		if(isEnd == 1){
			//������������Ϣ
			ret2.put("win", 1);
			channel2.write(new ResponseMessage(2,5,null, ret2));
			ret2.put("win", 0);
			ret2.remove("gameDto");
			channel.write(new ResponseMessage(2,5,null, ret2));

		}

	}

	/**
	 * ��ҷ��ط���
	 * @param reqMsg
	 * @param channel
	 */
	@SocketService(module = 2,cmd = 3)
	public void goBackRoom(RequestMessage reqMsg, Channel channel){
		//��ȡ�����е����
		Map<String,Object> map= (Map<String,Object>)reqMsg.getRequestData();
		Integer roomNo = Integer.parseInt(map.get("roomNo") + "");
		Map<Integer, Player> roomUsers = RoomHolder.get(roomNo);
		Map<String,Object> ret = new HashMap<>();
		if(roomUsers != null){
			ret.put("backOk", 1);
		}else{
			ret.put("backOk", 0);
		}
		channel.write(new ResponseMessage(2, 4,null, ret));

	}

}