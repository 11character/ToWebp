package Frame;
import handler.ButtonHandler;
import handler.WindowHandler;

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

public class WindowFrame extends JFrame{
	
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
	private int y02 = (frameHeight/7)*4;
	private int y03 = (frameHeight/7)*5;
	
	private DefaultListModel df;
	private JList pathList;
	private JScrollPane jListScroll;
	
	private JButton msgButton;
	private JButton addButton;
	private JButton delButton;
	private JButton delAllButton;
	private JButton runButton;
	private JLabel savePathLabel;
	private JTextField savePathField;
	
	public WindowFrame(){
		super ("ToWebp : "+System.getProperty("os.name"));	//메인 프레임
				
		ButtonHandler buttonH = new ButtonHandler();
		
		df = new DefaultListModel();
		pathList = new JList(df);
		jListScroll = new JScrollPane(pathList);
		
		msgButton = new JButton("메세지창");
		addButton = new JButton("추가");
		delButton = new JButton("삭제");
		delAllButton = new JButton("전체삭제");
		runButton = new JButton("실행");
		savePathLabel = new JLabel("저장위치", SwingConstants.CENTER);
		savePathField = new JTextField();
		
		msgButton.setActionCommand("msg");;
		addButton.setActionCommand("oPath");
		delButton.setActionCommand("del");
		delAllButton.setActionCommand("delAll");
		runButton.setActionCommand("run");
		
		jListScroll.setSize(frameWidth-(x01*2),(frameHeight/8)*4);
		msgButton.setSize(buttonWidth, buttonHeight);
		addButton.setSize(buttonWidth,buttonHeight);
		delButton.setSize(buttonWidth,buttonHeight);
		delAllButton.setSize(buttonWidth,buttonHeight);
		runButton.setSize(buttonWidth,buttonHeight);
		savePathLabel.setSize(buttonWidth,buttonHeight);
		savePathField.setSize((frameWidth/10)*8,buttonHeight);
		
		jListScroll.setLocation(x01, y01);
		msgButton.setLocation(x04, y02);
		addButton.setLocation(x01, y02);
		delButton.setLocation(x02, y02);
		delAllButton.setLocation(x03, y02);
		runButton.setLocation(x05, y02);
		savePathLabel.setLocation(x01, y03);
		savePathField.setLocation(x02,y03);
		
		savePathField.setEditable(false);
		
		msgButton.addActionListener(buttonH);
		addButton.addActionListener(buttonH);
		delButton.addActionListener(buttonH);
		delAllButton.addActionListener(buttonH);
		runButton.addActionListener(buttonH);
		
		//모니터 해상도를 구한다.
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		
		//메인 프레임 사이즈, 위치
		setLayout(null);//레이아웃 메니저 설정을 해제한다.
		setSize(frameWidth,frameHeight);
		setResizable(false);
		setLocation((screenSize.width/2)-(frameWidth/2),(screenSize.height/2)-(frameHeight/2));
		
		addWindowListener(new WindowHandler());
		
		add(jListScroll);
		add(msgButton);
		add(addButton);
		add(delButton);
		add(delAllButton);
		add(runButton);
		add(savePathLabel);
		add(savePathField);
		
		setVisible(true);
		
		addButton.requestFocus();// 추가버튼에 포커스
		
		System.out.println("JFrame(main) : on");
	}
	
	/**
	 * 대상경로가 저장되는 리스트창 객체를 반환한다.
	 */
	public JList getPathList(){
		return pathList;
	}
	
	/**
	 * 저장폴더 경로 객체를 반환한다.
	 */
	public JTextField getSavePathField(){
		return savePathField;
	}
	
}
