package com.mackin.enemypatterns;

import org.json.JSONObject;

import com.mackin.formation.Wave;

public class AttackPattern 
{
	protected Wave[] enemyWaves;

	public Wave[] getEnemyWaves() 
	{
		return enemyWaves;
	}

	public static AttackPattern generateWavesFromJson(JSONObject json)
	{
		return null;
	}
}
