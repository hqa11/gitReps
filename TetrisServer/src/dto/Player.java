package dto;

import java.io.Serializable;

public class Player implements Serializable{

	public Player(String userToken, String userName,Integer userStatus) {
		super();
		this.userToken = userToken;
		this.userName = userName;
		this.userStatus = userStatus;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userToken;
	private String userName;
	//0.δ׼�� 1.׼����  4.��ȷ�����2.��Ϸ��  3.δ���뷿��
	private Integer userStatus = 0;
	public Player() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	
}
