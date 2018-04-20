package com.dl.popup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    PopupLayout bottomPopupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomPopupLayout = findViewById(R.id.buttomPopup);
        bottomPopupLayout.setOnDismissListener(new PopupLayout.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                Toast.makeText(MainActivity.this, "onDismiss", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.popup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bottomPopupLayout.isShowing()) {
                    bottomPopupLayout.show();
                } else {
                    bottomPopupLayout.dismiss();
                }
            }
        });
    }


}
