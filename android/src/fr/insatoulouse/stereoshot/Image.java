package fr.insatoulouse.stereoshot;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Base64;

public class Image {
	String jpegDataB64;
	byte jpegDataRaw[];
	Bitmap imageBitmap;
	byte[] buffer;
	
	Image(byte[] jpegData) {
		jpegDataRaw = jpegData;
	}
	Image(String jpegB64) {
		jpegDataB64 = jpegB64;
	}
	Image(JSONObject obj) throws JSONException {
		jpegDataB64 = obj.getString("image");
	}
	
	String getJpegDataBase64() {
		if(jpegDataB64 == null) {
			jpegDataB64 = Base64.encodeToString(jpegDataRaw, Base64.DEFAULT);
		}
		return jpegDataB64;
	}
	byte[] getJpegDataRaw() {
		if(jpegDataRaw == null) {
			jpegDataRaw = Base64.decode(jpegDataB64, Base64.DEFAULT);
		}
		return jpegDataRaw;
	}
	
	Bitmap getJpegBitmap() {
		byte[] jpeg = getJpegDataRaw();
		
		// Decode the jpeg
		Options opts = new Options();
		if(imageBitmap != null)
			opts.inBitmap = imageBitmap;
		opts.inPreferQualityOverSpeed = false;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		if(buffer == null)
			buffer = new byte[32*1024];
		opts.inTempStorage = buffer;
		
		try {
			imageBitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, opts);
		} catch(IllegalArgumentException e) {
			opts.inBitmap = null;
			imageBitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length, opts);
		}
		
		return imageBitmap;
	}

	JSONObject createJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.putOpt("type", "image");
		obj.putOpt("image", getJpegDataBase64());
		return obj;
	}
	
}
