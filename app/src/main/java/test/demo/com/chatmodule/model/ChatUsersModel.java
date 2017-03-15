package test.demo.com.chatmodule.model;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

import test.demo.com.chatmodule.Url_handler;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.UsersData;
import test.demo.com.chatmodule.volley.NetworkRequest;

/**
 * Created by phoosaram on 3/7/2017.
 */

public class ChatUsersModel extends Observable implements Response.Listener, Response.ErrorListener {
    public void getUsersList(Map<String, String> params) {
        NetworkRequest request = new NetworkRequest(Request.Method.POST, Url_handler.CHAT_LIST, this, this, params, null, UsersData.class);
        ChatApplication.getVolleyRequestQueue().add(request);
    }


    @Override
    public void onResponse(Object response) {
        notifyObservers(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        notifyObservers(error);
    }

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
