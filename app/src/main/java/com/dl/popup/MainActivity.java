package com.dl.popup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dl.popuplayout.PopupLayout;


public class MainActivity extends AppCompatActivity {
    PopupLayout mPopupLayout;
    private RadioGroup mAnimChange;
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPopupLayout = findViewById(R.id.buttomPopup);
        mAnimChange = findViewById(R.id.anim);
        mBtn=findViewById(R.id.popup);
        setAnimType(mAnimChange.getCheckedRadioButtonId());
        mPopupLayout.setOnDismissListener(new PopupLayout.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                mBtn.setText("open");
//                Toast.makeText(MainActivity.this, "onDismiss", Toast.LENGTH_SHORT).show();
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPopupLayout.isShowing()) {
                    mPopupLayout.show();
                    mBtn.setText("close");
                } else {
                    mPopupLayout.dismiss();
                }
            }
        });

        mAnimChange.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setAnimType(checkedId);
            }
        });
    }

    private void setAnimType(int checkedId) {
        switch (checkedId) {
            case R.id.top:
                mPopupLayout.setAnimType(PopupLayout.ANIM.TOP);
                break;
            case R.id.right:
                mPopupLayout.setAnimType(PopupLayout.ANIM.RIGHT);
                break;
            case R.id.bottom:
                mPopupLayout.setAnimType(PopupLayout.ANIM.BOTTOM);
                break;
            case R.id.left:
                mPopupLayout.setAnimType(PopupLayout.ANIM.LEFT);
                break;
        }
        setPopImg(mPopupLayout.getAnimType());
    }

    public void setPopImg(PopupLayout.ANIM anim) {
        findViewById(R.id.top_bottom).setVisibility(View.GONE);
        findViewById(R.id.left_right).setVisibility(View.GONE);
        if (anim == PopupLayout.ANIM.LEFT || anim == PopupLayout.ANIM.RIGHT) {
            findViewById(R.id.left_right).setVisibility(View.VISIBLE);
        }
        if (anim == PopupLayout.ANIM.TOP || anim == PopupLayout.ANIM.BOTTOM) {
            findViewById(R.id.top_bottom).setVisibility(View.VISIBLE);
        }
    }


}
