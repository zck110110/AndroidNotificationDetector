package com.example.chikai_zhang.listenwhatup;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;

import java.util.List;


public class MainActivity extends Activity {

    static String LOGTAG ="myaccess";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected  void  onResume(){

        super.onResume();
//        Intent goToSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivity(goToSettings);
        logInstalledAccessiblityServices(getApplicationContext());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static void logInstalledAccessiblityServices(Context context) {

        AccessibilityManager am = (AccessibilityManager) context
                .getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> runningServices = am
                .getInstalledAccessibilityServiceList();
        for (AccessibilityServiceInfo service : runningServices) {
            Log.i(LOGTAG, service.getId());
        }
    }

//    public boolean isAccessibilityEnabled(){
//        int accessibilityEnabled = 0;
//        final String LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService";
//        boolean accessibilityFound = false;
//        try {
//            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(),android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//            Log.d("access", "ACCESSIBILITY: " + accessibilityEnabled);
//        } catch (Settings.SettingNotFoundException e) {
//            Log.d("access", "Error finding setting, default accessibility to not found: " + e.getMessage());
//        }
//
//        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
//
//        if (accessibilityEnabled==1){
//            Log.d("access", "***ACCESSIBILIY IS ENABLED***: ");
//
//
//            String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
//            Log.d(LOGTAG, "Setting: " + settingValue);
//            if (settingValue != null) {
//                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
//                splitter.setString(settingValue);
//                while (splitter.hasNext()) {
//                    String accessabilityService = splitter.next();
//                    Log.d(LOGTAG, "Setting: " + accessabilityService);
//                    if (accessabilityService.equalsIgnoreCase(".NotificaitonListener")){
//                        Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!");
//                        return true;
//                    }
//                }
//            }
//
//            Log.d(LOGTAG, "***END***");
//        }
//        else{
//            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***");
//        }
//        return accessibilityFound;
//    }

}
