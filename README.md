# Altimit-Server
Open source Java server and C# Unity Client

Running Server:
---
Step 1: Open a cmd/terminal in this folder.

Step 2: Type "java -jar Altimit_Hazelcast.jar" and press Enter. Hazelcast will start up.

Step 3: Open a second cmd/terminal in the same folder.

Step 4: Type "java -jar Altimit_Server.jar" and press Enter. The Server will run and will say "Ready for clients" when its done.


The server runs on the defualt port of 1025. This can currently only be changed in the server code within the Main thread where StartServer method is called.


Running Control Panel:
---
//NOTE: This will change in the future to just work when the server is ran. Sorry for the trouble.

Step 1: Install a web server application that will work with javascript.

Step 2: Move the Control Panel files into the web servers html folder.


Connecting to Server in Unity:

Step 1: Import the AltimitNetwork package into your project.

Step 2: Create a new C# script. This will be the network manager.

Step 3: Type this under the other using's.
```javascript 
using Altimit; 
``` 

Step 4: Type this:
```javascript 
"AltimitNetwork.Connect("127.0.0.1", 1025);". 
``` 
When ran the connection will start. Currently there is a little pause when connecting (due to threading hold-up. Should be fixed soon).



Known Issues:
---
1) Pause when starting connection (Compiling the list of RPC mothods does this)

2) Everything is threaded so methods that are called from the server or called methods running other methods will not work with unity specific thigns like spawning of items since that needs to happen on the main thread. This will be fixed when AltimitView is done and has Altimit.RPC feature in.

3) If there is issues with joining a room after connecting (Disconnection on the client before server says it disconnected them) just restart all the IDE's. It seems not to be a server error. Might just be me though.

