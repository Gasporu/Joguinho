package com.gaspao.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.World;





public class ArrowShoot extends Entity {

	private double dx;
	private double dy;
	private double spd = 4;
	private int testArrow = 0;
	private int testArrow2 = 0;
	
	
	private BufferedImage rightArrow;
	private BufferedImage leftArrow;
	private BufferedImage upArrow;
	private BufferedImage downArrow;
	private BufferedImage rightUpArrow;
	private BufferedImage leftUpArrow;
	private BufferedImage rightDownArrow;
	private BufferedImage leftDownArrow;
	
	private int life = 0, curLife = 45;
	private int contador = 0, contadorMax = 30;
	private int contador2 = 0;
	
	public ArrowShoot(int x, int y, int width, int height, BufferedImage sprite, double dx,double dy) {
		super(x, y, width, height, sprite);
		
		rightArrow = Game.spritesheet.getSprite(96, 80, 16, 16);
		leftArrow = Game.spritesheet.getSprite(96, 96, 16, 16);
		
		upArrow = Game.spritesheet.getSprite(80, 96, 16, 16);
		downArrow = Game.spritesheet.getSprite(80, 80, 16, 16);
		
		rightUpArrow = Game.spritesheet.getSprite(80, 64, 16, 16);
		leftUpArrow = Game.spritesheet.getSprite(80, 48, 16, 16);
		
		rightDownArrow = Game.spritesheet.getSprite(96, 48, 16, 16);
		leftDownArrow = Game.spritesheet.getSprite(96, 64, 16, 16);
		
		
		this.dx = dx;
		this.dy = dy;
		
	}
	

	public void tick() {
		if (World.isFree(this.getX(),this.getY())) {
			
			x+=dx*spd;
			y+=dy*spd;
			curLife--;
		}
		else {
			
			if (contador2 == 0) {
				x+=dx*spd;
				y+=dy*spd;
				curLife--;
				contador2++;
			}
				
			contador ++;
			if(contador == contadorMax) {
				contador = 0;
				curLife--;
				}
			}
		
		if (curLife == life) {
			Game.arrows.remove(this);
			return;
		}
	}
	
	
	public void render(Graphics g) {
		
		if (((Game.player.angleX >= 0 && Game.player.angleY <= 0) || testArrow == 1) && testArrow!=2 && testArrow!=3 && testArrow!=4 ) {
			// direita cima
			testArrow = 1;
			if((((Game.player.angleX+0.1)/((Game.player.angleY*-1)+0.1) > 2) || (testArrow2 == 1)) && testArrow2!=2 && testArrow2!=3) {
				testArrow2 = 1;
				g.drawImage(rightArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}
			else if(((((Game.player.angleY*-1)+1)/(Game.player.angleX+1) > 2) || (testArrow2 == 2)) && testArrow2!=1 && testArrow2!=3) {
				testArrow2 = 2;
				g.drawImage(upArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}else {
				testArrow2 = 3;
				g.drawImage(rightUpArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}			
		}
		else if (((Game.player.angleX >= 0 && Game.player.angleY > 0) || testArrow == 2) && testArrow!=1 && testArrow!=3 && testArrow!=4) {
			// direita baixo
			testArrow = 2;
			if((((Game.player.angleX+0.1)/(Game.player.angleY+0.1) > 2) || (testArrow2 == 1)) && testArrow2!=2 && testArrow2!=3) {
				testArrow2 = 1;
				g.drawImage(rightArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}
			else if((((Game.player.angleY+1)/(Game.player.angleX+1) > 2) || (testArrow2 == 2)) && testArrow2!=1 && testArrow2!=3) {
				testArrow2 = 2;
				g.drawImage(downArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}else {
				testArrow2 = 3;
				g.drawImage(rightDownArrow,this.getX() - Camera.x,this.getY()- Camera.y, null);
				return;
			}			
		}
		else if (((Game.player.angleX < 0 && Game.player.angleY > 0) || testArrow == 3) && testArrow!=2 && testArrow!=1 && testArrow!=4) {
			//esquerda baixo
			testArrow = 3;
			if(((((Game.player.angleX*-1))/(Game.player.angleY) > 2) || (testArrow2 == 1)) && testArrow2!=2 && testArrow2!=3) {
				testArrow2 = 1;
				g.drawImage(leftArrow,this.getX() - Camera.x,this.getY() - Camera.y, null);
				return;
			}
			else if((((Game.player.angleY)/(Game.player.angleX*-1)) > 2 || (testArrow2 == 2)) && testArrow2!=1 && testArrow2!=3) {
				testArrow2 = 2;
				g.drawImage(downArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}else {
				testArrow2 = 3;
				g.drawImage(leftDownArrow,this.getX() - Camera.x,this.getY() - Camera.y, null);
				return;
			}			
		}
		else if (((Game.player.angleX < 0 && Game.player.angleY <= 0) || testArrow == 4) && testArrow!=2 && testArrow!=3 && testArrow!=1) {
			// esquerda cima
			testArrow = 4;
			if(((((Game.player.angleX*-1)+0.1)/((Game.player.angleY*-1)+0.1) > 2) || (testArrow2 == 1)) && testArrow2!=2 && testArrow2!=3) {
				testArrow2 = 1;
				g.drawImage(leftArrow,this.getX() - Camera.x,this.getY() - Camera.y, null);
				return;
			}
			else if(((((Game.player.angleY*-1)+1)/((Game.player.angleX*-1)+1) > 2) || (testArrow2 == 2)) && testArrow2!=1 && testArrow2!=3) {
				testArrow2 = 2;
				g.drawImage(upArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);
				return;
			}else {
				testArrow2 = 3;
				g.drawImage(leftUpArrow,this.getX() - Camera.x ,this.getY()  - Camera.y, null);			
				return;
			}			
		}
		
		
		
		//g.setColor(Color.WHITE);
		//g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
		
	}
	
	
}
