package com.fbhack.memoapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class IdentifyService extends Service {
	private static final String LIVE_CARD_ID = "identify";

  private TimelineManager mTimelineManager;
  private LiveCard mLiveCard;
  private TextToSpeech mSpeech;
  private String mEncodedImage;
  
  private IdentifyActivity ia;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("MEMO", "SANITY");
		mTimelineManager = TimelineManager.from(this);

		mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				// Do nothing.
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new Binder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {
			mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);

			mLiveCard.setNonSilent(true);

			mLiveCard.publish();
		}

        //mLiveCard.publish();
		Intent i = new Intent(getBaseContext(), IdentifyActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(i);
		
	
    //ia = new IdentifyActivity();
    //ia.take_a_pic();
       return super.onStartCommand(intent, flags, startId);
	}
	
    private HttpResponse httpPost(String initURL, List<NameValuePair> nameValuePairs) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
                HttpPost httppost = new HttpPost(initURL);
                UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(nameValuePairs);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                return response;

        } catch(Exception e) {
                Log.d("Exception", e.toString());
        }
        return null;
    }

    private HttpResponse httpGet(String initURL) {
        HttpClient httpclient = new DefaultHttpClient();
        try {

            HttpGet httpGet = new HttpGet(initURL);
            HttpResponse response = httpclient.execute(httpGet);
            return response;

        } catch(Exception e) {
                Log.d("Exception", e.toString());
        }
        return null;
    } 
    
    private void updater(String response) {
    	//do something;
    	return;
    }
    
    private class ImageSender extends AsyncTask<Void, Void, Void> {

        @Override
        public Void doInBackground(Void... voids) {
            String initURL = "http://54.201.41.99/process/memo.php";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("picture", mEncodedImage));
            HttpEntity result = httpPost(initURL, nameValuePairs).getEntity();
            try {
				updater(EntityUtils.toString(result, "UTF-8"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return null;
        }
    }

	@Override
	public void onDestroy() {
		if (mLiveCard != null && mLiveCard.isPublished()) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}

		mSpeech.shutdown();

		mSpeech = null;

		super.onDestroy();
	}
}
