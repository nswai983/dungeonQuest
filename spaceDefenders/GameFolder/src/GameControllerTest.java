//-----------------------------------------------------------
//File:   GameControllerTest.java
//Desc:   This file holds test cases for Space Defenders
//-----------------------------------------------------------
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

import model.*;
import model.GameObject.*;

public class GameControllerTest {

    /*
    @author Nathan Swaim
    Last revision: Oct. 31
    */

    private GameController controller = GameController.instance();
    @Test
    public void test_saveGame() {
        GameController.reset();
        World world = controller.getWorld();
        Player player = world.getPlayer();

        // set difficulty
        world.setChosenDifficulty(DifficultyTypes.EASY);

        // set player data
        player.setUsername("Nathan");
        player.setID(1);
        player.setDistanceTraveled(1000);
        player.setSpeed(34);
        player.setLocation(new Coordinate(55, 65));
        player.getLocation().setHeading(player.getLocation().getAngle(new Coordinate(100, 23)));
        player.setHitpoints(5);
        player.setTotalAliensKilled(5);
        player.setTotalArtifactsCollected(3);

        // add enemies, artifacts, and debris
        // ArrayList<Actor> aliens = new ArrayList<Actor>();
        // aliens.add(new AlienShipTier1());
        // aliens.add(new AlienShipTier1());
        world.getActiveActors().add(new AlienShipTier1());
        world.getActiveActors().add(new AlienShipTier1());

        // ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
        // artifacts.add(new Artifact());
        // artifacts.add(new Artifact());
        // world.setArtifacts(artifacts);
        world.getActiveActors().add(new Artifact());
        world.getActiveActors().add(new Artifact());

        // ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        // asteroids.add(new Asteroid());
        // asteroids.add(new Asteroid());
        // world.setAsteroids(asteroids);
        world.getActiveActors().add(new Asteroid());
        world.getActiveActors().add(new Asteroid());


        assertEquals(true, controller.save("gameState.txt"));
    }

    @Test
    public void test_loadGame() {
        GameController.reset();
        assertEquals(true, controller.load("gameStateExample.txt"));
        World world = controller.getWorld();

        // validate player data
        assertEquals("Bob", world.getPlayer().getUsername());
        assertEquals(1000, world.getPlayer().getDist());
        assertEquals(25, world.getPlayer().getSpeed());
        assertEquals(450, world.getPlayer().getLocation().getX());
        assertEquals(800, world.getPlayer().getLocation().getY());
        // assertEquals(90, world.getPlayer().getLocation().getHeading());
        assertEquals(2, world.getPlayer().getHealth());
        assertEquals(55, world.getPlayer().getTotalAliensKilled());
        assertEquals(20, world.getPlayer().getTotalArtifactsCollected());

        // validate enemy and debris data
        assertEquals(5, world.count(ActorTypes.ALIENSHIP1));
        assertEquals(4, world.count(ActorTypes.ASTEROID));

        // check enemy
        Actor alien = (Actor) world.getSpecificActors(ActorTypes.ALIENSHIP1)[2]; // data: 004,1,600,350,270,15
        assertEquals(4, alien.getID());
        // assertEquals(1, alien.getType());
        assertEquals(600, alien.getLocation().getX());
        assertEquals(350, alien.getLocation().getY());
        assertEquals(270, alien.getLocation().getHeading());
        assertEquals(15, alien.getSpeed());
    }

    @Test
    public void test_load() {
        try {SerializationHandler.load("gameStateExample.txt", new World());}
        catch (Exception e) { return; }

    }

    @Test
    public void test_toString() {
        GameController.reset();
        World world = controller.getWorld();
        Player player = world.getPlayer();
        Actor alien1 = new AlienShipTier1();

        // setup Player
        player.setID(1);
        player.setDistanceTraveled(1000);
        player.setSpeed(34);
        player.setLocation(new Coordinate(55, 65));
        player.getLocation().setHeading(player.getLocation().getAngle(new Coordinate(100, 23)));
        player.setHitpoints(5);
        player.setTotalAliensKilled(5);
        player.setTotalArtifactsCollected(3);

        // test toString() method
        assertEquals("001,1,1000,34,55,65,316,5,5,3", player.toString());

        // setup Alien
        alien1.setID(2);

        player.setLocation(new Coordinate(55, 65));
        player.getLocation().setHeading(player.getLocation().getAngle(new Coordinate(100, 23)));
        player.setSpeed(34);
        player.setHitpoints(5);
        player.setTotalAliensKilled(5);
        player.setTotalArtifactsCollected(3);

        // test toString() method
        assertEquals("002,1,55,65,316,45", player.toString());
    }
}
