/**
 * File: Difficulty.java
 * Desc: Model Class, affects the game's difficulty level
 *
 * @author  Jeremy Tan
 */

package model;

import model.GameObject.ActorTypes;

public class Difficulty {

    //Instance Variable Declerations

    double enemySpeedMult; //enemy speed multiplier
    double enemyHealthMult;//enemy health multiplier
    double pointsMult; //player points multiplier

    //number of seconds for spawn to have
    int spawnRateForAsteroids; //spawn rate for asteroids
    int spawnRateForArtifacts=2; //spawn rate for artifacts
    int spawnRateForTier1; //spawn rate for tier 1 enemy
    int spawnRateForTier2; //spawn rate for tier 2 enemy
    int spawnRateForTier3; //spawn rate for tier 3 enemy

    DifficultyTypes chosenDifficulty; //chosen difficulty for game

    /**
      * Constructor
      * Instantiates instance variables based off of parameter
      * @param chosenDifficulty the player's chosen difficulty level of enum type
      * @return N/A
     */
    public Difficulty(DifficultyTypes chosenDifficulty) {
        this.chosenDifficulty = chosenDifficulty;
        switch (chosenDifficulty) {
            case EASY:
                enemySpeedMult=0.5;
                enemyHealthMult=0.5;
                pointsMult=0.5;
                spawnRateForAsteroids=3;
                spawnRateForTier1=3;
                break;
            case NORMAL:
                enemySpeedMult=1.0;
                enemyHealthMult=1.0;
                pointsMult=1.0;
                spawnRateForAsteroids=4;
                spawnRateForTier1=3;
                spawnRateForTier2=5;
                break;
            case HARD:
                // System.out.println("HARD");
                enemySpeedMult=1.25;
                enemyHealthMult=1.25;
                pointsMult=1.25;
                spawnRateForAsteroids=5;
                spawnRateForTier1=4;
                spawnRateForTier2=6;
                spawnRateForTier3=1;
                break;
        }
    }

    /** 
      * @param . N/A
      * @return the stored chosen difficulty level of this object
     */
    public DifficultyTypes getChosenDifficulty() {
        return chosenDifficulty;
    }

    /**
      * Stores the passed in chosen difficulty type 
      * @param chosenDifficulty the chosen actor type
      * @return N/A
     */
    public void setChosenDifficulty(DifficultyTypes chosenDifficulty) {
        this.chosenDifficulty = chosenDifficulty;
    }

    /** 
      * @param actortype the chosen actor type
      * @return spawnrate the int spawn rate that corresponds to the chosen actor
     */
    public int getSpawnRateForActor(ActorTypes actortype) {
        if (actortype==ActorTypes.ARTIFACT) {
            return spawnRateForArtifacts;
        }
        else if (actortype==ActorTypes.ASTEROID) {
            return spawnRateForAsteroids;
        }
        else if (actortype==ActorTypes.ALIENSHIP1) {
            return spawnRateForTier1;
        }
        else if (actortype==ActorTypes.ALIENSHIP2) {
            return spawnRateForTier2;
        }
        else if (actortype==ActorTypes.ALIENSHIP3) {
            return spawnRateForTier3;
        }
        return 0;
    }


    //GETTERS

    public int getSpawnRateAsteroids() {
        return spawnRateForAsteroids;
    }

    public int getSpawnRateAlienTier1() {
        return spawnRateForTier1;
    }

    public int getSpawnRateAlienTier2() {
        return spawnRateForTier2;
    }

    public int getSpawnRateAlienTier3() {
        return spawnRateForTier3;
    }

    public int getSpawnRateArtifacts() {
        return spawnRateForArtifacts;
    }

    public double getEnemySpeedMult() {
        return enemySpeedMult;
    }
    
    public double getEnemyHealthMult() {
        return enemyHealthMult;
    } 

    public double getPointsMult() {
        return pointsMult;
    }

    
}
