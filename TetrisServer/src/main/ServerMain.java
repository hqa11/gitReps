package main;

import javax.swing.JFrame;
import javax.swing.UIManager;

import dto.ServerConfig;
import holder.ServiceHolder;
import server.ServerUtil;
import ui.ServerFrame;

public class ServerMain {

	public static void main(String[] args) throws Exception {
		//װ�ص��ȷ���
		ServiceHolder.loadServiceMethods();
		//����������
		JFrame.setDefaultLookAndFeelDecorated(true);
		System.setProperty( "Quaqua.tabLayoutPolicy","wrap");
		try{
			UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
		}catch(Exception e){}
		ServerConfig config = ServerConfig.getConfig();
		if(config.getStartModel() == 0){
			//����ģʽ
			new ServerFrame();
		}else if(config.getStartModel() == 1){
			//cmdģʽ
			ServerUtil.startUpServer(config.getHost(),config.getPort());
		}
	}
	
}
