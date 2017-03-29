/*
 * 게임 메인 화면 
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
	
	JPanel p_west;		// 왼쪽 컨트롤 영역
	JPanel p_center;	// 단어 그래픽 처리 영역
	
	JLabel la_user;		// 게임에 로그인 한 사용자명
	JLabel la_score;	// 게임 점수
	Choice choice;		// 단어 선택 dropbox
	JTextField t_input;	// 단어 입력창
	JButton bt_start;	// 게임 시작 버튼
	JButton bt_pause;	// 게임 잠시 중단
	
	String res="C:/java_workspace2/project0329/res/";
	
	// utf-8 인코딩 지원이 안되므로 바꿈ㅠㅠ
	//FileReader reader;	// 문자 기반 + 파일 입력 스트림, 파일을 대상으로 한 문자 스트림(안쪽 빨대)
	FileInputStream fis;
	InputStreamReader reader;	// 인코딩 지원 가능
	BufferedReader buffr; // 문자 기반의 버퍼 스트림(바깥쪽 빨대)
	
	// 선택한 단어.txt 내용(단어) 저장(Generic type-String)
	ArrayList<String> wordList=new ArrayList<String>();
	
	// 게임이 시작하면 단어를 움직일 쓰레드
	Thread thread;
	
	// 게임을 멈출 수 있는 변수(run에서 사용)
	boolean flag=true;
	
	// 단어의 현재 위치를 조절할 변수(y축값)
	//int y=100;
	
	ArrayList<Word> wordArr=new ArrayList<Word>();
	
	public GamePanel(GameWindow gameWindow){
		this.gameWindow=gameWindow;
		setLayout(new BorderLayout());
		
		p_west=new JPanel();
		
		// 추출한 단어를 이 영역에 출력
		// 다시 그려야 하므로 내부 익명 클래스로 재정의
		p_center=new JPanel(){

			public void paintComponent(Graphics g) {
				// 하나의 단어 움직임에만 최적화됨 -> 클래스 필요(단어를 하나의 객체로 간주)
				// 기존의 그림 지우기
				g.setColor(Color.white);
				g.fillRect(0, 0, 750, 700);
				g.setColor(Color.pink);
				
				//g.drawString("야호", 100, y);
				
				// 모든 word 객체에 대해 render() 호출
				for(int i=0; i<wordArr.size(); i++){
					// render()는 Graphics g가 필요하므로 패널에서 호출
					wordArr.get(i).render(g);
				}
			}
		};
		
		la_user=new JLabel("장현아 님");
		la_score=new JLabel("0점");
		choice=new Choice();
		t_input=new JTextField(10);
		bt_start=new JButton("START");
		bt_pause=new JButton("PAUSE");
		
		choice.add("플레이할 단어 선택");
		choice.setPreferredSize(new Dimension(135, 40));
		
		// choice와 리스너 연결
		choice.addItemListener(this);
		
		p_west.setPreferredSize(new Dimension(150, 700));
		//p_west.setBackground(Color.cyan);
		
		// 버튼과 리스너 연결
		bt_start.addActionListener(this);
		bt_pause.addActionListener(this);
		
		p_west.add(la_user);
		p_west.add(choice);
		p_west.add(t_input);
		p_west.add(bt_start);
		p_west.add(bt_pause);
		p_west.add(la_score);
		
		add(p_west,BorderLayout.WEST);
		add(p_center);
		
		setVisible(false);	// 첫 화면은 로그인 폼이므로 출력 비활성화
		setBackground(Color.pink);
		setPreferredSize(new Dimension(900, 700));
		
		getCategory();
		
	}
	
	// Choice 컴포넌트에 채워질 파일명 조사
	public void getCategory(){
		// File(URI uri) or File(String pathname)
		File file=new File(res);
		
		// 파일과 디렉토리가 섞여있음
		File[] fileList=file.listFiles();
		
		// 파일 추출 후 .txt확장자를 가진 파일만 저장
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
	
	// 단어 읽어오기
	public void getWord(){
		// 첫번째 값인 플레이할 단어 선택 - 은 파일이 아니므로 제외시키기!
		int index=choice.getSelectedIndex();
		if(index!=0){
			// new FileReader(String)
			// Choice.getSelectedIten() -> String 반환
			String name=choice.getSelectedItem();
			// System.out.println(name);
			try {
				fis=new FileInputStream(res+name);	// 스트림 생성
				// 문서 파일 대상(txt)
				reader=new InputStreamReader(fis,"utf-8");	// 인코딩 지원 가능한 스트림 생성됨
				
				// 스트림을 버퍼 처리 수준으로 업그레이드
				buffr=new BufferedReader(reader);	// 버퍼 스트림으로 줄바꿈마다 읽어오기
				String data;	// 버퍼스트림으로 읽을 때마다 스트링을 담을 변수
				
				while(true){
					data=buffr.readLine();	// 한 줄
					if(data==null)	break;
					//System.out.println(data);	// 제대로 읽어오는지 확인
					wordList.add(data);		// 읽어오는 단어 저장
				}
				
				// 준비 완료 된 단어들을 화면에 출력
				createWord();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				// 바깥쪽부터 스트림 닫기
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
	
	// 단어 생성(Word 객체 생성)
	public void createWord() {
		for(int i=0; i<wordList.size(); i++){
			//String name=wordList.get(i);
			Word word=new Word(wordList.get(i),(i*(75)+10),100);
			// 생성된 word객체들을 담아놔야 접근 가능
			// word 객체 명단 만들기
			wordArr.add(word);
		}
	}

	// 게임 시작
	public void startGame(){
		// Runnable을 재정의한 현재 클래스를 대상으로 넣어주면 재정의한 run() 수행
		// 쓰레드가 메모리에 올라오지 않은 경우(최초)에만 쓰레드 실행, thread=null
		if(thread==null){
			thread=new Thread(this);
			thread.start();
		}
	}
	
	// 게임 중지
	public void pauseGame(){
		flag=false;
	}
/*	
	// 단어 움직이기
	public void down(){
		/*
		// y값을 증가시키고
		y+=10;
		
		// p_center 패널이 그림을 다시 그리기
		p_center.repaint();
		//System.out.println("내려오고 있어!");
		
	}
*/	
	// item 선택 메소드(플레이할 단어.txt 파일 선택)
	public void itemStateChanged(ItemEvent e) {
		//System.out.println("바꿨닝");
		getWord();
	}
	
	// 쓰레드 run()
	public void run() {
		// flag가 true인 경우 계속 수행
		while(flag){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//down();	// Word 객체에서 내려오는 효과가 존재하므로 필요X
			// 모든 단어에 대해서 tick() & render()
			for(int i=0; i<wordArr.size(); i++){
				wordArr.get(i).tick();
				// render()는 Graphics g가 필요하므로 패널에서 호출
				// wordArr.get(i).render(g);
			}
			
			// 모든 단어들에 대해 repaint()
			p_center.repaint();
		}
	}
	
	// 버튼에 따른 실행
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_start){
			startGame();
		} else if(obj==bt_pause){
			pauseGame();
		}
	}
}
