package com.altimit_server;

import java.util.*;

/**
 * Heart beat messenger to see if clients are still connected.
 */
class AltimitHeatBeat {

    /**
     * List of client UUID's that will be disconnected.
     */
    static List<UUID> badUUIDs = new ArrayList<>();

    /**
     * Timer to send a message to all clients and then disconnect the bad ones.
     */
    static void StartChecks() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if(main.localClientMap.size() > 0) {
                    PostMan.SendPost("HeartBeat");
                }

                if(badUUIDs.size() > 0){
                    for (UUID badUUID : badUUIDs) {
                        main.DisconnectUser(badUUID, false);
                    }

                    badUUIDs.clear();
                }

                StartChecks();
                Thread.currentThread().stop();
            }
        }, 30000);
    }
}
