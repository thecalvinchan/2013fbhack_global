package com.fbhack.memoapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.Camera;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.RemoteViews;

public class IdentifyActivity extends Activity {
	
    private String mEncodedImage;
    private LiveCard mLiveCard;
    private TimelineManager mTimelineManager;
    private static final String LIVE_CARD_ID = "identify";

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// Do nothing.
		}
	};
	
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
    	System.out.println(response);
    	//do something;
    	return;
    }
    

    private class ImageSender extends AsyncTask<Void, Void, String> {
    	
        @Override
        public String doInBackground(Void... voids) {
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpPost httppost = new HttpPost("http://54.201.41.99/process/recognize/");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("picture", mEncodedImage));
            nameValuePairs.add(new BasicNameValuePair("user", "1234"));
            try {
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
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
	            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
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
            HttpPost to_http = new HttpPost(initURL);
            MultipartEntity entity = new MultipartEntity();
           
            
            HttpEntity result = httpPost(initURL, nameValuePairs).getEntity();
            try {
				updater(EntityUtils.toString(result, "UTF-8"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} */
            return null;
        }
        
        @Override
        protected void onPostExecute(String result) {
        	System.out.println(result);
        	Card card = new Card(null);
        	card.setText(result);
        	card.toView();
        }
    }
    

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
				mEncodedImage = Base64.encodeToString(image_arr, Base64.DEFAULT);
				ImageSender is = new ImageSender();
				is.execute();
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
