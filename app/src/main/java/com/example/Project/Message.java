package com.example.Project;

class Message {
    static final int TYPE_RECEIVE = 1;
    static final int TYPE_SEND = 2;
    String content;
    String time;
    String user_name;
    int id = 0;
    String user_id = "";
    int type;

    public Message(String content, String user_id, String user_name) {
        this.content = content;
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public Message(int id, String content, String user_id, String user_name, int type, String time) {
        this.content = content;
        this.type = type;
        this.time = time;
        this.user_name = user_name;
        this.id = id;
        this.user_id = user_id;
    }
}
