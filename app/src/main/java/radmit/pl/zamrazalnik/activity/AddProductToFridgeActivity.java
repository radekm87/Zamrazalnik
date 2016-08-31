package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

import radmit.pl.zamrazalnik.R;

/**
 * Created by rmorawski on 31.08.16.
 */
public class AddProductToFridgeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Button btnReturn = (Button)findViewById(R.id.buttonReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}
