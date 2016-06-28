package com.mackin.agalag;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mackin.enemypatterns.DefaultPattern;
import com.mackin.formation.AttackFormation;
import com.mackin.managers.TextureManager;
import com.mackin.ship.EnemyShip;
import com.mackin.ship.PlayerShip;

/**
 * TODO: Move the world to the server D:
 * @author jmackin
 *
 */
public class Space implements Screen, Disposable
{	
	private AgalagGame game;
	private TextureManager textures;
	
	// Camera
	private OrthographicCamera gameCamera;
	private Viewport gamePort;
	
	//Box2D
	//private World world;
	
	// Debugging
	//private Box2DDebugRenderer b2dr;
	
	// Player
	private PlayerShip player;
	private Socket socket;
	private String id;
	
	// Networking
	private HashMap<String, PlayerShip> friendlyPlayers;
	private HashMap<String, EnemyShip> enemyUnits;
	private ServerReader serverReader;
	
	private AttackFormation enemyFormation;

	// TODO: HUD to have this information;
	BitmapFont font;
	private String displayMessage;
	
	public Space(AgalagGame game, Socket socket, BufferedReader fromServer, String id)
	{
		this.game = game;
		this.id = id;
		this.socket = socket;

		// Setup networking immediately 
		serverReader = new ServerReader(this, fromServer);
		new Thread(serverReader).start();
		
		// Load textures
		textures = new TextureManager();
		
		friendlyPlayers = new HashMap<String, PlayerShip>();
		enemyUnits = new HashMap<String, EnemyShip>();
		
		
		if(true)	// Later check if non-default attack pattern exists
		{
			enemyFormation = new AttackFormation(new DefaultPattern());
		}

		
		gameCamera = new OrthographicCamera();
		gamePort = new FitViewport(AgalagGame.V_WIDTH / AgalagGame.PPM,
								  AgalagGame.V_HEIGHT / AgalagGame.PPM,
								   gameCamera);
		
		font = new BitmapFont();
		font.getData().setScale(.25f);
		displayMessage = null;
		
		// Once ready, ask the server to... 
		game.tellServer("INIT");
		game.tellServer("GET_PLAYERS");
		// TODO: Send custom formations (this is the far-out dream goal)
		game.tellServer("SUBMIT_FORMATION " + enemyFormation);
	}

	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(.075F, 0, .1F, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(player != null)
		{
			update(delta);
	
			game.batch.setProjectionMatrix(gameCamera.combined);
			game.batch.begin();
			player.draw(game.batch);
			
			for(Entry<String, PlayerShip> entry: friendlyPlayers.entrySet())
			{
				entry.getValue().draw(game.batch);
			}
			for(Entry<String, EnemyShip> entry: enemyUnits.entrySet())
			{
				entry.getValue().draw(game.batch);
			}
			if(displayMessage!= null)
				font.draw(game.batch, displayMessage, -AgalagGame.V_WIDTH / (2 * AgalagGame.PPM) + (167 / AgalagGame.PPM), 125 / AgalagGame.PPM);
		
			game.batch.end();
			
			//b2dr.render(world, gameCamera.combined);

		}
	}
	
	/**
	 * Since the server updates the players and sends their locations on a schedule,
	 * all this method has to do is read keyboard input. 
	 * @param delta
	 */
	public void update(float delta)
	{
		input(delta);
				
		// TODO: Not following the player, for now at least
		//gameCamera.position.x = player.getPositionX();
		//gameCamera.position.y = player.getPositionY();
		gameCamera.update();
	}

	public void input(float delta)
	{
		player.handleInput(delta);
	}

	/*public void createBoundaries()
	{
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set(0, 0);
		body = world.createBody(bdef);
		
		PolygonShape southZone = new PolygonShape();
		southZone.setAsBox(AgalagGame.V_WIDTH / (2 * AgalagGame.PPM), 2 / AgalagGame.PPM, 
							new Vector2(0, -(.5F * AgalagGame.V_HEIGHT) / AgalagGame.PPM), 0);
		fdef.shape = southZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape northZone = new PolygonShape();
		northZone.setAsBox(AgalagGame.V_WIDTH / (2 * AgalagGame.PPM), 2 / AgalagGame.PPM, 
							new Vector2(0, (.5F * AgalagGame.V_HEIGHT) / AgalagGame.PPM), 0);
		fdef.shape = northZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape eastZone = new PolygonShape();
		eastZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2((.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.shape = eastZone;
		body.createFixture(fdef);
		
		fdef = new FixtureDef();
		PolygonShape westZone = new PolygonShape();
		westZone.setAsBox(2 / AgalagGame.PPM, AgalagGame.V_HEIGHT / (2 * AgalagGame.PPM),
							new Vector2(-(.5F * AgalagGame.V_WIDTH) / AgalagGame.PPM, 0), 0);
		fdef.shape = westZone;
		body.createFixture(fdef);
	}*/
	
	@Override
	public void resize(int width, int height) 
	{
		gamePort.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
	
	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() 
	{
		System.out.println("[CLIENT] DISCONNECTING");
		game.tellServer("CLOSE_CONNECTION");
		
	}

	public void handle(String in) 
	{
		String[] params = in.split("\\s+");

		if(params[0].equals("INIT"))
		{
			
			if(params[1].equals(id))
				player = new PlayerShip(game, textures.get(""), Float.parseFloat(params[2]), Float.parseFloat(params[3]));
			else
				System.out.println("The server sent me the wrong id!!!!");
		}
		else if(params[0].equals("UPDATE_PLAYER"))
		{
			if(player != null)
			if(params[1].equals(id))
				player.update(Float.parseFloat(params[2]), Float.parseFloat(params[3]));
			else
				friendlyPlayers.get(params[1]).update(Float.parseFloat(params[2]), Float.parseFloat(params[3]));
		}
		else if(params[0].equals("PLAYER"))
			friendlyPlayers.put(params[1], new PlayerShip(game, textures.get(""), Float.parseFloat(params[2]), Float.parseFloat(params[3])));
		else if(params[0].equals("UPDATE"))
		{
			try {
				throw new Exception("This should be dead code...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(params[0].equals("UPDATE_ENEMY"))
			enemyUnits.get(params[1]).update(Float.parseFloat(params[2]), Float.parseFloat(params[3]));
		else if(params[0].equals("DISPLAY"))
			if(params[1].equals("CLEAR"))
				displayMessage = null;
			else
				displayMessage = params[1];
		else if(params[0].equals("DEPLOY"))
		{
			enemyUnits.put(params[1], new EnemyShip(game, textures.get(params[2]), 
						Float.parseFloat(params[3]), Float.parseFloat(params[4])));
		}
		else if(params[0].equals("PROJECTILE"))
		{
			
		}
		else if(in.equals("CLOSE_CONNECTION"))
		{
			// TODO: Do more than just closing the socket
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

/*	public void configSocketEvents()
	{
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() 
		{	
			@Override
			public void call(Object... arg0) 
			{
				Gdx.app.log("SocketIO", "Connected");
				player = new PlayerShip(world, textures.get(""), 0, 0);

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
				friendlyPlayers.put(id, new PlayerShip(world, textures.get(""), -1,0));
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
					PlayerShip removedShip = friendlyPlayers.remove(id);
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
						PlayerShip coopPlayer = new PlayerShip(world, textures.get(""), 0, 0);
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
	}*/

}
