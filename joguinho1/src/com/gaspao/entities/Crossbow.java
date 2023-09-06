package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class Crossbow extends Entity {

	private int frames = 0;
	private int maxFrames = 30;
	private int index = 0;
	private int maxIndex = 3;

	private BufferedImage[] crossbowAnimation;

	public Crossbow(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		crossbowAnimation = new BufferedImage[4];
		crossbowAnimation[0] = Game.spritesheet.getSprite(112, 0, 16, 16);
		crossbowAnimation[1] = Game.spritesheet.getSprite(112, 16, 16, 16);
		crossbowAnimation[2] = Game.spritesheet.getSprite(112, 32, 16, 16);
		crossbowAnimation[3] = Game.spritesheet.getSprite(112, 48, 16, 16);
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
		
		g.drawImage(crossbowAnimation[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		
	}
	
}
