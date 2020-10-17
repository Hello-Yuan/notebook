package com.example.notebook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notebook.R;
import com.example.notebook.sqlite.MySQLiteOpenHelper;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_user;
    private EditText et_pass;
    private EditText et_vercode;

    private Button btn_getcode;
    private Button btn_register;
    private Button btn_reset;
    private EventHandler eventHandler;
    private Timer timer = new Timer();
    private int second = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        MobSDK.init(this);
    }

    public void initView(){
        et_name = (EditText)findViewById(R.id.et_name);
        et_user = (EditText)findViewById(R.id.et_user);
        et_pass = (EditText)findViewById(R.id.et_password);
        et_vercode = (EditText)findViewById(R.id.et_vercode);

        btn_register = findViewById(R.id.btn_register);
        btn_reset = findViewById(R.id.btn_reset);
        btn_getcode = findViewById(R.id.btn_getcode);

        btn_register.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_getcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if(et_name.length()>0&&et_user.length()>0&&et_pass.length()>0){
                    // 提交验证码，其中的code表示验证码，如“1357”
                    SMSSDK.submitVerificationCode("86", et_user.getText().toString(), et_vercode.getText().toString());
                }else {
                    Toast.makeText(RegisterActivity.this,"请输入完整信息",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_reset:
                et_name.setText("");
                et_user.setText("");
                et_pass.setText("");
                et_vercode.setText("");
                break;
            case R.id.btn_getcode:
                cooldown();
                break;
        }
    }
  public void getcode(){
      // 在尝试读取通信录时以弹窗提示用户（可选功能）
      SMSSDK.setAskPermisionOnReadContact(true);

      EventHandler eventHandler = new EventHandler() {
          public void afterEvent(int event, int result, Object data) {
              // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
              Message msg = new Message();
              msg.arg1 = event;
              msg.arg2 = result;
              msg.obj = data;
              new Handler(Looper.getMainLooper(), new Handler.Callback() {
                  @Override
                  public boolean handleMessage(Message msg) {
                      int event = msg.arg1;
                      int result = msg.arg2;
                      Object data = msg.obj;
                      if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                          if (result == SMSSDK.RESULT_COMPLETE) {
                              // TODO 处理成功得到验证码的结果
                              // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                          } else {
                              // TODO 处理错误的结果
                              ((Throwable) data).printStackTrace();
                          }
                      } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                          if (result == SMSSDK.RESULT_COMPLETE) {
                              // TODO 处理验证码验证通过的结果
                              MySQLiteOpenHelper helper = MySQLiteOpenHelper.getHelper(getApplicationContext());
                              SQLiteDatabase db = helper.getWritableDatabase();
                              ContentValues contentValues =new ContentValues();
                              contentValues.put("name",et_name.getText().toString());
                              contentValues.put("user",et_user.getText().toString());
                              contentValues.put("pass",et_pass.getText().toString());
                              try{
                                  //开启事务
                                  db.beginTransaction();
                                  //插入
                                  db.insert("t_user",null,contentValues);
                                  //设置成功
                                  db.setTransactionSuccessful();

                                  db.endTransaction();
                              }catch (Exception e){

                              }finally {
                                  helper.close();
                              }
                              Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                              Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                              startActivity(intent);
                              RegisterActivity.this.finish();
                              Log.i("tag","验证成功");
                          } else {
                              // TODO 处理错误的结果
                              Toast.makeText(RegisterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                              ((Throwable) data).printStackTrace();
                          }
                      }
                      // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                      return false;
                  }
              }).sendMessage(msg);
          }
      };
      // 注册一个事件回调，用于处理SMSSDK接口请求的结果
      SMSSDK.registerEventHandler(eventHandler);

      // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
      SMSSDK.getVerificationCode("86", et_user.getText().toString());


  }
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    public void cooldown(){
        if(et_user.length()==11){
            getcode();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            second--;
                            btn_getcode.setText("已发送("+second+"秒)");
                            btn_getcode.setEnabled(false);
                            if(second<1){
                                timer.cancel();
                                btn_getcode.setText("发送验证码");
                                btn_getcode.setEnabled(true);
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask,1000,1000);
        }else {
            Toast.makeText(RegisterActivity.this,"请输入手机号",Toast.LENGTH_LONG).show();
        }

    }
}
