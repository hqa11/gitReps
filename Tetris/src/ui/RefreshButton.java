package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import dto.RequestMessage;
import holder.ServiceHolder;

public class RefreshButton extends JButton{
	public RefreshButton(final String name){
		this.setFont(new Font("΢���ź�",Font.ITALIC, 15));
		this.setText(name);
		this.setBounds(250,15,130,35);
		Cursor Cusor = new Cursor(Cursor.HAND_CURSOR);
		this.setCursor(Cusor);
		this.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RefreshButton.this.setEnabled(false);
				//���ٵ�ǰ�б���������F�����¼�
				RequestMessage resMsg = new RequestMessage(1,17,null,null);
				try {
					ServiceHolder.execute(resMsg);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		//�˰�ťһ����ʼ��
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(20000);
						RefreshButton.this.setText("�����Զ�ˢ��...");
						RefreshButton.this.setForeground(Color.red);
						RefreshButton.this.doClick();
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		}).start();
		
	}

}
