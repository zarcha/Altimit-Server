package com.altimit_server;

import com.altimit_server.types.*;
import com.hazelcast.core.*;

import java.util.*;

/**
 * This class is used for manipulation of room map data on hazelcast
 */
class Rooms {

    /**
     * Hazelcast instance
     */
    private static HazelcastInstance mainInstance = main.hazelcastInstance;

    /**
     * The user map stored on Hazelcast
     */
    static IMap<String, Room> roomMap = mainInstance.getMap("rooms");

    /**
     * Joins a user to a room or creates one if the room deosnt exist.
     * @param roomName The name of the room to be created or joined.
     * @param maxUsers The maximum amount of users that can be within the room.
     * @param clientUUID The UUID of the client attempting to join a room.
     */
    @AltimitCmd
    public static void JoinRoom(String roomName, int maxUsers, UUID clientUUID){
        Room tempRoom;
        User tempUser = Users.GetUser(clientUUID);

        if(!tempUser.roomName.equals("")){
            LeaveRoom(clientUUID);
        }

        if (!roomMap.containsKey(roomName)) {
            tempRoom = new Room(tempUser, maxUsers);
            roomMap.put(roomName, tempRoom);
            System.out.println("Room " + roomName + " has been created. " + roomMap.size() + " room exist.");
            Users.SetRoomName(clientUUID, roomName);
            PostMan.SendPost(clientUUID, "JoinedRoom", roomName, true);
        } else {
            tempRoom = roomMap.get(roomName);
            if (tempRoom.max_users >= (tempRoom.users.size() + 1) || tempRoom.max_users == -1) {
                tempRoom.users.put(tempUser.uuid, tempUser);
                roomMap.put(roomName, tempRoom);
                Users.SetRoomName(clientUUID, roomName);
                PostMan.SendPost(clientUUID, "JoinedRoom", roomName, false);
            }
        }

        SendUserCount(roomName);
    }

    /**
     * Remove a user from a room and delete rooms when no one exists.
     * If the owner leaves a room then a new owenr is assigned to the room.
     * @param clientUUID The client UUID of the user that will leave the room.
     */
    @AltimitCmd
    public static void LeaveRoom(UUID clientUUID){
        Room tempRoom;
        User tempUser = Users.GetUser(clientUUID);

        if(tempUser.roomName != "" || tempUser.roomName != null) {
            if (roomMap.containsKey(tempUser.roomName)) {
                tempRoom = roomMap.get(tempUser.roomName);

                if(tempRoom.ownerId == tempUser.id){
                    if(tempRoom.users.size() > 1){
                        tempRoom.users.remove(tempUser.uuid);
                        Map.Entry<UUID, User> userEntry =  tempRoom.users.entrySet().iterator().next();
                        tempRoom.ownerId = userEntry.getValue().id;
                        roomMap.put(tempUser.roomName, tempRoom);
                        System.out.println("Room " + tempUser.roomName + " has a new owner.");
                        SendUserCount(tempUser.roomName);
                    }else{
                        roomMap.remove(tempUser.roomName);
                        System.out.println("Room " + tempUser.roomName + " was deleted!");
                    }
                }else{
                    tempRoom.users.remove(tempUser.uuid);
                    roomMap.put(tempUser.roomName, tempRoom);
                    SendUserCount(tempUser.roomName);
                }
                Users.SetRoomName(clientUUID, "");
            }
        }
    }

    /**
     * Sends the user count to each player when someone leaves or
     * @param roomName Name of the room.
     */
    private static void SendUserCount (String roomName){

        if (roomMap.containsKey(roomName)) {
            PostMan.SendPost(roomName, "SetUserCount", roomMap.get(roomName).users.size());
        }
    }

    /**
     * Gets a list of users in a room.
     * @param roomName Name of the room.
     * @return List of users in the specified room.
     */
    static HashMap<UUID, User> GetRoomUsers(String roomName) {
        HashMap<UUID, User> tempMap;

        if (roomMap.containsKey(roomName)) {
            tempMap = roomMap.get(roomName).users;
        } else {
            tempMap = null;
        }

        return tempMap;
    }

    /**
     * Gets the number of rooms on the Hazelcast room map.
     * @return Number of rooms.
     */
    static int RoomCount(){
        return roomMap.size();
    }
}
