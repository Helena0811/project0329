// 로그인 화면을 담당할 Panel
/*
 * 가장 바깥	BorderLayout(center, south)
 * center 내 GridLayout
 * 전체		FlowLayout
 * */
package game.word;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JPanel implements ActionListener{
	GameWindow gameWindow;
	
	JPanel container;	// BorderLayout
	JPanel p_center;	// GridLayout
	JPanel p_south;		// button
	
	JLabel la_id, la_pw;
	JTextField t_id;
	JPasswordField t_pw;
	JButton bt;
	
	GamePanel gamePanel;
	
	public LoginForm(GameWindow gameWindow) {
		this.gameWindow=gameWindow;
		
		container=new JPanel();
		p_center=new JPanel();
		p_south=new JPanel();
		
		la_id=new JLabel("ID");
		la_pw=new JLabel("Password");
		
		t_id=new JTextField("batman",15);
		t_pw=new JPasswordField("1234",15);
		
		bt=new JButton("LogIn");
		
		container.setLayout(new BorderLayout());
		p_center.setLayout(new GridLayout(2, 2));
		
		p_center.add(la_id);
		p_center.add(t_id);
		p_center.add(la_pw);
		p_center.add(t_pw);
		
		p_south.add(bt);
		
		container.add(p_center);
		container.add(p_south, BorderLayout.SOUTH);
		
		add(container);
		
		// 버튼과 리스너 연결
		bt.addActionListener(this);
		setBackground(Color.green);
		setPreferredSize(new Dimension(400, 100));

	}
	
	public void loginCheck(){
		String id=t_id.getText();
		// JPassword.getPassword() : char[] 배열 반환
		// String(char[] value)
		String pw=new String(t_pw.getPassword());
		/*
		 * 보안상의 문제를 해결하기 위해서 아이디만 혹인 비밀번호만 틀린 경우는 표시해주지 않음
		 * */
		// id와 password를 알맞게 넣었다면 로그인 성공
		if(id.equals("batman")&&pw.equals("1234")){
			JOptionPane.showMessageDialog(this, "로그인 성공");
			//gamePanel=new GamePanel();
			// 메인 화면 출력
			gameWindow.setPage(1);
		}
		else{
			JOptionPane.showMessageDialog(this, "로그인 정보가 올바르지 않습니다.");
		}
	}
		
	public void actionPerformed(ActionEvent e) {
		loginCheck();
	}
}
