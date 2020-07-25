package android_serialport_api.sample;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android_serialport_api.SerialPort;
import android_serialport_api.UIwidget.SemicircleProgressBar;
import android_serialport_api.util.AliyunIoTSignUtil;

public class WorkActivity extends Activity {

	private static final String TAG = WorkActivity.class.getSimpleName();

	//设置变量 接收数据
	//左风机转速L、 左风机转速H、右风机转速L、右风机转速H、左风机转速、右风机转速
	public static int fanRotatespeedLeftL, fanRotatespeedLeftH, fanRotatespeedRightL, fanRotatespeedRightH, fanRotatespeedLeft, fanRotatespeedRight;
	//发动机转速L、发动机转速H、发动机转速
	public static int engineRotateSpeedL, engineRotateSpeedH, engineRotateSpeed;
	//水压L、水压H、水压
	public static int waterPressureL, waterPressureH, waterPressure;
	//行走速度L、行走速度H、行走速度
	public static int walkingSpeedL, walkingSpeedH, walkingSpeed;
	//排棉辊转速、碎棉辊转速、传输带转速、成膜马达转速
	public static int paimiangunRotateSpeedL, paimiangunRotateSpeedH, paimiangunRotateSpeed;
	public static int suimiangunRotateSpeedL, suimiangunRotateSpeedH, suimiangunRotateSpeed;
	public static int chuanshudaiRotateSpeedL, chuanshudaiRotateSpeedH, chuanshudaiRotateSpeed;
	public static int chengmomadaRotateSpeedL, chengmomadaRotateSpeedH, chengmomadaRotateSpeed;
	//摆臂轴压力、成膜压力、成膜直径
	public static int baibizhouPressureL, baibizhouPressureH, baibizhouPressure;
	//public static int chengmoPressureL,chengmoPressureH,chengmoPressure;
	public static int shengyumoNum;
	public static int chengmozhijingL, chengmozhijingH;
	public static double chengmozhijing;
	//油位、警告状态灯、can在线
	public static int oilLevel, stateLightL, stateLightH, stateLight, onlineWorking1, onlineWorking2, onlineWorking;
	//发动机启动、空档、锁定、润滑、仿形、箱满、手动驾驶、自动驾驶
	public static int engineqidong, neutral, lock, lubricate, imitate, boxFull, drivingManual, drivingAuto;
	//发动机工作时间、风机工作时间
	public static int timeworkEngine, timeworkFan;

	//声明1-1部分TextView控件
	private TextView mTvfanRotatespeedLeft, mTvfanRotatespeedRight, mTvenginerotateSpeed, mTvwaterpressure, mTvspeedText;
	//声明1-3部分ImageView控件
	private ImageView mIvstatusworking, mIvOnlineworking;
	private SemicircleProgressBar mSpvpprogress;
	//声明1-4部分TextView控件
	private TextView mTvpaimiangunRotateSpeed, mTvsuimiangunRotateSpeed, mTvchuanshudaiRotateSpeed;
	private TextView mTvchengmomadaRotateSpeed, mTvshengyumoshu, mTvbaibizhouPressure, mTvchengmozhijing;
	//声明2部分ImageView和TextView控件
	private View mVengineqidong, mVneutral, mVlock, mVlubricate, mVimitate, mVboxFull, mVdrivingback;
	private ImageView mIvdrivingImageviewback;
	private TextView mTvdrivingTextviewback, mTvtimeworkEngine, mTvtimeworkFan;
	//声明页面跳转
	private Button mBtnstatus, mBtnsetting, mBtntest, mBtngps;

	private SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	protected InputStream mInputStream;
	//private ReadThread mReadThread;

	private MqttClient mqttClient = null;

	public static String portPath = "/dev/ttyMT0";

	//productKey = "替换自己的产品key"
	public static String productKey = "202001";
	//deviceName = "替换自己的产品deviceName"
	public static String deviceName = "AndroidDevice1";
	//deviceSecret = "替换自己的产品secret"
	public static String deviceSecret = "123";

	//property post topic
	private static String pubTopic = "/sys/" + productKey + "/" + deviceName + "/thing/event/property/post";

