package test.demo.com.chatmodule.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import test.demo.com.chatmodule.Fonts.Roboto_Bold_TextView;
import test.demo.com.chatmodule.R;
import test.demo.com.chatmodule.application.ChatApplication;
import test.demo.com.chatmodule.dao.ChatMessageData;
import test.demo.com.chatmodule.utility.Util;

/**
 * Created by Admin on 3/7/2017.
 */

public class DetailChatAdapter extends ArrayAdapter<ChatMessageData> {
    Context context;
    ArrayList<ChatMessageData> data = null;
    private View.OnClickListener clickListener;
    private LayoutInflater inflater;

    public DetailChatAdapter(Activity context, ArrayList<ChatMessageData> data, View.OnClickListener clickListener) {
        super(context, R.layout.detailchatting_list_adapter_layout, data);
        this.context = context;
        this.data = data;
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(this.context);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        ChatMessageData value = data.get(position);
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.detailchatting_list_adapter_layout, null);
            holder.recieverlayout = (LinearLayout) view.findViewById(R.id.reciever_layout);
            holder.recieveruserimg = (CircleImageView) view.findViewById(R.id.reciever_user_img);
            holder.recieverlastmsg = (Roboto_Bold_TextView) view.findViewById(R.id.reciever_lastmsg);
            holder.recieverusername = (Roboto_Bold_TextView) view.findViewById(R.id.reciever_user_name);
            holder.senderlayout = (LinearLayout) view.findViewById(R.id.sender_layout);
            holder.senderlastmsg = (Roboto_Bold_TextView) view.findViewById(R.id.sender_lastmsg);
            holder.senderusername = (Roboto_Bold_TextView) view.findViewById(R.id.sender_user_name);
            holder.senderimg = (CircleImageView) view.findViewById(R.id.sender_img);
            view.setTag(holder);
        } else {
            holder = ((ViewHolder) view.getTag());
        }
        holder.recieverlayout.setTag(value);
        holder.recieverlayout.setOnClickListener(clickListener);
        holder.senderlayout.setOnClickListener(clickListener);

        holder.user_from = value.user_from;
        if (holder.user_from.equalsIgnoreCase(ChatApplication.FROM_USER_ID)) {
            holder.senderlayout.setVisibility(View.GONE);
            holder.recieverlayout.setVisibility(View.VISIBLE);
            holder.recieverlastmsg.setText(value.message);
            holder.recieverusername.setText(value.first_name);
            Util.loadImage(context, value.picture_url, holder.recieveruserimg);
        } else {
            holder.recieverlayout.setVisibility(View.GONE);
            holder.senderlayout.setVisibility(View.VISIBLE);
            holder.senderusername.setText(value.first_name);
            holder.senderlastmsg.setText(value.message);
            Util.loadImage(context, value.picture_url, holder.senderimg);
        }
        return view;
    }

    private class ViewHolder {
        private String user_from;
        private CircleImageView senderimg;
        private Roboto_Bold_TextView senderusername;
        private Roboto_Bold_TextView senderlastmsg;
        private android.widget.LinearLayout senderlayout;
        private Roboto_Bold_TextView recieverusername;
        private Roboto_Bold_TextView recieverlastmsg;
        private CircleImageView recieveruserimg;
        private android.widget.LinearLayout recieverlayout;
    }

}