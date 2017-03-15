package test.demo.com.chatmodule.views;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import test.demo.com.chatmodule.Adapters.DetailChatAdapter;
import test.demo.com.chatmodule.R;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.ChatDetailData;
import test.demo.com.chatmodule.dao.ChatMessageData;
import test.demo.com.chatmodule.dao.SendMessageData;
import test.demo.com.chatmodule.dao.UserInfoData;
import test.demo.com.chatmodule.database.DatabaseManager;
import test.demo.com.chatmodule.model.ChatDetailModel;
import test.demo.com.chatmodule.utility.AppConstants;
import test.demo.com.chatmodule.utility.Util;

public class ChatDetailActivity extends AppCompatActivity implements View.OnClickListener, Observer, SwipeRefreshLayout.OnRefreshListener {

    private android.widget.ListView recyclerviewchatroom;
    private android.widget.EditText message;
    private android.widget.ImageView btnsend;
    private UserInfoData userInfoData;
    private ChatDetailModel chatDetailModel = new ChatDetailModel();
    private DetailChatAdapter detailChatAdapter;
    private ArrayList<ChatMessageData> messagesList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout pullRefreshLayout;
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                ChatMessageData chatMessageData = ((ChatMessageData) intent.getSerializableExtra(AppConstants.SERIALIZABLE_DATA));
                messagesList.add(chatMessageData);
                notifyAdapter();
                scrollMyListViewToBottom();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatApplication.currentActivity = ChatDetailActivity.this;
        chatDetailModel.addObserver(ChatDetailActivity.this);
        setContentView(R.layout.activity_chat_detail);
        init();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(AppConstants.ACTION_FOR_CHAT_BROADCAST);
        this.registerReceiver(chatReceiver, intentFilter);
    }

    /**
     * method to initialize all views related to this screen
     */
    private void init() {
        messagesList.clear();
        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.SERIALIZABLE_DATA)) {
            userInfoData = ((UserInfoData) intent.getSerializableExtra(AppConstants.SERIALIZABLE_DATA));
        }
        progressDialog = new ProgressDialog(ChatDetailActivity.this);
        this.btnsend = (ImageView) findViewById(R.id.btn_send);
        this.message = (EditText) findViewById(R.id.message);
        this.recyclerviewchatroom = (ListView) findViewById(R.id.recycler_view_chatroom);
        this.pullRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorPrimary);
        pullRefreshLayout.setOnRefreshListener(this);
        btnsend.setOnClickListener(this);
        detailChatAdapter = new DetailChatAdapter(ChatDetailActivity.this, messagesList, ChatDetailActivity.this);
        recyclerviewchatroom.setAdapter(detailChatAdapter);
        getChatDetail();
    }

    @Override
    public void onClick(View v) {
        int ItemId = v.getId();
        switch (ItemId) {
            case R.id.btn_send:
                String messageText = message.getText().toString().trim();
                if (TextUtils.isEmpty(messageText)) {
                    Toast.makeText(this, getResources().getString(R.string.empty_message_alert), Toast.LENGTH_SHORT).show();
                    return;
                }
                send_msg(messageText);
                break;
        }
    }

    private void send_msg(String textMessage) {
        if (Util.hasInternet(this)) {
            Map params = new HashMap();
            params.put("user_from", ChatApplication.FROM_USER_ID);
            params.put("user_to", userInfoData.user_to);
            params.put("message", textMessage);
            params.put("type", "Text");
            progressDialog.show();
            chatDetailModel.sendChat(params);
            message.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatApplication.currentActivity = ChatDetailActivity.this;
        registerReceiver();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(chatReceiver);
        super.onStop();
    }

    private void getChatDetail() {
        if (Util.hasInternet(this)) {
            Map params = new HashMap();
            params.put("user_from", ChatApplication.FROM_USER_ID);
            params.put("user_to", userInfoData.user_to);
            progressDialog.show();
            chatDetailModel.getUsersDetailChat(params);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        try {
            if (o instanceof SendMessageData) {
                SendMessageData sendMessageData = ((SendMessageData) o);
                if (sendMessageData.status) {
                    messagesList.addAll(sendMessageData.message_list);
                    notifyAdapter();
                    scrollMyListViewToBottom();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.unable_to_send_message), Toast.LENGTH_SHORT).show();
                }
            } else if (o instanceof ChatDetailData) {
                ChatDetailData chatDetailData = ((ChatDetailData) o);
                if (chatDetailData.status) {
                    if (chatDetailData.message_list != null && !chatDetailData.message_list.isEmpty()) {
                        messagesList.addAll(0, chatDetailData.message_list);
                        notifyAdapter();
                        scrollMyListViewToBottom();
                        saveHistoryToLocalDatabase(chatDetailData.message_list);
                    }
                } else {
                    Toast.makeText(this, chatDetailData.message, Toast.LENGTH_SHORT).show();
                }
            } else if (o instanceof VolleyError) {
                Toast.makeText(ChatDetailActivity.this, getResources().getString(R.string.default_server_error), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method to notify the adapter whenever data in the list is changed
     */
    private void notifyAdapter() {
        if (detailChatAdapter != null) {
            detailChatAdapter.notifyDataSetChanged();
        }
    }

    private void saveHistoryToLocalDatabase(ArrayList<ChatMessageData> list) {
        try {
            DatabaseManager.getInstance(ChatDetailActivity.this).insertDataToChatHistoryTable(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Select the last row so it will scroll into view...
     */
    private void scrollMyListViewToBottom() {
        recyclerviewchatroom.post(new Runnable() {
            @Override
            public void run() {
                recyclerviewchatroom.setSelection(detailChatAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onRefresh() {

    }
}
