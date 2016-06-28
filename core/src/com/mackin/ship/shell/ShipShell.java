package com.mackin.ship.shell;

/**
 * This class holds the information to create a ship body and sprite but exists as neither
 * @author jmackin
 *
 */
public class ShipShell 
{
	private float xInit;
	private float yInit;
	private String texture;
	private int aiStyle;
	
	public static ShipShell defineEnemyShip(String texture, float f, float g, int aiStyle) 
	{
		ShipShell shell = new ShipShell();
		shell.xInit = f;
		shell.yInit = g;
		shell.texture = texture;
		shell.aiStyle = aiStyle;
		return shell;
	}

	public Object toJson() {
		return null;
	}

	public float getXInit() 
	{
		return xInit;
	}

	public float getYInit() 
	{
		return yInit;
	}

	public String getShipTexture() 
	{
		return texture;
	}

	public int getAIStyle() 
	{
		return aiStyle;
	}

}
