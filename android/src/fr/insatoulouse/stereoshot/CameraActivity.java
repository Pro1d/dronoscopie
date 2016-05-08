package fr.insatoulouse.stereoshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.KeySensor;
import com.fbessou.sofa.sensor.Sensor;

import fr.insatoulouse.stereoshot.camera.CameraHelper;
import fr.insatoulouse.stereoshot.camera.CameraHelper.OnImageListener;
import fr.insatoulouse.stereoshot.camera.FileManager;
/************ Activity camera *************
 * Must be launched on embedded smartphones
 * Client 'camera', auto connect to server
 * CustomMessage:
 * 		JSON {action:string=capture} -> take imageBitmap, save on local storage, send message "I took picture." to server
 * UI:
 * 		camera preview
 * 		button 'start server'
 * 		button 'photo manually'
 * 
 */
public class CameraActivity extends Activity implements OnImageListener, OnCustomMessageReceivedListener, ConnectionStateChangedListener {
	public static final int LEFT = 0, RIGHT = 1;
	CameraHelper camera;
	SurfaceView cameraSurfaceView;
	GamePadIOHelper easyIO;
	FileManager fileMng = new FileManager("StereoShot");
	String nextFileName = "default.jpg";
    
	boolean connectedToServer = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		/** Initialize the camera **/
		cameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceViewCamera);
		camera = new CameraHelper(cameraSurfaceView);
		camera.setOnImageListener(this);
		camera.enableAutoFocusOnClickSurfaceView();
		
		/** SOFA **/
		GamePadInformation info = new GamePadInformation(this);
		info.setNickname("camera");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		easyIO.attachSensor(new KeySensor(Sensor.KEY_CATEGORY_VALUE+2, (CheckBox)findViewById(R.id.switch_side_cam)));
		
		findViewById(R.id.b_capture_cam).setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				if(camera.performCapture()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd-kk-mm-ss-SSS");
					nextFileName = "manual_" + sdf.format(new Date()) + ".jpg";
				}
			}
		});
	}

    @Override
    protected void onResume() {
        super.onResume();
        camera.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.pause();
    }
	
	/** Called when a picture has been taken and saved to jpeg **/
    public void onPhotoTaken(byte[] jpegData) {
    	Toast.makeText(this, "Photo taken!", Toast.LENGTH_SHORT).show();
    	if(connectedToServer) {
    		Image img = new Image(jpegData);
	    	try {
	    		// Might take a while
				JSONObject json = img.createJSON();
		    	easyIO.sendCustomMessage(json.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	} else {
        	Toast.makeText(this, "Save in "+nextFileName, Toast.LENGTH_SHORT).show();
    		fileMng.writeFileAsync(jpegData, nextFileName);
    	}
    }

	@Override
	public void onCustomMessageReceived(String customMessage) {
		try {
			JSONObject o = new JSONObject(customMessage);

			if(o.getString("action").equals("capture")) {
				nextFileName = o.getString("filename");
				camera.performCapture();				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectedToProxy() {
		
	}

	@Override
	public void onConnectedToGame() {
		connectedToServer = true;
		((CheckBox)findViewById(R.id.switch_side_cam)).setChecked(false);
	}

	@Override
	public void onDisconnectedFromGame() {
		connectedToServer = false;
	}

	@Override
	public void onDisconnectedFromProxy() {
	}

}
