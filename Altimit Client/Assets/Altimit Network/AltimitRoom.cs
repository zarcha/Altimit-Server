using UnityEngine;
using System.Collections;
using Altimit;
using System;
using System.Timers;

namespace Altimit
{
    public class AltimitRoom : MonoBehaviour
    {

        private static string roomName;
		//<summary>
		// Gets the name of the room currently bound to the user.
		//</summary>
        public static string RoomName
        {
            get
            {
                return roomName;
            }
        }

        private static bool roomOwner;
		//<summary>
		// Tells if the client is the owner of the room or not.
		//</summary>
        public static bool RoomOwner
        {
            get
            {
                return roomOwner;
            }
        }

        private static int usersInRoom;
		//<summary>
		// Number of users in the room currently.
		//</summary>
        public static int UsersInRoom
        {
            get
            {
                return usersInRoom;
            }
        }

		//<summary>
		// Joins the specified room.
		//</summary>
        public static void JoinRoom(String roomName)
        {
            AltimitNetwork.Send("JoinRoom", roomName, -1);
        }

		//<summary>
		// Joins a room but sets a max user if that room is being created.
		//</summary>
        public static void JoinRoom(String roomName, int Max)
        {
            AltimitNetwork.Send("JoinRoom", roomName, Max);
        }

		//<summary>
		// Sets certain data when the Join room was confirmed.
		//</summary>
        [AltimitRPC]
        public void JoinedRoom(string sentRoomName, bool created)
        {
            Debug.Log("Joined room: " + sentRoomName);
            roomName = sentRoomName;
            roomOwner = created;
        }

		//<summary>
		// Leaves the current room.
		//</summary>
        public void LeaveRoom()
        {
            AltimitNetwork.Send("LeaveRoom");
        }

		//<summary>
		// Sets the current number of users in a room.
		//</summary>
        [AltimitRPC]
        public void SetUserCount(int userCount)
        {
            Debug.Log("got stuff");
            usersInRoom = userCount;
        }
    }
}
