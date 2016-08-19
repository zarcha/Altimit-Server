package com.altimit_server;


import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.altimit_server.types.Room;
import com.altimit_server.types.User;
import com.altimit_server.util.BodyParse;
import com.altimit_server.util.JsonTransformer;
import com.google.gson.Gson;
import com.hazelcast.util.JsonUtil;
import javafx.geometry.Pos;
import spark.*;

/**
 * Rest service used for providing basic information and actions for the control panel. These can be used for websites too (Server status mainly).
 */
public class AltimitRest {

    /**
     * Starts the Spark Rest server and adds the GET & POST endpoints
     */
    public static void StartAll(){
        AltimitRest.apply();

        /** Returns the number of rooms on the server **/
        get("/roomCount", (req, res) -> {
            return Rooms.RoomCount();
        });

        /** Returns the number of users on the server **/
        get("/userCount", (req, res) -> {
            return Users.GetUserCount();
        });

        /**
         * Returns true. If the CPanel get something it means the server is online.
         * Use this to provide something else if there is is a reason to say offline if online.
         */
        get("/serverStatus", (reg, res) -> {
           return true;
        });

        /**
         * Returns a list of users and their information in a json form.
         */
        get("/userList", "application/json", (req, res) -> {
            return Users.userMap;
        }, new JsonTransformer());

        /**
         * Returns a list of users and their information in json form.
         */
        get("/roomList", "application/json", (req, res) -> {
            return Rooms.roomMap;
        }, new JsonTransformer());

        /**
         * Post command that takes a UUID (in string form) used to disconnect the client.
         */
        post("/kickPlayer", (request, response) -> {
            System.out.print("rest");
            main.DisconnectUser(UUID.fromString(BodyParse.parseBody(request).get("playerUUID")), true);
            return "Success";
        });
    }

    /**
     * Rest headers.
     */
    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    /**
     * Adds to the corsHeaders map.
     */
    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    /**
     * Fixes Cross-Site scripting issues for the CPanel
     */
    public final static void apply() {
        Filter filter = new Filter() {
            @Override
            public void handle(Request request, Response response) throws Exception {
                corsHeaders.forEach((key, value) -> {
                    response.header(key, value);
                });
            }
        };
        Spark.after(filter);
    }
}
