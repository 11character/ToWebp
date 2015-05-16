package service;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

import main.MainStart;
import Frame.MessageFrame;
import Frame.WindowFrame;


public class RunMacThread implements Runnable {
	public static WindowFrame mainFrame;		//메인 창
	public static MessageFrame messageFrame;	//메세지 창
	public static RunMessageThread message;		//메세지 쓰레드
	public static String rootPath;				//현재위치

	public void run(){
		System.out.println("RunMacThread : start");
		try {
			rootPath = getRootPath(); //기준이 되는 루트 경로를 구한다.
			//생성도구 위치와 존재여부 체크
			File webpUtility = new File(rootPath
										+File.separator+"Plugins"
										+File.separator+"webp_utility");
			if(webpUtility.exists()){
				System.out.println("webp_utility path : "+webpUtility.getCanonicalPath());
				mainFrame = new WindowFrame();
				
				//진행상황을 표시할 메세지 창
				messageFrame = new MessageFrame();//메세지창
			}else{
				System.out.println("error : webp_utility 디렉토리가 존재하지 않습니다.");
				JOptionPane.showMessageDialog(mainFrame,"/Plugins/webp_utility 디렉토리가 존재하지 않습니다.\n"
														+rootPath
														+"\n위치에 /Plugins/webp_utility 디렉토리가 있어야 하며, 생성유틸이 들어있어야 합니다.");
				System.exit(0);
			}
		} catch (Exception e) {}
	}
	
	private String getRootPath(){
		File classPathFile = new File(MainStart.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		String rootPath = "";
		try {
			rootPath = classPathFile.getCanonicalPath();
			int lastIndex = rootPath.lastIndexOf("/");
			rootPath = rootPath.substring(0,rootPath.lastIndexOf("/", lastIndex-1));
			rootPath = URLDecoder.decode(rootPath,"UTF-8");//url 한글경로가 깨지는 문제 처리
		} catch (IOException e) {
			e.printStackTrace();
			rootPath = System.getProperty("user.dir");
			JOptionPane.showMessageDialog(mainFrame,"프로그램 경로를 가져오는 도중에 문제가 생겼습니다.\n"
													+"패키지 내부의 Contents/Plugins/webp_utility 디렉토리리를\n"
													+rootPath+"로 복사해 주세요.","",JOptionPane.ERROR_MESSAGE);
		}
		return rootPath;
	}
}
