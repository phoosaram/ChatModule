package test.demo.com.chatmodule.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import test.demo.com.chatmodule.Adapters.UserListAdapter;
import test.demo.com.chatmodule.R;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.UserInfoData;
import test.demo.com.chatmodule.dao.UsersData;
import test.demo.com.chatmodule.database.DatabaseManager;
import test.demo.com.chatmodule.model.ChatUsersModel;
import test.demo.com.chatmodule.utility.AppConstants;
import test.demo.com.chatmodule.utility.Util;

public class ChatList extends AppCompatActivity implements View.OnClickListener, Observer {

    private ChatUsersModel chatUsersModel = new ChatUsersModel();
    private android.widget.ListView list;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatApplication.currentActivity = ChatList.this;
        chatUsersModel.addObserver(ChatList.this);
        setContentView(R.layout.chat_list);
        init();
    }

    /**
     * method to initialize all views related to this screen
     */
    private void init() {
        this.list = (ListView) findViewById(R.id.list);
        progressDialog = new ProgressDialog(ChatList.this);
        getUsersList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatApplication.currentActivity = ChatList.this;
        setUsersOnUI();
    }

    /**
     * method to fetch users list
     */
    private void getUsersList() {
        if (Util.hasInternet(this)) {
            Map params = new HashMap();
            params.put("user_from", ChatApplication.FROM_USER_ID);
            progressDialog.show();
            chatUsersModel.getUsersList(params);
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_error_message), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        int vid = view.getId();
        if (vid == R.id.llUserInfoContainer) {
            UserInfoData userInfoData = ((UserInfoData) view.getTag());
            Intent intent = new Intent(ChatList.this, ChatDetailActivity.class);
            intent.putExtra(AppConstants.SERIALIZABLE_DATA, userInfoData);
            startActivity(intent);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        try {
            if (o instanceof UsersData) {
                UsersData usersData = ((UsersData) o);
                if (usersData.status) {
                    if (usersData.listuser != null && !usersData.listuser.isEmpty()) {
                        DatabaseManager.getInstance(getApplicationContext()).insertDataToUsersTable(usersData.listuser);
                        setUsersOnUI();
                    }
                } else {
                    Toast.makeText(this, usersData.message, Toast.LENGTH_SHORT).show();
                }
            } else if (o instanceof VolleyError) {
                Toast.makeText(ChatList.this, getResources().getString(R.string.default_server_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUsersOnUI() {
        try {
            ArrayList<UserInfoData> usersList = DatabaseManager.getInstance(getApplicationContext()).fetchUsersList();
            UserListAdapter userListAdapter;
            userListAdapter = new UserListAdapter(ChatList.this, usersList, ChatList.this);
            list.setAdapter(null);
            list.setAdapter(userListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
