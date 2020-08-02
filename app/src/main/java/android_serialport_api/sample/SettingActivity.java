package android_serialport_api.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

public class SettingActivity extends Activity {

    //设置变量 接收数据
    //左采头输入转速
    public static int leftPickingheadInputRsL,leftPickingheadInputRsH,leftPickingheadInputRs;
    //右采头输入转速
    public static int rightPickingheadInputRsL,rightPickingheadInputRsH,rightPickingheadInputRs;
    //采头前1-6转速,后1-6转速
    public static int frontPickinghead1RsL,frontPickinghead1RsH,frontPickinghead1Rs;
    public static int backPickinghead1RsL,backPickinghead1RsH,backPickinghead1Rs;
    public static int frontPickinghead2RsL,frontPickinghead2RsH,frontPickinghead2Rs;
    public static int backPickinghead2RsL,backPickinghead2RsH,backPickinghead2Rs;
    public static int frontPickinghead3RsL,frontPickinghead3RsH,frontPickinghead3Rs;
    public static int backPickinghead3RsL,backPickinghead3RsH,backPickinghead3Rs;
    public static int frontPickinghead4RsL,frontPickinghead4RsH,frontPickinghead4Rs;
    public static int backPickinghead4RsL,backPickinghead4RsH,backPickinghead4Rs;
    public static int frontPickinghead5RsL,frontPickinghead5RsH,frontPickinghead5Rs;
    public static int backPickinghead5RsL,backPickinghead5RsH,backPickinghead5Rs;
    public static int frontPickinghead6RsL,frontPickinghead6RsH,frontPickinghead6Rs;
    public static int backPickinghead6RsL,backPickinghead6RsH,backPickinghead6Rs;

    public static int rawidcar;
    //注意：因为存在2位小数，所以这些系数发送的时候乘以100，然后对方接收的时候再除以100
    public static int rawwaterPressureCO,rawspeedCO,rawfanRotatespeedCO,rawareaCO;

/*    //ID
    public static int idCarL, idCarH, idCar;
    //水路压力系数L\H，水路压力系数
    public static int waterPressureCOL, waterPressureCOH, waterPressureCO;
    //行走速度系数L\H，行走速度系数
    public static int speedCOL, speedCOH, speedCO;
    //风机转速系数L\H，风机转速系数
    public static int fanRotatespeedCOL, fanRotatespeedCOH, fanRotatespeedCO;
    //收获面积系数L\H
    public static int areaCOL, areaCOH, areaCO;
    //推杆比例系数L\H，推杆比例系数
    public static int pushRodCOL, pushRodCOH, pushRodCO;
    //转速系数L\H，转速系数
    public static int rotateSpeedCOL, rotateSpeedCOH, rotateSpeedCO;
    //采头报警系数L\H，采头报警系数
    public static int pickingheadWarnCOL, pickingheadWarnCOH, pickingheadWarnCO;
    //采头行程系数L\H，采头行程系数
    public static int pickingheadrRouteCOL,pickingheadrRouteCOH,pickingheadrRouteCO;
    //采头锁定系数L\H，采头锁定系数
    public static int pickingheadLockCOL,pickingheadLockCOH,pickingheadLockCO;
    //前采头齿数L\H，前采头齿数
    public static int frontPickingheadnNumL,frontPickingheadnNumH,frontPickingheadnNum;
    //后采头齿数L\H，后采头齿数
    public static int backPickingheadnNumL,backPickingheadnNumH,backPickingheadnNum;
    //采头输入齿数L\H，采头输入齿数
    public static int pickingheadUInputNumL,pickingheadUInputNumH,pickingheadUInputNum;*/


