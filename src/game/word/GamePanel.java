/*
 * ���� ���� ȭ�� 
 * */
package game.word;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

public class GamePanel extends JPanel implements ItemListener, Runnable, ActionListener{
	GameWindow gameWindow;
	GamePanel gamePanel;
	
	JPanel p_west;		// ���� ��Ʈ�� ����
	JPanel p_center;	// �ܾ� �׷��� ó�� ����
	
	JLabel la_user;		// ���ӿ� �α��� �� ����ڸ�
	JLabel la_score;	// ���� ����
	Choice choice;		// �ܾ� 0���� dropbox
	JTextField t_input;	// �ܾ� �Է�â
	JButton bt_start;	// ���� ���� ��ư
	JButton bt_pause;	// ���� ��� �ߴ�
	JButton bt_quit;	// ���� ����
	
	String res="C:/java_workspace2/project0329/res/";
	
	// FileReader�� utf-8 ���ڵ� ������ �ȵǹǷ� �ٲޤФ�
	//FileReader reader;	// ���� ��� + ���� �Է� ��Ʈ��, ������ ������� �� ���� ��Ʈ��(���� ����)
	FileInputStream fis;
	InputStreamReader reader;	// ���ڵ� ���� ����
	BufferedReader buffr; // ���� ����� ���� ��Ʈ��(�ٱ��� ����)
	
	// ������ �ܾ�.txt ����(�ܾ�) ����(Generic type-String)
	ArrayList<String> wordList=new ArrayList<String>();
	
	// ������ �����ϸ� �ܾ ������ ������
	Thread thread;
	
	// ������ ���� �� �ִ� ����(run���� ���)
	boolean flag=true;
	
	// �ܾ��� �������� ������ ����(tick(), render())
	boolean isDown=true;
	
	// �ܾ��� ���� ��ġ�� ������ ����(y�ప)
	//int y=100;
	
	// �������� �� �ܾ�(��ü)���� ���� ����
	ArrayList<Word> wordArr=new ArrayList<Word>();
	
	public GamePanel(GameWindow gameWindow){
		this.gameWindow=gameWindow;
		setLayout(new BorderLayout());
		
		p_west=new JPanel();
		
		// ������ �ܾ �� ������ ���
		// �ٽ� �׷��� �ϹǷ� ���� �͸� Ŭ������ ������
		p_center=new JPanel(){

			public void paintComponent(Graphics g) {
				// �ϳ��� �ܾ� �����ӿ��� ����ȭ�� -> Ŭ���� �ʿ�(�ܾ �ϳ��� ��ü�� ����)
				// ������ �׸� �����
				g.setColor(Color.white);
				g.fillRect(0, 0, 750, 700);
				g.setColor(Color.pink);
				
				//g.drawString("��ȣ", 100, y);
				
				// ��� word ��ü�� ���� render() ȣ��
				for(int i=0; i<wordArr.size(); i++){
					// render()�� Graphics g�� �ʿ��ϹǷ� �гο��� ȣ��
					wordArr.get(i).render(g);
				}
			}
		};
		
		la_user=new JLabel("������ ��");
		la_score=new JLabel("0��");
		choice=new Choice();
		t_input=new JTextField(10);
		bt_start=new JButton("START");
		bt_pause=new JButton("PAUSE");
		bt_quit=new JButton("QUIT");
		
		choice.add("�÷����� �ܾ� ����");
		choice.setPreferredSize(new Dimension(135, 40));
		
		// choice�� ������ ����
		choice.addItemListener(this);
		
		p_west.setPreferredSize(new Dimension(150, 700));
		//p_west.setBackground(Color.cyan);
		
		// ��ư�� ������ ����
		bt_start.addActionListener(this);
		bt_pause.addActionListener(this);
		bt_quit.addActionListener(this);
		
		// �ؽ�Ʈ�ʵ�� ������ ����
		t_input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// ����Ű�� ������
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					// Ű����� ���� �Է°��� ���� �������� �ִ� �ܾ�(wordArr) ��
					// �����ϴٸ� wordArr���� ��ü ����
					String value=t_input.getText();
					for(int i=0; i<wordArr.size(); i++){
						if(wordArr.get(i).name.equals(value)){
							wordArr.remove(i);
						}
					}
					
					//System.out.println("����Ű ������");
				}
			}
		});
		
		p_west.add(la_user);
		p_west.add(choice);
		p_west.add(t_input);
		p_west.add(bt_start);
		p_west.add(bt_pause);
		p_west.add(bt_quit);
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
				
				// ������ wordList�� ���� �����ؾ� ��(2�� �̻� �����ϸ� ��� �߰��ǹǷ�)
				wordList.removeAll(wordList);
				
				while(true){
					data=buffr.readLine();	// �� ��
					if(data==null)	break;
					//System.out.println(data);	// ����� �о������ Ȯ��
					wordList.add(data);		// �о���� �ܾ� ����
				}
				
				 System.out.println("������� wordList��"+ wordList.size());
				
				// �غ� �Ϸ� �� �ܾ���� ȭ�鿡 ���
				createWord();
				
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
	
	// �ܾ� ����(Word ��ü ����)
	public void createWord() {
		for(int i=0; i<wordList.size(); i++){
			//String name=wordList.get(i);
			Word word=new Word(wordList.get(i),(i*(75)+10),100);
			// ������ word��ü���� ��Ƴ��� ���� ����
			// word ��ü ��� �����
			wordArr.add(word);
		}
	}

	// ���� ����
	public void startGame(){
		// Runnable�� �������� ���� Ŭ������ ������� �־��ָ� �������� run() ����
		// �����尡 �޸𸮿� �ö���� ���� ���(����)���� ������ ����, thread=null
		if(thread==null){
			// �������� �����ϴ� run()�� while�� �۵���Ű��
			flag=true;
			thread=new Thread(this);
			thread.start();
		}
	}
	
	// ���� �Ͻ� ���� or �����
	public void pauseGame(){
		// run()�� ������ ������ ����ϴµ�, flag=false�� ������ while���� ������ ������ ������ �����
		// flag=false;
		// �ܾ� �������� �����Ӹ� ��� ���߱�
		isDown=!isDown;
	}
