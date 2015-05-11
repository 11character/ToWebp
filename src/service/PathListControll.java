package service;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * 리스트, 리스트항목 관련 기능
 * @FileName  : PathListControll.java
 * @Project     : ToWebp
 * @Date         : 2015. 5. 11.
 * @author      : 이은표
 */
public class PathListControll {
	private Frame mainFrame = RunMacThread.mainFrame;														
	private JList pathList = RunMacThread.mainFrame.getPathList();									//JList 객체를 받는다.
	private DefaultListModel pathListModel = (DefaultListModel) pathList.getModel();	//listModel을 받는다.
	private JTextField savePathField = RunMacThread.mainFrame.getSavePathField(); 						//savePathField를 받는다.
	private static String lastOpenDirectoryPath = System.getProperty("user.home");								//파일추가 기본경로
	
	/**
	 * 대상 경로를 추가하고 창에 표시되는 목록을 새로고침 한다.
	 * @Method Name  : addOpenPath
	 * @date   : 2015. 5. 10.
	 * @author   : 이은표
	 */
	public void addOpenPath() {
		try{
			JFileChooser jc = new JFileChooser(lastOpenDirectoryPath);
			File dir = new File(lastOpenDirectoryPath);
			
			//기본경로 설정
			if(dir.exists() && dir.isDirectory()){
				jc.setCurrentDirectory(dir);
			}else{
				lastOpenDirectoryPath = System.getProperty("user.home");
				jc.setCurrentDirectory(dir);
			}
			
			//MultiSelect 설정
			jc.setMultiSelectionEnabled(true);
			
			//필터설정
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg & *.png","jpg","png");
			jc.setAcceptAllFileFilterUsed(false);//전체선택 제거
			jc.setFileFilter(filter);
			
			//파일선택, 중복파일 제거, 위치저장.
			int status = jc.showOpenDialog(mainFrame);
			if (status == JFileChooser.APPROVE_OPTION){
				File[] files = jc.getSelectedFiles();
				for(File tempFile : files){
					int chack = 0;
					//기존에 같은 항목이 있는지 체크
					for(int i=0; i<pathListModel.getSize(); i++){
						String oldPath = (String) pathListModel.get(i);
						if(oldPath.equals(tempFile.getCanonicalPath())){
							chack += 1;
							System.out.println("중복 : "+tempFile.getCanonicalPath());
						}
					}
					if(chack==0){
						//저장 경로는 첫번째 파일의 경로 아래 webp 폴더에 생성
						if(savePathField.getText().trim().length() == 0){
							savePathField.setText(tempFile.getParent()+File.separator+"webp");
						}
						System.out.println("open path : "+tempFile.getCanonicalPath());
						pathListModel.addElement(tempFile.getCanonicalPath());
					}
				}
			}
			System.out.println("open path : end");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 선택된 항목을 삭제한다.
	 * @Method Name  : delPath
	 * @date   : 2015. 5. 10.
	 * @author   : 이은표
	 */
	public void delPath() {
		int sIndex = pathList.getSelectedIndex();
		if(sIndex >= 0){
			System.out.println("del : "+pathListModel.get(sIndex));
			pathListModel.remove(sIndex);
		}else{
			System.out.println("del : no select");
			JOptionPane.showMessageDialog(mainFrame,"선택된 항목이 없습니다.");
		}
		
		if(pathListModel.getSize() == 0){
			savePathField.setText("");
		}
		
		System.out.println("del : end");
	}
	
	/**
	 * 모든 항목을 삭제한다.
	 * @Method Name  : delAllPath
	 * @date   : 2015. 5. 10.
	 * @author   : 이은표
	 */
	public void delAllPath() {
		pathListModel.removeAllElements();
		savePathField.setText("");
		
		System.out.println("delAll : end");
	}
	
	/**
	 * 선택 항목을 위로 올린다.
	 * @Method Name  : itemUp
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 */
	public void itemUp(){
		if(pathList.getSelectedIndex() > 0){
			int selectedIndex = pathList.getSelectedIndex();
			String tempValue = (String) pathListModel.get(selectedIndex-1);
			pathListModel.set(selectedIndex-1,pathListModel.get(selectedIndex));
			pathListModel.set(selectedIndex, tempValue);
			pathList.setSelectedIndex(selectedIndex-1);
		}
		
		System.out.println("itemUp : end");
	}
	
	/**
	 * 선택 항목을 아래로 내린다.
	 * @Method Name  : itemDown
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 */
	public void itemDown(){
		if(pathList.getSelectedIndex() < pathListModel.size()){
			int selectedIndex = pathList.getSelectedIndex();
			String tempValue = (String) pathListModel.get(selectedIndex+1);
			pathListModel.set(selectedIndex+1, pathListModel.get(selectedIndex));
			pathListModel.set(selectedIndex, tempValue);
			pathList.setSelectedIndex(selectedIndex+1);
		}
		
		System.out.println("itemDown : end");
	}
}
