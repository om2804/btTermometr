import javax.microedition.lcdui.*; 

public class MyCanvas extends Canvas
{
	short temp = 0;
	
	public MyCanvas() { super(); }
	
	public void setTemp(short _temp)
	{
		temp = _temp;
		repaint();
	}
	
	// перерисовка
	protected  void paint(Graphics g) 
	{ 
		g.setColor( 255, 255, 255 );
			g.fillRect( 0, 0, 172, 220 );
			g.setColor( 0, 0, 0 );
			g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
			g.drawString("Temp: " + temp + " C", 10, 12, g.LEFT|g.TOP);
	}
}