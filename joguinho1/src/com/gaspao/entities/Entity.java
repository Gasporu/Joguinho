package com.gaspao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite (96, 0, 16, 16);
	public static BufferedImage CROSSBOW_EN = Game.spritesheet.getSprite (112, 0, 16, 16);
	public static BufferedImage ARROW_EN = Game.spritesheet.getSprite (96, 64, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite (128, 0, 16, 16);
	public static BufferedImage BAT_EN = Game.spritesheet.getSprite (128, 64, 16, 16);
	public static BufferedImage CROSSBOW_RIGHT = Game.spritesheet.getSprite (112, 64, 16, 16);
	public static BufferedImage CROSSBOW_LEFT = Game.spritesheet.getSprite (112, 80, 16, 16);
	public static BufferedImage PORTAL_UP = Game.spritesheet.getSprite (96, 112, 16, 16);
	public static BufferedImage PORTAL_DOWN = Game.spritesheet.getSprite (96, 128, 16, 16);
	public static BufferedImage CHEST = Game.spritesheet.getSprite (160, 16, 16, 16);
	public static BufferedImage RING_OF_LIFE = Game.spritesheet.getSprite (192, 0, 16, 16);
	public static BufferedImage RING_OF_BLOOD = Game.spritesheet.getSprite (208, 0, 16, 16);
	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int maskx,masky,mwidth,mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;		
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
		
	}
	
	public void setMask(int maskx,int masky,int mwidth,int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;		
		
	}
	

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getX() {
		return (int)x;
	}

	public int getY() {
		return (int)y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
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

	public int getMwidth() {
		return mwidth;
	}

	public void setMwidth(int mwidth) {
		this.mwidth = mwidth;
	}

	public int getMheight() {
		return mheight;
	}

	public void setMheight(int mheight) {
		this.mheight = mheight;
	}
	
	public void tick() {
		
		
	}
	
	public static boolean isColidding(Entity e1,Entity e2) {
		
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx ,e1.getY() + e1.masky ,e1.mwidth,e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	
	
	public void render(Graphics g) {
		
		g.drawImage(sprite,this.getX() - Camera.x ,this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y, mwidth, height);
	}	
 	
}
