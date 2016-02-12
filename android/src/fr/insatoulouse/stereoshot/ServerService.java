package fr.insatoulouse.stereoshot;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.fbessou.sofa.GameIOClient.ConnectionStateChangedListener;
import com.fbessou.sofa.GameIOHelper;
import com.fbessou.sofa.GameIOHelper.CustomMessageListener;
import com.fbessou.sofa.GameIOHelper.GamePadCustomMessage;
import com.fbessou.sofa.GameIOHelper.GamePadInputEvent;
import com.fbessou.sofa.GameIOHelper.GamePadStateChangedEvent;
import com.fbessou.sofa.GameIOHelper.InputEventListener;
import com.fbessou.sofa.GameIOHelper.StateChangedEventListener;
import com.fbessou.sofa.sensor.Sensor;
import com.fbessou.sofa.GameInformation;

public class ServerService extends Service implements InputEventListener, StateChangedEventListener, CustomMessageListener, ConnectionStateChangedListener {
	GameIOHelper easyIO;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		easyIO = new GameIOHelper((Activity)((Context)this), new GameInformation(this), this, this, this);
		easyIO.start(this);
	}

	@Override
	public void onInputEvent(GamePadInputEvent event) {
		if(easyIO.getGamePadInformation(event.gamePadId).staticInformations.getNickname()=="Controller") {
			if(event.event.padId == Sensor.KEY_CATEGORY_VALUE+1) {
				easyIO.sendCustomMessageBroadcast("capture");
			}
		}
	}

	@Override
	public void onPadEvent(GamePadStateChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCustomMessageReceived(GamePadCustomMessage message) {
		// TODO Auto-generated method stub
		
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
