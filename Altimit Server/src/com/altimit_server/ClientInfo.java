package com.altimit_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Information for of a client
 */
class ClientInfo {

    /** Client socket **/
    Socket socket;
    /** Data stream to send information to clients. **/
    DataOutputStream outputStream;
    /** Data stream to get information from clients **/
    DataInputStream inputStream;
    /** The clients thread used for all client messages **/
    Thread clientThread;

    /**
     * Client info constructor.
     * @param socket Socket of the client.
     * @param outputStream Data stream to send information to clients.
     * @param inputStream Data stream to get information from clients.
     * @param clientThread The clients thread used for all client messages.
     */
    ClientInfo(Socket socket, DataOutputStream outputStream, DataInputStream inputStream, Thread clientThread) {
        this.socket = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.clientThread = clientThread;
    }
}
