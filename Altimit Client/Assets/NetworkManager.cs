using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using Altimit;
using System;

public class NetworkManager : MonoBehaviour
{

    // Use this for initialization
    void Start()
    {
        AltimitNetwork.Connect("127.0.0.1", 1025);
    }

}