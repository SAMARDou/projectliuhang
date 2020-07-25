package android_serialport_api.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

public class TestActivity extends Activity {


    //设置变量 接收数据
    //喂棉马达转速传感器电压值
    public static double weimianmadaRotateSensorVoltage;
    //碎棉马达转速传感器电压值
    public static double suimianmadaRotateSensorVoltage;
    //排棉马达转速传感器电压值
    public static double paimianmadaRotateSensorVoltage;
    //上辊马达转速传感器电压值
    public static double shanggunmadaRotateSensorVoltage;
    //下辊马达转速传感器电压值
    public static double xiagunmadaRotateSensorVoltage;
    //棉膜辊马达转速传感器电压值
    public static double mianmogunmadaRotateSensorVoltage;

    //传感器电压值 和 传感器状态 显示
    TextView mTvweimianmadaRotateSensorVoltage,mTvweimianmadaRotateSensorVoltageStatus;
    TextView mTvsuimianmadaRotateSensorVoltage,mTvsuimianmadaRotateSensorVoltageStatus;
    TextView mTvpaimianmadaRotateSensorVoltage,mTvpaimianmadaRotateSensorVoltageStatus;
    TextView mTvshanggunmadaRotateSensorVoltage,mTvshanggunmadaRotateSensorVoltageStatus;
    TextView mTvxiagunmadaRotateSensorVoltage,mTvxiagunmadaRotateSensorVoltageStatus;
    TextView mTvmianmogunmadaRotateSensorVoltage,mTvmianmogunmadaRotateSensorVoltageStatus;

    private SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    String portPath = "/dev/ttyMT0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //找到控件
        mTvweimianmadaRotateSensorVoltage = (TextView) findViewById(R.id.sensor_voltage_weimianmadarotate);
        mTvweimianmadaRotateSensorVoltageStatus = (TextView) findViewById(R.id.status_sensor_voltage_weimianmadarotate);
        mTvsuimianmadaRotateSensorVoltage = (TextView)findViewById(R.id.sensor_voltage_suimianmadarotate);
        mTvsuimianmadaRotateSensorVoltageStatus = (TextView) findViewById(R.id.status_sensor_voltage_suimianmadarotate);
        mTvpaimianmadaRotateSensorVoltage = (TextView) findViewById(R.id.sensor_voltage_paimianmadarotate);
        mTvpaimianmadaRotateSensorVoltageStatus = (TextView) findViewById(R.id.status_sensor_voltage_paimianmadarotate);
        mTvshanggunmadaRotateSensorVoltage = (TextView) findViewById(R.id.sensor_voltage_shanggunmadarotate);
        mTvshanggunmadaRotateSensorVoltageStatus = (TextView) findViewById(R.id.status_sensor_voltage_shanggunmadarotate);
        mTvxiagunmadaRotateSensorVoltage = (TextView) findViewById(R.id.sensor_voltage_xiagunmadarotate);
        mTvxiagunmadaRotateSensorVoltageStatus  = (TextView) findViewById(R.id.status_sensor_voltage_xiagunmadarotate);
        mTvmianmogunmadaRotateSensorVoltage = (TextView) findViewById(R.id.sensor_voltage_mianmogunmadarotate);
        mTvmianmogunmadaRotateSensorVoltageStatus = (TextView) findViewById(R.id.status_sensor_voltage_mianmogunmadarotate);

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

            case 0X19C5D2D3:
                //传感器电压值
                weimianmadaRotateSensorVoltage = data[0] >> 2;
                suimianmadaRotateSensorVoltage = data[1] >> 2;
                paimianmadaRotateSensorVoltage = data[2] >> 2;
                shanggunmadaRotateSensorVoltage = data[3] >> 2;
                xiagunmadaRotateSensorVoltage = data[4] >> 2;
                mianmogunmadaRotateSensorVoltage = data[5] >> 2;


