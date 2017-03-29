// ũ�Ⱑ ������ ������ �ȵ� -> �α��� ��/���� ȭ�� ũ�� �ٸ�
// �� ������� ũ�Ⱑ �����Ǿ� ���� �ʾƾ� �Ѵ�!
// -> ������ �ȿ� �� �г��� �� ũ�⸦ �����ؾ� ��
// �α��� ����� ���� �۰�, ���� �� ȭ�鿡���� ũ�� ����
// �߿��� ������ �������� ��� ����, �޼ҵ�� �����س��� -> ����
package game.word;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame{
	LoginForm loginForm;
	GamePanel gamePanel;
	
	// ���������� �迭�� ����
	JPanel[] page=new JPanel[2];
	
	public GameWindow() {
		setLayout(new FlowLayout());
		page[0]=new LoginForm(this);
		page[1]=new GamePanel(this);
		
		add(page[0]);
		add(page[1]);
		
		setPage(0);		// 0��° LoginForm ���
		
		setVisible(true);
		//setLocationRelativeTo(null); gamePanel�� ��µ� ��� ��ġ �̻���
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	// ������ �ȿ� � �г��� ���� �����ϴ� �޼ҵ� ����
	// � �г��� �������� �μ� index�� ����
	public void setPage(int index){
		for(int i=0; i<page.length; i++){
			if(i==index){
				page[i].setVisible(true);
			}
			else{
				page[i].setVisible(false);
			}		
		}
		// pack()�� �̿��ؼ� �ϳ��� �����츦 ���� ���·� ���� ����
		pack();	// ���빰�� ũ�⸸ŭ ������ ũ�⸦ ����
		setLocationRelativeTo(null);
	}
	
	public static void main(String[] args) {
		new GameWindow();
	}

}
