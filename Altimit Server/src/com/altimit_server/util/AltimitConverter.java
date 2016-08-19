package com.altimit_server.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Converts variables to and from byte array for sending or receiving method calls.
 */
public class AltimitConverter {

    /**
     * Gets a list of class types of the paramaters provided
     * @param parameters These are the parameters object versions of the variables.
     * @return The class's of the valiables.
     */
    public static Class[] GetParameterClasses(Object[] parameters){
        Class[] endParameters = new Class[parameters.length];

        for(int i = 0; i < parameters.length; i ++){
            endParameters[i] = parameters[i].getClass();
        }
        return endParameters;
    }

    /**
     * Creates a byte array of the message that will be sent.
     * The first 4 bytes is the end size of the whole message.
     * The last 4 bytes is the message key used to identify the end of a message and later to be used for a security purpose.
     * @param methodName Name of the method to be called on the client.
     * @param args These are the parameters that will be passed to the client.
     * @return Byte array to be sent to sent to the client.
     */
    public static byte[] SendConversion(String methodName, Object... args){
        List<byte[]> byteList = new ArrayList<>();

        byte[] byteArray = null;
        if(methodName != null || methodName != "") {
            byte[] method = convertToByteArray(methodName);
            byteList.add(method);

            Integer size = method.length;
            String type = "";

            for (Object params : args) {
                type = params.getClass().getTypeName();
                byte[] currentArr = null;
                switch (type) {
                    case "java.lang.Character":
                        char castChar = (char) params;
                        currentArr = convertToByteArray(castChar);
                        break;
                    case "java.lang.Integer":
                        int castInt = (Integer) params;
                        currentArr = convertToByteArray(castInt);
                        break;
                    case "java.lang.Long":
                        long castLong = (long) params;
                        currentArr = convertToByteArray(castLong);
                        break;
                    case "java.lang.Short":
                        short castShort = (short) params;
                        currentArr = convertToByteArray(castShort);
                        break;
                    case "java.lang.Float":
                        float castFloat = (float) params;
                        currentArr = convertToByteArray(castFloat);
                        break;
                    case "java.lang.Double":
                        double castDouble = (double) params;
                        currentArr = convertToByteArray(castDouble);
                        break;
                    case "java.lang.Boolean":
                        boolean castBoolean = (boolean) params;
                        currentArr = convertToByteArray(castBoolean);
                        break;
                    case "java.lang.String":
                        String castString = (String) params;
                        currentArr = convertToByteArray(castString);
                        break;
                    default:
                        System.out.println("No supported type for this variable");
                        break;
                }

                size += currentArr.length;
                byteList.add(currentArr);
            }

            byte[] key = {5, 9, 0, 4};
            size += 8;

            byte[] temp = Arrays.copyOfRange(convertToByteArray(size), 1, 5);

            byteList.add(0, temp);
            byteList.add(key);

            byteArray = new byte[size];
            Integer currentIndex = 0;

            for (byte[] byteArr : byteList) {
                System.arraycopy(byteArr, 0, byteArray, currentIndex, byteArr.length);
                currentIndex += byteArr.length;
            }
        }

        return byteArray;
    }

