package socket;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import config.Constant;

/**
 * netty�ͻ���
 * @author Administrator
 *
 */
public class ClientFactory {
public static Channel getChannel(){
//  �ͻ�����
	ClientBootstrap bootstrap = new ClientBootstrap();
	
	//�̳߳�
	ExecutorService boss = Executors.newCachedThreadPool();
	ExecutorService worker = Executors.newCachedThreadPool();
	
	//����nioSocket����
	bootstrap.setFactory(new NioClientSocketChannelFactory(boss, worker));

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
			pipeline.addLast("HelloHandler",new CHandler());
			
			return pipeline;
		}
	});
	
	//�󶨶˿�
	ChannelFuture connect = bootstrap.connect(new InetSocketAddress(Constant.NETTY_HOST,Constant.NETTY_PORT));
	
	//�˴�Ҳ���Դ�handler��ctx�л�ȡ
	Channel channel = connect.getChannel();
	
	return channel;
}
	
public static void main(String[] args) {
 
	Channel channel = ClientFactory.getChannel();
	
	Scanner sc = new Scanner(System.in); 
	
	
	System.out.println("Client has be ready to connect to Server!");

	while(true){
		
		System.out.println("��������Ҫ���͵���Ϣ:");
		
		String msg = sc.next();

		channel.write(msg);
		
	}
	
}
}
