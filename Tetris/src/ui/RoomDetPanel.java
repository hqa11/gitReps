package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import config.Constant;

import dto.Player;

public class RoomDetPanel extends JPanel{
	private Player player1;
	private Player player2;
	private ReadyButton  rb;
	private ReturnButton rtb;
	public void setRtb(ReturnButton rtb) {
		this.rtb = rtb;
	}
	public ReturnButton getRtb() {
		return rtb;
	}
	Font font = new Font("΢���ź�",Font.BOLD,18);
	public RoomDetPanel(int roomNo){
		this.setLayout(null);
		this.setOpaque(false);
		this.setBackground(null);
		rb = new ReadyButton(roomNo); 
		rtb = new ReturnButton(roomNo);
		this.setRb(rb);
		this.add(rb);
		this.setRtb(rtb);
		this.add(rtb);
	}
@Override
public void paint(Graphics g) {
	
	g.drawImage(Constant.UI_CONSTANT.ROOM_BG_IMG, 0,
			0,this.getWidth(),this.getHeight(),null);
	//�Ȼ�������Сͷ��(��㻭)
	g.drawImage(Constant.UI_CONSTANT.PLAYER1_IMG, 40,
			50,120,120,null);
	
    //����VS����
	g.setFont(new Font("΢���ź�",Font.BOLD,40));
	g.setColor(Color.PINK);
	g.drawString("VS", 170, 120);
	
	g.drawImage(player2 != null ? Constant.UI_CONSTANT.PLAYER2_IMG : Constant.UI_CONSTANT.PLAYER3_IMG, 240,
			50,120,120,null);
	
	
	//��������һ���ready
	if(player1 != null && player1.getUserStatus() == 1){	
		g.drawImage(Constant.UI_CONSTANT.TX_READY_IMG, 60,
			70,120,120,null);
	}
	
	if(player2 != null && player2.getUserStatus() == 1){
		g.drawImage(Constant.UI_CONSTANT.TX_READY_IMG, 260,
				70,120,120,null);
	}
	
	
	g.setColor(Color.ORANGE);
	g.setFont(font);
	if(player1 != null){
		g.drawString("���:" + player1.getUserName(),50,210);
	}
	if(player2 != null){
		g.drawString("���:" + player2.getUserName(),250,210);
	}else{
		g.setColor(Color.RED);
		g.drawString("���:" + "�������",250,210);
	}
	super.paint(g);
}
public Player getPlayer1() {
	return player1;
}
public void setPlayer1(Player player1) {
	this.player1 = player1;
}
public Player getPlayer2() {
	return player2;
}
public void setPlayer2(Player player2) {
	this.player2 = player2;
}
public ReadyButton getRb() {
	return rb;
}
public void setRb(ReadyButton rb) {
	this.rb = rb;
}
     
	
}