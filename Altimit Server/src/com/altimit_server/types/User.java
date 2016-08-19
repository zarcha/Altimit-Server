package com.altimit_server.types;

import java.io.Serializable;
import java.util.UUID;

/**
 * User class is used for storing user data
 */
public class User implements Serializable{

    /** The Id of users client **/
    public int id;
    //TODO: find a better way to get the UUID from rooms, this is a crappy way to do it
    /** The UUID of the user, needed for when sending messages **/
    public UUID uuid;
    /** The user name related to the user **/
    public String username = "";
    /** The name of room the user currently exists in **/
    public String roomName = "";

    /**
     * The constructor to create the user object.
     * @param id The id of users client.
     */
    public User(int id){
        this.id = id;
    }
}