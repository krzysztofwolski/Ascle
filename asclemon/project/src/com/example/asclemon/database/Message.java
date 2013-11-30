package com.example.asclemon.database;

public class Message {
int Id;
int SenderId;
int ReceiverId;
String text;
boolean isNew;


public Message(int id, int senderId, int receiverId, String text, boolean isNew) {
	super();
	Id = id;
	SenderId = senderId;
	ReceiverId = receiverId;
	this.text = text;
	this.isNew = isNew;
}


}
