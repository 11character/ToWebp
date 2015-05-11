package main;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;

import service.RunMacThread;

public class MainStart {
	
	//기본값 설정 및 Thread 실행
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//***해당 실행파일의 인코딩 설정을 변경해주는 코드.
		System.setProperty("file.encoding","UTF-8"); 
		Field charset = Charset.class.getDeclaredField("defaultCharset"); 
		charset.setAccessible(true); 
		charset.set(null,null);
		
		//인코딩설정 확인
		String encoding = new java.io.OutputStreamWriter(System.out).getEncoding();
	    System.out.println("Encoding : " + encoding);
		
	    String osType = System.getProperty("os.name");
	    System.out.println("OS : "+osType);
	    
		if(osType.indexOf("Mac") != -1){
			RunMacThread rmt = new RunMacThread();
			Thread macThread = new Thread(rmt,"macThread");
			macThread.start();
		}else{
			JOptionPane.showMessageDialog(null,"Mac에서 사용이 가능합니다.");
		}		
	}
}
