package sg.edu.rp.c346.s19024292.c302_p09_mcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AddMenuItemActivity extends AppCompatActivity {
    Button btnAdd, btnDelete, btnUpdate;
    EditText etItem, etPrice;
    private AsyncHttpClient client;
    String loginID, apikey, catID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        client = new AsyncHttpClient();

        Intent i = getIntent();
        catID = i.getStringExtra("catID");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginID = prefs.getString("loginID","");
        apikey = prefs.getString("apiKey", "");

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        etPrice = findViewById(R.id.etPrice);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddMenuItemActivity.this);
                loginID = prefs.getString("loginID","");
                apikey = prefs.getString("apiKey", "");


                params.add("menu_item_description", etItem.getText().toString());
                params.add("menu_item_unit_price", etPrice.getText().toString());
                params.add("apikey", apikey);
                params.add("loginId", loginID);

                client.post("http://localhost/C302_P09_mCafe/updateMenuItemById.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());
                            Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddMenuItemActivity.this);
                loginID = prefs.getString("loginID","");
                apikey = prefs.getString("apiKey", "");

                RequestParams params = new RequestParams();
                params.add("menu_item_category_id", catID);
                params.add("loginId", loginID);
                params.add("apikey", apikey);

                client.post("http://localhost/C302_P09_mCafe/deleteMenuItemById.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try{
                            Toast.makeText(AddMenuItemActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                });
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(AddMenuItemActivity.this, etItem.getText().toString(), Toast.LENGTH_SHORT).show();
                RequestParams params = new RequestParams();
                params.add("menu_item_category_id", catID);
                params.add("menu_item_description", etItem.getText().toString());
                params.add("menu_item_unit_price", etPrice.getText().toString());
                params.add("loginId", loginID);
                params.add("apikey", apikey);

                client.post("http://localhost/C302_P09_mCafe/addMenuItem.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Log.i("JSON Results: ", response.toString());
                            Toast.makeText(getApplicationContext(),response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Code for step 1 start
        Intent intent = getIntent();
        catID = intent.getStringExtra("catID");
        String loginId = intent.getStringExtra("loginId");
        String apikey = intent.getStringExtra("apikey");
        RequestParams params = new RequestParams();
        params.add("categoryId", catID);
        params.add("loginId", loginId);
        params.add("apikey", apikey);
        client.post("http://localhost/C302_P09_mCafe/getMenuItemsByCategory.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    etItem.setText(response.getString("menu_item_description"));
                    etPrice.setText(response.getString("menu_item_unit_price"));
                    Toast.makeText(getApplicationContext(), response.getString("Success"), Toast.LENGTH_LONG).show();
                    finish();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}