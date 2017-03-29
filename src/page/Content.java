package page;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Content extends JPanel{
	JLabel lb;
	
	public Content() {
		lb=new JLabel("¼¼Á¾´ë¿Õ");
		add(lb);
		setBackground(Color.cyan);
		setPreferredSize(new Dimension(700, 500));
	}
}
