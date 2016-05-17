package fr.insatoulouse.stereoshot;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fbessou.sofa.GameIOHelper.GamePadCustomMessage;
import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.KeySensor;
import com.fbessou.sofa.sensor.Sensor;

import fr.insatoulouse.stereoshot.IServer.OnCustomMessageReceivedByControllerListener;
import fr.insatoulouse.stereoshot.camera.FileManager;
/************ Controller activity ***********
 * Log and command for camera
 * Client 'controller', auto connect to server
 * 
 * CustomMessage from IServer's interface:
 * 		{type:string="log",text:string} -> display text on textview
 * 		{type:string="camera",side:int=CameraActivity.LEFT/RIGHT,message:string=JSONObject}
 * 			->message:{type:string="image",image:string=<-constructor Image.class}
 */
public class RemoteControllerActivity extends Activity implements ConnectionStateChangedListener, OnCustomMessageReceivedListener, OnCustomMessageReceivedByControllerListener {
	GamePadIOHelper easyIO;
	TextView text;
	SurfaceView surfaceViewer;
	SurfaceHolder holderViewer;
	Canvas bufCanvas;
	Bitmap bufBitmap;
	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Image imgRight, imgLeft;
	float fovCorrectionScale = 0.0f;
	float distance = 0.0f;
	IServer server = null;
	FileManager fileMng = new FileManager("StereoShot");
	int configuration = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		text=(TextView)findViewById(R.id.tv_controller);
		((SeekBar)findViewById(R.id.sb_correction_fov_contr)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float x = (float)(progress - seekBar.getMax()/2) / seekBar.getMax();
				float newScale = x * 0.5f;
				if(fovCorrectionScale != newScale) {
					fovCorrectionScale = newScale;
					Log.i("VALUE","fovCorrectionScale="+fovCorrectionScale);
					Log.i("VALUE","CardBoard.deltaScale="+CardBoard.deltaScale);
					Log.i("VALUE","CardBoard.deltaShape="+CardBoard.deltaShape);
					Log.i("VALUE","distance="+distance);
					drawImages();
				}
			}
		});
		((SeekBar)findViewById(R.id.sb_zoom_contr)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float x = (float)(progress - seekBar.getMax()/2) / seekBar.getMax();
				CardBoard.deltaScale = -x*0.5f;
				Log.i("VALUE","fovCorrectionScale="+fovCorrectionScale);
				Log.i("VALUE","CardBoard.deltaScale="+CardBoard.deltaScale);
				Log.i("VALUE","CardBoard.deltaShape="+CardBoard.deltaShape);
				Log.i("VALUE","distance="+distance);
				drawImages();
			}
		});
		((SeekBar)findViewById(R.id.sb_distortion_contr)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float x = (float)(progress - seekBar.getMax()/2) / seekBar.getMax();
				CardBoard.deltaShape = x*0.5f;
				Log.i("VALUE","fovCorrectionScale="+fovCorrectionScale);
				Log.i("VALUE","CardBoard.deltaScale="+CardBoard.deltaScale);
				Log.i("VALUE","CardBoard.deltaShape="+CardBoard.deltaShape);
				Log.i("VALUE","distance="+distance);
				drawImages();
			}
		});
		((SeekBar)findViewById(R.id.sb_inter_contr)).setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				float x = (float)(progress - seekBar.getMax()/2) / seekBar.getMax();
				distance = x*0.3f + 0.0f;
				Log.i("VALUE","fovCorrectionScale="+fovCorrectionScale);
				Log.i("VALUE","CardBoard.deltaScale="+CardBoard.deltaScale);
				Log.i("VALUE","CardBoard.deltaShape="+CardBoard.deltaShape);
				Log.i("VALUE","distance="+distance);
				drawImages();
			}
		});
		findViewById(R.id.b_save).setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd-kk-mm-ss-SSS");
				String now = "manual_" + sdf.format(new Date()) + ".jpg";

			    try {
			        // image naming and path  to include sd card  appending name you choose for file
			        String mPath = Environment.getExternalStorageDirectory().toString() + "/StereoShot/stereo_" + now + ".jpg";
			        
			        Bitmap bitmap = Bitmap.createBitmap(bufBitmap);

			        File imageFile = new File(mPath);

			        FileOutputStream outputStream = new FileOutputStream(imageFile);
			        int quality = 100;
			        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
			        outputStream.flush();
			        outputStream.close();
			        Toast.makeText(RemoteControllerActivity.this, "Saved in"+mPath, Toast.LENGTH_SHORT).show();
			    } catch (Throwable e) {
			        e.printStackTrace();
			    }
			}
		});
		findViewById(R.id.b_capture_cam).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    Toast.makeText(RemoteControllerActivity.this, "Capture request!", Toast.LENGTH_SHORT).show();
			}
		});
		findViewById(R.id.b_switch).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				configuration = 1-configuration;
				drawImages();
			}
		});
		surfaceViewer = (SurfaceView) findViewById(R.id.stereoView);
		holderViewer = surfaceViewer.getHolder();
		
		/** Client **/
		GamePadInformation info = new GamePadInformation(this);
		info.setNickname("controller");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		easyIO.attachSensor(new KeySensor(Sensor.KEY_CATEGORY_VALUE+1, findViewById(R.id.b_capture_cam)));
		
		/** server **/
		server = new IServer(RemoteControllerActivity.this, this);
		hideSystemUI();
	}
	@Override
	public void onCustomMessageReceivedByController(GamePadCustomMessage m, int side) {
		try {
			JSONObject o = new JSONObject(m.customMessage);
			switch(o.getString("type")) {
			case "log":
				printTextLog(o.getString("text"));
				break;
			case "image":
				onImageReceived(side, o.getString("image"));
				break;
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
	}
	// This snippet hides the system bars.
	private void hideSystemUI() {
	    // Set the IMMERSIVE flag.
	    // Set the content to appear under the system bars so that the content
	    // doesn't resize when the system bars hide and show.
		this.getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	// This snippet shows the system bars. It does this by removing all the flags
	// except for the ones that make the content appear under the system bars.
	private void showSystemUI() {
		this.getWindow().getDecorView().setSystemUiVisibility(
	            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			JSONObject j = new JSONObject();
			try {
				j.put("type", "capture");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			easyIO.sendCustomMessage(j.toString());
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			View v= findViewById(R.id.ll_overlay);
			if(v.getVisibility()==View.VISIBLE) {
				v.setVisibility(View.GONE);
				hideSystemUI();
			}
			else {
				v.setVisibility(View.VISIBLE);
				//showSystemUI();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	Thread lastThread=null;
	void drawImages() {
		if(lastThread != null && lastThread.isAlive())
			lastThread.interrupt();
		(lastThread=new Thread() {
			@Override
			public void run() {
				Canvas canvas = holderViewer.lockCanvas();
				if(canvas == null)
					return;
				if(bufCanvas != null) bufCanvas.drawColor(0xFF000000);
				for(int side = 0; side < 2; side++) {
					Image img = ((side+configuration)%2 == CameraActivity.LEFT ? imgLeft : imgRight);
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
							(RemoteControllerActivity.this.fovCorrectionScale < 0 ? -RemoteControllerActivity.this.fovCorrectionScale : 0) : 
								(RemoteControllerActivity.this.fovCorrectionScale > 0 ? RemoteControllerActivity.this.fovCorrectionScale : 0));
					
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
					int offsetX = +(int)((side*2-1)*width*distance);
					Rect dst = new Rect(side*width+offsetX,0,width+(side*width)+offsetX,height);
					CardBoard.drawWithDistortion(image, src, dst, bufCanvas, (configuration+side)%2==1);
					//bufCanvas.drawBitmap(image, src, dst, paint);
					// update view
					canvas.drawBitmap(bufBitmap, 0, 0, null);
				}
				holderViewer.unlockCanvasAndPost(canvas);
			}
		}).start();
	}
	public void onImageReceived(final int side, final String jpegBase64) {
		clearTextLog();
		printTextLog("Image from side "+side);
		new Thread(new Runnable() {
			@Override
			public void run() {
				Image img = new Image(jpegBase64);
				
				if(side == CameraActivity.LEFT) {
					if(imgLeft != null && imgLeft.imageBitmap != null) imgLeft.imageBitmap.recycle();
					imgLeft = img;
				}
				else {
					if(imgRight != null && imgRight.imageBitmap != null) imgRight.imageBitmap.recycle();
					imgRight = img;
				}
				
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
	@Override
	public void onCustomMessageReceived(String customMessage) {
		// TODO Auto-generated method stub
		
	}
	
}
