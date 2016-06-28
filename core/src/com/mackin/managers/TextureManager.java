package com.mackin.managers;

import com.badlogic.gdx.graphics.Texture;

// Preeeettty jank right now
public class TextureManager 
{
	private Texture shipTexture;
	private Texture friendlyShipTexture;
	
	public TextureManager()
	{
		shipTexture = new Texture("ship.png");
		friendlyShipTexture = new Texture("ship.png");
	}
	
	
	public Texture get(String string) 
	{
		return shipTexture;
	}

}
