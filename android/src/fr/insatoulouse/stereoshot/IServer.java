package fr.insatoulouse.stereoshot;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.fbessou.sofa.GameIOHelper;
import com.fbessou.sofa.GameIOHelper.CustomMessageListener;
import com.fbessou.sofa.GameIOHelper.GamePadCustomMessage;
import com.fbessou.sofa.GameIOHelper.GamePadInputEvent;
import com.fbessou.sofa.GameIOHelper.GamePadStateChangedEvent;
import com.fbessou.sofa.GameIOHelper.InputEventListener;
import com.fbessou.sofa.GameIOHelper.StateChangedEventListener;
import com.fbessou.sofa.GameInformation;
import com.fbessou.sofa.InputEvent;
import com.fbessou.sofa.sensor.Sensor;
/*********** Server *************
 * Accept "camera" and only one "controller" clients
 * CustomMessages from camera:
 * 		{type:string='image',image:string=base64ImgData} -> transmitted to controller
 * CustomMessage from controller:
 *		.
 * KeyEvent:
 * 		from controller, id:Sensor.KEY_CATEGORY_VALUE+1, event:keydown
 * 			-> send broadcast : {action:string="capture", filename:string}
 * 		from camera, id:Sensor.STATE_CATEGORY_VALUE+1, event:change
 * 			-> set side LEFT/RIGHT
 */
class IServer implements InputEventListener, StateChangedEventListener, CustomMessageListener, com.fbessou.sofa.GameIOClient.ConnectionStateChangedListener {

	GameIOHelper serverIO;
	int controllerPadId = -1;
	SparseIntArray cameraSide = new SparseIntArray();
	int captureCount = 0;
	Activity activity;
	OnCustomMessageReceivedByControllerListener listener;
	IServer(Activity a, OnCustomMessageReceivedByControllerListener l) {
		activity = a;
		serverIO = new GameIOHelper(a, new GameInformation(a), this, this, this);
		serverIO.start(this);
		listener = l;
		//CameraActivity.this.startService(new Intent(CameraActivity.this, ServerService.class));
	}
	void sendLogMessageToController(String msg) {
		if(controllerPadId != -1) {
			JSONObject json = new JSONObject();
			try {
				json.put("type", "log");
				json.put("text", msg);
			} catch(JSONException e) {
				e.printStackTrace();
			}
			serverIO.sendCustomMessage(json.toString(), controllerPadId);
		}
	}
	void transmitCustomMessageToController(GamePadCustomMessage m) {
		listener.onCustomMessageReceivedByController(m, cameraSide.get(m.gamePadId));
	}
	
	@Override
	public void onCustomMessageReceived(GamePadCustomMessage message) {
		if(message.gamePadId != controllerPadId) {
			transmitCustomMessageToController(message);
			/*JSONObject o;
			try {
				o = new JSONObject(message.customMessage);
				switch(o.getString("type")) {
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// message received from a camera
			sendLogMessageToController("camera"+message.gamePadId+" said \""+message.customMessage+"\"");*/
		}
		else if(message.gamePadId == controllerPadId && controllerPadId != -1) {
			// message received from the controller
			try {
				JSONObject o = new JSONObject(message.customMessage);
				switch(o.getString("type")) {
				case "capture":
					sendCaptureRequest();
					break;
				}
			} catch(JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPadEvent(GamePadStateChangedEvent event) {
		int gpid = event.gamePadId;
		if(serverIO.getGamePadInformation(gpid).staticInformations.getNickname().equals("controller")) {
			switch(event.eventType) {
			case JOINED:
				Toast.makeText(activity, "Controller gamepad id is "+gpid, Toast.LENGTH_SHORT).show();
				controllerPadId = gpid;
				break;
			case LEFT:
			case UNEXPECTEDLY_DISCONNECTED:
				Toast.makeText(activity, "Controller is gone :(", Toast.LENGTH_SHORT).show();
				controllerPadId = -1;
				break;
			case INFORMATION:
				break;
			}
		} else {
			switch(event.eventType) {	
			case JOINED:
				Toast.makeText(activity, "camera"+gpid+" has joined.", Toast.LENGTH_SHORT).show();
				sendLogMessageToController("camera"+gpid+" has joined.");
				cameraSide.put(gpid, CameraActivity.LEFT);
				break;
			case LEFT:
				Toast.makeText(activity, "camera"+gpid+" has left.", Toast.LENGTH_SHORT).show();
				sendLogMessageToController("camera"+gpid+" has left.");
				break;
			case UNEXPECTEDLY_DISCONNECTED:
				sendLogMessageToController("camera"+gpid+" has been disconnected. :(");
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onInputEvent(GamePadInputEvent event) {
		//if(serverIO.getGamePadInformation(event.gamePadId).staticInformations.getNickname()=="Controller") {
			if(event.gamePadId == controllerPadId
					&& event.event.padId == Sensor.KEY_CATEGORY_VALUE+1
					&& event.event.eventType == com.fbessou.sofa.InputEvent.Type.KEYDOWN) {
				sendCaptureRequest();
			}
			else if(event.gamePadId != controllerPadId
					&& event.event.padId == Sensor.KEY_CATEGORY_VALUE+2) {
				int side = event.event.eventType == InputEvent.Type.KEYDOWN ? CameraActivity.RIGHT : CameraActivity.LEFT;
				cameraSide.put(event.gamePadId, side);
				sendLogMessageToController("Camera "+event.gamePadId+" is now on "+(side==CameraActivity.LEFT?"left":"right")+" side");
			}
		//}
	}
	@SuppressLint("SimpleDateFormat")
	private void sendCaptureRequest() {
		try {
			JSONObject o = new JSONObject();
			o.put("action", "capture");
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd-kk-mm-ss-SSS");
			String seq_name = "capture";
			String filename = seq_name + "_" + sdf.format(new Date()) + ".jpg";
			o.put("filename", filename);
			captureCount++;
			serverIO.sendCustomMessageBroadcast(o.toString());
		} catch (JSONException e) {
			e.printStackTrace();
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

	
	public void loadParameters(Context context) {
		SharedPreferences sp = context.getSharedPreferences("", 0);
		sp.getInt("repeat-delay", 1000);// temps entre les captures
		sp.getInt("repeat-count", -1);// nombre de répétition, -1 = infini
		sp.getString("seq-name", "capture"); // nom de la séquence de photo, présent dans le nom des fichiers	
	}

	public interface OnCustomMessageReceivedByControllerListener {
		public void onCustomMessageReceivedByController(GamePadCustomMessage m, int side);
	}
}