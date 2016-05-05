package fr.insatoulouse.stereoshot;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.KeySensor;
import com.fbessou.sofa.sensor.Sensor;
/************ Controller activity ***********
 * Log and command for camera
 * Client 'controller', auto connect to server
 * 
 * CustomMessage:
 * 		{type:string="log",text:string} -> display text on textview
 * 		{type:string="camera",side:int=CameraActivity.LEFT/RIGHT,message:string=JSONObject}
 * 			->message:{type:string="image",image:string=<-constructor Image.class}
 */
public class RemoteControllerActivity extends Activity implements ConnectionStateChangedListener, OnCustomMessageReceivedListener {
	GamePadIOHelper easyIO;
	TextView text;
	SurfaceView surfaceViewer;
	SurfaceHolder holderViewer;
	Canvas bufCanvas;
	Bitmap bufBitmap;
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Image imgRight, imgLeft;
	float fovCorrectionScale;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		text=(TextView)findViewById(R.id.tv_controller);
		((SeekBar)findViewById(R.id.sb_correction_fov_contr)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float x = (float)(progress - seekBar.getMax()/2) / seekBar.getMax();
				float newScale = x * 0.2f;
				if(fovCorrectionScale != newScale) {
					fovCorrectionScale = newScale;
					drawImages();
				}
			}
		});
		surfaceViewer = (SurfaceView) findViewById(R.id.stereoView);
		holderViewer = surfaceViewer.getHolder();
		GamePadInformation info = new GamePadInformation(this);
		info.setNickname("controller");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		
		easyIO.attachSensor(new KeySensor(Sensor.KEY_CATEGORY_VALUE+1, findViewById(R.id.b_capture_cam)));
	}
	@Override
	public void onCustomMessageReceived(String customMessage) {
		try {
			JSONObject o = new JSONObject(customMessage);
			switch(o.getString("type")) {
			case "log":
				printTextLog(o.getString("text"));
				break;
			case "camera":
				JSONObject inner = new JSONObject(o.getString("message"));
				if(inner.getString("type").equals("image"))
					onImageReceived(o.getInt("side"), inner.getString("image"));
				break;
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	void drawImages() {
		Canvas canvas = holderViewer.lockCanvas();
		if(canvas == null)
			return;
		for(int side = 0; side < 2; side++) {
			Image img = (side == CameraActivity.LEFT ? imgLeft : imgRight);
			if(img == null)
				continue;
			Bitmap image = img.getJpegBitmap();
			
			if(bufBitmap == null || canvas.getHeight() != bufBitmap.getHeight() || canvas.getWidth() != bufBitmap.getWidth()) {
				bufBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.RGB_565);
				bufCanvas = new Canvas(bufBitmap);
			}
			// draw on left or right side
			int width = canvas.getWidth()/2, height = canvas.getHeight();
			float ratio = (float)width / height;
			float fovCorrectionScale = 1.0f - (side == 1 ? 
					(this.fovCorrectionScale < 0 ? -this.fovCorrectionScale : 0) : 
						(this.fovCorrectionScale > 0 ? this.fovCorrectionScale : 0));
			
			float imgWidth = image.getWidth() * fovCorrectionScale;
			float imgHeight = image.getHeight() * fovCorrectionScale;
			// Correct ratio
			if(imgWidth/imgHeight > ratio)
				imgWidth = imgHeight * ratio;
			else
				imgHeight = imgWidth / ratio;

			Rect src = new Rect(
					(int)((image.getWidth() -imgWidth) /2),
					(int)((image.getHeight()-imgHeight)/2),
					(int)((image.getWidth() +imgWidth) /2),
					(int)((image.getHeight()+imgHeight)/2));
			Rect dst = new Rect(side*width,0,width+(side*width),height);
			bufCanvas.drawBitmap(image, src, dst, paint);
			// update view
			canvas.drawBitmap(bufBitmap, 0, 0, null);
		}
		holderViewer.unlockCanvasAndPost(canvas);
	}
	public void onImageReceived(final int side, final String jpegBase64) {
		clearTextLog();
		printTextLog("Image from side "+side);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Image img = new Image(jpegBase64);
				
				if(side == CameraActivity.LEFT) imgLeft = img;
				else imgRight = img;
				
				Bitmap image = img.getJpegBitmap();
				if(image == null)
					return;
				
				drawImages();
			}
		}).start();
	}
	
	@Override
	public void onConnectedToProxy() {
		printTextLog("Connected to proxy");
	}
	@Override
	public void onConnectedToGame() {
		printTextLog("Connected to game");
	}
	@Override
	public void onDisconnectedFromGame() {
		printTextLog("Disconnected from game");
	}
	@Override
	public void onDisconnectedFromProxy() {
		printTextLog("Disconnected from proxy");
	}

	private void printTextLog(String s) {
		text.setText(s+"\n"+text.getText().toString());
	}
	private void clearTextLog() {
		text.setText("");
	}
	
}
