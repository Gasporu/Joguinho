package com.gaspao.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class KingSlime extends Slime {


	private int maskx = -36, masky = -28, maskw = 38, maskh = 38;
	
	private double speed = 0.6;
	
	private BufferedImage[] spritesRightSlimeKing;
	private BufferedImage[] spritesLeftSlimeKing;
	
	private BufferedImage DamagedRight;
	private BufferedImage DamagedLeft;
	
	private BufferedImage[] spriteSlimeKingDying;
	private BufferedImage[] spriteRightMegaSlimeBorn;
	private BufferedImage[] spriteLeftMegaSlimeBorn;
	
	private boolean isDamaged = false;
	
	private int frames = 0;
	private int maxFrames = 15;
	private int index = 0;
	private int maxIndex = 3;
	
	private int dyingframes = 0;
	private int dyingmaxFrames = 15;
	private int dyingmaxIndex = 10;
	
	private double life = 150;
	private double maxLife = 150;
	
	private int dano = 25; 
	
	
	
	public KingSlime(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		DamagedRight = Game.spritesheet.getSprite(128, 192, 16, 16);
		DamagedLeft = Game.spritesheet.getSprite(144, 192, 16, 16);
		
		spritesRightSlimeKing = new BufferedImage[4];
		spritesLeftSlimeKing = new BufferedImage[4];
		
		spriteSlimeKingDying = new BufferedImage[11];
		spriteRightMegaSlimeBorn = new BufferedImage[11];
		spriteLeftMegaSlimeBorn = new BufferedImage[11];
		
		for(int i=0 ; i<4 ; i++) {
			spritesRightSlimeKing[i] = Game.spritesheet.getSprite(128, 208 + (i*16), 16, 16);
			spritesLeftSlimeKing[i] = Game.spritesheet.getSprite(144, 208 + (i*16), 16, 16);
		}
		for(int i=0 ; i<11 ; i++) {
			spriteSlimeKingDying[i] = Game.spritesheet.getSprite(192, 144 + (i*16), 16, 16);
			spriteRightMegaSlimeBorn[i] = Game.spritesheet.getSprite(208, 144 + (i*16), 16, 16);
			spriteLeftMegaSlimeBorn[i] = Game.spritesheet.getSprite(176, 144 + (i*16), 16, 16);
		}
		
		
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getLife() {
		return (int) life;
	}
	
	public double getSpeed() {
		return this.speed;
	}
	
	public double dandoDano() {
		Game.player.life=Game.player.life-this.dano;
		Game.player.isDamaged = true;
		return Game.player.life;
	}
	
	public void setIsDamaged(boolean Damaged) {
		this.isDamaged = Damaged;
	}

	public boolean isDamaged() {
		return isDamaged;
	}

	public void tickAnimation() {
		frames++;
		
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	public void tickDyingAnimation() {
		dyingframes++;
		
		if(dyingframes == dyingmaxFrames) {
			dyingframes = 0;
			dyingAnimationIdex++;
			if(dyingAnimationIdex > dyingmaxIndex) {
				dyingAnimationIdex = 0;
				die = true;
			}
		}
	}
	
	public void selfDestroy() {
		
		dyingAnimation = true;
		
		if(die) {
			
			MegaSlime RightMegaSlime = new MegaSlime((int)x + 16,(int)y - 16,32,32,Entity.ENEMY_EN);
			Game.entities.add(RightMegaSlime);
			Game.enemies.add(RightMegaSlime);
			
			MegaSlime LeftMegaSlime = new MegaSlime((int)x - 80,(int)y - 16,32,32,Entity.ENEMY_EN);
			Game.entities.add(LeftMegaSlime);
			Game.enemies.add(LeftMegaSlime);
			
			Game.entities.remove(this);
			Game.enemies.remove(this);
			
		}
	}

	
	
	public void render(Graphics g) {
		
		
		if(!isDamaged() && dyingAnimation == false ) {
			if (dir == right_dir) {
				g.drawImage(spritesRightSlimeKing[index],this.getX() - Camera.x - 48,this.getY() - 48 - Camera.y, 64,64 ,null);
			}
			else if(dir == left_dir) {
				g.drawImage(spritesLeftSlimeKing[index],this.getX() - Camera.x - 48,this.getY() - 48 - Camera.y, 64,64 ,null);
			}
		}
		else if (isDamaged() && dyingAnimation == false) {
			if (dir == right_dir) {
				g.drawImage(DamagedRight,this.getX() - Camera.x - 48,this.getY() - Camera.y - 48, 64,64 ,null);
			}
			else if(dir == left_dir) {
				g.drawImage(DamagedLeft,this.getX() - Camera.x - 48,this.getY() - Camera.y - 48, 64,64 ,null);
			}
		}
		else if(dyingAnimation == true) {
			
			g.drawImage(spriteSlimeKingDying[dyingAnimationIdex],this.getX() - Camera.x - 48,this.getY() - 48 - Camera.y, 64,64 ,null);
			g.drawImage(spriteRightMegaSlimeBorn[dyingAnimationIdex],this.getX() - Camera.x + 16,this.getY() - 16 - Camera.y, 32,32 ,null);
			g.drawImage(spriteLeftMegaSlimeBorn[dyingAnimationIdex],this.getX() - Camera.x - 80,this.getY() - 16 - Camera.y, 32,32 ,null);
			tickDyingAnimation();
		}
	
		g.setColor(Color.black);
		g.fillRect(7,136,223,10);
		
		g.setColor(Color.red);
		g.fillRect(8,137,221,8);
		
		g.setColor(Color.green);
		g.fillRect(8,137,(int)((life/maxLife)*221) ,8);
		
		g.setColor(Color.black);
		g.setFont(new Font("arial",Font.BOLD,8));
		g.drawString((int)life+"/"+(int)maxLife,111,144);
		
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,8));
		g.drawString((int)life+"/"+(int)maxLife,110,144);
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
		
		
	}
	
	//
	public int getMaskx() {
		return maskx;
	}

	public void setMaskx(int maskx) {
		this.maskx = maskx;
	}

	public int getMasky() {
		return masky;
	}

	public void setMasky(int masky) {
		this.masky = masky;
	}

	public int getMaskw() {
		return maskw;
	}

	public void setMaskw(int maskw) {
		this.maskw = maskw;
	}

	public int getMaskh() {
		return maskh;
	}

	public void setMaskh(int maskh) {
		this.maskh = maskh;
	}
	
}
