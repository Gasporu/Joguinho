package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.World;

public class Bat extends Enemy {

	private double speed = 0.8;
	
	//private int maxFrames = 15 , maxIndex = 3;
	
	private BufferedImage[] spritesRightBat;
	private BufferedImage[] spritesLeftBat;
	
	private BufferedImage DamagedRight;
	private BufferedImage DamagedLeft;
	
	private boolean isDamaged = false;
	
	private int frames = 0;
	private int maxFrames = 15;
	private int index = 0;
	private int maxIndex = 3;
	
	private int dano = 2; 

	public Bat(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		DamagedRight = Game.spritesheet.getSprite(128, 144, 16, 16);
		DamagedLeft = Game.spritesheet.getSprite(144, 144, 16, 16);
		
		spritesRightBat = new BufferedImage[4];
		spritesRightBat[0] = Game.spritesheet.getSprite(128, 80, 16, 16);
		spritesRightBat[1] = Game.spritesheet.getSprite(128, 96, 16, 16);
		spritesRightBat[2] = Game.spritesheet.getSprite(128, 112, 16, 16);
		spritesRightBat[3] = Game.spritesheet.getSprite(128, 128, 16, 16);
		
		spritesLeftBat = new BufferedImage[4];
		spritesLeftBat[0] = Game.spritesheet.getSprite(144, 80, 16, 16);
		spritesLeftBat[1] = Game.spritesheet.getSprite(144, 96, 16, 16);
		spritesLeftBat[2] = Game.spritesheet.getSprite(144, 112, 16, 16);
		spritesLeftBat[3] = Game.spritesheet.getSprite(144, 128, 16, 16);
		
		
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
		
		if(!isDamaged()) {
			if (dir == right_dir) {
				g.drawImage(spritesRightBat[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			}
			else if(dir == left_dir) {
				g.drawImage(spritesLeftBat[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
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
