package tankWar;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import tankWar.Map.Direction;
import tankWar.chuck.Tank;

@SuppressWarnings("serial")
public class Main extends JFrame {
	public static final int SQURE = 25;
	public static final int PORT = 8080;
	
	public Map map;
	public JToolBar toolbar;
	boolean action;
	private Set<Integer> keys = new HashSet<Integer>();
	private List<Socket> observers;
	private ServerSocket server;
	
	public Main() {
		this.setTitle("TankWar");
		this.setSize((int) (Map.WIDTH*SQURE), (int) (Map.HEIGHT*SQURE) + 40);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		map = new Map();
		this.add(map);
		
		
		Tank redTank = map.getRedTank();
		Tank blueTank = map.getBlueTank();
		
		action = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(action) {
					boolean redMove = false, blueMove = false;
					synchronized(keys) {
						for(Integer key: keys) {
							switch(key.intValue()) {
							case KeyEvent.VK_UP:
								blueTank.setDirection(Direction.UP);
								blueMove = true;
								break;
							case KeyEvent.VK_DOWN:
								blueTank.setDirection(Direction.DOWN);
								blueMove = true;
								break;
							case KeyEvent.VK_LEFT:
								blueTank.setDirection(Direction.LEFT);
								blueMove = true;
								break;
							case KeyEvent.VK_RIGHT:
								blueTank.setDirection(Direction.RIGHT);
								blueMove = true;
								break;
							case KeyEvent.VK_CONTROL:
								blueTank.fire();
								break;
							case KeyEvent.VK_W:
								redTank.setDirection(Direction.UP);
								redMove = true;
								break;
							case KeyEvent.VK_S:
								redTank.setDirection(Direction.DOWN);
								redMove = true;
								break;
							case KeyEvent.VK_A:
								redTank.setDirection(Direction.LEFT);
								redMove = true;
								break;
							case KeyEvent.VK_D:
								redTank.setDirection(Direction.RIGHT);
								redMove = true;
								break;
							case KeyEvent.VK_J:
								redTank.fire();
								break;
							}
						}
					}
					if(redMove)
						redTank.move();
					if(blueMove)
						blueTank.move();
					
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
		
		map.setFocusable(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					map.requestFocus();
				}
			}
			
		}).start();
		
		map.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				synchronized(keys) {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_CONTROL:
					case KeyEvent.VK_W:
					case KeyEvent.VK_S:
					case KeyEvent.VK_A:
					case KeyEvent.VK_D:
					case KeyEvent.VK_J:
						keys.add(e.getKeyCode());
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				synchronized(keys) {
					switch(e.getKeyCode()) {
					case KeyEvent.VK_UP:
					case KeyEvent.VK_DOWN:
					case KeyEvent.VK_LEFT:
					case KeyEvent.VK_RIGHT:
					case KeyEvent.VK_CONTROL:
					case KeyEvent.VK_W:
					case KeyEvent.VK_S:
					case KeyEvent.VK_A:
					case KeyEvent.VK_D:
					case KeyEvent.VK_J:
						keys.remove((Integer) e.getKeyCode());
					}
				}
			}
			
		});
		
		
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		observers = new LinkedList<Socket>();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Socket socket = server.accept();
						observers.add(socket);
					} catch (IOException e) {
//						e.printStackTrace();
						break;
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!map.gameover) {
					try {
//						Thread.sleep(Map.FRESH_MAP);
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(Socket observer: observers) {
						try {
							DataOutputStream writer = new DataOutputStream(observer.getOutputStream());
							writer.writeUTF(String.format("%d %d %d %d ", map.getRedFort().getHP(), map.getBlueFort().getHP(),
									map.getRedTank().getHP(), map.getBlueTank().getHP()));
						} catch (IOException e) {
//							e.printStackTrace();
							continue;
						}
					}
				}
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		this.setVisible(true);
		playWav("start.wav");
		map.run();
		action = false;
	}
	public static void playWav(String path) {
		try {
			AudioInputStream as = AudioSystem.getAudioInputStream(new File("src\\img\\"+path));
			Clip clip = AudioSystem.getClip();
			clip.open(as);
			clip.start();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		while(true) {
			
			Main m = new Main();

			int confirm = JOptionPane.showConfirmDialog(null, m.map.msg+"\nRestart?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(confirm == JOptionPane.NO_OPTION) {
				System.exit(0);
			}

			m.dispose();
		}
	}
}
