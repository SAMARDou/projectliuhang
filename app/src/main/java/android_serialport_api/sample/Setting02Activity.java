package android_serialport_api.sample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;

import android_serialport_api.SerialPort;
import android_serialport_api.UIwidget.RegionNumberEditText;

public class Setting02Activity extends Activity {

    private static final String TAG = "Setting02Activity";

    //喂棉马达比例阀/碎棉马达比例阀/排棉马达比例阀/打包泵比例阀/摇臂轴油缸伸出比例阀
    public static int weimianmadavalve,suimianmadavalve,paimianmadavalve,dabaobengvalve,baibiyougangshengchuvalve;
    //卸模伸出比例阀/卸模缩回比例阀/RMB门开比例阀/RMB门关比例阀/初始化总膜数
    public static int xiemoshengchuvalve,xiemosuohuivalve,RMBopenvalve,RMBclosevalve,chushihuamoshu;
    //棉模密度
    public static int mianmomidu;

    //圆包调试总开关
    public static int yuanbaotiaoshilock=0;
    //摇臂油缸缩回电磁阀/送膜装置缩回电磁阀/送膜装置伸出电磁阀/包膜传送电磁阀
    public static int yaobiyougangsuohuilock=0,songmosuohuilock=0,songmoshengchulock=0,baomochuansonglock=0;
    //卸模装置伸出电磁阀/卸模装置缩回电磁阀/RMB开电磁阀/RMB关电磁阀
    public static int xiemoshengchulock=0,xiemosuohuilock=0,RMBopenlock=0,RMBcloselock=0;
    //系统重大故障电磁阀/搅龙开电磁阀
    public static int xitongguzhanglock=0,jiaolongopencloselock=0;


    RegionNumberEditText mEtweimianmadavalve,mEtsuimianmadavalve,mEtpaimianmadavalve,mEtdabaobengvalve,mEtbaibiyougangshengchuvalve;
    RegionNumberEditText mEtxiemoshengchuvalve,mEtxiemosuohuivalve,mEtRMBopenvalve,mEtRMBclosevalve,mEtchushihuamoshu;
    RegionNumberEditText mEtmianmomidu;
    Switch mSwyuanbaotiaoshilock,mSwyaobiyougangsuohuilock,mSwsongmosuohuilock,mSwsongmoshengchulock,mSwbaomochuansonglock;
    Switch mSwxiemoshengchulock,mSwxiemosuohuilock,mSwRMBopenlock,mSwRMBcloselock,mSwxitongguzhanglock,mSwjiaolongopencloselock;
    //保存参数
    private Button mBtsave2;

    private SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String portPath = "/dev/ttyMT0";

