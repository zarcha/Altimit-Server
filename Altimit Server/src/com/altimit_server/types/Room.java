package com.altimit_server.types;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This is a class to hold Room information.
 */
public class Room implements Serializable{

    /** The id of the user that is currently the master client. **/
    public int ownerId;
    /** The max number of users that are allowed to be in the room. **/
    public int max_users;
    /** A list of user id that are in the room **/
    public HashMap<UUID, User> users = new HashMap<>();
    /** A list of network objects related to the room **/
    public List<AltimitObjects> networkObjects = new ArrayList<>();

    /**
     * The constructor to create a room with no max amount of users
     * @param owner The id of the user that is currently the master client.
     */
    public Room(User owner){
        this.ownerId = owner.id;
        users.put(owner.uuid, owner);
    }

    /**
     * The constructor to create a room with a max amount of users
     * @param owner The id of the user that is currently the master client.
     * @param max_users The max number of users that are allowed to be in the room.
     */
    public Room(User owner, int max_users){
        this.ownerId = owner.id;
        this.max_users = max_users;
        users.put(owner.uuid, owner);
    }
}

