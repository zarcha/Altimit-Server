package com.altimit.hazelcast;

import com.hazelcast.core.*;
import com.hazelcast.config.*;

import java.util.Map;
import java.util.UUID;

/**
 * The main class to run the hazelcast instance.
 */
public class Main {

    /**
     * The main starts a Hazelcast instance and then creates the Maps for Users, AltimitObjects, and Rooms.
     */
    public static void main(String[] args) {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<UUID, User> users = instance.getMap("users");
        Map<Integer, AltimitObjects> altimitObjects = instance.getMap("altimitObjects");
        Map<String, Room> rooms = instance.getMap("rooms");
        Map<String, Integer> mapSizes = instance.getMap("mapSizes");
        mapSizes.put("users", 0);
        mapSizes.put("rooms", 0);
        mapSizes.put("altimitObjects", 0);
    }
}
