package model;

import java.io.*;
import java.nio.file.attribute.AclEntryPermission;
import java.rmi.activation.ActivationGroup;

import javafx.scene.shape.ArcTo;
import model.GameObject.*;

/**
 * File: SerializationHandler.java Desc: Model Class that handles all
 * serialization functionality.
 *
 * @author Nathan Swaim
 */

public class SerializationHandler {

    // class: SerializationHandler
    // purpose: to handle saving and loading of game files

    // takes in a filename and world
    // overwrites gameState.txt with serialization data
    public static boolean save(String filename, World data) throws IOException {

        // create header data
        String gameTitle = "GAME: Space Defenders";
        String version = "VERSION: 1.0";
        String playerName = "PLAYER_NAME: " + data.getPlayer().getUsername();
        String playerScore = "PLAYER_SCORE: " + data.getPlayer().getTotalPoints(data.getDifficultyLevel());
        String playerProjectiles = "NUM_PLAYER_PROJECTILES: " + data.count(ActorTypes.PROJECTILE1_PLAYER);
        int numberOfEnemies = data.count(ActorTypes.ALIENSHIP1) + data.count(ActorTypes.ALIENSHIP2)
                + data.count(ActorTypes.ALIENSHIP3);
        String numEnemies = "NUM_ENEMIES: " + numberOfEnemies;
        String enemyProjectiles = "NUM_ENEMY_PROJECTILES: " + data.count(ActorTypes.PROJECTILE1_ALIEN);
        String numArtifacts = "NUM_ARTIFACTS: " + data.count(ActorTypes.ARTIFACT);
        String numDebris = "NUM_DEBRIS: " + data.count(ActorTypes.ASTEROID);
        DifficultyTypes difficultyLevel = data.getChosenDifficulty();
        String wave = "WAVE: " + data.getWave();
        String cheat = "CHEAT: 0";
        if (data.getCheat()) {
            cheat = "CHEAT: 1";
        }

        try (var writer = new PrintWriter(new FileWriter(filename))) {

            // write header data
            writer.println(gameTitle);
            writer.println(version);
            writer.println(playerName);
            writer.println(playerScore);
            writer.println(playerProjectiles);
            writer.println(numEnemies);
            writer.println(enemyProjectiles);
            writer.println(numArtifacts);
            writer.println(numDebris);

            switch (difficultyLevel) {
                case EASY: {
                    writer.println("DIFF: " + 1);
                    break;
                }
                case NORMAL: {
                    writer.println("DIFF: " + 2);
                    break;
                }
                case HARD: {
                    writer.println("DIFF: " + 3);
                    break;
                }
            }

            // writer.println(difficultyLevel);
            writer.println(wave);

            // write cheat mode status
            writer.println(cheat);

            // write player data
            writer.println("");
            writer.println("GAMESTATE");
            writer.println("    PLAYER");
            writer.println("        " + data.getPlayer().toString());
            writer.println("    PLAYER");

            // write player projectile data
            writer.println("    PLAYER_PROJECTILES");
            Object[] playerProjectileObjects = data.getSpecificActors(ActorTypes.PROJECTILE1_PLAYER);
            for (int i = 0; i < data.count(ActorTypes.PROJECTILE1_PLAYER); i++) {
                writer.println("        " + playerProjectileObjects[i].toString());
            }
            writer.println("    PLAYER_PROJECTILES");

            // write enemy data
            writer.println("    ENEMIES");

            // tier 1 aliens
            Object[] enemyObjects1 = data.getSpecificActors(ActorTypes.ALIENSHIP1);
            for (int i = 0; i < data.count(ActorTypes.ALIENSHIP1); i++) {
                writer.println("        " + enemyObjects1[i].toString());
            }

            // tier 2 aliens
            Object[] enemyObjects2 = data.getSpecificActors(ActorTypes.ALIENSHIP2);
            for (int i = 0; i < data.count(ActorTypes.ALIENSHIP2); i++) {
                writer.println("        " + enemyObjects2[i].toString());
            }

            // tier 3 aliens
            Object[] enemyObjects3 = data.getSpecificActors(ActorTypes.ALIENSHIP3);
            for (int i = 0; i < data.count(ActorTypes.ALIENSHIP3); i++) {
                writer.println("        " + enemyObjects3[i].toString());
            }

            writer.println("    ENEMIES");

            // write player projectile data
            writer.println("    ENEMY_PROJECTILES");
            Object[] enemyProjectileObjects = data.getSpecificActors(ActorTypes.PROJECTILE1_ALIEN);
            for (int i = 0; i < data.count(ActorTypes.PROJECTILE1_ALIEN); i++) {
                writer.println("        " + enemyProjectileObjects[i].toString());
            }
            writer.println("    ENEMY_PROJECTILES");

            // write artifacts data
            writer.println("    ARTIFACTS");
            Object[] artifactObjects = data.getSpecificActors(ActorTypes.ARTIFACT);
            for (int i = 0; i < data.count(ActorTypes.ARTIFACT); i++) {
                writer.println("        " + artifactObjects[i].toString());
            }
            writer.println("    ARTIFACTS");

            // write debris data
            writer.println("    DEBRIS");
            Object[] debrisObjects = data.getSpecificActors(ActorTypes.ASTEROID);
            for (int i = 0; i < data.count(ActorTypes.ASTEROID); i++) {
                writer.println("        " + debrisObjects[i].toString());
            }
            writer.println("    DEBRIS");

            // write ending of file
            writer.println("GAMESTATE");
            writer.println("END");
        }

        return true;

    }

