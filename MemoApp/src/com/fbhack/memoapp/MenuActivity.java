package com.fbhack.memoapp;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;


public class MenuActivity extends Activity {
	
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Do nothing.
        }
    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("Memo", "ONCREATE");
		bindService(new Intent(this, MemoService.class), mConnection, 0);
		setContentView(R.layout.activity_menu);
		displaySpeechRecognizer();
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        openOptionsMenu();
    }
	
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    private static final int SPEECH_REQUEST = 0;

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        startActivityForResult(intent, SPEECH_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
    	Log.e("Onresult","called");
        if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText.
            Log.e("spoken text", spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
}