    /**
     * Converts Char into byte array.
     * @param value The Char value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 2 bytes of the Char.
     */
    public static byte[] convertToByteArray(char value) {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte)1);
        buffer.position(1);
        buffer.putChar(value);
        return buffer.array();
    }

    /**
     * Converts Int into byte array.
     * @param value The Int value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 4 bytes of the Int.
     */
    public static byte[] convertToByteArray(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte)2);
        buffer.position(1);
        buffer.putInt(value);
        return buffer.array();
    }

    /**
     * Converts Long into byte array.
     * @param value The Long value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 8 bytes of the Long.
     */
    public static byte[] convertToByteArray(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put((byte)3);
        buffer.position(1);
        buffer.putLong(value);
        return buffer.array();
    }

    /**
     * Converts Short into byte array.
     * @param value The Short value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 2 bytes of the Short.
     */
    public static byte[] convertToByteArray(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.put((byte)4);
        buffer.position(1);
        buffer.putShort(value);
        return buffer.array();
    }

    /**
     * Converts Float into byte array.
     * @param value The Float value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 4 bytes of the Float.
     */
    public static byte[] convertToByteArray(float value) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put((byte)5);
        buffer.position(1);
        buffer.putFloat(value);
        return buffer.array();
    }

    /**
     * Converts Double into byte array.
     * @param value The Double value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The rest are the 8 bytes of the Double.
     */
    public static byte[] convertToByteArray(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put((byte)6);
        buffer.position(1);
        buffer.putDouble(value);
        return buffer.array();
    }

    /**
     * Converts String into byte array.
     * @param value The String value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The next are the 4 bytes telling the size of the string as an int byte array.
     * The rest is each character of the string in byte form.
     */
    public static byte[] convertToByteArray(String value) {
        Integer strLength = value.length();
        ByteBuffer buffer = ByteBuffer.allocate(strLength + 5);
        buffer.put((byte)7);
        buffer.position(1);
        buffer.putInt(strLength);
        buffer.position(5);
        buffer.put(value.getBytes());
        return buffer.array();

    }

    /**
     * Converts Boolean into byte array.
     * @param value The Boolean value to be converted.
     * @return Byte array of the value. 1st byte is the identifier. The next byte is 1 or 0 for true or false.
     */
    public static byte[] convertToByteArray(boolean value) {
        byte[] array = new byte[2];
        array[0] = 8;
        array[1] = (byte)(value ? 1 : 0);
        return array;
    }

    //

    /**
     * This is used to convert the byte array received from a client into the method name and parameters for a method call.
     * The format is the same as the SendConversion method's output
     * @param array The byte array to be converted
     * @return An object array containing the method name and the parameters for said method.
     */
    public static List<Object> ReceiveConversion(byte[] array){
        //String methodName = mapMethod.get(array[0]);
        List<Object> paramaters = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            switch (array[i]){
                case 1:
                    paramaters.add(convertToChar(Arrays.copyOfRange(array, i+1, i+3)));
                    i += 2;
                    break;
                case 2:
                    paramaters.add(convertToInteger(Arrays.copyOfRange(array, i+1, i+5)));
                    i += 4;
                    break;
                case 3:
                    paramaters.add(convertToLong(Arrays.copyOfRange(array, i+1, i+9)));
                    i += 8;
                    break;
                case 4:
                    paramaters.add(convertToShort(Arrays.copyOfRange(array, i+1, i+3)));
                    i += 2;
                    break;
                case 5:
                    paramaters.add(convertToFloat(Arrays.copyOfRange(array, i+1, i+5)));
                    i += 4;
                    break;
                case 6:
                    paramaters.add(convertToDouble(Arrays.copyOfRange(array, i+1, i+9)));
                    i += 8;
                    break;
                case 7:
                    int length = convertToInteger(Arrays.copyOfRange(array, i+1, i+5));
                    i += 5;
                    try {
                        paramaters.add(new String(Arrays.copyOfRange(array, i, i + length), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    i += length - 1;
                    break;
                case 8:
                    paramaters.add(convertToBoolean(array[i+1]));
                    i += 1;
                    break;
                case 9:
                    int length2 = convertToInteger(Arrays.copyOfRange(array, i+1, i+5));
                    i += 5;
                    try {
                        String strUUID = convertToString(Arrays.copyOfRange(array, i, i + length2));
                        UUID uuid = UUID.fromString(strUUID);
                        paramaters.add(uuid);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    i += length2 - 1;
                    break;
            }
        }

        return paramaters;
    }

    /**
     * Converts a byte array to a Char
     * @param array Byte array to be converted.
     * @return Char value
     */
    public static char convertToChar(byte[] array){
        return ByteBuffer.wrap(array).getChar();
    }

    /**
     * Converts a byte array to a Int
     * @param array Byte array to be converted.
     * @return Int value
     */
    public static Integer convertToInteger(byte[] array){
        return new BigInteger(array).intValue();
    }

    /**
     * Converts a byte array to a Long
     * @param array Byte array to be converted.
     * @return Long value
     */
    public static long convertToLong(byte[] array){
        return ByteBuffer.wrap(array).getLong();
    }

    /**
     * Converts a byte array to a Short
     * @param array Byte array to be converted.
     * @return Short value
     */
    public static short convertToShort(byte[] array){
        return ByteBuffer.wrap(array).getShort();
    }

    /**
     * Converts a byte array to a Float
     * @param array Byte array to be converted.
     * @return Float value
     */
    public static float convertToFloat(byte[] array){
        return ByteBuffer.wrap(array).getFloat();
    }

    /**
     * Converts a byte array to a Double
     * @param array Byte array to be converted.
     * @return Double value
     */
    public static double convertToDouble(byte[] array){
        return ByteBuffer.wrap(array).getDouble();
    }

    /**
     * Converts a byte array to a String
     * @param array Byte array to be converted.
     * @return String value
     */
    public static String convertToString(byte[] array){
        String temp = "";
        try {
            temp = new String(array, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  temp;
    }

    /**
     * Converts a byte array to a Boolean.
     * @param array Byte array to be converted.
     * @return Boolean value
     */
    public static boolean convertToBoolean(byte array){
        return (Byte.toString(array) == "1");
    }
}