    // takes in a filename and a world object
    // loads data from gameState.txt into the world object
    public static boolean load(String filename, World data) throws IOException {
        try (var rd = new BufferedReader(new FileReader(filename))) {

            // check that file has all required headers and data; if not, return
            // get header info
            String gameTitle = rd.readLine();

            // check that version number is correct
            String version = rd.readLine();
            if (!version.equals("VERSION: 1.0")) {
                return false;
            }

            // get and set player name
            String playerName = rd.readLine();
            data.getPlayer().setUsername(playerName.substring(13));

            // get player score
            String playerScore = rd.readLine();

            // get and set num player projectiles
            String playerProjectiles = rd.readLine();
            int numPlayerProjectiles = Integer.parseInt(playerProjectiles.substring(24));

            // get and set num enemies
            String enemies = rd.readLine();
            int numEnemies = Integer.parseInt(enemies.substring(13));

            // get and set num player projectiles
            String enemyProjectiles = rd.readLine();
            int numEnemyProjectiles = Integer.parseInt(enemyProjectiles.substring(23));

            // get and set num artifacts
            String artifacts = rd.readLine();
            int numArtifacts = Integer.parseInt(artifacts.substring(15));

            // get and set num debris
            String debris = rd.readLine();
            int numDebris = Integer.parseInt(debris.substring(12));

            // get and set difficulty level
            String difficulty = rd.readLine();
            int difficultyLevel = Integer.parseInt(difficulty.substring(6));

            switch (difficultyLevel) {
                case 1: {
                    Difficulty difficultyAdjuster = new Difficulty(DifficultyTypes.EASY);
                    data.setDifficultyLevel(difficultyAdjuster);
                    data.setChosenDifficulty(DifficultyTypes.EASY);
                    break;
                }
                case 2: {
                    Difficulty difficultyAdjuster = new Difficulty(DifficultyTypes.NORMAL);
                    data.setDifficultyLevel(difficultyAdjuster);
                    data.setChosenDifficulty(DifficultyTypes.NORMAL);
                    break;
                }
                case 3: {
                    Difficulty difficultyAdjuster = new Difficulty(DifficultyTypes.HARD);
                    data.setDifficultyLevel(difficultyAdjuster);
                    data.setChosenDifficulty(DifficultyTypes.HARD);
                    break;
                }
            }

            // set player score with difficulty multiplier
            data.getPlayer().setTotalPoints(
                    (int) (Integer.parseInt(playerScore.substring(14)) / data.getDifficultyLevel().getPointsMult()));

            // get and set wave data
            String wave = rd.readLine();
            int waveLevel = Integer.parseInt(wave.substring(6));
            data.setWave(waveLevel);

            String cheat = rd.readLine();
            if (cheat.substring(7).equals("1")) {
                data.setCheat(true);
            } else {
                data.setCheat(false);
            }

            // data.beginGame(data.getStageType(), playerName, data.getChosenDifficulty());

            // get player data
            for (int i = 0; i < 3; i++) {
                rd.readLine();
            } // skip formatting lines
            String playerData = rd.readLine().trim(); // read player data
            data.getPlayer().deserialize(playerData);

            // get player projectile data
            for (int i = 0; i < 2; i++) {
                rd.readLine();
            } // skip formatting lines
            for (int i = 0; i < numPlayerProjectiles; i++) {
                String playerProjectilesData = rd.readLine().trim();
                ProjectilePlayer playerProjectile = new ProjectilePlayer();
                playerProjectile.setDirection(90);
                playerProjectile.deserialize(playerProjectilesData); // add serialization data to alien
                data.getActiveActors().add(playerProjectile);
            }

            // get enemy data
            for (int i = 0; i < 2; i++) {
                rd.readLine();
            } // skip formatting lines
            for (int i = 0; i < numEnemies; i++) {
                String enemyData = rd.readLine().trim();
                String[] enemyDataArray = enemyData.split(",");
                Actor alien;
                // System.out.println(enemyDataArray);

                String enemyType = enemyDataArray[1];
                if (enemyType.equals("3")) {
                    alien = new AlienShipTier3();
                } else if (enemyType.equals("2")) {
                    alien = new AlienShipTier2();
                } else {
                    alien = new AlienShipTier1();
                }

                if (alien.equals(null)) {
                    alien = new AlienShipTier1();
                }
                // Actor alien = new AlienShipTier1();
                alien.deserialize(enemyData); // add serialization data to alien
                // data.getAliens().add(alien);
                data.getActiveActors().add(alien);
            }

            // get enemy projectile data
            for (int i = 0; i < 2; i++) {
                rd.readLine();
            } // skip formatting lines
            for (int i = 0; i < numEnemyProjectiles; i++) {
                String enemyProjectilesData = rd.readLine().trim();

                String[] enemyProjectileDataArray = enemyProjectilesData.split(",");
                Actor enemyProjectile;

                String enemyType = enemyProjectileDataArray[1];
                if (enemyType.equals("2")) {
                    enemyProjectile = new ProjectileAlien2();
                } else {
                    enemyProjectile = new ProjectileAlien1();
                }

                enemyProjectile.setDirection(270);
                enemyProjectile.deserialize(enemyProjectilesData); // add serialization data to alien
                data.getActiveActors().add(enemyProjectile);
            }

            // get artifacts data
            for (int i = 0; i < 2; i++) {
                rd.readLine();
            } // skip formatting lines
            for (int i = 0; i < numArtifacts; i++) {
                String artifactsData = rd.readLine().trim();
                Artifact artifact = new Artifact();
                artifact.deserialize(artifactsData); // add serialization data to alien
                // data.getArtifacts().add(artifact);
                data.getActiveActors().add(artifact);
            }

            // get debris data
            for (int i = 0; i < 2; i++) {
                rd.readLine();
            } // skip formatting lines
            for (int i = 0; i < numDebris; i++) {
                String asteroidData = rd.readLine().trim();
                Asteroid asteroid = new Asteroid();
                asteroid.deserialize(asteroidData); // add serialization data to alien

                if (i % 2 == 0) {
                    asteroid.spawnonLeftSide();
                } else {
                    asteroid.spawnonRightSide();
                }

                data.getActiveActors().add(asteroid);

            }

        }
        return true; // return true upon success
    }
}
