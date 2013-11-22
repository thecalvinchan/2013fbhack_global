package com.fbhack.memoapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.glass.media.Camera;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

public class MenuActivity extends Activity {
	private String mEncodedImage1;
	private String mEncodedImage2;

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
		Log.e("Memo", "ONCREATE");
		bindService(new Intent(this, MemoService.class), mConnection, 0);
		setContentView(R.layout.activity_menu);
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

	private static final int SPEECH_REQUEST = 0;
	private static final int CAM_REQUEST = 1;

	private void displaySpeechRecognizer() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		startActivityForResult(intent, SPEECH_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("Onresult", "called");
		if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
			List<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String spokenText = results.get(0);
			// Do something with spokenText.
			Log.e("spoken text", spokenText);
			ImageSender is = new ImageSender();
			is.execute(spokenText);
		} else if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			String file_path = extras.getString(Camera.EXTRA_PICTURE_FILE_PATH);
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
			if (mEncodedImage1 == null) {
				mEncodedImage1 = Base64.encodeToString(image_arr, Base64.DEFAULT);
				take_a_pic();
			} else if (mEncodedImage2 == null) {
				mEncodedImage2 = Base64.encodeToString(image_arr, Base64.DEFAULT);
				displaySpeechRecognizer();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void take_a_pic() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Log.d("TAKEAPIC", MediaStore.ACTION_IMAGE_CAPTURE);
		System.out.println(i);
		startActivityForResult(i, CAM_REQUEST);
	}

	private class ImageSender extends AsyncTask<String, Void, String> {

		@Override
		public String doInBackground(String... strings) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					//"http://www.google.com");
		"http://54.201.41.99/process/memo.php");
			String speech = strings[0];
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			//nameValuePairs
			//		.add(new BasicNameValuePair("picture1", mEncodedImage1));
			//nameValuePairs
			//		.add(new BasicNameValuePair("picture2", mEncodedImage2));
			//nameValuePairs.add(new BasicNameValuePair("user", "1234"));
			//nameValuePairs.add(new BasicNameValuePair("speech", speech));
			try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpResponse response = null;
			try {
				response = httpclient.execute(httppost);
				System.out.println(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = null;
				try {
					instream = entity.getContent();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String line = null;
				try {
					System.out.println(entity);
					while ((line = reader.readLine()) != null) {
						sb.append((line + "\n"));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						instream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return sb.toString();
			}
			/*
			 * HttpPost to_http = new HttpPost(initURL); MultipartEntity entity
			 * = new MultipartEntity();
			 * 
			 * 
			 * HttpEntity result = httpPost(initURL,
			 * nameValuePairs).getEntity(); try {
			 * updater(EntityUtils.toString(result, "UTF-8")); } catch
			 * (ParseException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */
			return null;
		}
	}
}
