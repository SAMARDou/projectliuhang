package android_serialport_api.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPort;

public class StatusActivity extends Activity {

    //设置变量 接收数据
    //吸入门1-6
    public static int suckGate1, suckGate2, suckGate3, suckGate4, suckGate5, suckGate6;
    //离合器1-6
    public static int clutch1, clutch2, clutch3, clutch4, clutch5, clutch6;
    //水温L、H,水温
    public static int waterTempL, waterTempH, waterTemp;
    //水压L、水压H、水压
    //public static int waterPressureL, waterPressureH, waterPressure;
    //油温L、H,油温
    public static int oilTempL, oilTempH,oilTemp;
    //油压L、油压H、油压
    //public static int oilPressureL, oilPressureH, oilPressure;
    //搅龙速度L、H，搅龙速度
    public static int augerSpeedL, augerSpeedH, augerSpeed;
    //搅龙压力L、H，搅龙压力
    public static int augerPressureL,augerPressureH,augerPressure;
    //小计面积LL、LH、HL、HH
    public static int areaSubtotalLL, areaSubtotalLH, areaSubtotalHL, areaSubtotalHH;
    //小计面积
    public static double areaSubtotal;
    //总计面积LL、LH、HL、HH
    public static int areaTotalLL, areaTotalLH, areaTotalHL, areaTotalHH;
    //总计面积
    public static double areaTotal;
    //风机报警(左右风机)，水压报警，水温报警，油压报警，油温报警，中位报警，空滤报警，卸棉报警
    public static int fanWarnLeft,fanWarnRight,fanWarn,waterPressureWarn,waterTempWarn,oilPressureWarn,oilTempWarn;
    public static int zhongweiWarn,airfilterWarn,dischargeCottonWarn;

    //总故障
    public static int totallTrouble;
    //内棉箱左故障，内棉箱右故障
    public static int hopperLeftTrouble,hopperRightTrouble;
    //前仓锁定钩左故障，前仓锁定钩右故障
    public static int frontWarehouseLeftTrouble,frontWarehouseRightTrouble;
    //搅龙故障，搅龙速度故障，搅龙压力故障
    public static int augerTrouble,augerSpeedTrouble,augerPressureTrouble;
    //喂棉马达故障，碎棉马达故障，排棉马达故障
    public static int weimianmadaTrouble,suimianmadaTrouble,paimianmadaTrouble;
    //打包泵故障
    public static int packingPumpTrouble;
    //门闩锁故障，门闩锁开了
    public static int latchLockTrouble,latchLockOpen;
    //正在出仓但是箱满、正在缠膜但是箱满
    public static int chucangxiangman,chanmoxiangman;


    //故障代码
    //public static int errorCode0, errorCode1, errorCode2, errorCode3, errorCode4, errorCode5, errorCode00, errorCode10, errorCode20, errorCode30, errorCode40, errorCode50;
    //棉箱侧翻，棉箱门开，门卸棉，棉箱返回，棉箱门关，全卸棉
    //public static int cottonBoxRollover, cottonBoxDoorOpen, doorDischargeCotton, cottonBoxBack, cottonBoxDoorClose, allDischargeCotton;


    //声明控件
    //吸入门
    ImageView mIvsuckGate1, mIvsuckGate2,mIvsuckGate3,mIvsuckGate4,mIvsuckGate5, mIvsuckGate6;
    //离合器
    ImageView mIvclutch1, mIvclutch2, mIvclutch3, mIvclutch4, mIvclutch5, mIvclutch6;
    //风机报警，水压报警，水温报警，油压报警，油温报警，中位报警，空滤报警，卸棉报警
    ImageView mIvfanWarn,mIvwaterPressureWarn,mIvwaterTempWarn,mIvoilPressureWarn,mIvoilTempWarn;
    ImageView mIvzhongweiWarn,mIvairfilterWarn,mIvdischargeCottonWarn;
    //故障报警
    ImageView mIvtotallTrouble;
    ImageView mIvhopperLeftTrouble,mIvhopperRightTrouble;
    ImageView mIvfrontWarehouseLeftTrouble,mIvfrontWarehouseRightTrouble;
    ImageView mIvaugerTrouble,mIvaugerSpeedTrouble,mIvaugerPressureTrouble;
    ImageView mIvweimianmadaTrouble,mIvsuimianmadaTrouble,mIvpaimianmadaTrouble;
    ImageView mIvpackingPumpTrouble;
    ImageView mIvlatchLockTrouble;
    //水温、油温、搅龙速度、搅龙压力
    TextView mTvwaterTemp,mTvoilTemp,mTvaugerSpeed,mTvaugerPressure;
    //小计面积、总计面积
    TextView mTvarea;
    ImageView mIvzongjiqiehuan,mIvxiaojiqingling;
    //故障详细信息显示
    TextView mTvguzhangxinxi1,mTvguzhangxinxi2,mTvguzhangxinxi3;

