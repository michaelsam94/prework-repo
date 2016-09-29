package com.example.pam_android.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by PAM-Android on 9/29/2016.
 */

public class EditItemActivity extends AppCompatActivity {
    private String result;
    private String item;
    private int itemPostion;
    private Intent fromMainActivity;
    private EditText editItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        fromMainActivity = getIntent();
        itemPostion = fromMainActivity.getIntExtra("postion", 0);
        item = fromMainActivity.getStringExtra("item");
        editItem = (EditText) findViewById(R.id.etEditItem);
        editItem.setText(item);
        editItem.setSelection(item.length());
    }
    public void onSaveItem(View v){
        if (item.equals(editItem.getText().toString())) {
            Toast.makeText(this, "please edit the note", Toast.LENGTH_SHORT).show();
        } else {
            result = editItem.getText().toString();
            fromMainActivity.putExtra("result", result);
            setResult(Activity.RESULT_OK, fromMainActivity);
            finish();
        }
    }
}
