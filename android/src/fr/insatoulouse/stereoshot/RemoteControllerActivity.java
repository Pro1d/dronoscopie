package fr.insatoulouse.stereoshot;

import android.app.Activity;
import android.os.Bundle;

import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.KeySensor;
import com.fbessou.sofa.sensor.Sensor;

public class RemoteControllerActivity extends Activity implements ConnectionStateChangedListener, OnCustomMessageReceivedListener {
	GamePadIOHelper easyIO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		
		GamePadInformation info = new GamePadInformation(this);
		info.setNickname("Controller");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		
		easyIO.attachSensor(new KeySensor(Sensor.KEY_CATEGORY_VALUE+1, findViewById(R.id.b_capture)));
	}
	@Override
	public void onCustomMessageReceived(String customMessage) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnectedToProxy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onConnectedToGame() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisconnectedFromGame() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDisconnectedFromProxy() {
		// TODO Auto-generated method stub
		
	}
}
