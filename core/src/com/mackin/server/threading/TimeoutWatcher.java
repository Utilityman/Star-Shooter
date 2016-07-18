package com.mackin.server.threading;

/**
 * @author jmackin
 *
 */
public class TimeoutWatcher implements Runnable
{
	private static final float TIME_OUT = 10000;
	private static final long SLEEP_TIME = 400;
	
	private float timer;
	
	private boolean triggered;
	private boolean running;
	
	public TimeoutWatcher()
	{
		running = true;
	}
	
	
	@Override
	public void run()
	{
		while(running)
		{
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timer += SLEEP_TIME;
			System.out.println(timer);
			if(timer >= TIME_OUT)
			{
				triggered = true;
				timer = TIME_OUT;
			}
		}
	}
	
	public void reset()
	{
		timer = 0;
		triggered = false;
	}

	public boolean isTriggered()
	{
		return triggered;
	}
	
}
