package com.mackin.formation;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.mackin.enemypatterns.AttackPattern;

public class AttackFormation implements Serializable
{
	/**
	 * So that you can send custom enemy patterns to the server
	 */
	private static final long serialVersionUID = 8882339855893101411L;
	private int currentWave;
	private boolean currentWaveFinished;
	private Wave[] enemyWaves;
	
	public AttackFormation(AttackPattern attackPattern)
	{
		enemyWaves = attackPattern.getEnemyWaves();
		currentWave = 0;
	}
	
	public boolean isFinished()
	{
		if(currentWave >= enemyWaves.length)
			return true;
		return false;
	}
	
	public boolean isCurrentWaveFinished()
	{
		return currentWaveFinished;
	}
	
	public Wave getNextWave()
	{
		Wave nextWave = enemyWaves[currentWave];
		currentWave++;
		return nextWave;
	}
	
	public JSONObject toJSON() 
	{
		JSONObject formationAsJson = new JSONObject();
		for(int i = 0; i < enemyWaves.length; i++)
		{
			try {
				formationAsJson.append("wave", enemyWaves[i].toJson().toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		System.out.println(formationAsJson);
		return formationAsJson;
	}
}
