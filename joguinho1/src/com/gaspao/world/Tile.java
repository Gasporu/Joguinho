package com.gaspao.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR1 = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static BufferedImage TILE_FLOOR2 = Game.spritesheet.getSprite(0, 16, 16, 16);
	public static BufferedImage TILE_FLOOR3 = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	public static BufferedImage TILE_WALLDOOR = Game.spritesheet.getSprite(16, 16, 16, 16);
	
	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
	
	
}
