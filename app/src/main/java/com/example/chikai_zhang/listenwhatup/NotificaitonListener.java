package com.example.chikai_zhang.listenwhatup;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class NotificaitonListener extends AccessibilityService  {

    private static final int ID_TITLE = 16908310;
    private static final int ID_TEXT;
    private static final int ID_FIRST_LINE = 16909023;

    static {
        //com.android.internal.R.id.text


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            ID_TEXT = 16908352;
        else
            ID_TEXT = 16908358; // on 4.0 / 14 and above

        // TODO: Extend further API version resource IDs
    }



    boolean isInit =false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event){
        Log.d("whatapp?","yes");
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            //Do something, eg getting packagename
            final String packagename = String.valueOf(event.getPackageName());
            Notification notification = (Notification) event.getParcelableData();
            Log.d("whatapp?","yes");
            Log.d("whatapp1?",packagename);
            Log.d("whatapp1?", event.getParcelableData().toString());
            Log.d("whatapp2?", event.getText().toString());
            Log.d("whatapp3?", "" + notification.tickerText);
            //notification.getClass().getDeclaredField()

            Bundle extras = notification.extras;
            CharSequence[] lines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

            if (lines!=null){

                for (CharSequence msg: lines){
                    Log.d("whatapp?",msg.toString());
                }
            }
            if (notification==null){
                Log.d("whatapp?","get null");
            }

            RemoteViews views = notification.bigContentView;
            if (views == null) {
                views = notification.contentView;
                Log.d("whatapp?","small object");
            }
            if (views == null)  Log.d("whatapp?","no object");

            Class rvClass = views.getClass();

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
                            Log.w("notificationck", "# Not setText: " + methodName);
                            Log.d("notificationck 5", "yes");
                            continue;
                        }

                        // should be 10 / Character Sequence, here
                        parcel.readInt();

                        // Store the actual string
                        String value = TextUtils.CHAR_SEQUENCE_CREATOR
                                .createFromParcel(parcel).toString();

                        Log.d("notificationck 6", "yes");
                        Log.d("notificationck", "viewId is " + viewId);
                        Log.d("notificationck", "Found value: " + value);

                        if (viewId == ID_FIRST_LINE){
                            int indexDelimiter = value.indexOf(':');

//                            if (indexDelimiter != -1) {
//                                result.sender = value.substring(0, indexDelimiter);
//                                result.message = value
//                                        .substring(indexDelimiter + 2);
//                            }

                        }


                        parcel.recycle();

                    }catch (Exception e) {
                        Log.d("notificationck 9", "yes");
                        Log.e("notificationck", "Error accessing object!", e);
                    }
                }
            } catch (Exception e) {
                Log.d("notificationck 10", "yes");
                Log.e("notificationck", "Could not access mActions!", e);
            }


        }
    }

    @Override
    protected void onServiceConnected() {
        if (isInit) {
            return;
        }
        Log.d("come here?", "yes");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;

        info.packageNames = new String[]{"com.whatsapp"};
        //info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        setServiceInfo(info);
        isInit = true;
    }
    @Override
    public void onInterrupt() {
        isInit = false;
    }
}
