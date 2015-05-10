package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Frame.WindowFrame;


public class RunCwebp implements Runnable{
	private JFrame mainFrame;						//메인창
	private RunMessageThread message;				
	private Thread msgThread;						//메세지처리 쓰레드
	
	private JList pathList;					//JList 객체
	private DefaultListModel pathListModel; //JList Model
	private JTextField savePathField;				//저장경로가 들어가는 JTextField객체
	
	private File thisPath;							//실행위치
	private File utilityPath;						//변환도구위치
	private File savePath;							//저장경로
	
	public RunCwebp(){		
		mainFrame = RunMacThread.mainFrame;
		pathList = RunMacThread.mainFrame.getPathList();
		
		pathListModel = (DefaultListModel) pathList.getModel();
		
		thisPath = RunMacThread.thisPath;
		utilityPath = new File(thisPath+File.separator+"webp_utility"+File.separator+"cwebp");
				
		savePathField = RunMacThread.mainFrame.getSavePathField();
		savePath = new File(savePathField.getText());
		
		System.out.println("thisPath : "+thisPath);
		System.out.println("utilityPath.exists() : "+utilityPath.exists());
		System.out.println("utilityPath : "+utilityPath);
		System.out.println("savePath.exists() : "+savePath.exists());
		System.out.println("savePath : "+savePath);
	}
	
	public int dirChack(){
		int chackNo = 0;
		String errorList = "";
		
		//대상목록 존재여부 체크
		for(int i = 0; i < pathListModel.getSize(); i++){
			File tempFile = new File((String) pathListModel.get(i));
			
			if(!tempFile.exists()){
				errorList += "파일이름 : "+tempFile.getName()+"\n"+"해당경로 : "+tempFile.getParent()+"\n\n";
			}
		}
		if(pathListModel.getSize() == 0 || errorList.trim().length() != 0){
			System.out.println("run : 대상 파일이 존재하지 않습니다. \n"+errorList);
			JOptionPane.showMessageDialog(mainFrame,"대상 파일이 존재하지 않습니다.\n"+errorList);
			chackNo += 1;
		}
		
		
		//저장 경로를 설정 했는지 체크
		if(!savePath.exists()){
			System.out.println("run : 저장 위치를 선택해 주세요.");
			JOptionPane.showMessageDialog(mainFrame,"저장 위치를 선택해 주세요.");
			chackNo += 1;
		}
		
		return chackNo;
	}
	
	public void run() {
		int errorChack = 0;
		String errorMsg = "";
		
		message = new RunMessageThread();
		msgThread = new Thread(message,"messageThread");
		msgThread.start();
		
		//실행 전 필수요소 체크
		if(dirChack() > 0){
			message.threadStop();
			return;
		}
		
		//메세지창 보이기
		RunMacThread.messageFrame.setVisible(true);
		
		try {
			for(int i = 0; i < pathListModel.getSize(); i++){
				File tempFile = new File((String) pathListModel.get(i));
				
				String fileType = tempFile.getName().substring(tempFile.getName().lastIndexOf(".")+1);
				String fileName = tempFile.getName().substring(0,tempFile.getName().lastIndexOf("."));
				String freamNumber = getZeroString(pathListModel.getSize())+i;
								
				String openfilePath = tempFile.getParent()+File.separator+fileName+"."+fileType;
				String saveFilePath = savePath.getCanonicalPath()+File.separator+"fream"+freamNumber+".webp";
				String utilityFilePath = utilityPath.getCanonicalPath();
				
				//경로에 있는 디렉토리명에 공백이 있는 경우. 이를 무시하기위해 공백 앞에 \를 넣어준다.
				openfilePath = openfilePath.trim().replace(" ", "\\ ");
				saveFilePath = saveFilePath.trim().replace(" ", "\\ ");			
				utilityFilePath = utilityFilePath.trim().replace(" ", "\\ ");
				
				System.out.println("utilityPath : "+openfilePath);
				System.out.println("saveFilePath : "+saveFilePath);
				System.out.println("utilityFilePath : "+utilityFilePath);
				
				try {
					
					String cmd = utilityFilePath+" "+openfilePath+" -o "+saveFilePath;
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
					
					
					if(msg.indexOf("Error!") != -1){
						errorChack++;
						errorMsg += "\n"+msg;
					};
					
					message.setMessage(msgCmd+msg);
					Thread.sleep(10);
					System.out.println(msg);
					System.out.println("cwebp : end");
	
				} catch (Exception e) {
					e.printStackTrace();
					errorChack++;
					errorMsg += "\n 변환도구 실행도중 오류가 발생했습니다.";
				}
	
			}
			
			//에러발생 확인
			if(errorChack > 0){
				message.setMessage("\n\n\n***************** "+errorChack+" 개의 파일에서 오류발생. *****************");
				Thread.sleep(10);
				message.setMessage(errorMsg);
				Thread.sleep(10);
				System.out.println("error : "+errorChack);
				System.out.println("errorMessage : "+errorMsg);
			}else{
				message.setMessage("\n***************** <처리완료> *****************\n");
				Thread.sleep(10);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("run : end");
		message.threadStop();
	}
	
	/**
	 * 입력한 사이즈의 자리수 만큼 0이 붙은 문자열을 반환한다.
	 * @Method Name  : getZeroString
	 * @date   : 2015. 5. 10.
	 * @author   : 이은표
	 * @param size
	 * @return
	 */
	private String getZeroString(int size){
		String zeroStr = "";
		String sizeStr = size+"";
		for(int i=1; i < sizeStr.length(); i++){
			zeroStr += "0";
		}
		return zeroStr;
	}
}
