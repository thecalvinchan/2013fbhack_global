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

import android.app.PendingIntent;
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
		/**if (mLiveCard == null) {
			mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);
			//mRenderer = new CompassRenderer(this, mOrientationManager, mLandmarks);

           // mLiveCard.enableDirectRendering(true).getSurfaceHolder().addCallback(mRenderer);
			mLiveCard.enableDirectRendering(true).getSurfaceHolder();
            mLiveCard.setNonSilent(true);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, IdentifyActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

            mLiveCard.publish();
        }**/

        //mLiveCard.publish();
		Intent i = new Intent(getBaseContext(), IdentifyActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(i);
		
	
    //ia = new IdentifyActivity();
    //ia.take_a_pic();
       return super.onStartCommand(intent, flags, startId);
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
