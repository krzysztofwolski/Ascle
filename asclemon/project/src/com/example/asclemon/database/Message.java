package com.example.asclemon.database;

import java.util.ArrayList;

public class Message {
int Id;
public int SenderId;
int ReceiverId;
public String text;
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
