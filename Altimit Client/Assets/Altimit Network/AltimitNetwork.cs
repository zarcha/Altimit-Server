using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Reflection;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text;
using System.Linq;

namespace Altimit {

	public class StateObject {
		//Client Socket
		public Socket workSocket = null;
		//Message Size
		public const int BufferSize = 1024;
		//Message
		public byte[] buffer = new byte[BufferSize];
	}

	public class AltimitNetwork : MonoBehaviour {

		//Need this to send data.
		private static Socket sendSocket = null;

		//Needed for messaging 
		static int fullMessageSize = 0; //Size of the array of the current data being read
		static byte[] fullMessage = new byte[0]; //

		//All threads created by messages
		static List<Thread> threadList = new List<Thread> ();

		//This clients UUID used to identify its self on the server
        public static Guid playerUUID;

		//<summary>
		// Connect to an Altimit Server at a specified ip and port
		//</summary>
        public static void Connect(String ip, int port){

            Thread.Sleep(1000);

            AltimitMethod.CompileAltimitMethods ();

			try{
				IPHostEntry ipHostInfo = Dns.GetHostEntry (ip);

				IPAddress ipAddress = null;

				foreach(IPAddress addr in ipHostInfo.AddressList){
					IPAddress address;
					if (IPAddress.TryParse(addr.ToString(), out address))
					{
						switch (address.AddressFamily)
						{
						case System.Net.Sockets.AddressFamily.InterNetwork:
							ipAddress = addr;
							break;
						default:
							// umm... yeah... I'm going to need to take your red packet and...
							break;
						}
					}
				}
			
				IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);

				Thread.Sleep (1000);

				Socket client = new Socket (AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

				client.BeginConnect (remoteEP, new AsyncCallback (ConnectCallBack), client);

			} catch (Exception e){
				Debug.LogError (e.ToString ());
			}
		}

		//<summary>
		// Method ran after connecting to the server.
		//</summary>
		private static void ConnectCallBack(IAsyncResult ar){

			threadList.Add (Thread.CurrentThread);

			try{
                Thread.Sleep(1000);

                Socket client = (Socket) ar.AsyncState;
				client.EndConnect(ar);

				Debug.LogFormat("Client connected to {0}", client.RemoteEndPoint.ToString());

				Receive(client);
				sendSocket = client;
                
                playerUUID = Guid.NewGuid();
				Send ("SetClientUUID", playerUUID);

			} catch (Exception e){
				Debug.LogError (e.ToString ());
			}
		}

		//<summary>
		// Start receving from the socket.
		//</summary>
		private static void Receive(Socket client){
			try{

				Thread.Sleep(1000);
				StateObject state = new StateObject();
				state.workSocket = client;

				client.BeginReceive(state.buffer, 0, StateObject.BufferSize, 0, new AsyncCallback(ReceiveCallBack), state);

			} catch (Exception e){
				Debug.LogError (e.ToString ());
			}
		}

