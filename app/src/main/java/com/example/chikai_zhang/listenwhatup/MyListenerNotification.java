package com.example.chikai_zhang.listenwhatup;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MyListenerNotification extends NotificationListenerService {

    private String TAG = "MyListenerNotification" ;
    private static final int ID_TITLE = 16908310;
    private static final int ID_TEXT;

    static {
         //com.android.internal.R.id.text


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            ID_TEXT = 16908352;
        else
            ID_TEXT = 16908358; // on 4.0 / 14 and above

        // TODO: Extend further API version resource IDs
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.d("notificationck 0", sbn.getPackageName());
        Log.d("notificationck 1", ""+sbn.getNotification().tickerText);

        RemoteViews views = sbn.getNotification().contentView;
        Class<?> rvClass = views.getClass();

// access private list mActions
        Field field = null;

        try {
            Log.d("notificationck 2", "yes");
            field = rvClass.getDeclaredField("mActions");
            field.setAccessible(true);

            @SuppressWarnings("uncheck")
            ArrayList<Parcelable> actions = (ArrayList<Parcelable>)field.get(views);
            Log.d("notificationck 3", "yes");

            for (Parcelable action : actions){

                try{
                    Log.d("notificationck 4", "yes");
                    // create parcel from action
                    Parcel parcel = Parcel.obtain();
                    action.writeToParcel(parcel, 0);
                    parcel.setDataPosition(0);

                    // check if is 2 / ReflectionAction
                    int tag = parcel.readInt();
                    if (tag != 2)
                        continue;

                    int viewId = parcel.readInt();

                    String methodName = parcel.readString();

                    if (methodName == null || !methodName.equals("setText")) {
                        Log.w(TAG, "# Not setText: " + methodName);
                        Log.d("notificationck 5", "yes");
                        continue;
                    }

                    // should be 10 / Character Sequence, here
                    parcel.readInt();

                    // Store the actual string
                    CharSequence value = TextUtils.CHAR_SEQUENCE_CREATOR
                            .createFromParcel(parcel);
                    Log.d("notificationck 6", "yes");
                    Log.d(TAG, "viewId is " + viewId);
                    Log.d(TAG, "Found value: " + value.toString());

                    if (viewId == ID_TITLE){
                        Log.d("notificationck 7", "yes");
                        Log.d(TAG, "value is " + value.toString());}
                    else if (viewId == ID_TEXT){
                        Log.d("notification 8", "yes");
                        Log.d(TAG, "value is " + value.toString());}

                    parcel.recycle();

                }catch (Exception e) {
                    Log.d("notificationck 9", "yes");
                    Log.e(TAG, "Error accessing object!", e);
                }
            }
        } catch (Exception e) {
            Log.d("notificationck 10", "yes");
            Log.e(TAG, "Could not access mActions!", e);
        }




    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d("zpf", "shut" + "-----" + sbn.toString());

    }
}
