package test.demo.com.chatmodule.utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import test.demo.com.chatmodule.R;
import test.demo.com.chatmodule.views.ChatList;

/**
 * Created by Admin on 3/6/2017.
 */

public class Util {
    public static boolean hasInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        try {
            Glide.with(context).load(url).thumbnail(0.1f).error(R.mipmap.ic_launcher).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateNotification(Serializable extra, Context context, String content) {
        try {
            Intent intent = new Intent(context, ChatList.class);
            intent.putExtra(AppConstants.SERIALIZABLE_DATA, extra);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setContentTitle(context.getString(R.string.app_name));
            notificationBuilder.setContentText(content);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