    //参数设置显示
    EditText mEtidCar;
    EditText mEtwaterPressureCO,mEtspeedCO,mEtfanRotatespeedCO,mEtareaCO,mEtpushRodCO,mEtrotateSpeedCO;
    EditText mEtpickingheadWarnCO,mEtpickingheadrRouteCO,mEtpickingheadLockCO,mEtfrontPickingheadnNum,mEtbackPickingheadnNum,mEtpickingheadUInputNum;
    //保存参数
    private Button mBtsave;
    //下一页（圆包参数界面）
    private Button mBtnextcontrol;
    //采头转速显示
    TextView mTvleftPickingheadInputRs,mTvrightPickingheadInputRs;
    TextView mTvfrontPickinghead1Rs,mTvbackPickinghead1Rs,mTvfrontPickinghead2Rs,mTvbackPickinghead2Rs;
    TextView mTvfrontPickinghead3Rs,mTvbackPickinghead3Rs, mTvfrontPickinghead4Rs,mTvbackPickinghead4Rs;
    TextView mTvfrontPickinghead5Rs,mTvbackPickinghead5Rs,mTvfrontPickinghead6Rs,mTvbackPickinghead6Rs;

    //声明页面跳转
    //Button work, status, more;

    private SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String portPath = "/dev/ttyMT0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        sharedPreferences = getSharedPreferences("MYDATA",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //找到控件
        mEtidCar = (EditText) findViewById(R.id.car_id);
        mEtidCar.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtidCar.setSelection(mEtidCar.getText().length());

        mEtwaterPressureCO = (EditText) findViewById(R.id.waterpressure_co);
        mEtwaterPressureCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtwaterPressureCO.setSelection(mEtwaterPressureCO.getText().length());

        mEtspeedCO = (EditText) findViewById(R.id.speed_co);
        mEtspeedCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtspeedCO.setSelection(mEtspeedCO.getText().length());

        mEtfanRotatespeedCO = (EditText) findViewById(R.id.fanrotatespeed_co);
        mEtfanRotatespeedCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtfanRotatespeedCO.setSelection(mEtfanRotatespeedCO.getText().length());

        mEtareaCO = (EditText) findViewById(R.id.area_co);
        mEtareaCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtareaCO.setSelection(mEtareaCO.getText().length());

        mEtpushRodCO = (EditText) findViewById(R.id.pushrod_co);
        mEtpushRodCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpushRodCO.setSelection(mEtpushRodCO.getText().length());

        mEtrotateSpeedCO = (EditText) findViewById(R.id.rotatespeed_co);
        mEtrotateSpeedCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtrotateSpeedCO.setSelection(mEtrotateSpeedCO.getText().length());

        mEtpickingheadWarnCO = (EditText) findViewById(R.id.pickingheadwarn_co);
        mEtpickingheadWarnCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpickingheadWarnCO.setSelection(mEtpickingheadWarnCO.getText().length());

        mEtpickingheadrRouteCO = (EditText) findViewById(R.id.pickingheadroute_co);
        mEtpickingheadrRouteCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpickingheadrRouteCO.setSelection(mEtpickingheadrRouteCO.getText().length());

        mEtpickingheadLockCO = (EditText) findViewById(R.id.pickingheadlock_co);
        mEtpickingheadLockCO.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpickingheadLockCO.setSelection(mEtpickingheadLockCO.getText().length());

        mEtfrontPickingheadnNum = (EditText) findViewById(R.id.frontpickinghead_num);
        mEtfrontPickingheadnNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtfrontPickingheadnNum.setSelection(mEtfrontPickingheadnNum.getText().length());

        mEtbackPickingheadnNum = (EditText) findViewById(R.id.backpickinghead_num);
        mEtbackPickingheadnNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtbackPickingheadnNum.setSelection(mEtbackPickingheadnNum.getText().length());

        mEtpickingheadUInputNum = (EditText) findViewById(R.id.pickingheadinput_num);
        mEtpickingheadUInputNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpickingheadUInputNum.setSelection(mEtpickingheadUInputNum.getText().length());

        mBtsave = (Button) findViewById(R.id.btn_save);
        mBtnextcontrol = (Button) findViewById(R.id.btn_next_control);

