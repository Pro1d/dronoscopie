package fr.insatoulouse.stereoshot;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

import com.fbessou.sofa.GameIOHelper;
import com.fbessou.sofa.GameIOHelper.CustomMessageListener;
import com.fbessou.sofa.GameIOHelper.GamePadCustomMessage;
import com.fbessou.sofa.GameIOHelper.GamePadInputEvent;
import com.fbessou.sofa.GameIOHelper.GamePadStateChangedEvent;
import com.fbessou.sofa.GameIOHelper.GamePadStateChangedEvent.Type;
import com.fbessou.sofa.GameIOHelper.InputEventListener;
import com.fbessou.sofa.GameIOHelper.StateChangedEventListener;
import com.fbessou.sofa.GameInformation;
import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.Sensor;

import fr.insatoulouse.stereoshot.camera.CameraHelper;
import fr.insatoulouse.stereoshot.camera.CameraHelper.OnImageListener;

public class CameraActivity extends Activity implements OnImageListener, OnCustomMessageReceivedListener, ConnectionStateChangedListener {
	CameraHelper camera;
	SurfaceView cameraSurfaceView;
	GamePadIOHelper easyIO;
    
	GameIOHelper serverIO;
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
		info.setNickname("Camera");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		
		
		findViewById(R.id.start_service).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(serverIO != null || connectedToServer)
					return;
				serverIO = new GameIOHelper(CameraActivity.this, new GameInformation(CameraActivity.this), isrv, isrv, isrv);
				serverIO.start(isrv);//CameraActivity.this.startService(new Intent(CameraActivity.this, ServerService.class));
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
	
	IServer isrv = new IServer();
	class IServer implements InputEventListener, StateChangedEventListener, CustomMessageListener, com.fbessou.sofa.GameIOClient.ConnectionStateChangedListener {
		int controllerPadId = -1;
		@Override
		public void onCustomMessageReceived(GamePadCustomMessage message) {
			if(message.gamePadId != controllerPadId && controllerPadId != -1) {
				serverIO.sendCustomMessage("camera"+message.gamePadId+" said \""+message.customMessage+"\"", controllerPadId);
			}
		}

		@Override
		public void onPadEvent(GamePadStateChangedEvent event) {
			int gpid = event.gamePadId;
			if(serverIO.getGamePadInformation(gpid).staticInformations.getNickname()=="Controller") {
				switch(event.eventType) {
				case JOINED:
					controllerPadId = gpid;
					break;
				case LEFT:
				case UNEXPECTEDLY_DISCONNECTED:
					controllerPadId = -1;
					break;
				case INFORMATION:
					break;
				}
			} else if(controllerPadId != -1) {
				switch(event.eventType) {	
				case JOINED:
					serverIO.sendCustomMessage("camera"+gpid+" has joined.", controllerPadId);
					break;
				case LEFT:
					serverIO.sendCustomMessage("camera"+gpid+" has left.", controllerPadId);
					break;
				case UNEXPECTEDLY_DISCONNECTED:
					serverIO.sendCustomMessage("camera"+gpid+" has been disconnected. :(", controllerPadId);
					break;
				default:
					break;
				}
			}
		}

		@Override
		public void onInputEvent(GamePadInputEvent event) {
			if(serverIO.getGamePadInformation(event.gamePadId).staticInformations.getNickname()=="Controller") {
				if(event.event.padId == Sensor.KEY_CATEGORY_VALUE+1) {
					serverIO.sendCustomMessageBroadcast("capture");
				}
			}
		}

		@Override
		public void onConnected() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnected() {
			// TODO Auto-generated method stub
			
		}
	
	}

    /** Called when a picture has been taken and saved to jpeg **/
    public void onPhotoTaken(byte[] jpegData) {
    	// TODO SAVE IMAGE
    }

	@Override
	public void onCustomMessageReceived(String customMessage) {
		switch(customMessage) {
		case "capture":
			camera.performCapture();
			break;
		}
	}

	@Override
	public void onConnectedToProxy() {
		
	}

	@Override
	public void onConnectedToGame() {
		connectedToServer = true;
	}

	@Override
	public void onDisconnectedFromGame() {
		connectedToServer = false;
	}

	@Override
	public void onDisconnectedFromProxy() {
	}

}