/*	
	// �ܾ� �����̱�
	public void down(){
		/*
		// y���� ������Ű��
		y+=10;
		
		// p_center �г��� �׸��� �ٽ� �׸���
		p_center.repaint();
		//System.out.println("�������� �־�!");
		
	}
*/	
	// ���� ����(&�ʱ�ȭ)
	/* (���� ���� ��)ó������ ������!
	 * 1. wordList(�ܾ���� ����ִ�) ����
	 * 2. wordArr(Word �ν��Ͻ����� ����ִ�) ����
	 * 3. choice �ʱ�ȭ(index=0)
	 * 4. flag=false
	 * 5. thread=null	�ٽ� start ��ư�� ������ �̹� ���� ������ ���� �־� thread�� null�� �ƴϱ� ������ ���������� ���� ����, null�� �ʱ�ȭ 
	 * */
	public void quitGame(){
		// �ܾ� ���� �ʱ�ȭ
		wordList.removeAll(wordList);
		
		// �����ߴ� �ܾ� ����(Word) ����
		wordArr.removeAll(wordArr);
		
		// choice ������Ʈ �ʱ�ȭ
		choice.select(0); 	// ù��° ��ҷ� ���� ����
		
		// run() �޼ҵ��� while�� ����
		flag=false;
		
		thread=null;

	}
	
	// item ���� �޼ҵ�(�÷����� �ܾ�.txt ���� ����) - ������ �ι� �̻� �����ϸ� ���õ� txt�� ������ ��� �߰���
	public void itemStateChanged(ItemEvent e) {
		//System.out.println("�ٲ��");
		getWord();
	}
	
	// ������ run(), ���� ����(���ξ����� ��� ����)
	public void run() {
		// flag�� true�� ��� ��� ����
		while(flag){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// �ܾ� �����̴� �ڵ常 �����ϸ� pause ���� ����
			if(isDown){
				//down();	// Word ��ü���� �������� ȿ���� �����ϹǷ� �ʿ�X
				// ��� �ܾ ���ؼ� tick() & render()
				for(int i=0; i<wordArr.size(); i++){
					wordArr.get(i).tick();
					// render()�� Graphics g�� �ʿ��ϹǷ� �гο��� ȣ��
					// wordArr.get(i).render(g);
				}
				
				// ��� �ܾ�鿡 ���� repaint()
				p_center.repaint();
			}
		}
	}
	
	// ��ư�� ���� ����
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_start){
			startGame();
		} else if(obj==bt_pause){
			pauseGame();
		} else if(obj==bt_quit){
			quitGame();
		}
	}
}
