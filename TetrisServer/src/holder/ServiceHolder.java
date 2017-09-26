package holder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;

import dto.RequestMessage;

import service.RemoteService;
import annotions.SocketService;

/**
 * ҵ��ִ����
 * @author Administrator
 *
 */
public final class ServiceHolder {
	
	
	public static volatile Map<Integer,Map<Integer,Method>> serviceHolder = new ConcurrentHashMap<>();
    private static final RemoteService rs = new RemoteService();
    
    
	public static void put(Integer module ,Integer cmd,Method method){
		Map<Integer,Method> cmdServices = serviceHolder.get(module);
		if(cmdServices == null)cmdServices = new HashMap<Integer, Method>();
		cmdServices.put(cmd,method);
		serviceHolder.put(module, cmdServices);
	}
	
	public static Method get(Integer module,Integer cmd){
		Map<Integer,Method> cmdServices = serviceHolder.get(module);
		if(cmdServices == null)return null;
		return cmdServices.get(cmd);
	}

	/**
	 * ����service����
	 * @throws Exception 
	 */
	public static void loadServiceMethods() throws Exception{
		Class<?> clazz = Class.forName(RemoteService.class.getName());
		//��ȡ�������з���
		Method[] methods = clazz.getDeclaredMethods();
		if(methods != null){
			for (Method method : methods) {
				SocketService socketService = method.getAnnotation(SocketService.class);
				if(socketService != null){
					if(ServiceHolder.serviceHolder.containsKey(socketService.module())){
							if(ServiceHolder.serviceHolder.get(socketService.module()).containsKey(socketService.cmd())){
								throw new Exception("--ERROR--ҵ�񷽷�ģ����ظ���װ�س�ʼ��ʧ�ܣ�");
							}
					}
					ServiceHolder.put(socketService.module(), socketService.cmd(), method);
				}
			}
		}
	}
     
	/**
	 * ִ��Ŀ�귽��
	 * @param msg
	 * @param channel 
	 * @throws Exception 
	 */
	public static void execute(Object... args) throws Exception {
		RequestMessage msg = (RequestMessage)args[0];
		if(msg == null)throw new Exception("��Ч������");
		Method method = ServiceHolder.get(msg.getModule(),msg.getCmd());
		if(method == null)throw new Exception("�Ҳ���Ŀ��ҵ�񷽷���");
		method.invoke(rs,args);
	}
}
