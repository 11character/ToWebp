package Frame;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import listener.ButtonListener;
import listener.WListener;
/**
 * 윈도우 디자인 설정
 * @FileName  : WindowFrame.java
 * @Project     : ToWebp
 * @Date         : 2015. 5. 11.
 * @author      : 이은표
 */
public class WindowFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 600;
	private int frameHeight = 350;
	private int buttonWidth = frameWidth/6;
	private int buttonHeight = frameHeight/12;
	
	private int x01 = frameWidth/60;
	private int x02 = x01+buttonWidth;
	private int x03 = x02+buttonWidth;
	private int x04 = frameWidth - ((x01+buttonWidth)*2);
	private int x05 = frameWidth - (x01+buttonWidth);
	
	private int y01 = frameHeight/35;
	private int y02 = (frameHeight/9)*5;
	private int y03 = (frameHeight/9)*6;
	private int y04 = (frameHeight/9)*7;
	
	private DefaultListModel<String> df;
	private JList<String> pathList;
	private JScrollPane jListScroll;
	
	private JButton upButton;
	private JButton downButton;
	private JLabel frameSpeedLabel;
	private JTextField frameSpeed;
	private JButton msgButton;
	private JButton addButton;
	private JButton delButton;
	private JButton delAllButton;
	private JButton runButton;
	private JLabel savePathLabel;
	private JTextField savePathField;
	
	public WindowFrame(){
		super ("ToWebp : "+System.getProperty("os.name"));	//메인 프레임
				
		ButtonListener buttonL = new ButtonListener();
		
		df = new DefaultListModel<String>();
		pathList = new JList<String>(df);
		jListScroll = new JScrollPane(pathList);
		
		upButton = new JButton("UP");
		downButton = new JButton("DOWN");
		frameSpeedLabel = new JLabel("속도", SwingConstants.CENTER);
		frameSpeed = new JTextField();
		msgButton = new JButton("메세지창");
		addButton = new JButton("추가");
		delButton = new JButton("삭제");
		delAllButton = new JButton("전체삭제");
		runButton = new JButton("실행");
		savePathLabel = new JLabel("저장위치", SwingConstants.CENTER);
		savePathField = new JTextField();
		
		upButton.addActionListener(buttonL);
		downButton.addActionListener(buttonL);
		msgButton.addActionListener(buttonL);
		addButton.addActionListener(buttonL);
		delButton.addActionListener(buttonL);
		delAllButton.addActionListener(buttonL);
		runButton.addActionListener(buttonL);
		
		upButton.setActionCommand("itemUp");
		downButton.setActionCommand("itemDown");
		msgButton.setActionCommand("msg");;
		addButton.setActionCommand("oPath");
		delButton.setActionCommand("del");
		delAllButton.setActionCommand("delAll");
		runButton.setActionCommand("run");
		
		jListScroll.setSize(frameWidth-(x01*2),(frameHeight/8)*4);
		upButton.setSize(buttonWidth, buttonHeight);
		downButton.setSize(buttonWidth, buttonHeight);
		frameSpeedLabel.setSize(buttonWidth/2,buttonHeight);
		frameSpeed.setSize(buttonWidth,buttonHeight);
		msgButton.setSize(buttonWidth, buttonHeight);
		addButton.setSize(buttonWidth,buttonHeight);
		delButton.setSize(buttonWidth,buttonHeight);
		delAllButton.setSize(buttonWidth,buttonHeight);
		runButton.setSize(buttonWidth,buttonHeight);
		savePathLabel.setSize(buttonWidth,buttonHeight);
		savePathField.setSize((frameWidth/10)*8,buttonHeight);
		
		jListScroll.setLocation(x01, y01);
		upButton.setLocation(x01, y02);
		downButton.setLocation(x02, y02);
		frameSpeedLabel.setLocation(x04-buttonWidth/2, y02);
		frameSpeed.setLocation(x04, y02);
		msgButton.setLocation(x04, y03);
		addButton.setLocation(x01, y03);
		delButton.setLocation(x02, y03);
		delAllButton.setLocation(x03, y03);
		runButton.setLocation(x05, y03);
		savePathLabel.setLocation(x01, y04);
		savePathField.setLocation(x02,y04);
		
		savePathField.setEditable(false);
		
		//모니터 해상도를 구한다.
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		
		//메인 프레임 사이즈, 위치
		setLayout(null);//레이아웃 메니저 설정을 해제한다.
		setSize(frameWidth,frameHeight);
		setResizable(false);
		setLocation((screenSize.width/2)-(frameWidth/2),(screenSize.height/2)-(frameHeight/2));
		
		addWindowListener(new WListener());
		
		add(jListScroll);
		add(upButton);
		add(downButton);
		add(frameSpeed);
		add(frameSpeedLabel);
		add(msgButton);
		add(addButton);
		add(delButton);
		add(delAllButton);
		add(runButton);
		add(savePathLabel);
		add(savePathField);
		
		setVisible(true);
		
		addButton.requestFocus();// 추가버튼에 포커스
		frameSpeed.setText("120");//기본 속도 120
		
		System.out.println("JFrame(main) : on");
	}
	
	/**
	 * 대상 파일경로가 표시되는 객체를 반환한다.
	 * @Method Name  : getPathList
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @return
	 */
	public JList<String> getPathList(){
		return pathList;
	}
	
	/**
	 * 저장폴더 경로 객체를 반환한다.
	 * @Method Name  : getSavePathField
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @return
	 */
	public JTextField getSavePathField(){
		return savePathField;
	}
	
	/**
	 * 프레임 속도입력 객체를 반환한다.
	 * @Method Name  : getFrameSpeed
	 * @date   : 2015. 5. 11.
	 * @author   : 이은표
	 * @return
	 */
	public JTextField getFrameSpeedField(){
		return frameSpeed;
	}
	
}
