/*
 * ���� �������� ���� : ���α׷� �
 * ���� ũ�Ⱑ ū ������ �����ϸ� ���� ��ư�� ���� ���·� ���α׷��� ������
 * ���� ������� ���� ������ �����Ű�� ����!!!
 * -> �����尡 ��� ���� ���� �۾� 
 * -> ��� : copy()�� �����尡 �����ϵ���
 * */
// ��ü ���� ũ�� : 100% = ���� ���� ������ ũ�� : x%
// ������ = 100% * ���� ���� ������ ũ�� / ��ü ���� ũ��
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
	
	File file;	// copy()���� �о�� ����(������ ����)�� �����ϴ� ����
	
	Thread thread;	// ���縦 ������ ���� ������
	/*
	 * ���� �޼ҵ�� �츮�� �˰� �ִ� �����(���ø����̼��� � ���) ���� ����
	 * ����, ���� ���� ������ ��� ���¿� ��Ʈ������ �ȵ�!
	 * */
	long fileSize;	// ���� ������ ��ü �뷮
	
	public CopyMain() {
		bt_open=new JButton("����");
		bt_save=new JButton("����");
		bt_copy=new JButton("����");
		
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
		
		// ��ư�� ������ ����
		bt_open.addActionListener(this);
		bt_save.addActionListener(this);
		bt_copy.addActionListener(this);
		
		setSize(500, 200);
		setVisible(true);
		setLocationRelativeTo(null); 	// ������ component�� ���� ���� null�� ���� ����� ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e) {
		// �̺�Ʈ�� ����Ų �̺�Ʈ �ҽ�(��ü)
		Object obj=e.getSource();
		
		// �ּҰ� �񱳳� ���� �񱳳� ����
		if(obj==bt_open){
			open();
		}
		else if(obj==bt_save){
			save();
		}
		else if(obj==bt_copy){
			//copy();	// ���� �����尡 ���� ����
			// ������ ���� ���縦 �������� �ʰ�, �����忡�� ����
			// -> ���ξ������ �����Ӱ� ���α׷� ��ü � ����!
			// ������ �����ڿ� Runnable ���� ��ü�� �μ��� ������, 
			// Runnable ��ü���� �������� run() ���� ����
			thread=new Thread(this);	
			thread.start();	// CopyMain�� run ����
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
			// ������ ��Ʈ���� ���� ������ �б�
			int data=-1;
			int count=0;
			while(true){
				data=fis.read();	// 1byte �б�
				if(data==-1){	break;	}
				fos.write(data);	// 1byte ���
				count++;
				
				// progress bar ����
				// read() Ƚ���� �� �о���� byte �� = ������� �о���� ��
				int v=(int)getPercent(count);
				bar.setValue(v);
				bar.setString(v+"%");
			}
			JOptionPane.showMessageDialog(this, "���� �Ϸ�");
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "������ ã�� �� �����ϴ�");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO ó�� �� ������ �߻��߽��ϴ�");
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
	// Runnable�� ������� ���X
	// Thread �����ڿ� runnable�� ������ ���� �������� run()�� �����
	public void run() {
		copy();
	}
	
	// ���� ������ ���ϴ� ����
	// ������ = 100% * ���� ���� ������ ũ�� / ��ü ���� ũ��
	// ���� ���� �����͸� �μ��� �ޱ�
	public long getPercent(int currentRead){
		return (100*currentRead)/fileSize;
	}
	
	public static void main(String[] args) {
		new CopyMain();
	}
}
