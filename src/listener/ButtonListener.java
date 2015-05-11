package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import service.PathListControll;
import service.RunCwebp;
import service.RunMacThread;

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
			int status = JOptionPane.showConfirmDialog(RunMacThread.mainFrame,"webp 디렉토리에 파일일 있는경우\n기존 파일을 전부 지웁니다.","",JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if(status == JOptionPane.OK_OPTION){
				RunCwebp rc = new RunCwebp();
				Thread rcThread = new Thread(rc);
				rcThread.start();				
			}
		}
	}

}
