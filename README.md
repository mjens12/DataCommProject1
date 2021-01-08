# DataCommProject1
Project 1 for Data Communications
The end goal of this project was to create a multi-threaded and text-based FTP server. This server allows for text commands and multiple simultaneous user access.
Using the text commands, the user can connect to the server, list the files currently offered on the server, add and transfer files to be stored to the server,
retrieve specific files from the server, and close the connection to the server. Like a standard FTP server, the server handles two connections with each client.
One transfers the control messages between the client and server, and the other handles the actual transfer of data between the two machines.
We used Java to write this program as it was the language we were most familiar with, and provides effective multi-threading,
socket programming and input and output stream implementation.
