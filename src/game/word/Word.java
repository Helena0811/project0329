/*
 * ���ӿ� ������ ��� �ܾ ���� y���� ���� ���� ������,
 * �뷮���� �����Ǿ�� �ϹǷ� ���뼺�� ���� �ڵ� ����=Ŭ������ ����!
 * */
package game.word;

import java.awt.Graphics;

public class Word {
	String name;	// ��ü�� ��� �� �ܾ�
	int x;			// �ܾ��� x�� ��
	int y;			// �ܾ��� y�� ��
	/*int interval;	
	 * ���� �ٸ� interval�� ������ �ȴٸ� ���� �ٸ� �ð��� �׸��� �ٽ� �׸��� �ǹǷ�
	 * (�񵿱�ȭ) interval�� ����ȭ�Ǿ�� ��(�ϳ��� ����)
	*/
	int velX;	
	int velY;	// �ܾ �����̴� �ӵ�
	
	// �ܾ ������ �� ���߾���� �ʱⰪ ����
	public Word(String name, int x, int y) {
		this.name=name;
		this.x=x;
		this.y=y;
	}
	
	/*
	 * ���� ���� �ʼ� �޼ҵ�
	 * 1. ������ ��ȭ(������)
	 * 2. �׸� �׸���
	 * */
	// ��ü�� �ݿ��� ������ ��ȭ �ڵ�
	public void tick(){
		y+=10;
	}
	
	// �ݿ��� �����͸� �̿��Ͽ� ȭ�� �׸���
	public void render(Graphics g){
		g.drawString(name, x, y);
	}
	// tick�� render�� ����ο��� �׻� ȣ��Ǿ�� ��
}
