package com.mackin.agalag;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mackin.ship.Ship;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Space implements Screen 
{
	private final float UPDATE_TIME = 1/60f;
	float timer;
	
	private AgalagGame game;
	
	// Camera
	private OrthographicCamera gameCamera;
	private Viewport gamePort;
	
	//Box2D
	private World world;
	
	// Debugging
	private Box2DDebugRenderer b2dr;
	
	private Ship player;
	private Socket socket;
	
	private Texture shipTexture;
	private Texture friendlyShipTexture;
	
	private HashMap<String, Ship> friendlyPlayers;
	
	public Space(AgalagGame game, Socket socket)
	{
		this.game = game;
		this.socket = socket;

		shipTexture = new Texture("ship.png");
		friendlyShipTexture = new Texture("ship.png");
		friendlyPlayers = new HashMap<String, Ship>();
		
		configSocketEvents();

		
		gameCamera = new OrthographicCamera();
		gamePort = new FitViewport(AgalagGame.V_WIDTH / AgalagGame.PPM,
								  AgalagGame.V_HEIGHT / AgalagGame.PPM,
								   gameCamera);
		
		world = new World(new Vector2(0,0), true);
		b2dr = new Box2DDebugRenderer();
		
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(.075F, 0, .075F, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(player != null)
		{
			update(delta);
	
			game.batch.setProjectionMatrix(gameCamera.combined);
			game.batch.begin();
				player.draw(game.batch);
			
			for(Entry<String, Ship> entry: friendlyPlayers.entrySet())
			{
				entry.getValue().draw(game.batch);
			}
			
			game.batch.end();
			
			b2dr.render(world, gameCamera.combined);

		}
	}
	
	public void update(float delta)
	{
		timer += delta;
		if(timer >= UPDATE_TIME && player.hasMoved())
		{
			JSONObject data = new JSONObject();
			try
			{
				data.put("x", player.getPositionX());
				data.put("y", player.getPositionY());
				socket.emit("playerMoved", data);
			}catch(JSONException e)
			{
				Gdx.app.log("Socket.io", "Errorsending update data");
			}
		}
		input(delta);
		
		world.step(1 / 60F, 6, 2);
		
		player.update(delta);
		for(Entry<String, Ship> entry: friendlyPlayers.entrySet())
		{
			entry.getValue().update(delta);
		}		
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
	public void dispose() 
	{
		
	}
	
	public void configSocketEvents()
	{
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() 
		{	
			@Override
			public void call(Object... arg0) 
			{
				Gdx.app.log("SocketIO", "Connected");
				player = new Ship(world, shipTexture, 0, 0);

			}
		}).on("socketID",  new Emitter.Listener() {
			
			@Override
			public void call(Object... args) 
			{
				JSONObject data = (JSONObject) args[0];
				try
				{
				String id = data.getString("id");
				Gdx.app.log("SocketIO",  "My ID: " + id);
				}catch(JSONException e)
				{
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer",  new Emitter.Listener() {
			
			@Override
			public void call(Object... args) 
			{
				JSONObject data = (JSONObject) args[0];
				try
				{
				String id = data.getString("id");
				Gdx.app.log("SocketIO",  "New Player Connected: " + id);
				friendlyPlayers.put(id, new Ship(world, friendlyShipTexture, -1,0));
				}catch(JSONException e)
				{
					Gdx.app.log("SocketIO", "Error getting new Player ID");
				}
			}
		}).on("playerDisconnected",  new Emitter.Listener() {
			
			@Override
			public void call(Object... args) 
			{
				JSONObject data = (JSONObject) args[0];
				try
				{
					String id = data.getString("id");
					Ship removedShip = friendlyPlayers.remove(id);
					removedShip.remove();
				}catch(JSONException e)
				{
					Gdx.app.log("SocketIO", "Error getting new Player ID");
				}
			}
		}).on("getPlayers",  new Emitter.Listener() {
			
			@Override
			public void call(Object... args) 
			{
				JSONArray objects = (JSONArray) args[0];
				try
				{
					for(int i = 0; i < objects.length(); i++)
					{
						Ship coopPlayer = new Ship(world, friendlyShipTexture, 0, 0);
						Vector2 position = new Vector2();
						position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
						position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
						coopPlayer.setBodyPosition(position.x, position.y);
						
						friendlyPlayers.put(objects.getJSONObject(i).getString("id"), coopPlayer);
					}
				}catch(JSONException e)
				{
					System.out.println(e);
				}
			}
		}).on("playerMoved",  new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try
				{
					String playerID = data.getString("id");
					Double x = data.getDouble("x");
					Double y = data.getDouble("y");
					if(friendlyPlayers.get(playerID) != null)
					{
						friendlyPlayers.get(playerID).setBodyPosition(x.floatValue(), y.floatValue());
					}
				}catch(JSONException e)
				{
					System.out.println(e);
				}
			}
		});
	}

}
