/**
 * File: ActorTypes.java
 * Desc: Model Enum, describes the possible types of Actors in this game
 *
 * @author  Jeremy Tan
 */


package model.GameObject;
import model.*;

//jtan
public enum ActorTypes {
    PLAYER, //Player entity
    ALIENSHIP1, //Tier 1 Enemy
    ALIENSHIP2, //Tier 2 Enemy
    ALIENSHIP3, //Tier 3 Enemy
    ARTIFACT,
    ASTEROID,
    PROJECTILE1_PLAYER, //The Player's standard projectile
    PROJECTILE1_ALIEN, //Standard projectile for Tier 1 Enemy
    PROJECTILE2_ALIEN, //Standard projectile for Tier 2 Enemy
    PROJECTILE3_ALIEN //Never Used
}
