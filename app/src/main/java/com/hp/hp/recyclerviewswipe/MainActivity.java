package com.hp.hp.recyclerviewswipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView etName;

    @BindView(R.id.button) Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     //   ButterKnife.bind(this);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.button)
    public void buttonclick() {
        startActivity(new Intent(getApplicationContext(),Rv.class));
    }

}
