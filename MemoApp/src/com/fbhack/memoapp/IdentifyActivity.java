package com.fbhack.memoapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.provider.MediaStore;


public class IdentifyActivity extends Activity {
	
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
		Log.v("Identify", "ONCREATE");
		bindService(new Intent(this, IdentifyService.class), mConnection, 0);
		setContentView(R.layout.activity_menu);
		//Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    //Log.d("TAKEAPIC", MediaStore.ACTION_IMAGE_CAPTURE);
	    //System.out.println(i);
	    //startActivityForResult(i, 0);
		take_a_pic();
	}
	
	@Override
    protected void onResume() {
        super.onResume();
    }
	
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 0) {
    		System.out.println(resultCode);
    		System.out.println(data);
    	}
    }

    public void take_a_pic() {
      Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      Log.d("TAKEAPIC", MediaStore.ACTION_IMAGE_CAPTURE);
      System.out.println(i);
      startActivityForResult(i, 0);
    }
}
