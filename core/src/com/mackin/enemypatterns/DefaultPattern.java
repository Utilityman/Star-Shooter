package com.mackin.enemypatterns;

import com.mackin.formation.ShipShell;
import com.mackin.formation.SpawnLocations;
import com.mackin.formation.Wave;

public class DefaultPattern extends AttackPattern
{
	public DefaultPattern()
	{
		enemyWaves = new Wave[2];
		Wave wave = new Wave();
		wave.setDownTime(2000);
		wave.setEnemiesPerStrand(2);
		//wave.setStrands(1);
		ShipShell[] enemies = 
		{
				ShipShell.defineEnemyShip("enemyTexture", SpawnLocations.TOP_CENTER[0], SpawnLocations.TOP_CENTER[1], 0),
				ShipShell.defineEnemyShip("enemyTexture", SpawnLocations.TOP_CENTER[0], SpawnLocations.TOP_CENTER[1], 0),
				ShipShell.defineEnemyShip("enemyTexture", SpawnLocations.TOP_CENTER[0], SpawnLocations.TOP_CENTER[1], 0),
		};
		wave.addEnemies(enemies);

		enemyWaves[0] = wave;
		enemyWaves[1] = wave;
	}
}
