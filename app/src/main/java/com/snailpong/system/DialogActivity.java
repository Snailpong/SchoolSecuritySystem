package com.snailpong.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends Activity implements
        OnClickListener {

    private Button mConfirm, mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        Intent intent = getIntent();
        Data data = (Data)intent.getSerializableExtra("Data");

        ((TextView)findViewById(R.id.dialog_id)).setText(data.getId());
        ((TextView)findViewById(R.id.dialog_name)).setText(data.getName());
        ((TextView)findViewById(R.id.dialog_parent)).setText(data.getParent());
        ((TextView)findViewById(R.id.dialog_busnumber)).setText(data.getBusnumber());

        if(data.getStates() != null) ((TextView)findViewById(R.id.dialog_states)).setText(data.getStates());
        else{
            ((TextView)findViewById(R.id.dialog_states)).setText("Not found");
            ((TextView)findViewById(R.id.dialog_time)).setText("-");
        }
        if(data.getBusdriver() == null || data.getBusdriver().equals("")) ((TextView)findViewById(R.id.dialog_busdriver)).setText("-");
        else ((TextView)findViewById(R.id.dialog_busdriver)).setText(data.getBusdriver());
        if(data.getBusdriver() == null || data.getBusnumber().equals("")) ((TextView)findViewById(R.id.dialog_busnumber)).setText("-");
        else ((TextView)findViewById(R.id.dialog_busnumber)).setText(data.getBusnumber());

        if(data.getTime() != null){
            int times = Integer.parseInt(data.getTime());
            ((TextView)findViewById(R.id.dialog_time)).setText(String.format("%02d:%02d:%02d",times%86400000/3600000+15,times%3600000/60000,times%60000/1000));
        }

        setContent();
    }

    private void setContent() {
        mConfirm = (Button) findViewById(R.id.btnConfirm);
        mConfirm.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                this.finish();
                break;
            default:
                break;
        }
    }
}
