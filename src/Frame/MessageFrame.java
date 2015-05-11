package Frame;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import service.RunMacThread;

public class MessageFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private int frameWidth = RunMacThread.mainFrame.getWidth();
	private int frameHeight = 300;
	private int mainX = RunMacThread.mainFrame.getLocation().x;
	private int mainY = RunMacThread.mainFrame.getLocation().y;
	
	private JScrollPane messageScroll;
	private JTextArea messageArea;
	
	public MessageFrame(){
		super("message");
		
		messageArea = new JTextArea();
		messageArea.setEditable(false);
		messageScroll = new JScrollPane(messageArea);
		
		add(messageScroll);
				
		setSize(frameWidth, frameHeight);
		setLocation(mainX, mainY-(frameHeight/2));
		
		setVisible(false);
		System.out.println("JFrame(message) : on");
	}
	
	/**
	 * 처리 결과 메세지창 객체를 반환한다.
	 */
	public JTextArea getMessageArea(){
		return messageArea;
	}
}
