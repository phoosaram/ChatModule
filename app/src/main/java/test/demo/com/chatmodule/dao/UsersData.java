package test.demo.com.chatmodule.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 3/6/2017.
 */

public class UsersData extends CommonResponseData implements Serializable {
    public static final String FLD_ID = "_id";
    public static final String FLD_USER_ID = "user_id";
    public static final String FLD_PROFILE_PIC_URL = "profile_pic_url";
    public static final String FLD_DISPLAY_NAME = "display_name";
    public static final String FLD_USER_FROM = "user_from";
    public static final String FLD_MSG_ID = "msg_id";
    public static final String FLD_TYPE = "type";
    public static final String FLD_MESSAGE = "message";
    public static final String FLD_DATE = "date";
    public static final String FLD_TIME = "time";

    public ArrayList<UserInfoData> listuser;
}
