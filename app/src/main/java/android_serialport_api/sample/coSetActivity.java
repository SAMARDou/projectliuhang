package android_serialport_api.sample;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
public class coSetActivity extends Activity{
    EditText shuiluTxt, xingzouTxt, fengjiTxt, shouhuoTxt, youwenTxt, youweiTxt;
    ImageView shuilu, xingzou, fengji, shouhuo, youwen, youwei;
    String shuiluCo, xingzouCo, fengjiCo, shouhuoCo, youwenCo, youweiCo;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coset_layout);
        shuiluTxt = (EditText)findViewById(R.id.editText1);
        xingzouTxt = (EditText)findViewById(R.id.editText2);
        fengjiTxt = (EditText)findViewById(R.id.editText3);
        shouhuoTxt = (EditText)findViewById(R.id.editText4);
        youwenTxt = (EditText)findViewById(R.id.editText5);
        youweiTxt = (EditText)findViewById(R.id.editText6);
        shuilu = (ImageView)findViewById(R.id.shuilu);
        xingzou = (ImageView)findViewById(R.id.xingzou);
        fengji = (ImageView)findViewById(R.id.fengji);
        shouhuo = (ImageView)findViewById(R.id.shouhuo);
        youwen = (ImageView)findViewById(R.id.youwen);
        youwei = (ImageView)findViewById(R.id.youwei);
        shuilu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shuiluCo = shuiluTxt.getText().toString();
                analysisSendData1(shuiluCo);
            }
        });
        xingzou.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                xingzouCo = xingzouTxt.getText().toString();
                analysisSendData1(xingzouCo);
            }
        });
        fengji.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fengjiCo = fengjiTxt.getText().toString();
                analysisSendData1(fengjiCo);
            }
        });
        shouhuo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shouhuoCo = shouhuoTxt.getText().toString();
                analysisSendData1(shouhuoCo);
            }
        });
        youwen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                youwenCo = youwenTxt.getText().toString();
                analysisSendData2(youwenCo);
            }
        });
        youwei.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                youweiCo = youweiTxt.getText().toString();
                analysisSendData2(youweiCo);
            }
        });

    }
    private void analysisSendData1(String s){
        String str1 = "80 00 C3 D2 D3 ";

    }
    private void analysisSendData2(String s){
        String str1 = "80 00 C4 D2 D3 ";

    }
    static public byte[] HexToByteArr(String inHex)
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
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

    static public int isOdd(int num)
    {
        return num & 0x1;
    }
    static public byte HexToByte(String inHex)
    {
        return (byte)Integer.parseInt(inHex,16);
    }

}
