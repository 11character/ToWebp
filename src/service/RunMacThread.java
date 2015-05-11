package service;

import java.io.File;

import javax.swing.JOptionPane;

import Frame.MessageFrame;
import Frame.WindowFrame;


public class RunMacThread implements Runnable {
	public static WindowFrame mainFrame;		//메인 창
	public static MessageFrame messageFrame;	//메세지 창
	public static RunMessageThread message;		//메세지 쓰레드
	public static String rootPath;				//현재위치

	public void run(){
		System.out.println("Thread : start");
		try {
			rootPath = System.getProperty("user.dir"); //현재위치를 절대경로로 팔일객체를 만든다.
			
			//생성도구 위치와 존재여부 체크
			File webpUtility = new File(rootPath+File.separator+"webp_utility");
			if(webpUtility.exists()){
				System.out.println("webp_utility path : "+webpUtility.getCanonicalPath());
				mainFrame = new WindowFrame();
				
				//진행상황을 표시할 메세지 창
				messageFrame = new MessageFrame();//메세지창
			}else{
				System.out.println("error : webp_utility 디렉토리가 존재하지 않습니다.");
				JOptionPane.showMessageDialog(mainFrame,"webp_utility 디렉토리가 존재하지 않습니다.\n"
														+rootPath
														+"\n위치에 webp_utility 이름의 디렉토리가 있어야 하며, 생성유틸이 있어야 합니다.");
				System.exit(0);
			}
		} catch (Exception e) {}
	}
}
