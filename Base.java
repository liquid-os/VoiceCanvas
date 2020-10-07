package src;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class Base {
	
	JFrame frame = new JFrame();
	DrawPanel panel = new DrawPanel();
	
	public static void main(String[] args) {
		new Base();
	}
	
	String[] cmd_start = new String[] {
			"start",
			"draw",
			"drawing",
			"start drawing",
			"go"
		};
	
	String[] cmd_stop = new String[] {
			"stop",
			"stop drawing",
			"pause"
		};
	
	String[] cmd_exit = new String[] {
			"exit",
			"end session",
			"close",
			"finish"
		};
	
	String[] cmd_undo = new String[] {
			"undo",
		};
	
	String[] cmd_clear = new String[] {
			"clear page",
		};
	
	String[] cmd_plus = new String[] {
			"increase",
			"increase brush size",
			"increase line size",
			"larger",
		};
	
	String[] cmd_minus = new String[] {
			"decrease",
			"decrease brush size",
			"decrease line size",
			"smaller",
		};
	
	
	public Base() {
		frame.setSize(800, 600);
		frame.setTitle("Drawing Assistant");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setEnabled(true);
		frame.setContentPane(panel);
		panel.setEnabled(true);
		panel.setVisible(true);
		panel.setFocusable(true);
		panel.setSize(800,600);
		panel.requestFocusInWindow();
		
		Configuration config = new Configuration();
		config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		config.setDictionaryPath("/Users/Pube/eclipse-workspace/InteractionDesign/src/res/6750.dic");
		config.setLanguageModelPath("/Users/Pube/eclipse-workspace/InteractionDesign/src/res/6750.lm");
		LiveSpeechRecognizer recog = null;
		try {
			recog = new LiveSpeechRecognizer(config);
			recog.startRecognition(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(recog != null) {
			SpeechResult res;
			while((res = recog.getResult()) != null) {
				String cmd = res.getHypothesis().trim();
				System.out.println("You said: "+cmd);
				panel.lastCommand = cmd;
				for(String s : cmd_start) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.startDrawing();
					}
				}
				for(String s : cmd_stop) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.stopDrawing();
					}
				}
				for(String s : cmd_undo) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.undo();
					}
				}
				for(String s : cmd_exit) {
					if(cmd.equalsIgnoreCase(s)) {
						System.exit(0);
					}
				}
				for(String s : cmd_clear) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.clearpage();
					}
				}
				for(String s : cmd_plus) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.currentSize += 2;
					}
				}
				for(String s : cmd_minus) {
					if(cmd.equalsIgnoreCase(s)) {
						panel.currentSize -= 2;
					}
				}
				if(cmd.equalsIgnoreCase("red")) {
					panel.currentColor = Color.red;
				}
				if(cmd.equalsIgnoreCase("green")) {
					panel.currentColor = Color.green;
				}
				if(cmd.equalsIgnoreCase("blue")) {
					panel.currentColor = Color.blue;
				}
				if(cmd.equalsIgnoreCase("black")) {
					panel.currentColor = Color.black;
				}
				if(cmd.equalsIgnoreCase("yellow")) {
					panel.currentColor = Color.yellow;
				}
			}
		}
	}
}
