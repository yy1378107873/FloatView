package com.song.demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.song.floatwindow.FloatConfig;
import com.song.floatwindow.FloatPermission;
import com.song.floatwindow.FloatWindow;


public class MainActivity extends AppCompatActivity {

    private FloatWindow floatWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showClick(View view) {
        FloatPermission floatPermission = new FloatPermission();
        if (floatPermission.isHavePermission(this)) {
            if (floatWindow==null){
                FloatConfig floatConfig = new FloatConfig();
                View contentView = LayoutInflater.from(this).inflate(R.layout.float_view, null);
                floatConfig.setContentView(contentView);
                floatConfig.setContext(this);
                floatConfig.setStartX(0);
                floatConfig.setStartY((int) (1400 * 0.7));
                floatWindow = new FloatWindow(floatConfig);
            }
            floatWindow.show();
        } else {
            floatPermission.gotoPermission(this);
        }
    }

    public void hideClick(View view){
        if (floatWindow!=null){
            floatWindow.hidden();
        }
    }

}
