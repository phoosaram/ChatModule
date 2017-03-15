package test.demo.com.chatmodule.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import test.demo.com.chatmodule.Fonts.Roboto_Bold_TextView;
import test.demo.com.chatmodule.R;
import test.demo.com.chatmodule.dao.UserInfoData;
import test.demo.com.chatmodule.utility.Util;

/**
 * Created by Admin on 3/6/2017.
 */

public class UserListAdapter extends ArrayAdapter<UserInfoData> {
    private Context context;
    private ArrayList<UserInfoData> data = null;
    private View.OnClickListener clickListener;
    private LayoutInflater inflater;

    public UserListAdapter(Context context, ArrayList<UserInfoData> data, View.OnClickListener clickListener) {
        super(context, -1, data);
        this.context = context;
        this.data = data;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(this.context);
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.chatting_list_adapter_layout, null);
            holder = new ViewHolder();
            holder.userimage = (CircleImageView) view.findViewById(R.id.chatting_list_user_img);
            holder.username = (Roboto_Bold_TextView) view.findViewById(R.id.chatting_list_user_name);
            holder.textview_lastmsg = (Roboto_Bold_TextView) view.findViewById(R.id.textview_lastmsg);
            holder.llUserInfoContainer = (LinearLayout) view.findViewById(R.id.llUserInfoContainer);
            view.setTag(holder);
        } else {
            holder = ((ViewHolder) view.getTag());
        }
        UserInfoData userInfoData = data.get(position);
        holder.llUserInfoContainer.setTag(userInfoData);
        holder.llUserInfoContainer.setOnClickListener(clickListener);
        holder.username.setText(userInfoData.username);
        holder.textview_lastmsg.setText(userInfoData.last_message != null ? userInfoData.last_message.message : "");
        Util.loadImage(context, userInfoData.picture_url, holder.userimage);
        return view;
    }

    private class ViewHolder {
        protected de.hdodenhof.circleimageview.CircleImageView userimage;
        protected TextView username, textview_lastmsg;
        protected LinearLayout llUserInfoContainer;
    }
}

