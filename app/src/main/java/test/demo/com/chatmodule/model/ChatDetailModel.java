package test.demo.com.chatmodule.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;
import java.util.Observable;

import test.demo.com.chatmodule.Url_handler;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.ChatDetailData;
import test.demo.com.chatmodule.dao.SendMessageData;
import test.demo.com.chatmodule.volley.NetworkRequest;

/**
 * Created by phoosaram on 3/8/2017.
 */

public class ChatDetailModel extends Observable implements Response.Listener, Response.ErrorListener {
    public void getUsersDetailChat(Map<String, String> params) {
        NetworkRequest request = new NetworkRequest(Request.Method.POST, Url_handler.CHAT_DETAIL, this, this, params, null, ChatDetailData.class);
        ChatApplication.getVolleyRequestQueue().add(request);
    }

    public void sendChat(Map<String, String> params) {
        NetworkRequest request = new NetworkRequest(Request.Method.POST, Url_handler.SEND_MSG, this, this, params, null, SendMessageData.class);
        ChatApplication.getVolleyRequestQueue().add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        notifyObservers(error);
    }

    @Override
    public void onResponse(Object response) {
        notifyObservers(response);
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
