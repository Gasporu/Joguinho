package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gaspao.graficos.Spritesheet;
import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.World;

public class Player extends Entity {

	public boolean right,up,left,down;
	public int right_dir = 0, left_dir = 1; 
	public int dir = right_dir;
	public double speed = 1.4;
	
	private int frames = 0, maxFrames = 5,index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
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
	public int mx = 0,my =0;
	public double angleX, angleY;
	public double angleXX, angleYY;
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
	
		playerDamageRight = Game.spritesheet.getSprite(32, 32, 16, 16);
		playerDamageLeft = Game.spritesheet.getSprite(48, 32, 16, 16);
		
		for(int i=0 ; i<4 ; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		
		for(int i=0 ; i<4 ; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	}

	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			
			x-=speed;
		}
		if (up&& World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			y-=speed;
		}
		else if (down&& World.isFree(this.getX(),(int)(y+speed))) {
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
					if (life > 100)
						life = 100;
					Game.entities.remove(atual);
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
	
	
	public void render(Graphics g) {
		
		int lado = 1;
		
		if(Game.gameState == "GAME_OVER") {
			if (lado == 1)
				g.drawImage(playerDamageRight,this.getX() - Camera.x,this.getY() - Camera.y, null);
			else
				g.drawImage(playerDamageLeft,this.getX()- Camera.x,this.getY()- Camera.y, null);
			
		}
		else if (!isDamaged) {
			
			
			if (angleXX >= 0) {
				g.drawImage(rightPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y, null);
				lado = 1;
			}
			else if(angleXX < 0) {
				g.drawImage(leftPlayer[index],this.getX()- Camera.x,this.getY()- Camera.y, null);
				lado = 0;
			}
		} else {
			if (angleXX >= 0) {
				g.drawImage(playerDamageRight,this.getX() - Camera.x,this.getY() - Camera.y, null);
				lado = 1;
			}
			else if(angleXX < 0) {
				g.drawImage(playerDamageLeft,this.getX()- Camera.x,this.getY()- Camera.y, null);
				lado = 0;
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
