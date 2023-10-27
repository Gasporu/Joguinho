package com.gaspao.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import com.gaspao.entities.*;

import com.gaspao.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 16;
	
	private int i = 0;
	private int numeroChao = 6;
	private int numeroParede = 1;
	private int numeroInimigo = 3;
	
	public int cordenadaPortalUpX = 0;
	public int cordenadaPortalUpY = 0;
	public int cordenadaPortalDownX = 0;
	public int cordenadaPortalDownY = 0;

	
	public World(String path) {
		Random random = new Random();
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth()*map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0,map.getWidth(),map.getHeight(),pixels, 0,map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++) {

				for(int yy = 0; yy<map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					
					
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR1);
					
					if(pixelAtual == 0xFF000000) {
							// chao (pixel preto)
						
						int opcaoEscolhida = random.nextInt(numeroChao);
						if(opcaoEscolhida == 0 || opcaoEscolhida == 1 || opcaoEscolhida == 2)
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR1);
						else if(opcaoEscolhida == 3 || opcaoEscolhida == 4)
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR2);
						else if (opcaoEscolhida == 5)
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR3);
						
					}
					else if(pixelAtual == 0xFF4CFF00) {
						// chao (pixel verde claro)
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR3);
				    }
					else if(pixelAtual == 0xFF267F00) {
						// chao (pixel verde escuro)
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR2);
				    }
					if(pixelAtual == 0xFF808080) {
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR1);
					}
					else if (pixelAtual == 0xFFFFFFFF) {
							// parede (pixel branco)
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
					}
					else if(pixelAtual == 0xFF6B6B6B) {
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALLDOOR);
					}
					else if (pixelAtual == 0xFF0026FF) {
						// player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}
					else if(pixelAtual == 0xFFFF0000) {
						// inimigo
						
						int opcaoEscolhida = random.nextInt(numeroInimigo);
						if(opcaoEscolhida == 0 || opcaoEscolhida == 1) {
							Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
							Game.entities.add(en);
							Game.enemies.add(en);
						}
						else if (opcaoEscolhida == 2) {
							Bat bat = new Bat(xx*16,yy*16,16,16,Entity.BAT_EN);
							Game.entities.add(bat);
							Game.enemies.add(bat);
						}

					}
					else if(pixelAtual == 0xFFB200FF) {
						Bat bat = new Bat(xx*16,yy*16,16,16,Entity.BAT_EN);
						Game.entities.add(bat);
						Game.enemies.add(bat);
					}
					else if(pixelAtual == 0xFFFF6A00) {
						// Crossbow
						if(!Game.gun)
						Game.entities.add(new Crossbow(xx*16,yy*16,16,16,Entity.CROSSBOW_EN));
						else
							Game.entities.add(new Arrow(xx*16,yy*16,16,16,Entity.ARROW_EN));
					}
					
					else if(pixelAtual == 0xFFFF7F7F) {
						// vidinha
						Lifepack pack = new Lifepack(xx*16,yy*16,16,16,Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					}
					else if(pixelAtual == 0xFFFFD800) {
						// flecha
						Game.entities.add(new Arrow(xx*16,yy*16,16,16,Entity.ARROW_EN));
					}
					else if(pixelAtual == 0xFF00FFFF) {
						// parte cima portal
						cordenadaPortalUpX = xx*16;
						cordenadaPortalUpY = yy*16;
						
					}
					else if(pixelAtual == 0xFF0084FF) {
						// parte baixo portal
						cordenadaPortalDownX = xx*16;
						cordenadaPortalDownY = yy*16;
						
					}
					if(pixelAtual == 0xFF7A3500) {
						// bau
						Chest chest = new Chest(xx*16,yy*16,16,16,Entity.CHEST);
						Game.entities.add(chest);
						
					}
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isFree(int xnext, int ynext) {
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				 (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile)    );
	}
	
	public static void restartGame(String level) {
		Game.entities.clear();
		Game.arrows.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		//Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32,0,16,16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
		return;
	}
	
	
	public void render(Graphics g) {
		
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		
		for (int xx = xstart; xx <= xfinal ; xx++) {
			for (int yy = ystart; yy <= yfinal  ; yy++) {
				if (xx<0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
	
}
