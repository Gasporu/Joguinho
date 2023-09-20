package com.gaspao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.World;

public class Enemy extends Entity {

	protected double speed = 0.6;
	
	

	private int maskx = 5, masky = 7, maskw = 9, maskh = 9;
	
	public int right_dir = 0, left_dir = 1; 
	public int dir = right_dir;
	
	private int frames = 0;
	private int maxFrames = 18;
	private int index = 0;
	private int maxIndex = 3;
	
	private boolean isDamaged = false;
	private int damageFrames = 8, damageCurrent = 0;
	private int dropItens = 10;
	private Random random = new Random();
	
	private BufferedImage[] spritesRight;
	private BufferedImage[] spritesLeft;

	private BufferedImage DamagedRight;
	private BufferedImage DamagedLeft;
	
	private int life = 3;
	
	protected int dano = 4;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		
		DamagedRight = Game.spritesheet.getSprite(128, 64, 16, 16);
		DamagedLeft = Game.spritesheet.getSprite(144, 64, 16, 16);
		
		spritesRight = new BufferedImage[4];
		spritesRight[0] = Game.spritesheet.getSprite(128, 0, 16, 16);
		spritesRight[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
		spritesRight[2] = Game.spritesheet.getSprite(128, 32, 16, 16);
		spritesRight[3] = Game.spritesheet.getSprite(128, 48, 16, 16);
		spritesLeft = new BufferedImage[4];
		spritesLeft[0] = Game.spritesheet.getSprite(144, 0, 16, 16);
		spritesLeft[1] = Game.spritesheet.getSprite(144, 16, 16, 16);
		spritesLeft[2] = Game.spritesheet.getSprite(144, 32, 16, 16);
		spritesLeft[3] = Game.spritesheet.getSprite(144, 48, 16, 16);
	}

	public void tick() {
		
		if(isColiddingWithPlayer() == false) {
				
			
		
			if(x<Game.player.getX() && World.isFree((int)(x+getSpeed()), this.getY())
					&& !isColidding((int)(x+getSpeed()), this.getY())) {
				dir = right_dir;
				x+=getSpeed();
			}
			if(x>Game.player.getX() && World.isFree((int)(x-getSpeed()), this.getY())
					&& !isColidding((int)(x-getSpeed()), this.getY())) {
				dir = left_dir;
				x-=getSpeed();
			}
		
			if(y<Game.player.getY() && World.isFree(this.getX(),(int)(y+getSpeed()))
					&& !isColidding(this.getX(),(int)(y+getSpeed()))) {
				y+=getSpeed();
			}
			if(y>Game.player.getY() && World.isFree(this.getX(),(int)(y-getSpeed()))
					&& !isColidding(this.getX(),(int)(y-getSpeed()))) {
				y-=getSpeed();
			}
		} else {
			// ta colidindo com player
			if(Game.rand.nextInt(100) < 10) {
				dandoDano();
				
				
			}
			
		}
		
		tickAnimation();
		
			
		collidingBullet();
		
				
		if(life <=0) {
			selfDestroy();
			return;
		}	
		if (isDamaged()) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				boolean Damaged = false;	
				setIsDamaged(Damaged);
			}
		}
				
	}
	
	
	
	public void setIsDamaged(boolean Damaged) {
		this.isDamaged = Damaged;
	}

	public boolean isDamaged() {
		return isDamaged;
	}

	public void tickAnimation() {
		
		frames++;
		
		if(frames >= maxFrames) {
			
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	
	
	public void selfDestroy() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
		
		int opcaoEscolhida = random.nextInt(dropItens);
		if (opcaoEscolhida == 0) {
			Lifepack pack = new Lifepack((int)x,(int)y,16,16,Entity.LIFEPACK_EN);
			Game.entities.add(pack);
		}
		if (opcaoEscolhida == 1 || opcaoEscolhida == 2 || opcaoEscolhida == 3) {
			Game.entities.add(new Arrow((int)x,(int)y,16,16,Entity.ARROW_EN));
		}
		else {
			// nada
		}
	}
	
	public void collidingBullet() {
		for(int i=0; i<Game.arrows.size(); i++) {
			Entity e = Game.arrows.get(i);			
				if(Entity.isColidding(this, e)) {
					
					
					boolean Damaged = true;	
					setIsDamaged(Damaged);
					life -= Game.player.damage;
					Game.arrows.remove(i);
					return;
				
			}
		}
		
	}
	
	
	public double getSpeed() {
		return speed;
	}

	public double dandoDano() {
		Game.player.life=Game.player.life-this.dano;
		Game.player.isDamaged = true;
		return Game.player.life;
	}
	
	
	public boolean isColiddingWithPlayer() {
		
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		
		return enemyCurrent.intersects(player);
	}
	
	
	public boolean isColidding(int xnext, int ynext) {
		
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		
		for(int i=0; i<Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx,e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				
				return true;
			}
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		
		if (!isDamaged()) {
			if (dir == right_dir) {
				g.drawImage(spritesRight[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
			else if(dir == left_dir) {
				g.drawImage(spritesLeft[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
		}
		else {
			if (dir == right_dir) {
				g.drawImage(DamagedRight,this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
			else if(dir == left_dir) {
				g.drawImage(DamagedLeft,this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
		}
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
	}
	
	
}
