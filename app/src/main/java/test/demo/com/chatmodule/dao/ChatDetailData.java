package test.demo.com.chatmodule.dao;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 3/8/2017.
 */

public class ChatDetailData extends CommonResponseData implements Serializable {
    public static final String FLD_USER_FROM = "user_from";
    public static final String FLD_USER_TO = "user_to";
    public static final String FLD_MSG_ID = "msg_id";
    public static final String FLD_TYPE = "type";
    public static final String FLD_MESSAGE = "message";
    public static final String FLD_IS_LIKE = "is_like";
    public static final String FLD_DATE = "date";
    public static final String FLD_TIME = "time";
    public static final String FLD_RECEIVED = "received";
    public static final String FLD_FIRST_NAME = "first_name";
    public static final String FLD_PICTURE_URL = "picture_url";
    public static final String FLD_ATTACHMENT = "attachments";
    public static final String FLD_ATTACHMENT_FILE_NAME = "attachment_file_name";
    public static final String FLD_EXTENSION = "extension";
    public static final String FLD_ATTACHMENT_DOWNLOAD_PATH = "attachment_download_path";

    public boolean message_list_status;
    public ArrayList<ChatMessageData> message_list;
}
