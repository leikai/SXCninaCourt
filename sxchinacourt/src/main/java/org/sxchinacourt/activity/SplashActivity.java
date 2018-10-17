package org.sxchinacourt.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import org.sxchinacourt.CApplication;
import org.sxchinacourt.R;


/**
 *
 * @author baggio
 * @date 2017/2/13
 */

public class SplashActivity extends Activity {
    private ImageView btn_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                String token = CApplication.getInstance().getCurrentToken();
                Log.e("token",""+token);
                if (CApplication.getInstance().getCurrentToken() == null) {
                    intent.setClass(SplashActivity.this, LoginActivity.class);
                } else {
                    intent.setClass(SplashActivity.this, TabsActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
