package com.mackin.agalag.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mackin.agalag.AgalagGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 480;
		config.height = 600;
		//config.resizable = false;
	
		new LwjglApplication(new AgalagGame(), config);
	}
}
