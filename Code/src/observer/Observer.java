package observer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tankWar.Main;

@SuppressWarnings("serial")
public class Observer extends JFrame {
	private JTextArea textField;
	public Observer() {
		textField = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textField);
		this.setSize(300, 500);
		this.setLocationRelativeTo(null);
		this.setTitle("Observer");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(scrollPane);
		textField.setEditable(false);
		
		this.setVisible(true);
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("127.0.0.1", Main.PORT);
			DataInputStream reader = new DataInputStream(socket.getInputStream());
			while(true) {
				String line = reader.readUTF();
				String[] data = line.split(" ");
//				System.out.format("\nHP:\nRedFort: %d\nBlueFort: %d\nRedTank: %d\nBlueTank: %d\n", 
//						Integer.valueOf(data[0]), Integer.valueOf(data[1]), 
//						Integer.valueOf(data[2]), Integer.valueOf(data[3]));
				textField.setText(textField.getText() + String.format(
						"\nHP:\nRedFort: %d\nBlueFort: %d\nRedTank: %d\nBlueTank: %d\n", 
						Integer.valueOf(data[0]), Integer.valueOf(data[1]), 
						Integer.valueOf(data[2]), Integer.valueOf(data[3])));
				if(Integer.valueOf(data[0]) == 0 || Integer.valueOf(data[1]) == 0)
					break;
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Observer ob = new Observer();
	}
}