		//<summary>
		// Once the data has been recieved from the socket this is ran.
		//</summary>
		private static void ReceiveCallBack(IAsyncResult ar){
			
			threadList.Add (Thread.CurrentThread);

			byte[] key = { 5, 9, 0, 4 };
			int messageSize = 0;
			int messageOffset = 0;
			byte[] currentMessage;

			try{
				if (isConnected()){
					StateObject state = (StateObject) ar.AsyncState;
					Socket client = state.workSocket;

					int buffSize = client.EndReceive(ar);

					do{
						if(buffSize != 0 || fullMessage.Length != 0){
							if(fullMessage.Length != 0 && buffSize != 0){
								Array.Resize(ref fullMessage, fullMessage.Length + buffSize);
								byte[] newMessage = new byte[buffSize];
								newMessage = AltimitArray.copyOfRange(state.buffer, 0, buffSize);
								Array.ConstrainedCopy(newMessage, 0, fullMessage, fullMessageSize, newMessage.Length);
								messageSize = 0;
							} else if(buffSize != 0){
								fullMessage = new byte[buffSize];
								fullMessage = AltimitArray.copyOfRange(state.buffer, 0, buffSize);
								messageSize = 0;
							} else if(fullMessage.Length != 0){
								messageSize = 0;
							}

							if(messageSize == 0 && fullMessage.Length >= 4){
								messageSize = AltimitConverter.convertToInt(AltimitArray.copyOfRange(fullMessage, 0, 4));
								if(messageSize <= fullMessage.Length){
									byte[] messageKey = AltimitArray.copyOfRange(fullMessage, messageSize - 4, messageSize);
									if(key.SequenceEqual(messageKey)){
										currentMessage = new byte[messageSize - 8];
										messageOffset = 4;
										Array.ConstrainedCopy(fullMessage, messageOffset, currentMessage, 0, currentMessage.Length);
										messageOffset = messageSize;
                                        
                                        new Thread(() =>
                                        {
                                            List<object> sentMessage = AltimitConverter.ReceiveConversion(currentMessage);
                                            InvokeMessage(sentMessage);
                                        }).Start();

                                        fullMessage = AltimitArray.copyOfRange(fullMessage, messageOffset, fullMessage.Length);
										fullMessageSize = fullMessage.Length;

										buffSize = 0;
									} else {
										Debug.Log("Key was not found. Message will try to be completed in next read!");
									}
								}else{
									break;
								}
							}else{
								break;
							}
						}
					}while(fullMessage.Length > 0);

					client.BeginReceive(state.buffer,0,StateObject.BufferSize,0,new AsyncCallback(ReceiveCallBack), state);
				} else {
					Disconnect();
					return;
				}
			} catch(Exception e){
				Debug.LogError (e.ToString ());
				Disconnect ();
				return;
			} 
		}

		//<summary>
		// Invokes a method using the Object List that was returned from conversion 
		//</summary>
		private static void InvokeMessage(List<object> sentMessage){
			String MethodName = (string)sentMessage [0];
			sentMessage.RemoveAt (0);

			AltimitMethod.CallAltimitMethod (MethodName, sentMessage.ToArray());
		}

		//<summary>
		// Starts the sending of a strings method and its paramaters. For now the GUUID is auto added.
		//</summary>
		public static void Send(String MethodName, params object[] data){
            List<object> tempData = data.ToList<object>();
            //TODO: find a better way to auto add uuid's for methods that use it on server side
            tempData.Add(playerUUID);
			byte[] messageData = AltimitConverter.SendConversion (MethodName, tempData.ToArray());

			sendSocket.BeginSend (messageData, 0, messageData.Length, 0, new AsyncCallback (SendCallBack), sendSocket);
		}

		//<summary>
		// This is ran once the sending of data is done.
		//</summary>
		private static void SendCallBack(IAsyncResult ar){
			threadList.Add (Thread.CurrentThread);

			try{
				Socket client = (Socket)ar.AsyncState;

				int bytesSent = client.EndSend (ar);
				Debug.LogFormat ("Sent {0} bytes to server.", bytesSent);
			} catch(Exception e){
				Debug.LogError (e.ToString());
			}
		}

		//<summary>
		// Method used for when the server sends a heatbeat message. Not needed at the moment for anything else.
		//</summary>
		[AltimitRPC]
		public void HeartBeat(){
			Debug.Log ("Server Heart Beat");
		}

		//<summary>
		// Disconnects the client from the server. Server does not know of the disconnection untill next Heart Beat attempt.
		//</summary>
		[AltimitRPC]
		public static void Disconnect(){
			sendSocket.Disconnect (false);
			Debug.Log ("Server has disconnected the client!");
		}

		//<summary>
		// Checks if the client is still connected. This checks the local connection and does not know if the server disconnects from it.
		//</summary>
		public static bool isConnected(){
			return sendSocket.Connected;
		}
	}
}