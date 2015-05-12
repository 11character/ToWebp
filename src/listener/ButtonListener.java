package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import service.PathListControll;
import service.RunCwebp;
import service.RunMacThread;
import Frame.WindowFrame;

public class ButtonListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		PathListControll pathListControll = new PathListControll();
		
		String actionCmd = e.getActionCommand();
		System.out.println("actionCmd : "+actionCmd);
		
		if(actionCmd.equals("itemUp")){	//선택항목 위로
			pathListControll.itemUp();
		}else if(actionCmd.equals("itemDown")){	//선택항목 아래로
			pathListControll.itemDown();
		}else if(actionCmd.equals("msg")){	//메세지창 보이기
			RunMacThread.messageFrame.setVisible(true);
		}else if(actionCmd.equals("oPath")){			//추가버튼
			pathListControll.addOpenPath();				
		}else if(actionCmd.equals("del")){		//삭제버튼
			pathListControll.delPath();
		}else if(actionCmd.equals("delAll")){	//전체삭제
			pathListControll.delAllPath();
		}else if(actionCmd.equals("run")){		//변환시작
			
			WindowFrame mainFrame = RunMacThread.mainFrame;
			JTextField frameSpeedField = mainFrame.getFrameSpeedField();
			if(!isNumber(frameSpeedField.getText())){
				System.out.println("ButtonListener : 정수가 아닌 수가 입력되었다.");
				JOptionPane.showMessageDialog(mainFrame,"프레임속도는 정수를 입력해야 합니다.","",JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			int status = JOptionPane.showConfirmDialog(RunMacThread.mainFrame,"webp 디렉토리에 파일일 있는경우\n기존 파일을 전부 지웁니다.","",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(status == JOptionPane.OK_OPTION){
				RunCwebp rc = new RunCwebp();
				Thread rcThread = new Thread(rc);
				rcThread.start();				
			}
		}
	}
	
	/**
	 * 해당 문자열이 정수인지 체크
	 * @Method Name  : isNumber
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @param nStr
	 * @return
	 */
	private boolean isNumber(String nStr){
		boolean result = false;
		Pattern p = Pattern.compile("^[0-9]*$");
		Matcher m = p.matcher(nStr.trim());
		if(m.matches()){
			result = true;				
		}
		return result;
	}

}
