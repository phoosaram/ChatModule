package test.demo.com.chatmodule.firebase;


import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

import test.demo.com.chatmodule.Url_handler;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.CommonResponseData;
import test.demo.com.chatmodule.volley.NetworkRequest;

/**
 * Created by Admin on 3/8/2017.
 */

public class FirebaseTokenService extends FirebaseInstanceIdService implements Response.ErrorListener, Response.Listener {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        updateFCMTokenOnServer(token);
    }

    /**
     * method to update fcm token on the server
     *
     * @param token
     */
    private void updateFCMTokenOnServer(String token) {
        try {
            if (!TextUtils.isEmpty(ChatApplication.FROM_USER_ID)) {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", ChatApplication.FROM_USER_ID);
                params.put("gcm_regid", token);
                NetworkRequest networkRequest = new NetworkRequest(Request.Method.POST, Url_handler.UPDATE_FCM_TOKEN, FirebaseTokenService.this, FirebaseTokenService.this, params, null, CommonResponseData.class);
                ChatApplication.getVolleyRequestQueue().add(networkRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Object response) {
        if (response instanceof CommonResponseData) {
            // TODO your code here after successfully updating the fcm token
        }
    }
}
