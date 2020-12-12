package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SerializationHandler {

    // saves current item list in an "items.dat" file
    public void save() throws IOException {

        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileName))) {

            writer.flush();

            writer.writeUTF("dungeonQuest (VERSION: 1.0)"); // signature

            GameController.instance().getWorld().getLevel().save(writer); // save current level in the save file

            writer.writeInt(currentActors.size());

            for (Actor actor : currentActors) {
                actor.save(writer);
            }
        }
    }

    // loads item list from an "items.dat" file
    public void load() throws IOException {


        try (DataInputStream reader = new DataInputStream(new FileInputStream(fileName))) {

            reader.readUTF();

        }
    }
}
