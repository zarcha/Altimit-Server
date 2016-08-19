package com.altimit_server.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Converts objects into json form for the rest services
 */
public class JsonTransformer implements ResponseTransformer {

    /**
     *
     */
    private Gson gson = new Gson();

    /**
     * Converts an object into json form
     * @param model Object to be converted
     * @return json version of the object.
     */
    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }
}