	private static final String payloadJson = "{\"id\":%s,\"params\":{" +
			"\"paimiangunRotateSpeed\": %s," +
			"\"suimiangunRotateSpeed\": %s," +
			"\"chuanshudaiRotateSpeed\": %s," +
			"\"chengmomadaRotateSpeed\": %s," +
			"\"chengmozhijing\": %s," +
			"\"baibizhouPressure\": %s," +
			"\"shengyumoNum\": %s," +
			"\"boxFull\": %s," +
			"\"stateLight\": %s," +
			"\"neutral\": %s," +
			"\"lubricate\": %s," +
			"\"lock\": %s," +
			"\"imitate\": %s," +
			"\"walkingSpeed\": %s," +
			"\"fanRotatespeedLeft\": %s," +
			"\"fanRotatespeedRight\": %s," +
			"\"engineRotateSpeed\": %s," +
			"\"waterPressure\": %s," +
			"\"onlineWorking\": %s},\"method\":\"thing.event.property.post\"}";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*this.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
		setContentView(R.layout.work_layout);

		//找到控件1-1：左风机转速、右风机转速、发动机转速、水压、行走速度
		mTvfanRotatespeedLeft = (TextView) findViewById(R.id.rotate_speed_left);
		mTvfanRotatespeedRight = (TextView) findViewById(R.id.rotate_speed_right);
		mTvenginerotateSpeed = (TextView) findViewById(R.id.rotate_speed_engine);
		mTvwaterpressure = (TextView) findViewById(R.id.waterpressure);
		mTvspeedText = (TextView) findViewById(R.id.speed);

		//找到控件1-3：油位、（状态灯）警告、can总线在线状态
		mSpvpprogress = (SemicircleProgressBar) findViewById(R.id.progressbar);
		mIvstatusworking = (ImageView) findViewById(R.id.status_working);
		mIvOnlineworking = (ImageView) findViewById(R.id.online_working);

		//找到控件1-4：排棉辊转速、碎棉辊转速、传输带转速、成膜马达转速、摆臂轴压力、成膜压力、成膜直径
		mTvpaimiangunRotateSpeed = (TextView) findViewById(R.id.paimian);
		mTvsuimiangunRotateSpeed = (TextView) findViewById(R.id.suimian);
		mTvchuanshudaiRotateSpeed = (TextView) findViewById(R.id.chuansongdai);
		mTvchengmomadaRotateSpeed = (TextView) findViewById(R.id.chengmomada);
		mTvbaibizhouPressure = (TextView) findViewById(R.id.baibizhouyali);
		mTvshengyumoshu = (TextView) findViewById(R.id.shengyumoshu);
		mTvchengmozhijing = (TextView) findViewById(R.id.chengmozhijing);

		//找到控件2：发动机启动、空档、锁定、润滑、仿形、箱满、手动驾驶、自动驾驶、发动机工作时间、风机工作时间
		mVengineqidong = findViewById(R.id.engine_qidong);
		mVneutral = findViewById(R.id.kongdang_back);
		mVlock = findViewById(R.id.suoding_back);
		mVlubricate = findViewById(R.id.runhua_back);
		mVimitate = findViewById(R.id.fangxing_back);
		mVboxFull = findViewById(R.id.xiangman_back);
		mVdrivingback = findViewById(R.id.driving_back);

		mIvdrivingImageviewback = (ImageView) findViewById(R.id.driving_imageview_back);
		mTvdrivingTextviewback = (TextView) findViewById(R.id.driving_textview_back);

		mTvtimeworkEngine = (TextView) findViewById(R.id.time_work_engine);
		mTvtimeworkFan = (TextView) findViewById(R.id.time_work_fan);

		mBtnstatus = (Button) findViewById(R.id.status);
		mBtnsetting = (Button) findViewById(R.id.setting);
		mBtntest = (Button) findViewById(R.id.test);
		mBtngps = (Button) findViewById(R.id.gps);


		//打开串口（只用执行一次）
		try {
			mSerialPort = new SerialPort(new File(portPath), 115200, 0);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}

		initAliyunIoTClient();


		//ReadThread线程封装在MyTask类里，并每隔50ms执行一次线程 delay:10表示从10ms后开始执行
		//时间越短越会闪退，在保存can通信的情况下短时间不会发生闪退
		new Timer().schedule(new MyTask(), 0, 50);

