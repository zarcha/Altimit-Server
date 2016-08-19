package com.altimit_server.types;

import java.io.Serializable;

/**
 * Quaternions are used to represent rotations.
 */
public class Quaternion implements Serializable{

    /** The W axis of the vector **/
    public float w;
    /** The X axis of the vector **/
    public float x;
    /** The Y axis of the vector **/
    public float y;
    /** The Z axis of the vector **/
    public float z;

    /**
     *Constructs new Quaternion with given x,y,z,w components.
     * @param w The W axis of the vector
     * @param x The X axis of the vector
     * @param y The Y axis of the vector
     * @param z The Z axis of the vector
     */
    public Quaternion(float w, float x, float y, float z){
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

