package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.ZamrazalnikRestReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.LocationEnum;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

public class AddLocationToDatabaseActivity extends Activity {

    Spinner levelSpinner  ;
    TextView nameLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        setGuiControls();

        Button bSave = (Button)findViewById(R.id.btnSaveLocation);
        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewLocation();
            }
        });
    }

    private void addNewLocation() {
        ZamrazalnikRestReaderHelper helper = new ZamrazalnikRestReaderHelper();
        RequestParams params = helper.addNewLocationParams(new Miejsce(nameLoc.getText().toString(), ((LocationEnum) levelSpinner.getSelectedItem())));

        OsaRestClient.post("/place", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("OK - Dodane miejsce");
                Intent intent = new Intent(getApplicationContext(), DisplayAllLocationsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("BLOND - miejsce nie dodane");
            }
        });
    }

    private void setGuiControls() {
        nameLoc = (TextView) findViewById(R.id.txtNameLocation);
        ArrayAdapter levelAdapter = new ArrayAdapter(AddLocationToDatabaseActivity.this, R.layout.spinner, LocationEnum.values());
        levelSpinner  = (Spinner) findViewById(R.id.spinnerLevel);
        levelSpinner.setAdapter(levelAdapter);
    }
}