		mBtnstatus.setOnClickListener(new View.OnClickListener() {
			@Override
			//public void onClick(View v){
//				Intent intent =new Intent();
//				intent.setClass(WorkActivity.this,StatusActivity.class);
//				//Intent intent = new Intent(WorkActivity.this, StatusActivity.class);
//				startActivity(intent);

			public void onClick(View v) {
				//用Bundle给状态界面发送数据
				Intent intent = new Intent(WorkActivity.this, StatusActivity.class);
				Bundle bundleStatus = new Bundle();
				intent.putExtras(bundleStatus);
				startActivity(intent);
//				startActivity(new Intent(WorkActivity.this, StatusActivity.class));
			}

		});
		mBtntest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WorkActivity.this, TestActivity.class);
				startActivity(intent);

			}

		});
		mBtngps.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(WorkActivity.this, GpsActivity.class);
				startActivity(intent);

			}

		});
		mBtnsetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder loginbuilder = new AlertDialog.Builder(WorkActivity.this);
				loginbuilder.setTitle("请输入密码");
				//loginbuilder.setCancelable(true);
				final View view = LayoutInflater.from(WorkActivity.this).inflate(R.layout.layout_login, null);
				loginbuilder.setView(view);
				//EditText etpassword = (EditText) view.findViewById(R.id.et_password);
				//Button mbtnLogin = (Button) view.findViewById(R.id.btn_login);
				//Button mbtnCancel = (Button) view.findViewById(R.id.btn_cancel);
				loginbuilder.setPositiveButton("登录", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText etpassword = (EditText) view.findViewById(R.id.et_password);
						String userpassword = etpassword.getText().toString();
						Log.d("MyLogin Dialog", "密码是" + userpassword);
						if (userpassword.equals("1")) {
							Intent intent = new Intent(WorkActivity.this, SettingActivity.class);
							startActivity(intent);
						} else {
							Toast.makeText(WorkActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
						}
					}
				});
				loginbuilder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
				loginbuilder.show();

			}

		});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
//		if (mReadThread != null)
//			mReadThread.interrupt();
		super.onDestroy();
		closeSerialPort();
		mSerialPort = null;

	}

	//读线程
