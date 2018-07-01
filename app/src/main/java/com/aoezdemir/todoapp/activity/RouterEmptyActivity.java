package com.aoezdemir.todoapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.aoezdemir.todoapp.crud.remote.ServiceFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouterEmptyActivity extends Activity {

    public static final String INTENT_IS_WEB_API_ACCESSIBLE = "IS_WEB_API_ACCESSIBLE";
    private static final String TAG = RouterEmptyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new isWebAPIAvailableHelper(this).execute();
        finish();
    }

    private static class isWebAPIAvailableHelper extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<RouterEmptyActivity> activityReference;

        isWebAPIAvailableHelper(RouterEmptyActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(ServiceFactory.BASE_URL).openConnection();
                c.setReadTimeout(1000);
                c.setConnectTimeout(1000);
                c.setRequestMethod("GET");
                c.setDoInput(true);
                c.connect();
                return true;
            } catch (IOException e) {
                Log.i(TAG, "Web API is offline, therefore no login is necessary.");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Intent intent;
            RouterEmptyActivity activity = activityReference.get();
            if (result) {
                intent = new Intent(activityReference.get(), LoginActivity.class);
            } else {
                intent = new Intent(activityReference.get(), OverviewActivity.class);
                intent.putExtra(INTENT_IS_WEB_API_ACCESSIBLE, false);
            }
            activity.startActivity(intent);
        }
    }
}