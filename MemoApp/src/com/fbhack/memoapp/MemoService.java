package com.fbhack.memoapp;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.speech.RecognizerIntent;
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
		// TODO Auto-generated method stub
		return new Binder();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {
			mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);

			mLiveCard.setNonSilent(true);

			ArrayList<String> voiceResults = intent.getExtras()
					.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
			Log.v("Memo", "Stuff");

			mLiveCard.publish();
		}

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
