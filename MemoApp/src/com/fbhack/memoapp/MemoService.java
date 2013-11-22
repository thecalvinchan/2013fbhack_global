package com.fbhack.memoapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class MemoService extends Service {
	private static final String LIVE_CARD_ID = "memo";

	private TimelineManager mTimelineManager;
	private LiveCard mLiveCard;
	private TextToSpeech mSpeech;

	@Override
	public void onCreate() {
    Log.e("Memopublius", "onCreate");
		super.onCreate();
		mTimelineManager = TimelineManager.from(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new Binder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
    /*
    ArrayList<String> voiceResults = intent.getExtras()
      .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
      */
    Log.e("Memopublius", "onStartCommand");
		if (mLiveCard == null) {
			mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);

			mLiveCard.setNonSilent(true);

			mLiveCard.publish();
		}
		
		Intent i = new Intent(getBaseContext(), MenuActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplication().startActivity(i);
		
		return START_STICKY;
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
