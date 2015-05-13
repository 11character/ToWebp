package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 * 이미지를 webp 포멧으로 변환
 * @FileName  : RunCwebp.java
 * @Project     : ToWebp
 * @Date         : 2015. 5. 11.
 * @author      : 이은표
 */
public class RunCwebp implements Runnable{
	private JFrame mainFrame;						//메인창
	private RunMessageThread message;				
	private Thread msgThread;						//메세지처리 쓰레드
	
	private JList<String> pathList;							//JList 객체
	private DefaultListModel<String> pathListModel; 		//JList Model
	private JTextField savePathFileField;				//저장경로가 들어가는 JTextField객체
	
	private String rootPath;							//실행위치
	private File utilityPathFile;						//변환도구위치
	private File savePathFile;							//저장경로
	
	public RunCwebp(){		
		mainFrame = RunMacThread.mainFrame;
		pathList = RunMacThread.mainFrame.getPathList();
		
		pathListModel = (DefaultListModel<String>) pathList.getModel();
		
		rootPath = RunMacThread.rootPath;
		utilityPathFile = new File(rootPath
									+File.separator+"PlugIns"
									+File.separator+"webp_utility"
									+File.separator+"cwebp");
				
		savePathFileField = RunMacThread.mainFrame.getSavePathField();
		savePathFile = new File(savePathFileField.getText());
		
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
		
		//실행 전 작업 디렉토리 정리
		if(!savePathFile.exists()){
			savePathFile.mkdir();
		}else{
			if(deleteDirectory(savePathFile)){
				savePathFile.mkdir();				
			}else{
				System.out.println("RunCwebp : 실행 준비도중 오류발생.\nwebp 디렉토리를 비워야 합니다.");
				JOptionPane.showMessageDialog(mainFrame,"실행 준비도중 오류발생.\nwebp 디렉토리를 비워야 합니다.","",JOptionPane.ERROR_MESSAGE);
				message.threadStop();
				return;
			}
		}
		
		//실행 전 필수요소 체크
		String sourceChack = sourceChack();
		if(sourceChack.length() > 0){
			System.out.println("RunCwebp : "+sourceChack);
			JOptionPane.showMessageDialog(mainFrame,sourceChack,"",JOptionPane.ERROR_MESSAGE);
			message.threadStop();
			return;
		}
		
		//메세지창 보이기
		RunMacThread.messageFrame.setVisible(true);
		//메인화면 잠금
		mainFrame.setEnabled(false);
		
		try {
			for(int i = 0; i < pathListModel.getSize(); i++){
				File tempFile = new File((String) pathListModel.get(i));
				
				String fileType = tempFile.getName().substring(tempFile.getName().lastIndexOf(".")+1);
				String fileName = tempFile.getName().substring(0,tempFile.getName().lastIndexOf("."));
				String freamNumber = getAddZeroString(pathListModel.getSize(), i);
								
				String openPath = tempFile.getParent()+File.separator+fileName+"."+fileType;
				String savePath = savePathFile.getCanonicalPath()+File.separator+"fream"+freamNumber+".webp";
				String utilityPath = utilityPathFile.getCanonicalPath();
				
				//경로에 있는 디렉토리명에 공백이 있는 경우. 이를 무시하기위해 공백 앞에 \를 넣어준다.
				openPath = openPath.trim().replace(" ", "\\ ");
				savePath = savePath.trim().replace(" ", "\\ ");			
				utilityPath = utilityPath.trim().replace(" ", "\\ ");
				
				System.out.println("openPath : "+openPath);
				System.out.println("savePath : "+savePath);
				System.out.println("utilityPath : "+utilityPath);
				
				try {
					
					String cmd = utilityPath+" "+openPath+" -o "+savePath;
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
				message.setMessage("\n***************** <프레임 파일 생성완료> *****************\n");
				Thread.sleep(10);
				RunWebpmux rc = new RunWebpmux();
				Thread rmThread = new Thread(rc);
				rmThread.start();
				rmThread.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mainFrame.setEnabled(true);
		System.out.println("run : end");
		message.threadStop();
	}
	
	/**
	 * 작업시 필요한 소스를 체크한다.
	 * @Method Name  : sourceChack
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @return
	 */
	public String sourceChack(){
		String errorList = "";
		String result = "";
		//대상목록 존재여부 체크
		for(int i = 0; i < pathListModel.getSize(); i++){
			File tempFile = new File((String) pathListModel.get(i));
			if(!tempFile.exists()){
				errorList += "파일이름 : "+tempFile.getName()+"\n"+"해당경로 : "+tempFile.getParent()+"\n\n";
			}
		}
		
		if(pathListModel.getSize() == 0 || errorList.trim().length() != 0){
			result = "대상 파일이 존재하지 않습니다.\n"+errorList;
		}
		
		return result;
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
	
	/**
	 * 디렉토리를 삭제하는 재귀함수
	 * @Method Name  : deleteDirectory
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @param dir
	 * @return
	 */
	private boolean deleteDirectory(File dir){
		if(!dir.exists()){
			return false;
		}
		
		File[] files = dir.listFiles();
		for(File file : files){
			if(file.isDirectory()){
				deleteDirectory(file);
			}else{
				file.delete();
			}
		}
		
		return dir.delete();
	}
}
