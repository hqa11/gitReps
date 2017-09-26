package socket;

import javax.swing.JOptionPane;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import config.GameCpHolder;
import dto.ResponseMessage;
import holder.ServiceHolder;
import ui.FrameGame;

public class CHandler extends SimpleChannelHandler {

	/**
	 * channel�رյ�ʱ��Żᴥ��
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelClosed...");
	}

	/**
	 * ���ӽ�����ʱ�򴥷�
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelConnected...");
	}

	/**
	 * �����������Ѿ��������ر�ͨ����ʱ��Żᴥ��
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		System.out.println("channelDisconnected...");
		FrameGame fg = (FrameGame)GameCpHolder.get("frameGame");
		if(fg != null)JOptionPane.showMessageDialog(fg,"��⣬���ƺ��Ѿ�������!","����", JOptionPane.DEFAULT_OPTION);
	}

	/**
	 * �쳣״̬����
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		System.out.println("exceptionCaught333..."+e.getCause().getStackTrace());
	}

	/**
	 * ������Ϣʱ�򴥷�
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		System.out.println("client messageReceived...");
		try {
			//��ȡ�ͻ��˵�������
			String message = e.getMessage().toString();
			Channel channel = ctx.getChannel();
			//����ת��Ϊʵ����
			ResponseMessage msg = null;
			try {
			//	msg = JSONUtils.fromJSON(message,ResponseMessage.class);
				msg = (ResponseMessage) e.getMessage();
			} catch (Exception e1) {
				channel.write("****ERROR***");
				e1.printStackTrace();
				return;
			}
			//Ŀ�귽��
			ServiceHolder.executeRsp(msg,channel);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
