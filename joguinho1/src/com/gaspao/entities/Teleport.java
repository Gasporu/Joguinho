package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class Teleport extends Entity {

	private int frames = 0;
	private int maxFrames = 15;
	private int index = 0;
	private int maxIndex = 2;
	
	private int teste = 0;
	
	private BufferedImage[] spritesUp;
	private BufferedImage[] spritesDown;
	
	private boolean up;
	
	public Teleport(int x, int y, int width, int height, BufferedImage sprite, boolean up) {
		super(x, y, width, height, sprite);
		
		this.up = up;
		
		spritesUp = new BufferedImage[3];
		spritesUp[0] = Game.spritesheet.getSprite(64, 112, 16, 16);
		spritesUp[1] = Game.spritesheet.getSprite(80, 112, 16, 16);
		spritesUp[2] = Game.spritesheet.getSprite(96, 112, 16, 16);
		spritesDown = new BufferedImage[3];
		spritesDown[0] = Game.spritesheet.getSprite(64, 128, 16, 16);
		spritesDown[1] = Game.spritesheet.getSprite(80, 128, 16, 16);
		spritesDown[2] = Game.spritesheet.getSprite(96, 128, 16, 16);

	}

	public void tick() {
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
		if (up) {
			g.drawImage(spritesUp[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
		
		else {
			g.drawImage(spritesDown[index],this.getX() - Camera.x,this.getY() - Camera.y,null);

		}
	}
	
	
}
