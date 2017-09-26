package util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class DataUtil {

	public static String getChineseName(){
		
		 Random random=new Random(System.currentTimeMillis());  
	        /* 598 �ټ��� */  
	        String[] Surname= {"��","Ǯ","��","��","��","��","֣","��","��","��","��","��","��","��","��","��","��","��","��","��",  
	                  "��","��","ʩ","��","��","��","��","��","��","κ","��","��","��","л","��","��","��","ˮ","�","��","��","��","��","��","��","��","��","��",  
	                  "³","Τ","��","��","��","��","��","��","��","��","Ԭ","��","ۺ","��","ʷ","��","��","��","�","Ѧ","��","��","��","��","��","��",  
	                  "��","��","��","��","��","��","��","��","ʱ","��","Ƥ","��","��","��","��","��","Ԫ","��","��","��","ƽ","��","��",  
	                  "��","��","��","Ҧ","��","տ","��","��","ë","��","��","��","��","��","�","��","��","��","��","̸","��","é","��","��","��","��",  
	                  "��","��","ף","��","��","��","��","��","��","ϯ","��","��","ǿ","��","·","¦","Σ","��","ͯ","��","��","÷","ʢ","��","��","��",  
	                  "��","��","��","��","��","��","��","��","��","��","��","��","��","֧","��","��","��","¬","Ī","��","��","��","��","��","��","Ӧ",  
	                  "��","��","��","��","��","��","��","��","��","��","��","��","ʯ","��","��","ť","��","��","��","��","��","��","½","��","��","��",  
	                  "��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","ɽ",  
	                  "��","��","��","�","��","ȫ","ۭ","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��",  
	                  "ղ","��","��","Ҷ","��","˾","��","۬","��","��","��","ӡ","��","��","��","��","ۢ","��","��","��","��","��","��","׿","��","��",  
	                  "��","��","��","��","��","��","��","��","˫","��","ݷ","��","��","̷","��","��","��","��","��","��","��","Ƚ","��","۪","Ӻ","ȴ",  
	                  "�","ɣ","��","�","ţ","��","ͨ","��","��","��","��","��","��","ũ","��","��","ׯ","��","��","��","��","��","Ľ","��","��","ϰ",  
	                  "��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","»",  
	                  "��","��","ŷ","�","��","��","ε","Խ","��","¡","ʦ","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��",  
	                  "��","��","ɳ","ؿ","��","��","��","��","��","��","��","��","��","��","��","��","��","ۣ","��","Ȩ","��","��","��","��","��","��",  
	                  "��","��","˧","��","��","��","�C","��","��","��","��","��","��","��","��","��","۳","Ϳ","��","��","Ĳ","��","٦","��","��","ī",  
	                  "��","��","��","��","��","��","١","��","��","��","��","��","��","��","��","��","��","չ","��","̴","��","��","��","��","˴","¥",  
	                  "��","ð","��","ֿ","��","��","��","��","ԭ","��","��","��","��","��","�","��","��","��","�","��","��","��","��","��","��","��",  
	                  "ľ","��","��","ۨ","��","ö","��","��","�","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��","��",  
	                  "��","��","��","�G","��ٹ","˾��","�Ϲ�","ŷ��","�ĺ�","���","����","����","����","�ʸ�","����","ξ��","����","�̨","��ұ","����",  
	                  "���","����","����","̫��","����","����","����","��ԯ","���","����","����","����","Ľ��","����","����","˾ͽ","˾��","أ��","˾��",  
	                  "����","����","�ӳ�","���","��ľ","����","����","���","����","����","����","�ذ�","�й�","�׸�","����","�θ�","����","����","΢��",  
	                  "����","����","����","����","�Ϲ�","����","����","����","̫ʷ","�ٳ�","����","��ͻ","����","����","����","��ĸ","˾��","����","Ӻ��",  
	                  "����","����","����","��®","����","�Ϲ�","����","����"};  
	          
	        int index=random.nextInt(Surname.length-1);       
	        String name = Surname[index]; //���һ�����������  
	          
	        /* �ӳ�������ѡȡһ������������Ϊ�� */  
	        if(random.nextBoolean()){  
	            name+=getChinese()+getChinese();  
	        }else {  
	            name+=getChinese();  
	        }  
	
	        return name;
	}
	
	
	 public static String getChinese() {  
	        String str = null;  
	        int highPos, lowPos;  
	        Random random = new Random();  
	        highPos = (176 + Math.abs(random.nextInt(71)));//���룬0xA0��ͷ���ӵ�16����ʼ����0xB0=11*16=176,16~55һ�����֣�56~87��������  
	        random=new Random();  
	        lowPos = 161 + Math.abs(random.nextInt(94));//λ�룬0xA0��ͷ����Χ��1~94��  
	  
	        byte[] bArr = new byte[2];  
	        bArr[0] = (new Integer(highPos)).byteValue();  
	        bArr[1] = (new Integer(lowPos)).byteValue();  
	        try {  
	            str = new String(bArr, "GB2312");   //��λ����ϳɺ���  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  
	            return str;  
	    }  
	
	 
	    /** 
	     * ���� 
	     *  
	     * @param content ��Ҫ���ܵ����� 
	     * @param password  �������� 
	     * @return 
	     */  
	    public static String encrypt(String content, String password) {  
	            try {             
	                    KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                    kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
	                    SecretKey secretKey = kgen.generateKey();  
	                    byte[] enCodeFormat = secretKey.getEncoded();  
	                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	                    Cipher cipher = Cipher.getInstance("AES");// ����������  
	                    byte[] byteContent = content.getBytes("utf-8");  
	                    cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��  
	                    byte[] result = cipher.doFinal(byteContent);  
	                    return parseByte2HexStr(result); // ����  
	            } catch (NoSuchAlgorithmException e) {  
	                    e.printStackTrace();  
	            } catch (NoSuchPaddingException e) {  
	                    e.printStackTrace();  
	            } catch (InvalidKeyException e) {  
	                    e.printStackTrace();  
	            } catch (UnsupportedEncodingException e) {  
	                    e.printStackTrace();  
	            } catch (IllegalBlockSizeException e) {  
	                    e.printStackTrace();  
	            } catch (BadPaddingException e) {  
	                    e.printStackTrace();  
	            }  
	            return null;  
	    }  
	    
	    /**���� 
	     * @param content  ���������� 
	     * @param password ������Կ 
	     * @return 
	     * @throws UnsupportedEncodingException 
	     */  
	    public static String decrypt(String content, String password) {
	    	content = content.trim();
	            try {  
	                     KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                     kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
	                     SecretKey secretKey = kgen.generateKey();  
	                     byte[] enCodeFormat = secretKey.getEncoded();  
	                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                     Cipher cipher = Cipher.getInstance("AES");// ����������  
	                    cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��  
	                    byte[] result = cipher.doFinal(parseHexStr2Byte(content));  
	                    return new String(result,"utf-8"); // ����  
	            } catch (NoSuchAlgorithmException e) {  
	                    e.printStackTrace();  
	            } catch (NoSuchPaddingException e) {  
	                    e.printStackTrace();  
	            } catch (InvalidKeyException e) {  
	                    e.printStackTrace();  
	            } catch (IllegalBlockSizeException e) {  
	                    e.printStackTrace();  
	            } catch (BadPaddingException e) {  
	                    e.printStackTrace();  
	            }  catch( UnsupportedEncodingException e){
	            	e.printStackTrace();
	            }
	            return null;  
	    } 
	    
	    
	    /**��16����ת��Ϊ������ 
	     * @param hexStr 
	     * @return 
	     */  
	    public static byte[] parseHexStr2Byte(String hexStr) {  
	            if (hexStr.length() < 1)  
	                    return null;  
	            byte[] result = new byte[hexStr.length()/2];  
	            for (int i = 0;i< hexStr.length()/2; i++) {  
	                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                    result[i] = (byte) (high * 16 + low);  
	            }  
	            return result;  
	    }  
	 
	    /**��������ת����16���� 
	     * @param buf 
	     * @return 
	     */  
	    public static String parseByte2HexStr(byte buf[]) {  
	            StringBuffer sb = new StringBuffer();  
	            for (int i = 0; i < buf.length; i++) {  
	                    String hex = Integer.toHexString(buf[i] & 0xFF);  
	                    if (hex.length() == 1) {  
	                            hex = '0' + hex;  
	                    }  
	                    sb.append(hex.toUpperCase());  
	            }  
	            return sb.toString();  
	    }  
	    
	    
	 public static void main(String[] args) {
		 String encrypt = DataUtil.encrypt("���찲","ss");
		 System.out.println(encrypt);
		 String decrypt = DataUtil.decrypt(encrypt, "ss");
		 System.out.println(decrypt);
	}
	 
	 
	 public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {    
	        try {
				if (map == null)   
				    return null;    
  
				Object obj = beanClass.newInstance();  
  
				BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
				for (PropertyDescriptor property : propertyDescriptors) {  
				    Method setter = property.getWriteMethod();    
				    if (setter != null) {  
				        setter.invoke(obj, map.get(property.getName()));   
				    }  
				}  
  
				return obj;
			} catch (Exception e) {
				e.printStackTrace();
			}  
	        return null;  
	    }  
	 
}
