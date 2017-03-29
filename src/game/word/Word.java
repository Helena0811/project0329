/*
 * 게임에 등장할 대상 단어가 각각 y축의 값을 따로 가지고,
 * 대량으로 생성되어야 하므로 재사용성을 위한 코드 집합=클래스로 구현!
 * */
package game.word;

import java.awt.Graphics;

public class Word {
	String name;	// 객체가 담게 될 단어
	int x;			// 단어의 x축 값
	int y;			// 단어의 y축 값
	/*int interval;	
	 * 서로 다른 interval을 가지게 된다면 각자 다른 시간에 그림을 다시 그리게 되므로
	 * (비동기화) interval은 동기화되어야 함(하나로 제어)
	*/
	int velX;	
	int velY;	// 단어가 움직이는 속도
	
	// 단어가 생성될 때 갖추어야할 초기값 지정
	public Word(String name, int x, int y) {
		this.name=name;
		this.x=x;
		this.y=y;
	}
	
	/*
	 * 게임 구현 필수 메소드
	 * 1. 물리량 변화(데이터)
	 * 2. 그림 그리기
	 * */
	// 객체에 반영될 데이터 변화 코드
	public void tick(){
		y+=10;
	}
	
	// 반영된 데이터를 이용하여 화면 그리기
	public void render(Graphics g){
		g.drawString(name, x, y);
	}
	// tick과 render는 심장부에서 항상 호출되어야 함
}
