package com.gaspao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class MegaSlime extends Enemy {

private double speed = 0.9;
	
	
	private BufferedImage[] spritesRight;
	private BufferedImage[] spritesLeft;

	private BufferedImage DamagedRight;
	private BufferedImage DamagedLeft;
	
	private boolean isDamaged = false;
	
	private int frames = 0;
	private int maxFrames = 10;
	private int index = 0;
	private int maxIndex = 3;
	
	private int maskx = 8, masky = 12, maskw = 18, maskh = 18;
	
	private int life = 50;
	private int dano = 8; 

	public MegaSlime(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
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

	public void setLife(int life) {
		this.life = life;
	}
	
	public int getLife() {
		return life;
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
	
	
	public void render(Graphics g) {
		
		if (!isDamaged()) {
			if (dir == right_dir) {
				g.drawImage(spritesRight[index],this.getX() - Camera.x,this.getY() - Camera.y,32,32,null);
			}
			else if(dir == left_dir) {
				g.drawImage(spritesLeft[index],this.getX() - Camera.x,this.getY() - Camera.y,32,32,null);
			}
		}
		else {
			if (dir == right_dir) {
				g.drawImage(DamagedRight,this.getX() - Camera.x,this.getY() - Camera.y,32,32,null);
			}
			else if(dir == left_dir) {
				g.drawImage(DamagedLeft,this.getX() - Camera.x,this.getY() - Camera.y,32,32,null);
			}
		}
		
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y,maskw,maskh);
	}
	
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