        mTvleftPickingheadInputRs = (TextView) findViewById(R.id.leftpickingheadinput_rs);
        mTvrightPickingheadInputRs = (TextView) findViewById(R.id.rightpickingheadinput_rs);
        mTvfrontPickinghead1Rs  = (TextView) findViewById(R.id.frontpickinghead1_rs);
        mTvbackPickinghead1Rs = (TextView) findViewById(R.id.backpickinghead1_rs);
        mTvfrontPickinghead2Rs  = (TextView) findViewById(R.id.frontpickinghead2_rs);
        mTvbackPickinghead2Rs = (TextView) findViewById(R.id.backpickinghead2_rs);
        mTvfrontPickinghead3Rs  = (TextView) findViewById(R.id.frontpickinghead3_rs);
        mTvbackPickinghead3Rs  = (TextView) findViewById(R.id.backpickinghead3_rs);
        mTvfrontPickinghead4Rs  = (TextView) findViewById(R.id.frontpickinghead4_rs);
        mTvbackPickinghead4Rs  = (TextView) findViewById(R.id.backpickinghead4_rs);
        mTvfrontPickinghead5Rs  = (TextView) findViewById(R.id.frontpickinghead5_rs);
        mTvbackPickinghead5Rs  = (TextView) findViewById(R.id.backpickinghead5_rs);
        mTvfrontPickinghead6Rs  = (TextView) findViewById(R.id.frontpickinghead6_rs);
        mTvbackPickinghead6Rs  = (TextView) findViewById(R.id.backpickinghead6_rs);

//        work = (Button)findViewById(R.id.work);
//        status = (Button)findViewById(R.id.status);
//        more = (Button)findViewById(R.id.more);

        //打开串口
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
        new Timer().schedule(new MyTask(),0,25);

        //保存参数
        String text1 = sharedPreferences.getString("idcar","");
        if(text1 == null){
            mEtidCar.setText("1");
        }else {
            mEtidCar.setText(text1);
        }

        String text2 = sharedPreferences.getString("waterPressurCO","");
        if(text2 == null){
            mEtwaterPressureCO.setText("1");
        }else{
            mEtwaterPressureCO.setText(text2);
        }

        String text3 = sharedPreferences.getString("speedCO","");
        if(text3 == null){
            mEtspeedCO.setText("1");
        }else{
            mEtspeedCO.setText(text3);
        }

        String text4 = sharedPreferences.getString("fanRotatespeedCO","");
        if(text4 == null){
            mEtfanRotatespeedCO.setText("1");
        }else {
            mEtfanRotatespeedCO.setText(text4);

        }

        String text5 = sharedPreferences.getString("areaCO","");
        if(text5 == null){
            mEtareaCO.setText("1");
        }else{
            mEtareaCO.setText(text5);

        }

/*        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MoreActivity.class);
                startActivity(intent);
            }
        });*/

        mBtnextcontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,Setting02Activity.class);
                startActivity(intent);
            }
        });

        mBtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rawidcarstring = mEtidCar.getText().toString().trim();
                String rawwaterPressurCOstring = mEtwaterPressureCO.getText().toString().trim();
                String rawspeedCOstring = mEtspeedCO.getText().toString().trim();
                String rawfanRotatespeedCOstring = mEtfanRotatespeedCO.getText().toString().trim();
                String rawareaCOstring = mEtareaCO.getText().toString().trim();


                if(!rawidcarstring.isEmpty()){
                    rawidcar = Integer.valueOf(rawidcarstring);
                    editor.putString("idcar",rawidcarstring);

                }else{
                    rawidcar = 1;
                    editor.putString("idcar","1");

                }

                if(!rawwaterPressurCOstring.isEmpty()){
                    rawwaterPressureCO = (int) ((Double.valueOf(rawwaterPressurCOstring))*100);
                    editor.putString("waterPressurCO",rawwaterPressurCOstring);

                }else{
                    rawwaterPressureCO = 100;
                    editor.putString("waterPressurCO","1.00");

                }
                if(!rawspeedCOstring.isEmpty()){
                    rawspeedCO = (int) ((Double.valueOf(rawspeedCOstring))*100);
                    editor.putString("speedCO",rawspeedCOstring);
                }else{
                    rawspeedCO = 100;
                    editor.putString("speedCO","1.00");

                }
                if(!rawfanRotatespeedCOstring.isEmpty()){
                    rawfanRotatespeedCO = (int) ((Double.valueOf(rawfanRotatespeedCOstring))*100);
                    editor.putString("fanRotatespeedCO",rawfanRotatespeedCOstring);

                }else{
                    rawfanRotatespeedCO = 100;
                    editor.putString("fanRotatespeedCO","1.00");

                }
                if(!rawareaCOstring.isEmpty()){
                    rawareaCO = (int) ((Double.valueOf(rawareaCOstring))*100);
                    editor.putString("areaCO",rawareaCOstring);

                }else{
                    rawareaCO = 1;
                    editor.putString("areaCO","1.00");
                }
                editor.apply();


                String stringidcar = String.format("%04X",rawidcar);
                String stringwaterPressureCO = String.format("%04X",rawwaterPressureCO);
                String stringspeedCO = String.format("%04X",rawspeedCO);
                String stringfanRotatespeedCO = String.format("%04X",rawfanRotatespeedCO);
                String stringareaCO = String.format("%04X",rawareaCO);

                StringBuilder strBuilder1 = new StringBuilder();
                strBuilder1.append("800202E0F508");
                strBuilder1.append(stringidcar);
                strBuilder1.append("000000000000");


                StringBuilder strBuilder2 = new StringBuilder();
                strBuilder2.append("800201E0F508");
                strBuilder2.append(stringwaterPressureCO);
                strBuilder2.append(stringspeedCO);
                strBuilder2.append(stringfanRotatespeedCO);
                strBuilder2.append(stringareaCO);

                try {
                    byte[] sendData1 = HexToByteArr(strBuilder1.toString());
                    byte[] sendData2 = HexToByteArr(strBuilder2.toString());
                    if (sendData1.length > 0 ||sendData2.length > 0  ) {
                        mOutputStream.write(sendData1);
                        mOutputStream.flush();
                        Thread.sleep(500);
                        mOutputStream.write(sendData2);
                        mOutputStream.flush();
                        Log.e("SettingActivity", "sendData1:" + Arrays.toString(sendData1));
                        Log.e("SettingActivity", "sendData2:" + Arrays.toString(sendData2));

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(SettingActivity.this,"发送参数并保存",Toast.LENGTH_SHORT).show();

            }
        });

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
            Log.i("SettingActivity","关闭串口失败");
            return;
        }
        Log.i("SettingActivity","关闭串口成功");
    }
    //报错调试
    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SettingActivity.this.finish();
            }
        });
        b.show();
    }

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
                size = mInputStream.read(buffer);
                if (size > 0) {
                    onDataReceived(buffer, size);
                    //connectServerWithTCPSocket(buffer, size);
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

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if(size > 0) {
                    String s = ByteArrToHex(buffer);
                    analysisData(s);
                }
            }
        });
    }

    //1字节转2个Hex字符
    public String Byte2Hex(Byte inByte)
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    //字节数组转hex字符串
    public String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder=new StringBuilder();
        int j = 14;
        for (int i = 0; i < j; i++)
        {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }


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

            case 0X0305F5E0:
                //采头转速1、2
                frontPickinghead1RsH = data[0];
                frontPickinghead1RsL = data[1];
                backPickinghead1RsH = data[2];
                backPickinghead1RsL = data[3];
                frontPickinghead2RsH = data[4];
                frontPickinghead2RsL = data[5];
                backPickinghead2RsH = data[6];
                backPickinghead2RsL = data[7];

                frontPickinghead1Rs = (frontPickinghead1RsH << 8) + frontPickinghead1RsL;
                backPickinghead1Rs = (backPickinghead1RsH << 8) + backPickinghead1RsL;
                frontPickinghead2Rs = (frontPickinghead2RsH << 8) + frontPickinghead2RsL;
                backPickinghead2Rs = (backPickinghead2RsH << 8) + backPickinghead2RsL;

                mTvfrontPickinghead1Rs.setText(String.valueOf(frontPickinghead1Rs));
                mTvbackPickinghead1Rs.setText(String.valueOf(backPickinghead1Rs));
                mTvfrontPickinghead2Rs.setText(String.valueOf(frontPickinghead2Rs));
                mTvbackPickinghead2Rs.setText(String.valueOf(backPickinghead2Rs));

                break;
            case 0X0306F5E0:
                //采头转速3、左输入
                frontPickinghead3RsH = data[0];
                frontPickinghead3RsL = data[1];
                backPickinghead3RsH = data[2];
                backPickinghead3RsL = data[3];
                leftPickingheadInputRsH = data[4];
                leftPickingheadInputRsL = data[5];


                frontPickinghead3Rs = (frontPickinghead3RsH << 8) + frontPickinghead3RsL;
                backPickinghead3Rs = (backPickinghead3RsH << 8) + backPickinghead3RsL;
                leftPickingheadInputRs = (leftPickingheadInputRsH << 8) + leftPickingheadInputRsL;

                mTvfrontPickinghead3Rs.setText(String.valueOf(frontPickinghead3Rs));
                mTvbackPickinghead3Rs.setText(String.valueOf(backPickinghead3Rs));
                mTvleftPickingheadInputRs.setText(String.valueOf(leftPickingheadInputRs));

                break;
            case 0X0307F5E0:

                //采头转速4、5
                frontPickinghead4RsH = data[0];
                frontPickinghead4RsL = data[1];
                backPickinghead4RsH = data[2];
                backPickinghead4RsL = data[3];
                frontPickinghead5RsH = data[4];
                frontPickinghead5RsL = data[5];
                backPickinghead5RsH = data[6];
                backPickinghead5RsL = data[7];

                frontPickinghead4Rs = (frontPickinghead4RsH << 8) + frontPickinghead4RsL;
                backPickinghead4Rs = (backPickinghead4RsH << 8) + backPickinghead4RsL;
                frontPickinghead5Rs = (frontPickinghead5RsH << 8) + frontPickinghead5RsL;
                backPickinghead5Rs = (backPickinghead5RsH << 8) + backPickinghead5RsL;

                mTvfrontPickinghead4Rs.setText(String.valueOf(frontPickinghead4Rs));
                mTvbackPickinghead4Rs.setText(String.valueOf(backPickinghead4Rs));
                mTvfrontPickinghead5Rs.setText(String.valueOf(frontPickinghead5Rs));
                mTvbackPickinghead5Rs.setText(String.valueOf(backPickinghead5Rs));

                break;
            case 0X0308F5E0:
                //采头转速6、右输入
                frontPickinghead6RsH = data[0];
                frontPickinghead6RsL = data[1];
                backPickinghead6RsH = data[2];
                backPickinghead6RsL = data[3];
                rightPickingheadInputRsH = data[4];
                rightPickingheadInputRsL = data[5];


                frontPickinghead6Rs = (frontPickinghead6RsH << 8) + frontPickinghead6RsL;
                backPickinghead6Rs = (backPickinghead6RsH << 8) + backPickinghead6RsL;
                rightPickingheadInputRs = (rightPickingheadInputRsH << 8) + rightPickingheadInputRsL;

                mTvfrontPickinghead6Rs.setText(String.valueOf(frontPickinghead6Rs));
                mTvbackPickinghead6Rs.setText(String.valueOf(backPickinghead6Rs));
                mTvrightPickingheadInputRs.setText(String.valueOf(rightPickingheadInputRs));
                break;
            case 0x0201F5E0:
                break;
            case 0X0202F5E0:
                break;
            default:
                break;
        }

    }

    //hex字符串转字节数组
    public byte[] HexToByteArr(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {//奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {//偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2)
        {
            result[j]=HexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
    //Hex字符串转byte
    public byte HexToByte(String inHex)
    {
        return (byte)Integer.parseInt(inHex,16);
    }
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public int isOdd(int num)
    {
        return num & 0x1;
    }


}