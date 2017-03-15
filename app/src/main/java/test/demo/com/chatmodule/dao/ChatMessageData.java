package test.demo.com.chatmodule.dao;

import java.io.Serializable;

/**
 * Created by Admin on 3/8/2017.
 */

public class ChatMessageData implements Serializable {
    public String user_from;
    public String user_to;
    public String msg_id;
    public String type;
    public String message;
    public boolean is_like;
    public String date;
    public String time;
    public String received;
    public String first_name;
    public String picture_url;
}
