package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class Lifepack extends Entity {

	private int frames = 0;
	private int maxFrames = 15;
	private int index = 0;
	private int maxIndex = 3;

	private BufferedImage[] lifePackAnimation;
	
	
	
	public Lifepack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		lifePackAnimation = new BufferedImage[4];

		lifePackAnimation[0] = Game.spritesheet.getSprite(96, 0, 16, 16);
		lifePackAnimation[1] = Game.spritesheet.getSprite(96, 0, 16, 16);
		lifePackAnimation[2] = Game.spritesheet.getSprite(96, 16, 16, 16);
		lifePackAnimation[3] = Game.spritesheet.getSprite(96, 32, 16, 16);
		
		
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
		
		g.drawImage(lifePackAnimation[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		
	}
	

}