    @TargetApi(14)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_control);

        sharedPreferences = getSharedPreferences("MYDATA",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mBtsave2 = (Button) findViewById(R.id.btn_save_2);
        //喂棉马达比例阀
        mEtweimianmadavalve = (RegionNumberEditText) findViewById(R.id.weimianmada_valve);
        mEtweimianmadavalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtweimianmadavalve.setSelection(mEtweimianmadavalve.getText().length());
        mEtweimianmadavalve.setRegion(100,0);
        mEtweimianmadavalve.setTextWatcher();
        //碎棉马达比例阀
        mEtsuimianmadavalve = (RegionNumberEditText) findViewById(R.id.suimianmada_valve);
        mEtsuimianmadavalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtsuimianmadavalve.setSelection(mEtsuimianmadavalve.getText().length());
        mEtsuimianmadavalve.setRegion(100,0);
        mEtsuimianmadavalve.setTextWatcher();
        //排棉马达比例阀
        mEtpaimianmadavalve = (RegionNumberEditText) findViewById(R.id.paimianmada_valve);
        mEtpaimianmadavalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtpaimianmadavalve.setSelection(mEtpaimianmadavalve.getText().length());
        mEtpaimianmadavalve.setRegion(100,0);
        mEtpaimianmadavalve.setTextWatcher();
        //打包泵比例阀
        mEtdabaobengvalve = (RegionNumberEditText) findViewById(R.id.dabaobeng_valve);
        mEtdabaobengvalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtdabaobengvalve.setSelection(mEtdabaobengvalve.getText().length());
        mEtdabaobengvalve.setRegion(100,0);
        mEtdabaobengvalve.setTextWatcher();
        //摆臂油缸伸出比例阀
        mEtbaibiyougangshengchuvalve = (RegionNumberEditText) findViewById(R.id.baibiyougangshengchu_valve);
        mEtbaibiyougangshengchuvalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtbaibiyougangshengchuvalve.setSelection(mEtbaibiyougangshengchuvalve.getText().length());
        mEtbaibiyougangshengchuvalve.setRegion(100,0);
        mEtbaibiyougangshengchuvalve.setTextWatcher();
        //卸模伸出比例阀
        mEtxiemoshengchuvalve = (RegionNumberEditText) findViewById(R.id.xiemoshengchu_valve);
        mEtxiemoshengchuvalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtxiemoshengchuvalve.setSelection(mEtxiemoshengchuvalve.getText().length());
        mEtxiemoshengchuvalve.setRegion(100,0);
        mEtxiemoshengchuvalve.setTextWatcher();
        //卸模缩回比例阀
        mEtxiemosuohuivalve = (RegionNumberEditText) findViewById(R.id.xiemosuohui_valve);
        mEtxiemosuohuivalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtxiemosuohuivalve.setSelection(mEtxiemosuohuivalve.getText().length());
        mEtxiemosuohuivalve.setRegion(100,0);
        mEtxiemosuohuivalve.setTextWatcher();
        //RMB门开比例阀
        mEtRMBopenvalve = (RegionNumberEditText) findViewById(R.id.RMBopen_valve);
        mEtRMBopenvalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtRMBopenvalve.setSelection(mEtRMBopenvalve.getText().length());
        mEtRMBopenvalve.setRegion(100,0);
        mEtRMBopenvalve.setTextWatcher();
        //RMB门关比例阀
        mEtRMBclosevalve = (RegionNumberEditText) findViewById(R.id.RMBclose_valve);
        mEtRMBclosevalve.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtRMBclosevalve.setSelection(mEtRMBclosevalve.getText().length());
        mEtRMBclosevalve.setRegion(100,0);
        mEtRMBclosevalve.setTextWatcher();
        //初始化总膜数
        mEtchushihuamoshu = (RegionNumberEditText) findViewById(R.id.chushihuamoshu);
        mEtchushihuamoshu.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtchushihuamoshu.setSelection(mEtchushihuamoshu.getText().length());
        mEtchushihuamoshu.setRegion(100,0);
        mEtchushihuamoshu.setTextWatcher();
        //棉模密度
        mEtmianmomidu = (RegionNumberEditText) findViewById(R.id.mianmomidu);
        mEtmianmomidu.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mEtmianmomidu.setSelection(mEtmianmomidu.getText().length());
        mEtmianmomidu.setRegion(255,0);
        mEtmianmomidu.setTextWatcher();

        //圆包调试总开关
        mSwyuanbaotiaoshilock = (Switch) findViewById(R.id.yuanbaotiaoshi_lock);
        mSwyuanbaotiaoshilock.setChecked(false);
        mSwyuanbaotiaoshilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwyuanbaotiaoshilock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //控制开关字体颜色
                if (isChecked) {
                    mSwyuanbaotiaoshilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    yuanbaotiaoshilock=1;
                    Log.d(TAG, "圆包调试总开关打开 ");
                } else {
                    mSwyuanbaotiaoshilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    yuanbaotiaoshilock=0;
                    Log.d(TAG, "圆包调试总开关关闭 ");
                }
            }
        });


        //摇臂油缸缩回电磁阀
        mSwyaobiyougangsuohuilock = (Switch) findViewById(R.id.yaobiyougangsuohui_lock);
        mSwyaobiyougangsuohuilock.setChecked(false);
        mSwyaobiyougangsuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwyaobiyougangsuohuilock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //控制开关字体颜色
                if (isChecked) {
                    mSwyaobiyougangsuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    yaobiyougangsuohuilock=1;
                    Log.d(TAG, "摇臂油缸缩回电磁阀打开 ");
                } else {
                    mSwyaobiyougangsuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    yaobiyougangsuohuilock=0;
                    Log.d(TAG, "摇臂油缸缩回电磁阀关闭 ");
                }
            }
        });
        //送膜装置缩回电磁阀
        mSwsongmosuohuilock = (Switch) findViewById(R.id.songmosuohui_lock);
        mSwsongmosuohuilock.setChecked(false);
        mSwsongmosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwsongmosuohuilock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwsongmosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    songmosuohuilock=1;
                    Log.d(TAG, "送膜装置缩回电磁阀打开 ");
                } else {
                    mSwsongmosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    songmosuohuilock=0;
                    Log.d(TAG, "送膜装置缩回电磁阀关闭 ");
                }
            }
        });
        //送膜装置伸出电磁阀
        mSwsongmoshengchulock = (Switch) findViewById(R.id.songmoshengchu_lock);
        mSwsongmoshengchulock.setChecked(false);
        mSwsongmoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwsongmoshengchulock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwsongmoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    songmoshengchulock=1;
                    Log.d(TAG, "送膜装置伸出电磁阀打开 ");
                } else {
                    mSwsongmoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    songmoshengchulock=0;
                    Log.d(TAG, "送膜装置伸出电磁阀关闭 ");
                }
            }
        });
        //包膜传送电磁阀
        mSwbaomochuansonglock = (Switch) findViewById(R.id.baomochuansong_lock);
        mSwbaomochuansonglock.setChecked(false);
        mSwbaomochuansonglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwbaomochuansonglock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwbaomochuansonglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    baomochuansonglock=1;
                    Log.d(TAG, "包膜传送电磁阀打开 ");
                } else {
                    mSwbaomochuansonglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    baomochuansonglock=0;
                    Log.d(TAG, "包膜传送电磁阀关闭 ");
                }
            }
        });
        //卸模装置伸出电磁阀
        mSwxiemoshengchulock  = (Switch) findViewById(R.id.xiemoshengchu_lock);
        mSwxiemoshengchulock.setChecked(false);
        mSwxiemoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwxiemoshengchulock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwxiemoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    xiemoshengchulock=1;
                    Log.d(TAG, "卸模装置伸出电磁阀打开 ");
                } else {
                    mSwxiemoshengchulock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    xiemoshengchulock=0;
                    Log.d(TAG, "卸模装置伸出电磁阀关闭 ");
                }
            }
        });
        //卸模装置缩回电磁阀
        mSwxiemosuohuilock = (Switch) findViewById(R.id.xiemosuohui_lock);
        mSwxiemosuohuilock.setChecked(false);
        mSwxiemosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwxiemosuohuilock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwxiemosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    xiemosuohuilock=1;
                    Log.d(TAG, "卸模装置缩回电磁阀打开 ");
                } else {
                    mSwxiemosuohuilock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    xiemosuohuilock=0;
                    Log.d(TAG, "卸模装置缩回电磁阀关闭 ");
                }
            }
        });
        //RMB开电磁阀
        mSwRMBopenlock = (Switch) findViewById(R.id.RMBopen_lock);
        mSwRMBopenlock.setChecked(false);
        mSwRMBopenlock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwRMBopenlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwRMBopenlock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    RMBopenlock=1;
                    Log.d(TAG, "RMB开电磁阀打开");
                } else {
                    mSwRMBopenlock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    RMBopenlock=0;
                    Log.d(TAG, "RMB开电磁阀关闭");
                }
            }
        });
        //RMB关电磁阀
        mSwRMBcloselock = (Switch) findViewById(R.id.RMBclose_lock);
        mSwRMBcloselock.setChecked(false);
        mSwRMBcloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwRMBcloselock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwRMBcloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    RMBcloselock=1;
                    Log.d(TAG, "RMB关电磁阀打开");
                } else {
                    mSwRMBcloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    RMBcloselock=0;
                    Log.d(TAG, "RMB关电磁阀关闭");
                }
            }
        });
        //系统重大故障电磁阀
        mSwxitongguzhanglock = (Switch) findViewById(R.id.xitongguzhang_lock);
        mSwxitongguzhanglock.setChecked(false);
        mSwxitongguzhanglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwxitongguzhanglock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwxitongguzhanglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    xitongguzhanglock=1;
                    Log.d(TAG, "系统重大故障电磁阀打开");
                } else {
                    mSwxitongguzhanglock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    xitongguzhanglock=0;
                    Log.d(TAG, "系统重大故障电磁阀关闭");
                }
            }
        });
        //搅龙开电磁阀
        mSwjiaolongopencloselock = (Switch) findViewById(R.id.jiaolongopenclose_lock);
        mSwjiaolongopencloselock.setChecked(false);
        mSwjiaolongopencloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
        mSwjiaolongopencloselock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mSwjiaolongopencloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_true);
                    jiaolongopencloselock=1;
                    Log.d(TAG, "系统重大故障电磁阀打开");
                } else {
                    mSwjiaolongopencloselock.setSwitchTextAppearance(Setting02Activity.this, R.style.s_false);
                    jiaolongopencloselock=0;
                    Log.d(TAG, "系统重大故障电磁阀关闭");
                }
            }
        });

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


        String text1 = sharedPreferences.getString("weimianmadavalve","");
        if(text1 == null){
            mEtweimianmadavalve.setText("000");
        }else {
            mEtweimianmadavalve.setText(text1);
        }

        String text2 = sharedPreferences.getString("suimianmadavalve","");
        if(text2 == null){
            mEtsuimianmadavalve.setText("000");
        }else {
            mEtsuimianmadavalve.setText(text2);
        }

        String text3 = sharedPreferences.getString("paimianmadavalve","");
        if(text3 == null){
            mEtpaimianmadavalve.setText("000");
        }else {
            mEtpaimianmadavalve.setText(text3);
        }

        String text4 = sharedPreferences.getString("dabaobengvalve","");
        if(text4 == null){
            mEtdabaobengvalve.setText("000");
        }else {
            mEtdabaobengvalve.setText(text4);
        }

        String text5 = sharedPreferences.getString("baibiyougangshengchuvalve","");
        if(text5 == null){
            mEtbaibiyougangshengchuvalve.setText("000");
        }else {
            mEtbaibiyougangshengchuvalve.setText(text5);
        }

        String text6 = sharedPreferences.getString("xiemoshengchuvalve","");
        if(text6 == null){
            mEtxiemoshengchuvalve.setText("000");
        }else {
            mEtxiemoshengchuvalve.setText(text6);
        }

        String text7 = sharedPreferences.getString("xiemosuohuivalve","");
        if(text7 == null){
            mEtxiemosuohuivalve.setText("000");
        }else {
            mEtxiemosuohuivalve.setText(text7);
        }

        String text8 = sharedPreferences.getString("RMBopenvalve","");
        if(text8 == null){
            mEtRMBopenvalve.setText("000");
        }else {
            mEtRMBopenvalve.setText(text8);
        }

        String text9 = sharedPreferences.getString("RMBclosevalve","");
        if(text9 == null){
            mEtRMBclosevalve.setText("000");
        }else {
            mEtRMBclosevalve.setText(text9);
        }

        String text10 = sharedPreferences.getString("chushihuamoshu","");
        if(text10 == null){
            mEtchushihuamoshu.setText("000");
        }else {
            mEtchushihuamoshu.setText(text10);
        }

        String text11 = sharedPreferences.getString("mianmomidu","");
        if(text11 == null){
            mEtmianmomidu.setText("000");
        }else {
            mEtmianmomidu.setText(text11);
        }

        mBtsave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //每个信息占1个字节（8位），每一个就是一个data
                String rawweimianmadavalve = mEtweimianmadavalve.getText().toString();
                String rawsuimianmadavalve = mEtsuimianmadavalve.getText().toString();
                String rawpaimianmadavalve = mEtpaimianmadavalve.getText().toString();
                String rawdabaobengvalve = mEtdabaobengvalve.getText().toString();
                String rawbaibiyougangshengchuvalve = mEtbaibiyougangshengchuvalve.getText().toString();
                String rawxiemoshengchuvalve = mEtxiemoshengchuvalve.getText().toString();
                String rawxiemosuohuivalve = mEtxiemosuohuivalve.getText().toString();
                String rawRMBopenvalve = mEtRMBopenvalve.getText().toString();
                String rawRMBclosevalve = mEtRMBclosevalve.getText().toString();
                String rawchushihuamoshu = mEtchushihuamoshu.getText().toString();
                String rawmianmomidu = mEtmianmomidu.getText().toString();

                //每个信息只占1位，所以需要拼8位，拼成data1
                StringBuilder strBuilder2data1 = new StringBuilder();
                strBuilder2data1.append(yaobiyougangsuohuilock);
                strBuilder2data1.append(songmosuohuilock);
                strBuilder2data1.append(songmoshengchulock);
                strBuilder2data1.append(baomochuansonglock);
                strBuilder2data1.append(xiemoshengchulock);
                strBuilder2data1.append(xiemosuohuilock);
                strBuilder2data1.append(RMBopenlock);
                strBuilder2data1.append(RMBcloselock);
                String rawid19C8D3D2data1 =strBuilder2data1.toString() ;
                int id19C8D3D2data1 = Integer.valueOf(rawid19C8D3D2data1,2);

                //每个信息只占1位，所以需要拼8位，拼成data2
                StringBuilder strBuilder2data2 = new StringBuilder();
                strBuilder2data2.append(xitongguzhanglock);
                strBuilder2data2.append(jiaolongopencloselock);
                strBuilder2data2.append(yuanbaotiaoshilock);
                strBuilder2data2.append("00000");
                String rawid19C8D3D2data2 =strBuilder2data2.toString() ;
                int id19C8D3D2data2 = Integer.valueOf(rawid19C8D3D2data2,2);


                if(!rawweimianmadavalve.isEmpty()){
                    weimianmadavalve = Integer.valueOf(rawweimianmadavalve);
                    editor.putString("weimianmadavalve",rawweimianmadavalve);
                }else{
                    weimianmadavalve = 0;
                    editor.putString("weimianmadavalve","000");
                }

                if(!rawsuimianmadavalve.isEmpty()){
                    suimianmadavalve = Integer.valueOf(rawsuimianmadavalve);
                    editor.putString("suimianmadavalve",rawsuimianmadavalve);
                }else{
                    suimianmadavalve = 0;
                    editor.putString("suimianmadavalve","000");
                }

                if(!rawpaimianmadavalve.isEmpty()){
                    paimianmadavalve = Integer.valueOf(rawpaimianmadavalve);
                    editor.putString("paimianmadavalve",rawpaimianmadavalve);
                }else{
                    paimianmadavalve = 0;
                    editor.putString("paimianmadavalve","000");
                }

                if(!rawdabaobengvalve.isEmpty()){
                    dabaobengvalve = Integer.valueOf(rawdabaobengvalve);
                    editor.putString("dabaobengvalve",rawdabaobengvalve);
                }else{
                    dabaobengvalve = 0;
                    editor.putString("dabaobengvalve","000");
                }

                if(!rawbaibiyougangshengchuvalve.isEmpty()){
                    baibiyougangshengchuvalve = Integer.valueOf(rawbaibiyougangshengchuvalve);
                    editor.putString("baibiyougangshengchuvalve",rawbaibiyougangshengchuvalve);

                }else{
                    baibiyougangshengchuvalve = 0;
                    editor.putString("baibiyougangshengchuvalve","000");
                }

                if(!rawxiemoshengchuvalve.isEmpty()){
                    xiemoshengchuvalve = Integer.valueOf(rawxiemoshengchuvalve);
                    editor.putString("xiemoshengchuvalve",rawxiemoshengchuvalve);

                }else{
                    xiemoshengchuvalve = 0;
                    editor.putString("xiemoshengchuvalve","000");
                }

                if(!rawxiemosuohuivalve.isEmpty()){
                    xiemosuohuivalve = Integer.valueOf(rawxiemosuohuivalve);
                    editor.putString("xiemosuohuivalve",rawxiemosuohuivalve);
                }else{
                    xiemosuohuivalve = 0;
                    editor.putString("xiemosuohuivalve","000");
                }

                if(!rawRMBopenvalve.isEmpty()){
                    RMBopenvalve = Integer.valueOf(rawRMBopenvalve);
                    editor.putString("RMBopenvalve",rawRMBopenvalve);
                }else{
                    RMBopenvalve = 0;
                    editor.putString("RMBopenvalve","000");
                }

                if(!rawRMBclosevalve.isEmpty()){
                    RMBclosevalve = Integer.valueOf(rawRMBclosevalve);
                    editor.putString("RMBclosevalve",rawRMBclosevalve);
                }else{
                    RMBclosevalve = 0;
                    editor.putString("RMBclosevalve","000");
                }

                if(!rawchushihuamoshu.isEmpty()){
                    chushihuamoshu = Integer.valueOf(rawchushihuamoshu);
                    editor.putString("chushihuamoshu",rawchushihuamoshu);
                }else{
                    chushihuamoshu = 0;
                    editor.putString("chushihuamoshu","000");
                }

                if(!rawmianmomidu.isEmpty()){
                    mianmomidu = Integer.valueOf(rawmianmomidu);
                    editor.putString("mianmomidu",rawmianmomidu);
                }else{
                    mianmomidu = 0;
                    editor.putString("mianmomidu","000");
                }
                editor.apply();


                String stringweimianmadavalve = String.format("%02X",weimianmadavalve);
                String stringsuimianmadavalve = String.format("%02X",suimianmadavalve);
                String stringpaimianmadavalve = String.format("%02X",paimianmadavalve);
                String stringdabaobengvalve = String.format("%02X",dabaobengvalve);
                String stringbaibiyougangshengchuvalve = String.format("%02X",baibiyougangshengchuvalve);
                String stringxiemoshengchuvalve = String.format("%02X",xiemoshengchuvalve);
                String stringxiemosuohuivalve = String.format("%02X",xiemosuohuivalve);
                String stringRMBopenvalve = String.format("%02X",RMBopenvalve);
                String stringRMBclosevalve = String.format("%02X",RMBclosevalve);
                String stringchushihuamoshu = String.format("%02X",chushihuamoshu);
                String stringmianmomidu = String.format("%02X",mianmomidu);
                String stringid19C8D3D2data1 = String.format("%02X",id19C8D3D2data1);
                String stringid19C8D3D2data2 = String.format("%02X",id19C8D3D2data2);


                StringBuilder strBuilder1 = new StringBuilder();
                strBuilder1.append("8019C9D3D208");
                strBuilder1.append(stringweimianmadavalve);
                strBuilder1.append(stringsuimianmadavalve);
                strBuilder1.append(stringpaimianmadavalve);
                strBuilder1.append(stringdabaobengvalve);
                strBuilder1.append(stringbaibiyougangshengchuvalve);
                strBuilder1.append(stringxiemoshengchuvalve);
                strBuilder1.append(stringxiemosuohuivalve);
                strBuilder1.append(stringRMBopenvalve);

                StringBuilder strBuilder2 = new StringBuilder();
                strBuilder2.append("8019C8D3D208");
                strBuilder2.append(stringRMBclosevalve);
                strBuilder2.append(stringid19C8D3D2data1);
                strBuilder2.append(stringid19C8D3D2data2);
                strBuilder2.append(stringchushihuamoshu);
                strBuilder2.append(stringmianmomidu);
                strBuilder2.append("000000");

                try {
                    byte[] sendData1 = HexToByteArr(strBuilder1.toString());
                    byte[] sendData2 = HexToByteArr(strBuilder2.toString());
                    if (sendData1.length > 0 ||sendData2.length > 0  ) {
                        mOutputStream.write(sendData1);
                        mOutputStream.flush();
                        Thread.sleep(500);
                        mOutputStream.write(sendData2);
                        mOutputStream.flush();
                        Log.e("Setting02Activity", "sendData1:" + Arrays.toString(sendData1));
                        Log.e("Setting02Activity", "sendData2:" + Arrays.toString(sendData2));

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(Setting02Activity.this,"发送参数并保存",Toast.LENGTH_SHORT).show();


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
                Setting02Activity.this.finish();
            }
        });
        b.show();
    }

    //工具
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
