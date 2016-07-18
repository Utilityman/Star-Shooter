package com.mackin.sprite.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mackin.agalag.AgalagGame;

public class ProjectileSprite extends Sprite
{
	private AgalagGame game;
	
	public ProjectileSprite(AgalagGame game, Texture texture, float parseFloat, float parseFloat2) 
	{
		super(new TextureRegion(texture));
		this.setScale(.010F / AgalagGame.PPM);
		this.game = game;
	}
	
	public void update(float parseFloat, float parseFloat2)
	{
		this.setPosition(parseFloat - getWidth() / 2, parseFloat2 - getHeight() / 2);	
	}

}
