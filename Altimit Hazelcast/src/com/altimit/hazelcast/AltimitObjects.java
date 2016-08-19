package com.altimit.hazelcast;

/**
 * AltimitObject is used for storing network object data
 */
public class AltimitObjects {
    /** The view id of the objects AltimitView. */
    public int viewId;
    /** The room id this network object resides in. **/
    public int roomId;
    /** The client id that created this network object. **/
    public int clientId;
    /** The world position of this network object. **/
    public Vector3 position;
    /** The world rotation of the network object. **/
    public Quaternion rotation;
    /** The name of the prefab this object. **/
    public String prefabName;

    /**
     * The constructor to create a network object that has tracking of position and rotation.
     * @param viewId The view id of the objects AltimitView.
     * @param roomId The room id this network object resides in.
     * @param clientId The client id that created this network object.
     * @param position The world position of this network object.
     * @param rotation The world rotation of the network object
     * @param prefabName The name of the prefab this object represents.
     */
    public AltimitObjects(int viewId, int roomId, int clientId, Vector3 position, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.roomId = roomId;
        this.clientId = clientId;
        this.position = position;
        this.rotation = rotation;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object without tracking its position and rotation
     * @param viewId The view id of the objects AltimitView.
     * @param roomId The room id this network object resides in.
     * @param clientId The client id that created this network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public AltimitObjects(int viewId, int roomId, int clientId, String prefabName) {
        this.viewId = viewId;
        this.roomId = roomId;
        this.clientId = clientId;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object that only has rotation tracking
     * @param viewId The view id of the objects AltimitView.
     * @param roomId The room id this network object resides in.
     * @param clientId The client id that created this network object.
     * @param rotation The world rotation of the network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public AltimitObjects(int viewId, int roomId, int clientId, Quaternion rotation, String prefabName) {
        this.viewId = viewId;
        this.roomId = roomId;
        this.clientId = clientId;
        this.rotation = rotation;
        this.prefabName = prefabName;
    }

    /**
     * The constructor to create a network object that only has position tracking
     * @param viewId The view id of the objects AltimitView.
     * @param roomId The room id this network object resides in.
     * @param clientId The client id that created this network object.
     * @param position The world position of this network object.
     * @param prefabName The name of the prefab this object represents.
     */
    public AltimitObjects(int viewId, int roomId, int clientId, Vector3 position, String prefabName) {
        this.viewId = viewId;
        this.roomId = roomId;
        this.clientId = clientId;
        this.position = position;
        this.prefabName = prefabName;
    }
}
