// Panel Customizing, Frame은 오직 하나만 둠
package page;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainApp extends JFrame implements ActionListener{
	JButton[] menu=new JButton[3];
	JPanel p_north;
	URL[] url=new URL[3];
	String[] path={"/login.png", "/content.png", "/etc.png"};
	ImageIcon[] icon=new ImageIcon[3];
	
	// 페이지들을 부착할 곳
	JPanel p_center;
	// 페이지들을 보유(Has a)	
	// -> 리스트화 시키기, 패널을 리스트에 넣으면 제어 가능할듯!
	/*
	LoginForm loginForm;
	Content content;
	Etc etc;
	*/
	// JPanel의 배열로 form 저장
	JPanel[] page=new JPanel[3];
	
	public MainApp() {
		p_north=new JPanel();
		for(int i=0; i<path.length; i++){
			url[i]=this.getClass().getResource(path[i]);
			icon[i]=new ImageIcon(url[i]);
			menu[i]=new JButton(icon[i]);
			p_north.add(menu[i]);
			
			// 버튼과 리스너 연결
			menu[i].addActionListener(this);
		}
		add(p_north, BorderLayout.NORTH);
		
		p_center=new JPanel();
		page[0]=new LoginForm();
		page[1]=new Content();
		page[2]=new Etc();
		
		// LoginForm 생성 및 부착
		p_center.add(page[0]);
		
		// Content 생성 및 부착
		p_center.add(page[1]);
		
		// Etc 생성 및 부착
		p_center.add(page[2]);
		
		add(p_center);
		
		setSize(700, 600);
		setVisible(true);
		setLocationRelativeTo(null);	// 화면 중앙
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// 각 버튼을 눌렀을 때 해당하는 이벤트 구현
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		/*
		// 유지보수성이 떨어짐
		if(obj==menu[0]){			// 로그인 폼 O, 컨텐츠&기타 X
			loginForm.setVisible(true);
			content.setVisible(false);
			etc.setVisible(false);
		} else if(obj==menu[1]){	// 내용 폼 O, 로그인&기타 X
			loginForm.setVisible(false);
			content.setVisible(true);
			etc.setVisible(false);
		} else if(obj==menu[2]){	// 기타 O, 로그인&컨텐츠 X
			loginForm.setVisible(false);
			content.setVisible(false);
			etc.setVisible(true);
		}
		*/
		
		// 자료형을 통일하여 for문 사용 가능, 반복되는 코드 재사용
		// panel이 많을 때 처리 가능하도록
		for(int i=0; i<page.length; i++){
			if(obj==menu[i]){
				page[i].setVisible(true);
			}
			else{
				page[i].setVisible(false);
			}
		}
	}
		
	public static void main(String[] args) {
		new MainApp();
	}
}
