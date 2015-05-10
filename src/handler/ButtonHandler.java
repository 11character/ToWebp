package handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import service.PathListControll;
import service.RunCwebp;
import service.RunMacThread;
import service.RunMessageThread;

public class ButtonHandler implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		PathListControll pathListControll = new PathListControll();
		
		String actionCmd = e.getActionCommand();
		System.out.println("actionCmd : "+actionCmd);
		
		if(actionCmd.equals("msg")){//메세지창 보이기
			RunMacThread.messageFrame.setVisible(true);
		}else if(actionCmd.equals("oPath")){			//추가버튼
			pathListControll.addOpenPath();				
		}else if(actionCmd.equals("del")){		//삭제버튼
			pathListControll.delPath();
		}else if(actionCmd.equals("delAll")){	//전체삭제
			pathListControll.delAllPath();
		}else if(actionCmd.equals("run")){		//변환시작
			RunCwebp rc = new RunCwebp();
			Thread rcThread = new Thread(rc);
			rcThread.start();
		}
	}

}