    //声明页面跳转
    //Button mBtnwork, mBtnsetting, mBtnmore;

    private SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    String portPath = "/dev/ttyMT0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_layout);

        //找到控件
        mIvsuckGate1 = (ImageView)findViewById(R.id.gate1);
        mIvsuckGate2 = (ImageView)findViewById(R.id.gate2);
        mIvsuckGate3 = (ImageView)findViewById(R.id.gate3);
        mIvsuckGate4 = (ImageView)findViewById(R.id.gate4);
        mIvsuckGate5 = (ImageView)findViewById(R.id.gate5);
        mIvsuckGate6 = (ImageView)findViewById(R.id.gate6);

        mIvclutch1 = (ImageView)findViewById(R.id.clutch1);
        mIvclutch2 = (ImageView)findViewById(R.id.clutch2);
        mIvclutch3 = (ImageView)findViewById(R.id.clutch3);
        mIvclutch4 = (ImageView)findViewById(R.id.clutch4);
        mIvclutch5 = (ImageView)findViewById(R.id.clutch5);
        mIvclutch6 = (ImageView)findViewById(R.id.clutch6);

        mTvwaterTemp = (TextView) findViewById(R.id.shuiwen);
        mTvoilTemp = (TextView) findViewById(R.id.youwen);
        mTvaugerSpeed = (TextView) findViewById(R.id.jiaolongsudu);
        mTvaugerPressure = (TextView) findViewById(R.id.jiaolongyali);

        mTvarea = (TextView) findViewById(R.id.area);
        mIvzongjiqiehuan = (ImageView) findViewById(R.id.zongjiqiehuan);
        mIvxiaojiqingling = (ImageView) findViewById(R.id.xiaojiqingling);

        mIvfanWarn = (ImageView)findViewById(R.id.fengjibaojing);
        mIvwaterPressureWarn = (ImageView)findViewById(R.id.shuiyabaojing);
        mIvwaterTempWarn = (ImageView)findViewById(R.id.shuiwenbaojing);
        mIvoilPressureWarn = (ImageView)findViewById(R.id.youyabaojing);
        mIvoilTempWarn = (ImageView)findViewById(R.id.youwenbaojing);
        mIvzhongweiWarn = (ImageView)findViewById(R.id.zhongwei);
        mIvdischargeCottonWarn = (ImageView) findViewById(R.id.xiemian);
        mIvairfilterWarn = (ImageView) findViewById(R.id.konglv);

        mIvtotallTrouble = (ImageView) findViewById(R.id.zongguzhang);
        mIvhopperLeftTrouble = (ImageView)findViewById(R.id.neimianxiangzuo);
        mIvhopperRightTrouble = (ImageView)findViewById(R.id.neimianxiangyou);
        mIvfrontWarehouseLeftTrouble = (ImageView)findViewById(R.id.suodinggouzuo);
        mIvfrontWarehouseRightTrouble = (ImageView)findViewById(R.id.suodinggouyou);
        mIvaugerTrouble = (ImageView)findViewById(R.id.jiaolongguzhang);
        mIvaugerSpeedTrouble = (ImageView)findViewById(R.id.jiaolongsuduguzhang);
        mIvaugerPressureTrouble = (ImageView)findViewById(R.id.jiaolongyaliguzhang);
        mIvweimianmadaTrouble = (ImageView)findViewById(R.id.weimianmadaguzhang);
        mIvsuimianmadaTrouble = (ImageView)findViewById(R.id.suimianmadaguzhang);
        mIvpaimianmadaTrouble = (ImageView)findViewById(R.id.paimianmadaguzhang);
        mIvpackingPumpTrouble = (ImageView)findViewById(R.id.dabaobengguzhang);
        mIvlatchLockTrouble = (ImageView)findViewById(R.id.menshuansuoguzhang);

        mTvguzhangxinxi1 = (TextView) findViewById(R.id.guzhangxinxi1);
        mTvguzhangxinxi2 = (TextView) findViewById(R.id.guzhangxinxi2);
        mTvguzhangxinxi3 = (TextView) findViewById(R.id.guzhangxinxi3);

//        mBtnwork = (Button)findViewById(R.id.work);
//        mBtnsetting = (Button)findViewById(R.id.setting);
//        mBtnmore = (Button)findViewById(R.id.more);

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
        new Timer().schedule(new MyTask(),0,50);


        mIvzongjiqiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTvarea.getText().toString().equals(String.valueOf(areaSubtotal)))
                {
                    mTvarea.setText(String.valueOf(areaTotal));
                }
                else
                {
                    mTvarea.setText(String.valueOf(areaSubtotal));
                }
            }
        });
        mIvxiaojiqingling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvarea.setText("0000");
                try {
                    String stringclear = "8019C5E0F5080000000000000001";
                    byte[] sendData0 = HexToByteArr(stringclear);
                    //byte[] sendData0 = {0x08, 00, 00, 00, 00, 0x08, 00, 00, 00, 00, 00, 00, 00, 01};
                    if (sendData0.length > 0) {
                        mOutputStream.write(sendData0);
                        mOutputStream.flush();
                        Log.e("StatusActivity", "sendData0:" + Arrays.toString(sendData0));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*try {
                    String stringclear = "8019C5E0F5080000000000000001";
                    //byte[] sendData0 = HexToByteArr(stringclear);
                    byte[] sendData0 = {80,19,00,00,00,00,00,00,00,00,00,00,00,01};
                    if (sendData0.length > 0) {
                        mOutputStream.write(sendData0);
                        mOutputStream.write('\n');
                        mOutputStream.flush();
                        Log.e("WorkActivity", "sendData0:" + sendData0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Toast.makeText(StatusActivity.this,"发送参数",Toast.LENGTH_SHORT).show();

            }
        });
        /*mBtnwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });
        mBtnmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, MoreActivity.class);
                startActivity(intent);
            }
        });
        mBtnsetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final AlertDialog.Builder loginbuilder = new AlertDialog.Builder(StatusActivity.this);
                View view = LayoutInflater.from(StatusActivity.this).inflate(R.layout.layout_login,null);
                final EditText etpassword = (EditText) view.findViewById(R.id.et_password);
                Button mbtnLogin = (Button) view.findViewById(R.id.btn_login);
                Button mbtnCancel = (Button) view.findViewById(R.id.btn_cancel);
                mbtnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userpassword = etpassword.getText().toString();
                        if(userpassword.equals("123456")){
                            Intent intent = new Intent(StatusActivity.this, SettingActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(StatusActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final AlertDialog dialog = loginbuilder.show();
                mbtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //关闭
                        dialog.dismiss();
                    }
                });
                loginbuilder.setTitle("请输入密码").setView(view).show();
            }

        });
*/
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
                    byte[] buffer = new byte[14];
                    if (mInputStream == null)
                        return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
//						connectServerWithTCPSocket(buffer, size);
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

    //报错调试
    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                StatusActivity.this.finish();
            }
        });
        b.show();
    }

    protected void analysisData(String s){
        //以空格分隔数据
        String[] rawData = s.split(" ");
        String rawId = "";
        for (int i = 1; i < 5; i++) {
            rawId += rawData[i];
        }
        int id = Integer.valueOf(rawId,16);
        int length = Integer.valueOf(rawData[5],16);
        //初始化data数组
        int[] data = new int[length];
        for(int i = 6,j = 0; i < length + 6; i++,j++) {
//			System.out.println(rawData[i]);
            data[j] = Integer.valueOf(rawData[i], 16);
        }

        //协议按照圆包六行采棉机的协议
        switch (id & 0XFFFFFFFF) {
            case 0X0301F5E0:

                suckGate1 = data[1] & 0X01;
                suckGate2 = (data[1] & 0X02) >> 1;
                suckGate3 = (data[1] & 0X04) >> 2;
                suckGate4 = (data[1] & 0X08) >> 3;
                suckGate5 = (data[1] & 0X10) >> 4;
                suckGate6 = (data[1] & 0X20) >> 5;
                waterPressureWarn = (data[1] & 0X40) >> 6;
                fanWarnLeft = (data[1] & 0X80) >> 7;

                clutch1 = data[2] & 0X01;
                clutch2 = (data[2] & 0X02) >> 1;
                clutch3 = (data[2] & 0X04) >> 2;
                clutch4 = (data[2] & 0X08) >> 3;
                clutch5 = (data[2] & 0X10) >> 4;
                clutch6 = (data[2] & 0X20) >> 5;
                fanWarnRight = (data[2] & 0X40) >> 6;
                zhongweiWarn = (data[2] & 0X80) >> 7;

                //空滤（没有信号）
                airfilterWarn = (data[3] & 0X10) >> 4;

                fanWarn = (fanWarnLeft | fanWarnRight);


                //吸入门 为1红色
                if (suckGate1 == 1) {
                    mIvsuckGate1.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate1.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (suckGate2 == 1) {
                    mIvsuckGate2.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate2.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (suckGate3 == 1) {
                    mIvsuckGate3.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate3.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (suckGate4 == 1) {
                    mIvsuckGate4.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate4.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (suckGate5 == 1) {
                    mIvsuckGate5.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate5.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (suckGate6 == 1) {
                    mIvsuckGate6.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvsuckGate6.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }

                //离合器 为1红色
                if (clutch1 == 0X01) {
                    mIvclutch1.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch1.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (clutch2 == 0X02) {
                    mIvclutch2.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch2.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (clutch3 == 0X04) {
                    mIvclutch3.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch3.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (clutch4 == 0X08) {
                    mIvclutch4.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch4.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (clutch5 == 0X10) {
                    mIvclutch5.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch5.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                if (clutch6 == 0X20) {
                    mIvclutch6.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvclutch6.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //风机报警
                if (fanWarn == 1) {
                    mIvfanWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvfanWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //水压报警
                if (waterPressureWarn == 1) {
                    mIvwaterPressureWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvwaterPressureWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //中位报警
                if (zhongweiWarn == 1) {
                    mIvzhongweiWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvzhongweiWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //空滤报警（没有）
                if (airfilterWarn == 1) {
                    mIvairfilterWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvairfilterWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }

                break;

            case 0X0303F5E0:
                oilTempL = data[5];
                oilTempH = data[4];
                oilTemp = (oilTempH << 8) + oilTempL;
                //显示油温
                mTvoilTemp.setText(String.valueOf(oilTemp));
                break;

            case 0X0304F5E0:
                //小计
                areaSubtotalLL = data[3];
                areaSubtotalLH = data[2];
                areaSubtotalHL = data[1];
                areaSubtotalHH = data[0];
                //总计
                areaTotalLL = data[7];
                areaTotalLH = data[6];
                areaTotalHL = data[5];
                areaTotalHH = data[4];

                //小计面积、总计面积（收获面积精确到小数点后2位，即0.01，CAN发送的时候乘以100，所以接收到以后要除以100）
                areaSubtotal = ((areaSubtotalHH << 32) + (areaSubtotalHL << 16) + (areaSubtotalLH << 8) + areaSubtotalLL)>>2;
                areaTotal = ((areaTotalHH << 32) + (areaTotalHL << 16) + (areaTotalLH << 8) + areaTotalLL)>>2;
                mTvarea.setText(String.valueOf(areaSubtotal));

                break;

            case 0X19C1D2D3:
                //搅龙速度
                augerSpeedL = data[0];
                augerSpeedH = data[1];
                augerSpeed = (augerSpeedH << 8) + augerSpeedL;
                //显示搅龙速度
                mTvaugerSpeed.setText(String.valueOf(augerSpeed));

                break;

            case 0X19C2D2D3:
                //搅龙压力
                augerPressureL = data[6];
                augerPressureH = data[7];
                augerPressure = (augerPressureH << 8) + augerPressureL;
                //显示搅龙压力
                mTvaugerPressure.setText(String.valueOf(augerPressure));

                break;

            case 0X19C4D2D3:

                dischargeCottonWarn = data[5] & 0X01;
                totallTrouble = (data[5] & 0X40) >> 6;
                hopperLeftTrouble = (data[5] & 0X80) >> 7;
                hopperRightTrouble = data[6] & 0X01;
                frontWarehouseLeftTrouble  = (data[6] & 0X02) >> 1;
                frontWarehouseRightTrouble = (data[6] & 0X04) >> 2;
                augerTrouble = (data[6] & 0X08) >> 3;
                augerSpeedTrouble = (data[6] & 0X10) >> 4;
                augerPressureTrouble = (data[6] & 0X20) >> 5;
                weimianmadaTrouble = (data[6] & 0X40) >> 6;
                suimianmadaTrouble = (data[6] & 0X80) >> 7 ;
                paimianmadaTrouble = data[7] & 0X01;
                packingPumpTrouble = (data[7] & 0X02) >> 1;
                latchLockTrouble  = (data[7] & 0X04) >> 2;
                latchLockOpen = (data[7] & 0X08) >> 3;
                chucangxiangman = (data[7] & 0X10) >> 4;
                chanmoxiangman = (data[7] & 0X20) >> 5;

                //卸棉报警
                if (dischargeCottonWarn == 1) {
                    mIvdischargeCottonWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvdischargeCottonWarn.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }

                //故障报警信息   1为故障（红色）
                int i=1;
                Calendar C = Calendar.getInstance();
                Date date = C.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(date);

                //总故障
                if (totallTrouble == 1) {
                    mIvtotallTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                }else{
                    mIvtotallTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //内棉箱左故障
                if (hopperLeftTrouble == 1) {
                    mIvhopperLeftTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                        mTvguzhangxinxi1.setText("内棉箱左故障"+time);
                        i++;
                }else{
                    mIvhopperLeftTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //内棉箱右故障
                if (hopperRightTrouble == 1) {
                    mIvhopperRightTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("内棉箱右故障"+time);
                        i++;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("内棉箱右故障"+time);
                        i++;
                    }
                }else{
                    mIvhopperRightTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //前仓锁定钩左故障
                if (frontWarehouseLeftTrouble == 1) {
                    mIvfrontWarehouseLeftTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("前仓锁定钩左故障"+time);
                        i++;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("前仓锁定钩左故障"+time);
                        i++;
                    }else{
                        mTvguzhangxinxi3.setText("前仓锁定钩左故障"+time);
                    }
                }else{
                    mIvfrontWarehouseLeftTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //前仓锁定钩右故障
                if (frontWarehouseRightTrouble == 1) {
                    mIvfrontWarehouseRightTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("前仓锁定钩右故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("前仓锁定钩右故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("前仓锁定钩右故障"+time);
                    }
                }else{
                    mIvfrontWarehouseRightTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //搅龙故障
                if (augerTrouble == 1) {
                    mIvaugerTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("搅龙故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("搅龙故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("搅龙故障"+time);
                    }
                }else{
                    mIvaugerTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //搅龙速度故障
                if (augerSpeedTrouble == 1) {
                    mIvaugerSpeedTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("搅龙速度故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("搅龙速度故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("搅龙速度故障"+time);
                    }
                }else{
                    mIvaugerSpeedTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //搅龙压力故障
                if (augerPressureTrouble == 1) {
                    mIvaugerPressureTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("搅龙压力故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("搅龙压力故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("搅龙压力故障"+time);
                    }
                }else{
                    mIvaugerPressureTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //喂棉马达故障
                if (weimianmadaTrouble == 1) {
                    mIvweimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("喂棉马达故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("喂棉马达故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("喂棉马达故障"+time);
                    }
                }else{
                    mIvweimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //碎棉马达故障
                if (suimianmadaTrouble == 1) {
                    mIvsuimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("碎棉马达故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("碎棉马达故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("碎棉马达故障"+time);
                    }
                }else{
                    mIvsuimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //排棉马达故障
                if (paimianmadaTrouble == 1) {
                    mIvpaimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("排棉马达故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("排棉马达故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("排棉马达故障"+time);
                    }
                }else{
                    mIvpaimianmadaTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //打包泵故障
                if (packingPumpTrouble == 1) {
                    mIvpackingPumpTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("打包泵故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("打包泵故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("打包泵故障"+time);
                    }
                }else{
                    mIvpackingPumpTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //门闩锁故障
                if (latchLockTrouble == 1) {
                    mIvlatchLockTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("门闩锁故障"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("门闩锁故障"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("门闩锁故障"+time);
                    }
                }else{
                    mIvlatchLockTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //门闩锁开了
                if (latchLockOpen == 1) {
                    mIvlatchLockTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_red));
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("门闩锁开了"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("门闩锁开了"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("门闩锁开了"+time);
                    }
                }else{
                    mIvlatchLockTrouble.setImageDrawable(getResources().getDrawable(R.drawable.light_green));
                }
                //正在出仓但箱满
                if (chucangxiangman == 1) {
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("正在出仓但箱满"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("正在出仓但箱满"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("正在出仓但箱满"+time);
                    }
                }
                //正在缠膜但箱满
                if (chanmoxiangman == 1) {
                    if(i == 1) {
                        mTvguzhangxinxi1.setText("正在缠膜但箱满"+time);
                        i = i+1;
                    }else if(i == 2){
                        mTvguzhangxinxi2.setText("正在缠膜但箱满"+time);
                        i = i+1;
                    }else{
                        mTvguzhangxinxi3.setText("正在缠膜但箱满"+time);
                    }
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
//		if (mReadThread != null)
//			mReadThread.interrupt();
        super.onDestroy();
        closeSerialPort();
        mSerialPort = null;

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
            Log.i("StatusActivity","关闭串口失败");
            return;
        }
        Log.i("StatusActivity","关闭串口成功");
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

    //Hex字符串转byte
    public byte HexToByte(String inHex) {
        return (byte)Integer.parseInt(inHex,16);
    }
    //转hex字符串转字节数组
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

    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public int isOdd(int num) {
        return num & 0x1;
    }
}
