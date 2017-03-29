/*
 * ���� ���� ȭ�� 
 * */
package game.word;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel implements ItemListener{
	GameWindow gameWindow;
	GamePanel gamePanel;
	
	JPanel p_west;		// ���� ��Ʈ�� ����
	JPanel p_center;	// �ܾ� �׷��� ó�� ����
	
	JLabel la_user;		// ���ӿ� �α��� �� ����ڸ�
	JLabel la_score;	// ���� ����
	Choice choice;		// �ܾ� ���� dropbox
	JTextField t_input;	// �ܾ� �Է�â
	JButton bt_start;	// ���� ���� ��ư
	JButton bt_pause;	// ���� ��� �ߴ�
	
	String res="C:/java_workspace2/project0329/res/";
	
	// utf-8 ���ڵ� ������ �ȵǹǷ� �ٲޤФ�
	//FileReader reader;	// ���� ��� + ���� �Է� ��Ʈ��, ������ ������� �� ���� ��Ʈ��(���� ����)
	FileInputStream fis;
	InputStreamReader reader;	// ���ڵ� ���� ����
	BufferedReader buffr; // ���� ����� ���� ��Ʈ��(�ٱ��� ����)
	
	// ������ �ܾ�.txt ����(�ܾ�) ����(Generic type-String)
	ArrayList<String> wordList=new ArrayList<String>();
	
	public GamePanel(GameWindow gameWindow){
		this.gameWindow=gameWindow;
		setLayout(new BorderLayout());
		
		p_west=new JPanel();
		
		// ������ �ܾ �� ������ ���
		// �ٽ� �׷��� �ϹǷ� ���� �͸� Ŭ������ ������
		p_center=new JPanel(){
			protected void paintComponent(Graphics g) {
				g.drawString("��ȣ", 100, 150);
			}
		};
		
		la_user=new JLabel("������ ��");
		la_score=new JLabel("0��");
		choice=new Choice();
		t_input=new JTextField(10);
		bt_start=new JButton("START");
		bt_pause=new JButton("PAUSE");
		
		choice.add("�÷����� �ܾ� ����");
		choice.setPreferredSize(new Dimension(135, 40));
		choice.addItemListener(this);
		
		p_west.setPreferredSize(new Dimension(150, 700));
		//p_west.setBackground(Color.cyan);
		
		p_west.add(la_user);
		p_west.add(choice);
		p_west.add(t_input);
		p_west.add(bt_start);
		p_west.add(bt_pause);
		p_west.add(la_score);
		
		add(p_west,BorderLayout.WEST);
		add(p_center);
		
		setVisible(false);	// ù ȭ���� �α��� ���̹Ƿ� ��� ��Ȱ��ȭ
		setBackground(Color.pink);
		setPreferredSize(new Dimension(900, 700));
		
		getCategory();
		
	}
	
	// Choice ������Ʈ�� ä���� ���ϸ� ����
	public void getCategory(){
		// File(URI uri) or File(String pathname)
		File file=new File(res);
		
		// ���ϰ� ���丮�� ��������
		File[] fileList=file.listFiles();
		
		// ���� ���� �� .txtȮ���ڸ� ���� ���ϸ� ����
		for(int i=0; i<fileList.length; i++){
			if(fileList[i].isFile()){
				String name=fileList[i].getName();
				String[] arr=name.split("\\.");
				if(arr[1].equals("txt")){
					choice.add(name);
				}
			}
		}
		System.out.println(choice);
	}
	
	// �ܾ� �о����
	public void getWord(){
		// ù��° ���� �÷����� �ܾ� ���� - �� ������ �ƴϹǷ� ���ܽ�Ű��!
		int index=choice.getSelectedIndex();
		if(index!=0){
			// new FileReader(String)
			// Choice.getSelectedIten() -> String ��ȯ
			String name=choice.getSelectedItem();
			// System.out.println(name);
			try {
				fis=new FileInputStream(res+name);	// ��Ʈ�� ����
				// ���� ���� ���(txt)
				reader=new InputStreamReader(fis,"utf-8");	// ���ڵ� ���� ������ ��Ʈ�� ������
				
				// ��Ʈ���� ���� ó�� �������� ���׷��̵�
				buffr=new BufferedReader(reader);	// ���� ��Ʈ������ �ٹٲ޸��� �о����
				String data;	// ���۽�Ʈ������ ���� ������ ��Ʈ���� ���� ����
				
				while(true){
					data=buffr.readLine();	// �� ��
					if(data==null)	break;
					//System.out.println(data);	// ����� �о������ Ȯ��
					wordList.add(data);		// �о���� �ܾ� ����
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				// �ٱ��ʺ��� ��Ʈ�� �ݱ�
				if(buffr!=null){
					try {
						buffr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(reader!=null){
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(fis!=null){
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	// item ���� �޼ҵ�(�÷����� �ܾ�.txt ���� ����)
	public void itemStateChanged(ItemEvent e) {
		//System.out.println("�ٲ��");
		getWord();
	}
}
