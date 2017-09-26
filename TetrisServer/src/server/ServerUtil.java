package server;


import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;

/**
 * netty�����
 * @author Administrator
 *
 */
public class ServerUtil {
	private ServerUtil(){}
	private static ServerBootstrap bootstrap;
	public static void stopServer(){
		bootstrap.releaseExternalResources();
		bootstrap.getFactory().shutdown();
	}
	
    /**
     * ��������
     */
	public static void startUpServer(String host,int port){
		//  ������
		ServerBootstrap bootstrap = new ServerBootstrap();

		ExecutorService boss = Executors.newCachedThreadPool();
		ExecutorService worker = Executors.newCachedThreadPool();

		//����nioSocket����
		bootstrap.setFactory(new NioServerSocketChannelFactory(boss, worker));

		//���ùܵ�����
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			@Override
			public ChannelPipeline getPipeline() throws Exception {

				//��ȡһ���ܵ�
				ChannelPipeline pipeline = Channels.pipeline();

				//���Ա����ڽ�����Ϣ��ʱ��תΪchannelbuffer,ֱ��ʹ��string����
				/*pipeline.addLast("decoder",new StringDecoder());
				//���Ա����ڷ�����Ϣ��ʱ��ʹ��channelBuffer,ֱ��ʹ��string����
				pipeline.addLast("encoder",new StringEncoder());*/
				pipeline.addLast("decoder",new ObjectDecoder(ClassResolvers.cacheDisabled(this
		                .getClass().getClassLoader())));
				pipeline.addLast("encoder",new ObjectEncoder());
				pipeline.addLast("Handler",new Handler());
				return pipeline;
			}
		});

		//�󶨶˿�
		bootstrap.bind(new InetSocketAddress(host,port));
		ServerUtil.bootstrap = bootstrap;
		System.out.println("Server has started!");
	}

}
