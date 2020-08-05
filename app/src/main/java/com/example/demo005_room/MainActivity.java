package com.example.demo005_room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demo005_room.db.AppDatabase;
import com.example.demo005_room.db.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Luuuzi
 * @date: 2020-08-04
 * @description: room的使用
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static final String tag = MainActivity.class.getSimpleName();
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_first_name2;
    private EditText et_last_name2;
    private TextView tv_content;
    AppDatabase db;
    private EditText et_uid1;
    private EditText et_uid2;
    private EditText et_uid3;
    private EditText et_last_name3;
    private EditText et_first_name3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建db对象，用于操作数据（通常，在整个APP中，只需要一个Room database实例。)
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")//database-name:数据库文件名称
                .allowMainThreadQueries()//允许在主线程中查询
                .build();

        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_uid1 = findViewById(R.id.et_uid1);
        et_first_name2 = findViewById(R.id.et_first_name2);
        et_last_name2 = findViewById(R.id.et_last_name2);
        et_uid2 = findViewById(R.id.et_uid2);
        et_first_name3 = findViewById(R.id.et_first_name3);
        et_last_name3 = findViewById(R.id.et_last_name3);
        et_uid3 = findViewById(R.id.et_uid3);
        tv_content = findViewById(R.id.tv_content);

        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_inquiry).setOnClickListener(this);
        findViewById(R.id.btn_updata).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                User user = new User(Integer.parseInt(et_uid1.getText().toString()), et_first_name.getText().toString(), et_last_name.getText().toString());
                db.userDao().inset(user);
                break;
            case R.id.btn_delete:
//                User user2 = new User(Integer.parseInt(et_uid2.getText().toString()), et_first_name2.getText().toString(), et_last_name2.getText().toString());
//                db.userDao().delete(user2);
                //测试批量删除
                User user44 = new User(4, "da", "da");
                User user55 = new User(5, "而且", "哦");
                User user58 = new User(58, "而且", "dfa");
                ArrayList<User> users2 = new ArrayList<>();
                users2.add(user44);
                users2.add(user55);
                users2.add(user58);
                int deletes = db.userDao().deletes(users2);
                Log.i(tag, "删除条数：" + deletes);
                break;
            case R.id.btn_updata:
//                User user3 = new User(Integer.parseInt(et_uid3.getText().toString()), et_first_name3.getText().toString(), et_last_name3.getText().toString());
//                db.userDao().update(user3);
                //测试批量更新
                Log.i(tag, "批量更新");
                User user4 = new User(2, "你飞而去好", "嘿嘿");
                User user5 = new User(3, "发儿", "是的");
                User user6 = new User(4, "你大法", "已看");
                ArrayList<User> users = new ArrayList<>();
                users.add(user4);
                users.add(user5);
                users.add(user6);
                int update = db.userDao().updates(users);
                Log.i(tag, "更新结果：" + update);
                break;
            case R.id.btn_inquiry:
                List<User> all = db.userDao().getAll();
                StringBuffer stringBuffer = new StringBuffer();
                for (User u : all) {
                    stringBuffer.append("").append(u).append(",").append("\n");
                }
                tv_content.setText(stringBuffer);
                break;
        }
    }
}
