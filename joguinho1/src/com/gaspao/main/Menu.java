package com.gaspao.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Menu {

	public String[] options = {"Novo jogo","Carregar jogo","Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	private BufferedImage imagem;
	
	
	public boolean up;
	public boolean down;
	public boolean enter;
	
	public boolean pause = false;
	
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
			if(options[currentOption] == "Novo jogo" || options[currentOption] == "continuar" ) {
				Game.gameState = "CHARACTER_CREATION";
				pause = false;
				enter = false;
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		imagem = Game.menuImage.getSprite(0, 0, 960, 650);
		
		if (pause == true) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		}
		
		else {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.drawImage(imagem,0 ,0, null);
		}
		
		//titulo
		g.setColor(Color.white);
		g.setFont(new Font("arial",Font.BOLD,50));
		g.drawString("Joguinho", (Game.WIDTH*Game.SCALE)/2-106 , (Game.HEIGHT*Game.SCALE)/2-130);
		
		// opçoes do menu
		g.setFont(new Font("arial",Font.BOLD,30));
		if(pause == false)
		g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE)/2-66 , (Game.HEIGHT*Game.SCALE)/2-30);
		else
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE)/2-60 , (Game.HEIGHT*Game.SCALE)/2-30);
		
		g.setFont(new Font("arial",Font.BOLD,30));
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE)/2-92 , (Game.HEIGHT*Game.SCALE)/2+40);
		
		g.setFont(new Font("arial",Font.BOLD,30));
		g.drawString("Sair", (Game.WIDTH*Game.SCALE)/2-15 , (Game.HEIGHT*Game.SCALE)/2+110);
		
		if(options[currentOption] == "Novo jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-95 , (Game.HEIGHT*Game.SCALE)/2-30);
		}else if(options[currentOption] == "Carregar jogo") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-120 , (Game.HEIGHT*Game.SCALE)/2+40);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">", (Game.WIDTH*Game.SCALE)/2-40 , (Game.HEIGHT*Game.SCALE)/2+110);
		}
		
	}
	
}
