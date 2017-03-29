// Panel Customizing, Frame�� ���� �ϳ��� ��
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
	
	// ���������� ������ ��
	JPanel p_center;
	// ���������� ����(Has a)	
	// -> ����Ʈȭ ��Ű��, �г��� ����Ʈ�� ������ ���� �����ҵ�!
	/*
	LoginForm loginForm;
	Content content;
	Etc etc;
	*/
	// JPanel�� �迭�� form ����
	JPanel[] page=new JPanel[3];
	
	public MainApp() {
		p_north=new JPanel();
		for(int i=0; i<path.length; i++){
			url[i]=this.getClass().getResource(path[i]);
			icon[i]=new ImageIcon(url[i]);
			menu[i]=new JButton(icon[i]);
			p_north.add(menu[i]);
			
			// ��ư�� ������ ����
			menu[i].addActionListener(this);
		}
		add(p_north, BorderLayout.NORTH);
		
		p_center=new JPanel();
		page[0]=new LoginForm();
		page[1]=new Content();
		page[2]=new Etc();
		
		// LoginForm ���� �� ����
		p_center.add(page[0]);
		
		// Content ���� �� ����
		p_center.add(page[1]);
		
		// Etc ���� �� ����
		p_center.add(page[2]);
		
		add(p_center);
		
		setSize(700, 600);
		setVisible(true);
		setLocationRelativeTo(null);	// ȭ�� �߾�
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// �� ��ư�� ������ �� �ش��ϴ� �̺�Ʈ ����
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		/*
		// ������������ ������
		if(obj==menu[0]){			// �α��� �� O, ������&��Ÿ X
			loginForm.setVisible(true);
			content.setVisible(false);
			etc.setVisible(false);
		} else if(obj==menu[1]){	// ���� �� O, �α���&��Ÿ X
			loginForm.setVisible(false);
			content.setVisible(true);
			etc.setVisible(false);
		} else if(obj==menu[2]){	// ��Ÿ O, �α���&������ X
			loginForm.setVisible(false);
			content.setVisible(false);
			etc.setVisible(true);
		}
		*/
		
		// �ڷ����� �����Ͽ� for�� ��� ����, �ݺ��Ǵ� �ڵ� ����
		// panel�� ���� �� ó�� �����ϵ���
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
