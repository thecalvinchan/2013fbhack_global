package com.fbhack.memoapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import com.google.android.glass.media.Camera;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

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
		// getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v("Identify", "ONCREATE");
		bindService(new Intent(this, IdentifyService.class), mConnection, 0);
		setContentView(R.layout.activity_menu);
		// Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Log.d("TAKEAPIC", MediaStore.ACTION_IMAGE_CAPTURE);
		// System.out.println(i);
		// startActivityForResult(i, 0);
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
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				String file_path = extras
						.getString(Camera.EXTRA_PICTURE_FILE_PATH);
				System.out.println(file_path);
				File img_file = new File(file_path);
				while (!img_file.exists()) {
					System.out.println("does it exist?");
					img_file = new File(file_path);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("OMQ IT EXIZTS");
				Bitmap img_bm = BitmapFactory.decodeFile(file_path);
				System.out.println(img_bm);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				img_bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
				byte[] image_arr = baos.toByteArray();
				String image_str = Base64.encodeToString(image_arr, Base64.DEFAULT);
			}
		}
	}

	public void take_a_pic() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Log.d("TAKEAPIC", MediaStore.ACTION_IMAGE_CAPTURE);
		System.out.println(i);
		startActivityForResult(i, 0);
	}
}
