package com.fbhack.memoapp;

import android.app.Service;
import android.content.Intent;
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
