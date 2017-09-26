package dto;

import java.io.Serializable;
import java.util.Map;

public class ResponseMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResponseMessage() {
		super();
	}
	//ģ��� 1.��ս����
	private int module;
	//��Ӧ�����1.���뷿��
	private int cmd;

	private Player player;
	//������
	private Object responseData;

	public ResponseMessage(int module, int cmd,Player player,  Map<String,Object> responseData) {
		super();
		this.module = module;
		this.cmd = cmd;
		this.setPlayer(player);
		this.responseData = responseData;
	}
	public int getModule() {
		return module;
	}
	public void setModule(int module) {
		this.module = module;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public  Object getRequestData() {
		return responseData;
	}
	public void setRequestData(Object responseData) {
		this.responseData = responseData;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
