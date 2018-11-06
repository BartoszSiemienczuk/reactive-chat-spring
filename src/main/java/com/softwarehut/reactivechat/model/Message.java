package com.softwarehut.reactivechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "messages")
@Data
@NoArgsConstructor
public class Message {
    @Id
    private String id;

    private String content;
    private String room;
    private String user;

    private Date createdAt = new Date();

    public Message(String content, String room) {
        this.content = content;
        this.room = room;
        this.user = "anonymous";
    }

    public Message(String content, String room, String user) {
        this(content, room);
        this.user = user;
    }
}
