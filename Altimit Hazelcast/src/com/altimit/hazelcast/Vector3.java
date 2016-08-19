package com.altimit.hazelcast;

/**
 * Representation of 3D vectors and points.
 */
public class Vector3 {

    /** The X axis of the vector **/
    float x;
    /** The Y axis of the vector **/
    float y;
    /** The Z axis of the vector **/
    float z;

    /**
     * Creates a new vector with given x, y, z components.
     * @param x The x axis of the vector.
     * @param y The x axis of the vector.
     * @param z The x axis of the vector/
     */
    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}