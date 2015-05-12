package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Frame.WindowFrame;
/**
 * 이미지를 webp 포멧으로 변환
 * @FileName  : RunCwebp.java
 * @Project     : ToWebp
 * @Date         : 2015. 5. 11.
 * @author      : 이은표
 */
public class RunWebpmux implements Runnable{
	private WindowFrame mainFrame;						//메인창
	private RunMessageThread message;				
	private Thread msgThread;						//메세지처리 쓰레드
	
	private JList pathList;							//JList 객체
	private DefaultListModel pathListModel; 		//JList Model
	private JTextField savePathField;				//저장경로가 들어가는 JTextField객체
	
	private String rootPath;						//실행위치
	private File utilityPathFile;					//변환도구위치
	private File savePathFile;						//프레임소스경로, 저장경로
	private JTextField frameSpeedField;					//프레임 속도
	
	public RunWebpmux(){		
		mainFrame = RunMacThread.mainFrame;
		pathList = mainFrame.getPathList();
		frameSpeedField = mainFrame.getFrameSpeedField();
		savePathField = mainFrame.getSavePathField();
		
		pathListModel = (DefaultListModel) pathList.getModel();
		
		rootPath = RunMacThread.rootPath;
		utilityPathFile = new File(rootPath
									+File.separator+"PlugIns"
									+File.separator+"webp_utility"
									+File.separator+"webpmux");
		
		savePathFile = new File(savePathField.getText());
		
		System.out.println("rootPath : "+rootPath);
		System.out.println("utilityPathFile.exists() : "+utilityPathFile.exists());
		System.out.println("utilityPathFile : "+utilityPathFile);
		System.out.println("savePathFile.exists() : "+savePathFile.exists());
		System.out.println("savePathFile : "+savePathFile);
	}
	
	public void run() {
		int errorChack = 0;
		String errorMsg = "";
		
		message = new RunMessageThread();
		msgThread = new Thread(message,"messageThread");
		msgThread.start();
		
		//실행 전 프레임 파일 목록 체크
		List<String> sourceList = sourceChack();
		if(sourceList == null){
			System.out.println("RunWebpmux : 프레임 소스의 개수에 문제가 있습니다.");
			JOptionPane.showMessageDialog(mainFrame,"프레임 소스의 개수에 문제가 있습니다.","",JOptionPane.ERROR_MESSAGE);
			message.threadStop();
			return;
		}
		
		//메세지창 보이기
		RunMacThread.messageFrame.setVisible(true);
		//메인화면 잠금
		mainFrame.setEnabled(false);
		
		try {
			String firstFilePath = (String) pathListModel.get(0);
			String firstFileName = firstFilePath.substring(firstFilePath.lastIndexOf("/")+1,firstFilePath.lastIndexOf("."));
			
			String savePath = savePathFile.getCanonicalPath()+File.separator+firstFileName+".webp";
			String utilityPath = utilityPathFile.getCanonicalPath();

			//경로에 있는 디렉토리명에 공백이 있는 경우. 이를 무시하기위해 공백 앞에 \를 넣어준다.
			savePath = savePath.trim().replace(" ", "\\ ");			
			utilityPath = utilityPath.trim().replace(" ", "\\ ");
			
			System.out.println("savePath : "+savePath);
			System.out.println("utilityPath : "+utilityPath);
			
			String sourceCmd = "";
			String frameSpeed = frameSpeedField.getText();
			for(String str : sourceList){
				str = str.trim().replace(" ", "\\ ");
				sourceCmd += " -frame "+str+" +"+frameSpeed+"+0+0";
			}
			
			String cmd = utilityPath+" "+sourceCmd+" -loop 0 -o "+savePath;
			String msgCmd = "\n===================<실행명령>===================\n"+cmd+"\n==============================================\n";
			System.out.println(msgCmd);
			
			String[] command = {"/bin/sh","-c",cmd};
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);				
			Process p = pb.start();
			System.out.println("cwebp : start");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line = "";
			String msg = "";
			
			while ((line = br.readLine()) != null) {
				msg += line+"\n";
			}
			
			br.close();
			p.destroy();
			
			
			if(msg.toLowerCase().indexOf("error") != -1){
				errorChack++;
				errorMsg += "\n"+msg;
			};
			
			message.setMessage(msgCmd+msg);
			Thread.sleep(10);
			System.out.println(msg);
			System.out.println("cwebp : end");
			
			//에러발생 확인
			if(errorChack > 0){
				message.setMessage("\n\n\n***************** 프레임을 합치는 중 오류발생. *****************");
				Thread.sleep(10);
				message.setMessage(errorMsg);
				Thread.sleep(10);
				System.out.println("error : "+errorChack);
				System.out.println("errorMessage : "+errorMsg);
			}else{
				message.setMessage("\n***************** <프레임 연결 완료> *****************\n");
				Thread.sleep(10);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mainFrame.setEnabled(true);
		
		System.out.println("run : end");
		message.threadStop();
	}
	
	/**
	 *  작업시 필요한 소스를 체크한다.
	 * @Method Name  : sourceChack
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @return
	 */
	public List<String> sourceChack(){
		List<String> sourcePathList = new ArrayList<String>();
		File[] files = savePathFile.listFiles();
		
		try {
			String savePath = savePathFile.getCanonicalPath();
			for(int i=0; i<files.length; i++){
				String freamNumber = getAddZeroString(files.length, i);
				String freamPath = savePath+File.separator+"fream"+freamNumber+".webp";
				File tempFile = new File(freamPath);
				if(tempFile.exists()){
					sourcePathList.add(savePath+File.separator+"fream"+freamNumber+".webp");				
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return (files.length == sourcePathList.size())? sourcePathList : null;
	}
	
	/**
	 * 입력한 사이즈의 자리수 만큼 0이 붙은 문자열을 반환한다.
	 * @Method Name  : getAddZeroString
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @param size
	 * @param no
	 * @return
	 */
	private String getAddZeroString(int size, int no){
		String sizeStr = size+"";
		String noStr = no+"";
		String zeroStr = "";
		
		for(int i=0; i < sizeStr.length() - noStr.length(); i++){
			zeroStr += "0";
		}
		
		return zeroStr+noStr;
	}
}