                mTvweimianmadaRotateSensorVoltage.setText(String.valueOf(weimianmadaRotateSensorVoltage));
                if(weimianmadaRotateSensorVoltage < 1.35){
                    mTvweimianmadaRotateSensorVoltageStatus.setText("短路");
                }else if (weimianmadaRotateSensorVoltage < 1.5){
                    mTvweimianmadaRotateSensorVoltageStatus.setText("断路");
                }else if(weimianmadaRotateSensorVoltage < 1.8){
                    mTvweimianmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvweimianmadaRotateSensorVoltageStatus.setText("正常");
                }

                mTvsuimianmadaRotateSensorVoltage.setText(String.valueOf(suimianmadaRotateSensorVoltage));
                if(suimianmadaRotateSensorVoltage < 1.35){
                    mTvsuimianmadaRotateSensorVoltageStatus.setText("短路");
                }else if (suimianmadaRotateSensorVoltage < 1.5){
                    mTvsuimianmadaRotateSensorVoltageStatus.setText("断路");
                }else if(suimianmadaRotateSensorVoltage < 1.8){
                    mTvsuimianmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvsuimianmadaRotateSensorVoltageStatus.setText("正常");
                }

                mTvpaimianmadaRotateSensorVoltage.setText(String.valueOf(paimianmadaRotateSensorVoltage));
                if(paimianmadaRotateSensorVoltage < 1.35){
                    mTvpaimianmadaRotateSensorVoltageStatus.setText("短路");
                }else if (paimianmadaRotateSensorVoltage < 1.5){
                    mTvpaimianmadaRotateSensorVoltageStatus.setText("断路");
                }else if(paimianmadaRotateSensorVoltage < 1.8){
                    mTvpaimianmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvpaimianmadaRotateSensorVoltageStatus.setText("正常");
                }

                mTvshanggunmadaRotateSensorVoltage.setText(String.valueOf(shanggunmadaRotateSensorVoltage));
                if(shanggunmadaRotateSensorVoltage < 1.35){
                    mTvshanggunmadaRotateSensorVoltageStatus.setText("短路");
                }else if (shanggunmadaRotateSensorVoltage < 1.5){
                    mTvshanggunmadaRotateSensorVoltageStatus.setText("断路");
                }else if(shanggunmadaRotateSensorVoltage < 1.8){
                    mTvshanggunmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvshanggunmadaRotateSensorVoltageStatus.setText("正常");
                }

                mTvxiagunmadaRotateSensorVoltage.setText(String.valueOf(xiagunmadaRotateSensorVoltage));
                if(xiagunmadaRotateSensorVoltage < 1.35){
                    mTvxiagunmadaRotateSensorVoltageStatus.setText("短路");
                }else if (xiagunmadaRotateSensorVoltage < 1.5){
                    mTvxiagunmadaRotateSensorVoltageStatus.setText("断路");
                }else if(xiagunmadaRotateSensorVoltage < 1.8){
                    mTvxiagunmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvxiagunmadaRotateSensorVoltageStatus.setText("正常");
                }

                mTvmianmogunmadaRotateSensorVoltage.setText(String.valueOf(mianmogunmadaRotateSensorVoltage));
                if(mianmogunmadaRotateSensorVoltage < 1.35){
                    mTvmianmogunmadaRotateSensorVoltageStatus.setText("短路");
                }else if (mianmogunmadaRotateSensorVoltage < 1.5){
                    mTvmianmogunmadaRotateSensorVoltageStatus.setText("断路");
                }else if(mianmogunmadaRotateSensorVoltage < 1.8){
                    mTvmianmogunmadaRotateSensorVoltageStatus.setText("传感器坏");
                }else{
                    mTvmianmogunmadaRotateSensorVoltageStatus.setText("正常");
                }

                break;
            default:
                break;
        }

    }

    //报错调试
    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                TestActivity.this.finish();
            }
        });
        b.show();
    }
}
