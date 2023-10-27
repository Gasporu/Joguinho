package com.gaspao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Rectangle;
import com.gaspao.graficos.Spritesheet;
import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.World;

public class Player extends Entity {

	public boolean right,up,left,down;
	public int right_dir = 0, left_dir = 1; 
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int maskxRight = 4, maskxLeft = 3, masky = 4, maskw = 10, maskh = 10;
	
	private int frames = 0, maxFrames = 8,index = 0, maxIndex = 3;
	private boolean moved = false;
	
	private BufferedImage[][] rightPlayer;
	private BufferedImage[][] leftPlayer;
	
	private BufferedImage[][] rightPlayerCape;
	private BufferedImage[][] leftPlayerCape;
	
	
	
	private int lado = 1;
	
	public int colorArmor = 0;
	public int colorCape = 0;
	public int maxBodyNumber = 3;
	public int maxCapeNumber = 3;
	
	private BufferedImage playerDamageRight;
	private BufferedImage playerDamageLeft;
	
	public boolean hasGun = false;
	
	public double angle = 0;
	
	public boolean portal = false;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 8 ,damageCurrent = 0;
	public boolean shoot = false;
	
	public double life = 100, maxLife = 100;
	public int damage = 1;
	
	public int mx = 0,my =0;
	public double angleX, angleY;
	public double angleXX, angleYY;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4][4];
		leftPlayer = new BufferedImage[4][4];
		
		rightPlayerCape = new BufferedImage[4][4];
		leftPlayerCape = new BufferedImage[4][4];
	
		playerDamageRight = Game.spritesheet.getSprite(32, 32, 16, 16);
		playerDamageLeft = Game.spritesheet.getSprite(48, 32, 16, 16);
		
		for(int j=0; j<4; j++) {
			for(int i=0 ; i<4 ; i++) {
				rightPlayer[i][j] = Game.spritesheet.getSprite(256 + (i*16), 0 + (j*32), 16, 16);
				rightPlayerCape[i][j] = Game.spritesheet.getSprite(256 + (i*16), 128 + (j*32), 16, 16);
			}
		}
		for(int j=0; j<4; j++) {
			for(int i=0 ; i<4 ; i++) {
				leftPlayer[i][j] = Game.spritesheet.getSprite(256 + (i*16), 16 + (j*32), 16, 16);
				leftPlayerCape[i][j] = Game.spritesheet.getSprite(256 + (i*16), 144 + (j*32), 16, 16);
			}
		}
		
	}

	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())  && !isColidding((int)(x+speed), this.getY()) ) {
			moved = true;
			
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())  && !isColidding((int)(x-speed), this.getY()) ) {
			moved = true;
			
			x-=speed;
		}
		if (up && World.isFree(this.getX(),(int)(y-speed)) && !isColidding(this.getX(), (int)(y-speed)) ) {
			moved = true;
			y-=speed;
		}
		else if (down&& World.isFree(this.getX(),(int)(y+speed)) && !isColidding(this.getX(), (int)(y+speed)) ) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		if (moved == false) {
			frames = 0;
			index = 0;
		}
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		checkCollisionChest();
		checkCollisionRingOfLife();
		checkCollisionRingOfBlood();
		checkCollisionTeleport();
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent ==this.damageFrames) {
				this.damageCurrent = 0;
				isDamaged = false;
			}
		}
		
		if (shoot) {			
			
			shoot = false;
			if (hasGun && ammo > 0){
				
				ammo --;
				//cria bala e atira	
				int px = 0;
				int py = 0;
				
				if(angleXX >= 0) {
					px = 1;
					angle = Math.atan2(my - (this.getY() - Camera.y), mx - (this.getX() - Camera.x));
				} else if(angleXX < 0) {
					px = -1;
					angle = Math.atan2(my - (this.getY() - Camera.y), mx - (this.getX() - Camera.x));
				}
								
				double dy = Math.sin(angle);
				double dx = Math.cos(angle);
				
				
				
				ArrowShoot arrow = new ArrowShoot(this.getX() + px,this.getY() + py,5,5,null,dx,dy);
				Game.arrows.add(arrow);
				
			}
			
			
		}
		
		
		if (life <= 0) {
			// GAME OVER
			life = 0;
			Game.gameState = "GAME_OVER";
		}
		
		updateCamera();
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX()  - (Game.WIDTH/2)+5,-1000/*0*/, 3600 );//World.WIDTH*16 - Game.WIDTH); 
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),-1000/*0*/, 3600);//World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkCollisionGun() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Crossbow) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					
					Game.entities.remove(atual);
				}
			}
		}		
	}
	
	public void checkCollisionAmmo() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Arrow) {
				if(Entity.isColidding(this, atual)) {
					ammo+=15;
					
					Game.entities.remove(atual);
				}
			}
		}		
	}
	
	
	
	public void checkCollisionLifePack() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColidding(this, atual)) {
					life+=32;
					if (life > maxLife)
						life = maxLife;
					Game.entities.remove(atual);
				}
			}
		}
	}
	public void checkCollisionChest() {
		
		
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Chest) {
				if(Entity.isColidding(this, atual)) {
					Chest.open = true;
					
				}
			}
		}
	}
	public void checkCollisionRingOfLife() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Ring_Life) {
				if(Entity.isColidding(this, atual)) {
					Game.entities.remove(atual);
					maxLife += 15;
					life+=15;
					if (life > maxLife)
						life = maxLife;
					
				}
			}
		}
	}
	public void checkCollisionRingOfBlood() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Ring_Blood) {
				if(Entity.isColidding(this, atual)) {
					Game.entities.remove(atual);
					damage += 2;
				}
			}
		}
	}
	public void checkCollisionTeleport() {
		
		for(int i = 0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Teleport) {
				if(Entity.isColidding(this, atual)) {
					
					portal = true;
					
				}
			}
		}		
	}
	
	public boolean isColidding(int xnext, int ynext) {
		
		
		Rectangle playerColisionRight = new Rectangle(xnext+maskxRight,ynext+masky,maskw,maskh);
		Rectangle playerColisionLeft = new Rectangle(xnext+maskxLeft,ynext+masky,maskw,maskh);
		
		for(int i=0; i<Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual == this)
				continue;
			if(atual instanceof Enemy) {
				continue;
			}
			Rectangle targetEntity = new Rectangle(atual.getX()+getMaskx(),atual.getY()+getMasky(),16,16);
			if(lado == 1) {
				if(playerColisionRight.intersects(targetEntity)) {
					
					return true;
				}
			}
			if(lado == 0) {
				if(playerColisionLeft.intersects(targetEntity)) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
	public void render(Graphics g) {
		

		
		if(Game.gameState == "GAME_OVER") {
			if (lado == 1)
				g.drawImage(playerDamageRight,this.getX() - Camera.x,this.getY() - Camera.y, null);
			else
				g.drawImage(playerDamageLeft,this.getX()- Camera.x,this.getY()- Camera.y, null);
			
		}
		else if (!isDamaged) {
			
			
				if (angleXX >= 0) {
					g.drawImage(rightPlayer[index][colorArmor],this.getX() - Camera.x,this.getY() - Camera.y, null);
					g.drawImage(rightPlayerCape[index][colorCape],this.getX() - Camera.x,this.getY() - Camera.y, null);
					lado = 1;
				
					//g.setColor(Color.blue);
					//g.fillRect(this.getX() + maskxRight - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
				
				}
				else if(angleXX < 0) {
					g.drawImage(leftPlayer[index][colorArmor],this.getX()- Camera.x,this.getY()- Camera.y, null);
					g.drawImage(leftPlayerCape[index][colorCape],this.getX()- Camera.x,this.getY()- Camera.y, null);
					lado = 0;	
				
					//g.setColor(Color.blue);
					//g.fillRect(this.getX() + maskxLeft - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
				}
			
			
		} else {
			if (angleXX >= 0) {				
				g.drawImage(playerDamageRight,this.getX() - Camera.x,this.getY() - Camera.y, null);
				lado = 1;
				
				//g.setColor(Color.blue);
				//g.fillRect(this.getX() + maskxRight - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
				
			}
			else if(angleXX < 0) {				
				g.drawImage(playerDamageLeft,this.getX()- Camera.x,this.getY()- Camera.y, null);
				lado = 0;
				
				//g.setColor(Color.blue);
				//g.fillRect(this.getX() + maskxLeft - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
				
			}	
		}
		if (hasGun && Game.gameState != "GAME_OVER") {
			
			if (Game.player.angleXX >= 0 && Game.player.angleYY <= 0) {
				if((Game.player.angleXX+0.1)/((Game.player.angleYY*-1)+0.1) > 2 ) {
					g.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
				}
				else if(((Game.player.angleYY*-1)+1)/(Game.player.angleXX+1) > 2) {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(-90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}else {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(-45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}			
			}
			else if (Game.player.angleXX >= 0 && Game.player.angleYY > 0) {
				if((Game.player.angleXX+0.1)/(Game.player.angleYY+0.1) > 2 ) {
					g.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
				}
				else if((Game.player.angleYY+1)/(Game.player.angleXX+1) > 2) {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(-90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}else {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_RIGHT,this.getX() - Camera.x +2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(-45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}			
			}
			else if (Game.player.angleXX < 0 && Game.player.angleYY > 0) {
				if(((Game.player.angleXX*-1))/(Game.player.angleYY) > 2 ) {
					g.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
				}
				else if((Game.player.angleYY)/(Game.player.angleXX*-1) > 2) {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(-90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}else {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(-45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}			
			}
			else if (Game.player.angleXX < 0 && Game.player.angleYY <= 0) {
				if(((Game.player.angleXX*-1)+0.1)/((Game.player.angleY*-1)+0.1) > 2 ) {
					g.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
				}
				else if(((Game.player.angleYY*-1)+1)/((Game.player.angleXX*-1)+1) > 2) {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(-90),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}else {
					Graphics2D g2 = (Graphics2D)g;
					g2.rotate(Math.toRadians(45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
					g2.drawImage(Entity.CROSSBOW_LEFT,this.getX() - Camera.x -2,this.getY() +2 - Camera.y, null);
					g2.rotate(Math.toRadians(-45),this.getX() - Camera.x + 8,this.getY() - Camera.y + 8);
				}			
			}
			
			
			
		}
		
		
	}
	
	
	
	
}
