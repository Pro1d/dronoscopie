package fr.insatoulouse.stereoshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;

import fr.insatoulouse.stereoshot.camera.CameraHelper;
import fr.insatoulouse.stereoshot.camera.CameraHelper.OnImageListener;

public class CameraActivity extends Activity implements OnImageListener, OnCustomMessageReceivedListener, ConnectionStateChangedListener {
	CameraHelper camera;
	SurfaceView cameraSurfaceView;
	GamePadIOHelper easyIO;
    
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
		info.setNickname("Camera");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);

		findViewById(R.id.start_service).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CameraActivity.this.startService(new Intent(CameraActivity.this, ServerService.class));
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
    	// TODO SAVE IMAGE
    }

	@Override
	public void onCustomMessageReceived(String customMessage) {
		if(customMessage == "capture") {
			camera.performCapture();
		}
	}

	
	@Override
	public void onConnectedToProxy() {
		
	}

	@Override
	public void onConnectedToGame() {
	}

	@Override
	public void onDisconnectedFromGame() {
	}

	@Override
	public void onDisconnectedFromProxy() {
	}

}
