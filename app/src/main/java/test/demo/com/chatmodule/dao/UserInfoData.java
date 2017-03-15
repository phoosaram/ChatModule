package test.demo.com.chatmodule.dao;

import java.io.Serializable;

/**
 * Created by Admin on 3/7/2017.
 */

public class UserInfoData implements Serializable {
    public String user_to;
    public String picture_url;
    public String username;
    public long lastUpdatedAtTrno;
    public LastMessageData last_message;
}
