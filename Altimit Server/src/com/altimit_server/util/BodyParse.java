package com.altimit_server.util;

import java.lang.reflect.Type;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.Request;

/**
 * Converts a json string into a hash map.
 */
public class BodyParse {
    /**
     * Converts a string json object into a hash map.
     * @param request String json object.
     * @return <Sring, String> hash map of the json key's and values.
     */
    public static Map<String, String> parseBody(Request request) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();

        return gson.fromJson(request.body(), type);
    }
}
