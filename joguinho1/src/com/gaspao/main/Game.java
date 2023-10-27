package com.gaspao.main;



import javax.swing.JFrame;

import com.gaspao.entities.ArrowShoot;
import com.gaspao.entities.Chest;
import com.gaspao.entities.Enemy;
import com.gaspao.entities.Entity;
import com.gaspao.entities.Player;
import com.gaspao.entities.Teleport;

import com.gaspao.graficos.Spritesheet;
import com.gaspao.graficos.UI;
import com.gaspao.world.Camera;
import com.gaspao.world.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class Game extends Canvas implements Runnable,KeyListener,MouseListener, MouseMotionListener {

	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public static final int WIDTH = 240;  /* 460; */
	public static final int HEIGHT = 160; /* 320; */ 
	public static final int SCALE = 4;
	
	public int CUR_LEVEL = 0;
	public int MAX_LEVEL = 2;
	
	private BufferedImage image;
	
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<ArrowShoot> arrows;
	public static Spritesheet spritesheet;
	public static Spritesheet menuImage;
	public static Spritesheet backGroundImage;
	
	public static World world;
	
	public static Player player;
	public int ammoAtual = 0;
	public double lifeAtual = 0;
	public double maxLife;
	public int damagePlayer;
	public static boolean gun = false;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	private int teste = 0;
	
	public Menu menu;
	public CharacterCreation characterCreation;
	private int armorColor;
	private int capeColor;
	
	
	public Game() {
		rand = new Random();
		
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		// Inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		arrows = new ArrayList<ArrowShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		menuImage = new Spritesheet("/menuImage.png");
		backGroundImage = new Spritesheet("/backGroundImage.png");
		
		player = new Player(0,0,16,16,spritesheet.getSprite(32,0,16,16));
		entities.add(player);
		
		world = new World("/level0.png");
		
		menu = new Menu();
		
		characterCreation = new CharacterCreation();
		
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
	}
	
	public void initFrame() {
		
		frame = new JFrame("Joguinho");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	} 
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} 
	
	
	public static void main(String args[]) {
		
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		
		if(player.hasGun)
			gun = true;
		
		if(gameState == "NORMAL") {
			this.restartGame = false;
			for(int i=0; i<entities.size(); i++) {
				Entity e = entities.get(i);				
				
				e.tick();
			}
			
			for(int i=0; i < arrows.size(); i++) {
				arrows.get(i).tick();
			}
			
			// mudar de lvl
			if(enemies.size() == 0 && teste == 0) {
				
				Teleport portalUp = new Teleport(world.cordenadaPortalUpX,world.cordenadaPortalUpY,16,16,Entity.PORTAL_UP, true);
				Game.entities.add(portalUp);
				Teleport portalDown = new Teleport(world.cordenadaPortalDownX,world.cordenadaPortalDownY,16,16,Entity.PORTAL_DOWN, false);
				Game.entities.add(portalDown);
				teste++;
				}
			
			if (player.portal) {
				ammoAtual = player.ammo;
				lifeAtual = player.life;
				maxLife = player.maxLife;
				damagePlayer = player.damage;
				armorColor = player.colorArmor;
				capeColor = player.colorCape;
				Game.entities.clear();
				Game.arrows.clear();
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) {
					CUR_LEVEL = 0;
				}
				teste = 0;
				String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
				player.ammo = ammoAtual;
				player.life = lifeAtual;
				
				player.colorArmor = armorColor;
				player.colorCape = capeColor;
				
				player.maxLife = maxLife;
				player.damage = damagePlayer;
				player.hasGun = true;
				Chest.open = false;
			}
		} else if (gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver)
					this.showMessageGameOver = false;
				else
					this.showMessageGameOver = true;
			}
			if(restartGame) {
				this.restartGame = false;
				this.gun = false;
				this.gameState = "NORMAL";
				CUR_LEVEL = 0;
				String newWorld = "level"+CUR_LEVEL+".png";
				
				Game.entities.clear();
				Game.arrows.clear();
				
				armorColor = player.colorArmor;
				capeColor = player.colorCape;
				
				World.restartGame(newWorld);
				
				player.colorArmor = armorColor;
				player.colorCape = capeColor;
				
			}
		} else if(gameState == "MENU") {
			// menuzinho dos gu
			menu.tick();
		} else if(gameState == "CHARACTER_CREATION") {
			// criação de personagem
			characterCreation.tick();
		}
		

	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		/* background jogo */
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// mundo
		world.render(g);
		
		// entidades
		for(int i=0; i<entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}		
		for(int i=0; i < arrows.size(); i++) {
			arrows.get(i).render(g);
		}
		ui.render(g);
		
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial",Font.BOLD,18));
		g.setColor(Color.white);
		g.drawString("Flecha: "+ player.ammo, 32, 70);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			
			g.setFont(new Font("arial",Font.BOLD,50));
			g.setColor(Color.white);
			g.drawString("Game Over",(WIDTH*SCALE)/2-110, (HEIGHT*SCALE)/2-10);
			
			g.setFont(new Font("arial",Font.BOLD,32));
			if(showMessageGameOver)
				g.drawString("Pressione Enter para reiniciar",(WIDTH*SCALE)/2-200, (HEIGHT*SCALE)/2+40);
		}else if(gameState == "MENU") {
			menu.render(g);
		}else if(gameState == "CHARACTER_CREATION") {
			characterCreation.render(g);
		}
		bs.show();
	}
	
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
							
			}
			if(System.currentTimeMillis() - timer >= 1000) {
				//System.out.println("FPS: "+ frames);
				frames = 0;
				timer+=1000;
			}
			
		}
		stop();
	}


	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// direita
			player.right = true;
			
			if(gameState == "CHARACTER_CREATION") {
				characterCreation.right = true;
			}
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// esquerda
			player.left = true;
			
			if(gameState == "CHARACTER_CREATION") {
				characterCreation.left = true;
			}
			
		}
		
		//
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			// cima
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}if(gameState == "CHARACTER_CREATION") {
				characterCreation.up = true;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			// baixo
			player.down = true;
			
			if(gameState == "MENU") {
				menu.down = true;
			}if(gameState == "CHARACTER_CREATION") {
				characterCreation.down = true;
			}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}if(gameState == "CHARACTER_CREATION") {
				characterCreation.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			menu.pause = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			// direita
			player.right = false;
			
			if(gameState == "CHARACTER_CREATION") {
				characterCreation.right = false;
			}
			
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			// esquerda
			player.left = false;
			
			if(gameState == "CHARACTER_CREATION") {
				characterCreation.left = false;
			}
			
		}
		
		//
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			// cima
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			// baixo
			player.down = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = false;
			if(gameState == "MENU") {
				menu.enter = false;
			}if(gameState == "CHARACTER_CREATION") {
				characterCreation.enter = false;
			}
		}

	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.shoot = true;
		player.mx = (e.getX()/SCALE);
		player.my = (e.getY()/SCALE);
		player.angleX = (player.mx - 128);
		player.angleY = (player.my - 88);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		player.mx = (e.getX()/SCALE);
		player.my = (e.getY()/SCALE);
		player.angleXX = (player.mx - 128);
		player.angleYY = (player.my - 88);
		
		
		
	}
	
	
}

