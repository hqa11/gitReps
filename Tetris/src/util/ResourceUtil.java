package util;

import java.io.File;

public class ResourceUtil {

	/**
	 * ��ȡ��Ŀ�ĸ�·��
	 * @return
	 */
	public static String getProPath(){
		String binPath = ResourceUtil.class.getClassLoader().getResource("").getPath();
		String parentPath = new File(binPath).getParent();
		return parentPath;
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		System.out.println(ResourceUtil.getProPath() + "/config/teris.txt");
		
	}
}
