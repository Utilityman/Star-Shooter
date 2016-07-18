package com.mackin.body;

/**
 * @author jmackin
 *
 */
public interface Formable 
{
	public float getY();
	public float getX();
	
	public void setToDestroy(boolean b);
	public void destroy();
	
	public void update(float delta);
}
