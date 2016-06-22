package com.mackin.agalag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mackin.ship.Ship;

public class Space implements Screen 
{
	private AgalagGame game;
	
	// Camera
	private OrthographicCamera gameCamera;
	private Viewport gamePort;
	
	//Box2D
	private World world;
	
	// Debugging
	private Box2DDebugRenderer b2dr;
	
	private Ship player;
	
	public Space(AgalagGame game)
	{
		this.game = game;
		
		gameCamera = new OrthographicCamera();
		gamePort = new FitViewport(AgalagGame.V_WIDTH / AgalagGame.PPM,
								  AgalagGame.V_HEIGHT / AgalagGame.PPM,
								   gameCamera);
		
		world = new World(new Vector2(0,0), true);
		b2dr = new Box2DDebugRenderer();
		
		player = new Ship(world, 0, 0);
						
	}

	@Override
	public void render(float delta) 
	{
		update(delta);
		//eventManager.handleEvents(delta);

		Gdx.gl.glClearColor(.075F, 0, .075F, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(gameCamera.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();
		
		b2dr.render(world, gameCamera.combined);

		
	}
	
	public void update(float delta)
	{
		input(delta);
		
		world.step(1 / 60F, 6, 2);
		
		player.update(delta);
		
		// TODO: Follow the player or no
		//gameCamera.position.x = player.getPositionX();
		gameCamera.position.y = player.getPositionY();
		gameCamera.update();
	}
	
	public void input(float delta)
	{
		player.handleInput(delta);
	}

	@Override
	public void resize(int width, int height) 
	{
		gamePort.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
