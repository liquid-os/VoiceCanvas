package src;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;


public class DrawPanel extends JPanel{
	public static ArrayList<Point> drawn = new ArrayList<Point>();
	public static ArrayList<Color> colours = new ArrayList<Color>();
	public static ArrayList<Integer> lineSizes = new ArrayList<Integer>();
	public static ArrayList<Point> lastDrawn = new ArrayList<Point>();
	public static ArrayList<Integer> connections = new ArrayList<Integer>();
	
	
	public static Color currentColor = Color.BLACK;
	public static int currentSize = 4;
	public static boolean isDrawing = false;
	public static String lastCommand = "...";
	
	public static boolean running = true;
	
	public static void startDrawing() {
		lastDrawn.clear();
		isDrawing = true;
	}
	
	public static void stopDrawing() {
		isDrawing = false;
		if(connections.size() > 0) {
			connections.remove(connections.size()-1);
			connections.add(0);
		}
	}
	
	public DrawPanel() {
		super();
		this.setBackground(Color.WHITE);
		this.addKeyListener(new KeyAdapter() {
	        @Override
	        public void keyReleased(KeyEvent e) {
	        	if(e.getKeyCode() == KeyEvent.VK_D) {
	    			if(!isDrawing)startDrawing();
	    			else stopDrawing();
	    		}
	    		if(e.getKeyCode() == KeyEvent.VK_U) {
	    			undo();
	    		}
	        }
	    });
		//loop();
	}
	
	public void loop() {
		while(running) {
			this.repaint();
		}
	}
	
	public void undo() {
		ArrayList<Integer> remIndexes = new ArrayList<Integer>();
		for(Point p : lastDrawn) {
			for(Point p1 : drawn) {
				if(p1.x == p.x && p1.y == p.y) {
					remIndexes.add(drawn.indexOf(p1));
				}
			}
		}
		for(int i : remIndexes) {
			if(i < drawn.size()) {
				drawn.remove(i);
			}
		}
		lastDrawn.clear();
	}
	
	public void drawTo(int x, int y) {
		
		colours.add(currentColor);
		lineSizes.add(currentSize);
		lastDrawn.add(new Point(x,y));
		drawn.add(new Point(x,y));
		connections.add(1);
	}
	
	public void clearpage() {
		this.getGraphics().setColor(Color.WHITE);
		this.getGraphics().fillRect(0, 0, 10000, 10000);
		for(int i = 0; i < colours.size(); ++i) {
			colours.set(i, Color.WHITE);
		}
			
		stopDrawing();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(isDrawing) {
			Point p = MouseInfo.getPointerInfo().getLocation();
			Point x = this.getLocationOnScreen();
			drawTo(p.x-x.x,p.y-x.y);
			//System.out.println("Drawing");
		}
		
		g.setColor(Color.RED);
		for(Point p : drawn) {
			Color c = colours.get(drawn.indexOf(p));
			g.setColor(c);
			int size = lineSizes.get(drawn.indexOf(p));
			//System.out.println("Painting "+size);
			int indexof = drawn.indexOf(p);
			if(indexof >= 1) {
				int x1 = p.x;
				int y1 = p.y;
				int x2 = 0;
				int y2 = 0;
				if(drawn.get(indexof-1) != null) {
					Point j = drawn.get(indexof-1);
					if(connections.get(indexof) == 1 && connections.get(indexof-1) == 1) {
						x2 = j.x;
						y2 = j.y;
						for(int k = 0; k < size; ++k) {
							if(Math.abs(x1-x2) > Math.abs(y1-y2)) {
								g.drawLine(x1, y1+k, x2, y2+k);
							}else {
								g.drawLine(x1+k, y1, x2+k, y2);
							}
						}
					}
				}
			}
			
			//g.fillOval(p.x - size / 2, p.y - size / 2, size, size);
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), 80);
		g.setColor(Color.WHITE);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		g.drawString("'"+lastCommand+"'", 20, 40);
		int spacer = g.getFontMetrics().stringWidth("'"+lastCommand+"'");
		g.drawString("Current Brush Size: "+currentSize, 60 + spacer, 40);
		g.drawString("Current Colour: ", 250 + spacer, 40);
		g.setColor(currentColor);
		g.fillOval(250 + spacer + 130, 10, 45, 45);
		g.setColor(Color.WHITE);
		g.drawOval(250 + spacer + 130, 10, 45, 45);
		
		repaint();
	}
}
