package com.fbhack.memoapp;

import android.app.Service;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
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
		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);

            mLiveCard.setNonSilent(true);

            // Display the options menu when the live card is tapped.
            Intent menuIntent = new Intent(this, MenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

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
