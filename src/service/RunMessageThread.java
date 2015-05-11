package service;

import javax.swing.JTextArea;

public class RunMessageThread implements Runnable{
	private String message = null;
	private Boolean writeSwitch = false;
	private Boolean run = true;
	
	JTextArea ja = RunMacThread.messageFrame.getMessageArea();
	
	int no = 0;
	public void run(){
		while(run){
			if(writeSwitch){
				ja.append(message);
				ja.setCaretPosition(ja.getText().length());//마지막 위치로 입력 위치를 잡는다.(스크롤을 최신내용이 있는 아래쪽으로 이동시켜주는 효과.)
				writeSwitch = false;
			}else{
				Thread.yield();
			}
		}
		System.out.println("메세지 쓰레드 종료");
	}
	
	public void setMessage(String msg){
		message = msg;
		writeSwitch = true;
	}

	public void threadStop() {
		run = false;
	}
}
