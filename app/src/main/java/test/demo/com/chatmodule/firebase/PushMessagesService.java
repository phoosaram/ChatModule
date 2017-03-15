package test.demo.com.chatmodule.firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Map;

import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.ChatPushNotificationData;
import test.demo.com.chatmodule.utility.AppConstants;
import test.demo.com.chatmodule.utility.Util;
import test.demo.com.chatmodule.views.ChatDetailActivity;

/**
 * Created by Admin on 3/8/2017.
 */

public class PushMessagesService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> messageData = remoteMessage.getData();
        if (messageData.containsKey(AppConstants.PN_TYPE)) {
            if ("chat".equals(messageData.get(AppConstants.PN_TYPE))) {
                String bodyData = messageData.get("body");
                handleMessage(bodyData);
            }
        }
    }

    /**
     * method to handle receive push message
     *
     * @param receivedMessage
     */
    private void handleMessage(String receivedMessage) {
        try {
            ChatPushNotificationData pushNotificationData = new Gson().fromJson(receivedMessage, ChatPushNotificationData.class);
            if (pushNotificationData != null) {
                if (ChatApplication.currentActivity instanceof ChatDetailActivity) {
                    Intent intent = new Intent(AppConstants.ACTION_FOR_CHAT_BROADCAST);
                    intent.putExtra(AppConstants.SERIALIZABLE_DATA, pushNotificationData.msg);
                    PushMessagesService.this.sendBroadcast(intent);
                } else {
                    Util.generateNotification(pushNotificationData.msg, getApplicationContext(),pushNotificationData.msg.first_name+":"+pushNotificationData.msg.message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
