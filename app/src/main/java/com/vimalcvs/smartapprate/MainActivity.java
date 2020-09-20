package com.vimalcvs.smartapprate;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.vimalcvs.smartapprate.util.RateDialogManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));


       LinearLayout linearLayout = findViewById(R.id.rate_ok);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDialogManager.showRateDialog(MainActivity.this, savedInstanceState);
            }
        });

    }


}
