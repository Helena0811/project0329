// 크기가 정해져 있으면 안됨 -> 로그인 폼/메인 화면 크기 다름
// 이 윈도우는 크기가 결정되어 있지 않아야 한다!
// -> 윈도우 안에 들어갈 패널이 그 크기를 결정해야 함
// 로그인 기능일 때는 작게, 게임 본 화면에서는 크게 구현
// 중요한 정보는 윈도우의 멤버 변수, 메소드로 구현해놓기 -> 공유
package game.word;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame{
	LoginForm loginForm;
	GamePanel gamePanel;
	
	// 페이지들을 배열로 저장
	JPanel[] page=new JPanel[2];
	
	public GameWindow() {
		setLayout(new FlowLayout());
		page[0]=new LoginForm(this);
		page[1]=new GamePanel(this);
		
		add(page[0]);
		add(page[1]);
		
		setPage(0);		// 0번째 LoginForm 출력
		
		setVisible(true);
		//setLocationRelativeTo(null); gamePanel이 출력될 경우 위치 이상함
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	// 윈도우 안에 어떤 패널이 올지 결정하는 메소드 정의
	// 어떤 패널이 들어올지는 인수 index로 결정
	public void setPage(int index){
		for(int i=0; i<page.length; i++){
			if(i==index){
				page[i].setVisible(true);
			}
			else{
				page[i].setVisible(false);
			}		
		}
		// pack()을 이용해서 하나의 윈도우를 여러 형태로 설정 가능
		pack();	// 내용물의 크기만큼 윈도우 크기를 설정
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		new GameWindow();
	}

}
