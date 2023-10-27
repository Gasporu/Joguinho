package com.gaspao.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.gaspao.world.Camera;

public class CharacterCreation {

	public String[] options = {"Armadura","Capa","Começar jogo","Voltar"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	private BufferedImage[] playerBody;
	private BufferedImage[] playerCape;
	
	public int bodyNumber = 0;
	public int capeNumber = 0;
	public int maxBodyNumber = 3;
	public int maxCapeNumber = 3;
	
	public boolean up;
	public boolean down;
	public boolean enter;
	public boolean right;
	public boolean left;
	
	private BufferedImage imagem;
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if (enter) {
			if(options[currentOption] == "Começar jogo") {
				Game.gameState = "NORMAL";
				enter = false;
			}else if(options[currentOption] == "Voltar") {
				Game.gameState = "MENU";
				enter = false;
			}
		}
		if (right) {
			if(options[currentOption] == "Armadura") {
				bodyNumber++ ;
				if(bodyNumber>maxBodyNumber)
					bodyNumber = 0;
				right = false;
				Game.player.colorArmor = bodyNumber ;
			}
			else if(options[currentOption] == "Capa") {
				capeNumber++ ;
				if(capeNumber>maxCapeNumber)
					capeNumber = 0;
				right = false;
				Game.player.colorCape = capeNumber;
			}
		}
		
		if (left) {
			if(options[currentOption] == "Armadura") {
				bodyNumber-- ;
				if(bodyNumber<0)
					bodyNumber = maxBodyNumber;
				left = false;
				Game.player.colorArmor = bodyNumber ;
			}
			else if(options[currentOption] == "Capa") {
				capeNumber-- ;
				if(capeNumber<0)
					capeNumber = maxCapeNumber;
				left = false;
				Game.player.colorCape = capeNumber;
			}
		}
	}
	
	public void render(Graphics g) {
		
		playerBody = new BufferedImage[4];
		playerCape = new BufferedImage[4];
		
		for(int i=0 ; i<4 ; i++) {
			playerBody[i] = Game.spritesheet.getSprite(256, 0 + (i*32), 16, 16);
		}
		for(int i=0 ; i<4 ; i++) {
			playerCape[i] = Game.spritesheet.getSprite(256, 128 + (i*32), 16, 16);
		}
		
		imagem = Game.backGroundImage.getSprite(0, 0, 1920, 948);
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.drawImage(imagem,0 ,0,1150,670,null);
		
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,150));
			g.fillRect(640, 250, 260, 270);
		
			//titulo
			g.setColor(Color.white);
			g.setFont(new Font("arial",Font.BOLD,50));
			g.drawString("Criação de personagem", (Game.WIDTH*Game.SCALE)/2-260 , (Game.HEIGHT*Game.SCALE)/2-160);
				
			// opçoes
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("armadura", (Game.WIDTH*Game.SCALE)/2+240 , (Game.HEIGHT*Game.SCALE)/2-30);
				
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("capa", (Game.WIDTH*Game.SCALE)/2+305 , (Game.HEIGHT*Game.SCALE)/2+40);
				
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("começar jogo", (Game.WIDTH*Game.SCALE)/2+185 , (Game.HEIGHT*Game.SCALE)/2+110);
				
			g.setFont(new Font("arial",Font.BOLD,30));
			g.drawString("voltar", (Game.WIDTH*Game.SCALE)/2+300 , (Game.HEIGHT*Game.SCALE)/2+170);
				
				if(options[currentOption] == "Armadura") {
					g.drawString("<                    >", (Game.WIDTH*Game.SCALE)/2+210 , (Game.HEIGHT*Game.SCALE)/2-30);
				}else if(options[currentOption] == "Capa") {
					g.drawString("<           >", (Game.WIDTH*Game.SCALE)/2+280 , (Game.HEIGHT*Game.SCALE)/2+40);
				}else if(options[currentOption] == "Começar jogo") {
					g.drawString(">", (Game.WIDTH*Game.SCALE)/2+160 , (Game.HEIGHT*Game.SCALE)/2+110);
				}else if(options[currentOption] == "Voltar") {
					g.drawString(">", (Game.WIDTH*Game.SCALE)/2+270 , (Game.HEIGHT*Game.SCALE)/2+170);
				}
				
				g.drawImage(playerBody[bodyNumber],(Game.WIDTH*Game.SCALE)/2-220, 310 , 192 , 192, null);
				g.drawImage(playerCape[capeNumber],(Game.WIDTH*Game.SCALE)/2-220, 310 , 192 , 192, null);
	}
}
