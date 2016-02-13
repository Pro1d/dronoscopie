package fr.insatoulouse.stereoshot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.fbessou.sofa.GamePadIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GamePadIOHelper;
import com.fbessou.sofa.GamePadIOHelper.OnCustomMessageReceivedListener;
import com.fbessou.sofa.GamePadInformation;
import com.fbessou.sofa.sensor.KeySensor;
import com.fbessou.sofa.sensor.Sensor;

public class RemoteControllerActivity extends Activity implements ConnectionStateChangedListener, OnCustomMessageReceivedListener {
	GamePadIOHelper easyIO;
	TextView text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		text=(TextView)findViewById(R.id.tv_controller);
		GamePadInformation info = new GamePadInformation(this);
		info.setNickname("Controller");
		easyIO = new GamePadIOHelper(this, info);
		easyIO.start(this);
		easyIO.setOnCustomMessageReceivedListener(this);
		
		easyIO.attachSensor(new KeySensor(Sensor.KEY_CATEGORY_VALUE+1, findViewById(R.id.b_capture)));
	}
	@Override
	public void onCustomMessageReceived(String customMessage) {
		text.setText(text.getText().toString()+"Custom msg: \""+customMessage+"\"\n");
	}
	@Override
	public void onConnectedToProxy() {
		text.setText(text.getText().toString()+"Connected to proxy\n");
	}
	@Override
	public void onConnectedToGame() {
		text.setText(text.getText().toString()+"Connected to game\n");
	}
	@Override
	public void onDisconnectedFromGame() {
		text.setText(text.getText().toString()+"Disconnected from game\n");
	}
	@Override
	public void onDisconnectedFromProxy() {
		text.setText(text.getText().toString()+"Disconnected from proxy\n");
	}
}
