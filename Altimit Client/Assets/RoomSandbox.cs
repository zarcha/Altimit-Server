using UnityEngine;
using System.Collections;
using Altimit;
using UnityEngine.UI;

public class RoomSandbox: MonoBehaviour {

    public GameObject roomLabel;
    public GameObject userCountLabel;
    public GameObject newRoomTextbox;

	void Update () {
        roomLabel.GetComponent<Text>().text = "Current Room: " + AltimitRoom.RoomName;
        userCountLabel.GetComponent<Text>().text = "User Count: " + AltimitRoom.UsersInRoom;
    }

    public void JoinRoom()
    {
        AltimitRoom.JoinRoom(newRoomTextbox.GetComponent<InputField>().text);
    }
}
