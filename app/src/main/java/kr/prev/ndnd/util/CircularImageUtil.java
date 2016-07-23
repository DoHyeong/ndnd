package kr.prev.ndnd.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CircularImageUtil {

	static private HashMap<String, Boolean> isInvalidImage = new HashMap<String, Boolean>();
	static private HashMap<String, SoftReference<Bitmap>> cachedImages = new HashMap<String,  SoftReference<Bitmap>>();

	static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView imageView;
		String url;

		public DownloadImageTask(ImageView imageView, String url) {
			this.imageView = imageView;
			this.url = url;
		}

		protected Bitmap doInBackground(String... params) {
			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;

			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				isInvalidImage.put(url, true);
				return;
			}

			imageView.setImageBitmap(result);
			cachedImages.put(url, new SoftReference<Bitmap>(result));
		}
	}

	public static void setImage(ImageView imageView, String url) {
		if (isInvalidImage.containsKey(url)) return;

		if (cachedImages.containsKey(url) )
			imageView.setImageBitmap(cachedImages.get(url).get());

		else {
			new DownloadImageTask(imageView, url)
				.execute(url);
		}
	}
}
