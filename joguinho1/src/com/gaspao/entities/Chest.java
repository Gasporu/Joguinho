package com.gaspao.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gaspao.main.Game;
import com.gaspao.world.Camera;
import com.gaspao.world.WallTile;
import com.gaspao.world.World;

public class Chest extends Entity {
	
	public static boolean open = false;
	
	private int dropItens = 2;
	private Random random = new Random();
	
	private BufferedImage chestClosed;
	private BufferedImage chestOpen;
	private BufferedImage OpenLid;
	
	private boolean safeLock = true;
	
	
	

	public Chest(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		chestClosed = Game.spritesheet.getSprite(160, 16, 16, 16);
		chestOpen = Game.spritesheet.getSprite(176, 16, 16, 16);
		OpenLid = Game.spritesheet.getSprite(176, 0, 16, 16);
		
		
	}
	
	public void tick() {
		if(open && safeLock) {
			safeLock = false;
			int opcaoEscolhida = random.nextInt(dropItens);
			if (opcaoEscolhida == 0) {
				Ring_Life ring_life = new Ring_Life((int)x,(int)y-32,16,16,Entity.RING_OF_LIFE);
				Game.entities.add(ring_life);
				//System.out.println("Ring of Life");
			}
			if (opcaoEscolhida == 1) {
				Ring_Blood ring_Blood = new Ring_Blood((int)x,(int)y-32,16,16,Entity.RING_OF_BLOOD);
				Game.entities.add(ring_Blood);
				//System.out.println("Ring of Blood");
			}
		}
	} 
	
	public void render(Graphics g) {
		
		
		if (open) {
			g.drawImage(OpenLid,this.getX() - Camera.x,this.getY()-16 - Camera.y,null);
			g.drawImage(chestOpen,this.getX() - Camera.x,this.getY() - Camera.y,null);
		}		
		else{
			g.drawImage(chestClosed,this.getX() - Camera.x,this.getY() - Camera.y,null);

		}
	}
	
	
	
}
