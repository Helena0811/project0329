/*
 * 메인 쓰레드의 목적 : 프로그램 운영
 * 현재 크기가 큰 파일을 복사하면 복사 버튼이 눌린 상태로 프로그램의 무응답
 * 메인 쓰레드는 무한 루프를 실행시키지 말자!!!
 * -> 쓰레드가 대신 무한 루프 작업 
 * -> 결론 : copy()를 쓰레드가 수행하도록
 * */
// 전체 파일 크기 : 100% = 현재 읽은 데이터 크기 : x%
// 진행율 = 100% * 현재 읽은 데이터 크기 / 전체 파일 크기
package homework4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class CopyMain extends JFrame implements ActionListener, Runnable{
	JButton bt_open, bt_save, bt_copy;
	JTextField t_open, t_save;
	JProgressBar bar;
	
	JFileChooser chooser;
	
	File file;	// copy()에서 읽어올 파일(복사할 원본)을 접근하는 변수
	
	Thread thread;	// 복사를 실행할 전용 쓰레드
	/*
	 * 메인 메소드는 우리가 알고 있는 실행부(어플리케이션의 운영 담당) 역할 수행
	 * 따라서, 절대 무한 루프나 대기 상태에 빠트려서는 안됨!
	 * */
	long fileSize;	// 원본 파일의 전체 용량
	
	public CopyMain() {
		bt_open=new JButton("열기");
		bt_save=new JButton("저장");
		bt_copy=new JButton("복사");
		
		t_open=new JTextField(35);
		t_save=new JTextField(35);
		
		bar=new JProgressBar();
		
		chooser=new JFileChooser("C:/db_study");
		
		bar.setPreferredSize(new Dimension(450,40));
		//bar.setBackground(Color.green);
		bar.setString("0%");
		
		setLayout(new FlowLayout());
		
		add(bar);
		add(bt_open);
		add(t_open);
		add(bt_save);
		add(t_save);
		add(bt_copy);
		
		// 버튼과 리스너 연결
		bt_open.addActionListener(this);
		bt_save.addActionListener(this);
		bt_copy.addActionListener(this);
		
		setSize(500, 200);
		setVisible(true);
		setLocationRelativeTo(null); 	// 의존할 component가 없을 때는 null로 놓아 가운데에 놓기
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
		// 이벤트를 일으킨 이벤트 소스(주체)
		Object obj=e.getSource();
		
		// 주소값 비교나 내용 비교나 같음
		if(obj==bt_open){
			open();
		}
		else if(obj==bt_save){
			save();
		}
		else if(obj==bt_copy){
			//copy();	// 메인 쓰레드가 복사 수행
			// 메인이 직접 복사를 수행하지 않고, 쓰레드에게 위임
			// -> 메인쓰레드는 자유롭게 프로그램 전체 운영 가능!
			// 쓰레드 생성자에 Runnable 구현 객체를 인수로 넣으면, 
			// Runnable 객체에서 재정의한 run() 수행 가능
			thread=new Thread(this);	
			thread.start();	// CopyMain의 run 수행
		}
	}
	
	public void open(){
		int result=chooser.showOpenDialog(this);
		
		if(result==JFileChooser.APPROVE_OPTION){
			file=chooser.getSelectedFile();
			t_open.setText(file.getAbsolutePath());
			fileSize=file.length();
		}
	}
	
	public void save(){
		int result=chooser.showSaveDialog(this);
		
		if(result==JFileChooser.APPROVE_OPTION){
			File file=chooser.getSelectedFile();
			t_save.setText(file.getAbsolutePath());
		}
	}
	
	public void copy(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		try {
			fis=new FileInputStream(file);
			// public FileOutputStream(String name)
			fos=new FileOutputStream(t_save.getText());
			// 생성된 스트림을 통해 데이터 읽기
			int data=-1;
			int count=0;
			while(true){
				data=fis.read();	// 1byte 읽기
				if(data==-1){	break;	}
				fos.write(data);	// 1byte 출력
				count++;
				
				// progress bar 적용
				// read() 횟수가 곧 읽어들인 byte 수 = 현재까지 읽어들인 수
				int v=(int)getPercent(count);
				bar.setValue(v);
				bar.setString(v+"%");
			}
			JOptionPane.showMessageDialog(this, "복사 완료");
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "파일을 찾을 수 없습니다");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO 처리 중 에러가 발생했습니다");
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	// implements Runnable
	// Runnable과 쓰레드는 상관X
	// Thread 생성자에 runnable을 넣으면 현재 재정의한 run()이 수행됨
	public void run() {
		copy();
	}
	
	// 현재 진행율 구하는 공식
	// 진행율 = 100% * 현재 읽은 데이터 크기 / 전체 파일 크기
	// 현재 읽은 데이터를 인수로 받기
	public long getPercent(int currentRead){
		return (100*currentRead)/fileSize;
	}
	
	public static void main(String[] args) {
		new CopyMain();
	}
}
