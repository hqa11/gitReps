package server;


import holder.FrameHolder;
import holder.RoomHolder;
import holder.ServiceHolder;
import holder.SocketHolder;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import dto.GameDto;
import dto.RequestMessage;
import service.RemoteService;
import util.JSONUtils;

public class Handler extends IdleStateAwareChannelHandler implements ChannelHandler {
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
		//����connectװ��Map
		Channel channel = ctx.getChannel();
		SocketHolder.put(channel.getId(), channel);
		System.out.println("channelConnected...");
		FrameHolder.consoleAppend("��������˷�����.......",0);
	}

	/**
	 * �����������Ѿ��������ر�ͨ����ʱ��Żᴥ��
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		Channel channel = ctx.getChannel();
		SocketHolder.remove(channel.getId());
		Integer roomNo = RoomHolder.getRoomChannelHolder().get(channel.getId());
		if(roomNo == null){
			FrameHolder.consoleAppend("��ҶϿ�������.......",0);
			return;
		}
		RoomHolder.getRoomChannelHolder().remove(channel.getId());
		RoomHolder.remove(roomNo,channel.getId());
		System.out.println("channelDisconnected...");
		FrameHolder.consoleAppend("�����Ϊ��"+roomNo+"�е���ҶϿ�������.......",0);
	}

	/**
	 * �쳣״̬����
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		System.out.println("exceptionCaught124..." + e.getCause().getStackTrace());
		FrameHolder.consoleAppend("exceptionCaught.......",1);
	}

	/**
	 * ������Ϣʱ�򴥷�
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			//��ȡ�ͻ��˵�������
			//			String message = e.getMessage().toString();
			Channel channel = ctx.getChannel();
			//����ת��Ϊʵ����
			RequestMessage msg = null;
			try {
				msg = (RequestMessage) e.getMessage();
				FrameHolder.consoleAppend("���յ�����moduleΪ"+msg.getModule()+",�����Ϊ"+msg.getCmd()+"�����ݰ�.......",0);
			} catch (Exception e1) {
				channel.write("****ERROR***");
				FrameHolder.consoleAppend("��ȡ�������ʽ������.,Exception:" + e1.getStackTrace(),1);
				e1.printStackTrace();
				return;
			}
			//Ŀ�귽��
			ServiceHolder.execute(msg,channel);
			super.messageReceived(ctx, e);
		} catch (Exception e1) {
			e1.printStackTrace();
			FrameHolder.consoleAppend("ҵ�񷽷�ִ�д���,Exception:" + e1.getStackTrace(),1);
		}
	}

}
