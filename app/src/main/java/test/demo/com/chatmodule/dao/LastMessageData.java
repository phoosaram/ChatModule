package test.demo.com.chatmodule.dao;

import java.io.Serializable;

/**
 * Created by Admin on 3/8/2017.
 */

public class LastMessageData implements Serializable {
    public String user_from;
    public String msg_id;
    public String type;
    public String message;
    public String date;
    public String time;
}