//	private class ReadThread extends Thread {
//
//		@Override
//		public void run() {
//			super.run();
//
//			while(!isInterrupted()) {
//				int size;
//				try {
//					//Thread.sleep(10);
//					byte[] buffer = new byte[64];
//					if (mInputStream == null)
//						return;
//					size = mInputStream.read(buffer);
//					if (size > 0) {
//						onDataReceived(buffer, size);
//						//connectServerWithTCPSocket(buffer, size);
//						//TestClientSocket(buffer);
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//					return;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	/*
	*
	* 初始化Client对象
	*
	* */
	private void initAliyunIoTClient() {
		try {
			//构建连接需要参数
			String clientId = "androidthings" + System.currentTimeMillis();

			Map<String, String> params = new HashMap<String, String>(16);
			params.put("productKey", productKey);
			params.put("deviceName", deviceName);
			params.put("clientId", clientId);
			String timestamp = String.valueOf(System.currentTimeMillis());
			params.put("timestamp", timestamp);

			String targetServer = "tcp://" + productKey + ".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
			//String targetServer = "tcp://106.14.149.8：3306";
			String mqttclientId = clientId + "|securemode=3,signmethod=hmacsha1,timestamp=" + timestamp + "|";
			String mqttUsername = deviceName + "&" + productKey;
			String mqttPassword = AliyunIoTSignUtil.sign(params, deviceSecret, "hmacsha1");

			connectMqtt(targetServer, mqttclientId, mqttUsername, mqttPassword);
		} catch (Exception e) {
			Log.e(TAG, "initAliyunIoTClient error " + e.getMessage(), e);
		}
	}

	public void connectMqtt(String url, String clientId, String mqttUsername, String mqttPassword) throws Exception {

		MemoryPersistence persistence = new MemoryPersistence();
		mqttClient = new MqttClient(url, clientId, persistence);
		MqttConnectOptions connOpts = new MqttConnectOptions();
		// MQTT 3.1.1
		connOpts.setMqttVersion(4);
		connOpts.setAutomaticReconnect(true);
		connOpts.setCleanSession(true);

		connOpts.setUserName(mqttUsername);
		connOpts.setPassword(mqttPassword.toCharArray());
		connOpts.setKeepAliveInterval(60);

		mqttClient.connect(connOpts);
		Log.d(TAG, "connected " + url);

	}


	//定时读Run方法
	class MyTask extends TimerTask {
		@Override
		public void run() {
			/* Create a receiving thread */
//				mReadThread = new ReadThread();
//				mReadThread.start();
			int size;
			try {
				//Thread.sleep(10);
				byte[] buffer = new byte[64];
				if (mInputStream == null)
					return;
				size = mInputStream.read(buffer);//返回值size是实际读取的字节数
				if (size > 0) {
					onDataReceived(buffer, size);
//					connectServerWithTCPSocket(buffer, size);
					//TestClientSocket(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	//报错调试
	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				WorkActivity.this.finish();
			}
		});
		b.show();
	}

	//数据接收
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (size > 0) {
					String s = ByteArrToHex(buffer);
//					rotateSpeed.append(new String(buffer, 0, size));
					analysisData(s);
//					rotateSpeed.setText(s);
				}
			}
		});
	}

	//数据发送
	public void sendSerialPort(String data) {
		try {
			byte[] sendData = HexToByteArr(data);
			mOutputStream.write(sendData);
			mOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//can协议分析方法
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void analysisData(String s) {
		//以空格分隔数据
		String[] rawData = s.split(" ");
		String rawId = "";
		for (int i = 1; i < 5; i++) {
			rawId += rawData[i];
		}
		int id = Integer.valueOf(rawId, 16);
		int length = Integer.valueOf(rawData[5], 16);
		//初始化data数组
		int[] data = new int[length];
		for (int i = 6, j = 0; i < length + 6; i++, j++) {
//			System.out.println(rawData[i]);
			data[j] = Integer.valueOf(rawData[i], 16);
		}

		//协议按照圆包六行采棉机的协议
		switch (id & 0XFFFFFFFF) {

			// 油位can没有 先瞎给了一个
			case 0X19C0D2D3:

				//油位,百分比进度条显示
				oilLevel = data[2];
				// vpProgressBar.setProgress(oilLevel);
				mSpvpprogress.setProgress(oilLevel);

				break;

			case 0X19C1D2D3:
				//排棉辊转速、碎棉辊转速、传输带转速
				paimiangunRotateSpeedL = data[2];
				paimiangunRotateSpeedH = data[3];
				suimiangunRotateSpeedL = data[4];
				suimiangunRotateSpeedH = data[5];
				chuanshudaiRotateSpeedL = data[6];
				chuanshudaiRotateSpeedH = data[7];

				//排棉辊转速、碎棉辊转速、传输带转速
				paimiangunRotateSpeed = (paimiangunRotateSpeedH << 8) + paimiangunRotateSpeedL;
				suimiangunRotateSpeed = (suimiangunRotateSpeedH << 8) + suimiangunRotateSpeedL;
				chuanshudaiRotateSpeed = (chuanshudaiRotateSpeedH << 8) + chuanshudaiRotateSpeedL;

				//显示排棉辊转速、碎棉辊转速、传输带转速
				mTvpaimiangunRotateSpeed.setText(String.valueOf(paimiangunRotateSpeed));
				mTvsuimiangunRotateSpeed.setText(String.valueOf(suimiangunRotateSpeed));
				mTvchuanshudaiRotateSpeed.setText(String.valueOf(chuanshudaiRotateSpeed));

				//油温
				//bundleStatus.putInt("oilTemp", oilTemp);

				break;

			case 0X19C2D2D3:

				//成膜马达转速
				chengmomadaRotateSpeedL = data[0];
				chengmomadaRotateSpeedH = data[1];
				chengmomadaRotateSpeed = (chengmomadaRotateSpeedH << 8) + chengmomadaRotateSpeedL;

				//显示成模马达转速
				mTvchengmomadaRotateSpeed.setText(String.valueOf(chengmomadaRotateSpeed));

				break;

			case 0X19C3D2D3:

				//摆臂轴压力、成模压力、成模直径(由摆臂角度乘以系数得到)
				chengmozhijingL = data[0];
				chengmozhijingH = data[1];
				//chengmoPressureL = data[5];
				//chengmoPressureH = data[4];
				baibizhouPressureL = data[2];
				baibizhouPressureH = data[3];

				//系数定为1.5
				chengmozhijing = (1.5 * ((chengmozhijingH << 8) + chengmozhijingL));
				//chengmoPressure = (chengmoPressureH << 8) + chengmoPressureL;
				baibizhouPressure = (baibizhouPressureH << 8) + baibizhouPressureL;

				//显示摆臂轴压力、成模压力、成模直径
				mTvbaibizhouPressure.setText(String.valueOf(baibizhouPressure));
				//mTvchengmoPressure.setText(String.valueOf(chengmoPressure));
				mTvchengmozhijing.setText(String.valueOf(chengmozhijing));

				break;

			case 0X19C4D2D3:

				//剩余膜数
				shengyumoNum = data[1];
				mTvshengyumoshu.setText(String.valueOf(shengyumoNum));

				//箱满
				boxFull = (data[2] & 0X80)>>7;
				if (boxFull == 1) {
					mVboxFull.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				} else {
					mVboxFull.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}

				//控制板心跳（can）
				onlineWorking2 = (data[7] & 0X80)>>7;

				break;

			case 0X0301F5E0:
				onlineWorking1 = data[0] & 0X01;
				stateLightL = data[0] & 0X02;
				stateLightH = data[0] & 0X04;
				//engineqidong =
				//空挡
				neutral = (data[3] & 0X04) >> 2;
				//润滑
				lubricate = (data[3] & 0X08) >> 3;
				//驻车/锁定
				lock = (data[3] & 0X20) >> 5;
				//仿形
				imitate = (data[3] & 0X40) >> 6;
				//drivingManual, drivingAuto;

				stateLight = (stateLightH + stateLightL)>>1;//状态灯

				//状态灯处理，stateLight为 00-正常;01-警告;10-报警
				if (stateLight == 0x10) {
					//报警（红灯）
					mIvstatusworking.setImageDrawable(getResources().getDrawable(R.drawable.light2222));

				} else if (stateLight == 0x01) {
					//警告（黄灯）
					mIvstatusworking.setImageDrawable(getResources().getDrawable(R.drawable.light3333));
				} else {
					//其余情况
					mIvstatusworking.setImageDrawable(getResources().getDrawable(R.drawable.light1111));

				}

				//can在线，1在线的闪烁，0不在线灰色
				/*if (onlineWorking == 1) {
					//can在线
					mIvOnlineworking.setImageDrawable(getResources().getDrawable(R.drawable.zaixian111));
				}
				if (onlineWorking != 1){
					mIvOnlineworking.setImageDrawable(getResources().getDrawable(R.drawable.zaixian222));
				}
*/
				//启动、空挡、锁定、润滑、仿型 为1亮灯
/*				if(engineqidong == 1) {
					mVengineqidong.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				}else {
					mVengineqidong.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}*/

				if (neutral == 1) {
					mVneutral.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				} else {
					mVneutral.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}
				if (lock == 1) {
					mVlock.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				} else {
					mVlock.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}
				if (lubricate == 1) {
					mVlubricate.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				} else {
					mVlubricate.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}
				if (imitate == 1) {
					mVimitate.setBackground(getResources().getDrawable(R.drawable.stroke_corner_green));
				} else {
					mVimitate.setBackground(getResources().getDrawable(R.drawable.stroke_corner_right));
				}

				break;

			case 0X0302F5E0:
				walkingSpeedH = data[0];
				walkingSpeedL = data[1];
				fanRotatespeedLeftH = data[2];
				fanRotatespeedLeftL = data[3];
				fanRotatespeedRightH = data[4];
				fanRotatespeedRightL = data[5];
				//发动机转速（没给，临时）
				engineRotateSpeedL = data[4];
				engineRotateSpeedH = data[5];

				walkingSpeed = (walkingSpeedH << 8) + walkingSpeedL;
				fanRotatespeedLeft = (fanRotatespeedLeftH << 8) + fanRotatespeedLeftL;
				fanRotatespeedRight = (fanRotatespeedRightH << 8) + fanRotatespeedRightL;
				engineRotateSpeed = (engineRotateSpeedH << 8) + engineRotateSpeedL;

				//显示左风机转速、右风机转速、行走速度、发动机转速（临时）
				mTvfanRotatespeedLeft.setText(String.valueOf(fanRotatespeedLeft));
				mTvfanRotatespeedRight.setText(String.valueOf(fanRotatespeedRight));
				mTvspeedText.setText(String.valueOf(walkingSpeed));
				mTvenginerotateSpeed.setText(String.valueOf(engineRotateSpeed));

				break;

			case 0X0303F5E0:
				waterPressureH = data[0];
				waterPressureL = data[1];
				waterPressure = (waterPressureH << 8) + waterPressureL;
				//显示水压
				mTvwaterpressure.setText(String.valueOf(waterPressure));
				break;
			case 0X0305F5E0:
				break;
			case 0X0306F5E0:
				break;
			case 0X0307F5E0:
				break;
			case 0X0308F5E0:
				break;
			case 0x0201F5E0:
				break;
			case 0X0202F5E0:
				break;

			default:
				break;
		}

		onlineWorking = onlineWorking1 & onlineWorking2 ;
		if (onlineWorking == 1) {
			//can在线
			mIvOnlineworking.setImageDrawable(getResources().getDrawable(R.drawable.zaixian111));
		} else {
			mIvOnlineworking.setImageDrawable(getResources().getDrawable(R.drawable.zaixian222));
		}

		//向云平台发送数据
		postDeviceProperties();

	}


	//关闭串口
	public void closeSerialPort() {
		try {
			mInputStream.close();
			mOutputStream.close();
			if (mSerialPort != null) {
				mSerialPort.close();
				mSerialPort = null;
			}
		} catch (IOException e) {
			Log.i("WorkActivity", "关闭串口失败");
			return;
		}
		Log.i("WorkActivity", "关闭串口成功");
	}

	//1字节转2个Hex字符
	public String Byte2Hex(Byte inByte) {
		return String.format("%02x", inByte).toUpperCase();
	}

	//字节数组转hex字符串
	public String ByteArrToHex(byte[] inBytArr) {
		StringBuilder strBuilder = new StringBuilder();
		int j = 14;
		for (int i = 0; i < j; i++) {
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}

	//Hex字符串转byte
	public byte HexToByte(String inHex) {
		return (byte) Integer.parseInt(inHex, 16);
	}

	//转hex字符串转字节数组
	public byte[] HexToByteArr(String inHex) {
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen) == 1) {//奇数
			hexlen++;
			result = new byte[(hexlen / 2)];
			inHex = "0" + inHex;
		} else {//偶数
			result = new byte[(hexlen / 2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i += 2) {
			result[j] = HexToByte(inHex.substring(i, i + 2));
			j++;
		}
		return result;
	}

	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
	public int isOdd(int num) {
		return num & 0x1;
	}


	/*
	payload格式
	{
	"id": 123243,
	"params": {
    "temperature": 25.6,
    "humidity": 60.3,
    "ch2o": 0.048
    },
    "method": "thing.event.property.post"
    }
    */
	private void postDeviceProperties() {

		try {

			//上报数据
			String payload = String.format(payloadJson, String.valueOf(System.currentTimeMillis()),
					String.valueOf(paimiangunRotateSpeed),
					String.valueOf(suimiangunRotateSpeed),
					String.valueOf(chuanshudaiRotateSpeed),
					String.valueOf(chengmomadaRotateSpeed),
					String.valueOf(chengmozhijing),
					String.valueOf(baibizhouPressure),
					String.valueOf(shengyumoNum),
					String.valueOf(boxFull),
					String.valueOf(stateLight),
					String.valueOf(neutral),
					String.valueOf(lubricate),
					String.valueOf(lock),
					String.valueOf(imitate),
					String.valueOf(walkingSpeed),
					String.valueOf(fanRotatespeedLeft),
					String.valueOf(fanRotatespeedRight),
					String.valueOf(engineRotateSpeed),
					String.valueOf(waterPressure),
					String.valueOf(onlineWorking)
					);

			MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
			message.setQos(1);

			if (mqttClient == null) {
				initAliyunIoTClient();
			} else {
				mqttClient.publish(pubTopic, message);
				Log.d(TAG, "publish topic=" + pubTopic + ",payload=" + payload);
			}

		} catch (Exception e) {
			Log.e(TAG, "postDeviceProperties error " + e.getMessage(), e);
		}

	}
}
