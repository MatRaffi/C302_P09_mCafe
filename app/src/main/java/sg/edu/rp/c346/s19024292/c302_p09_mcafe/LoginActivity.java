
package sg.edu.rp.c346.s19024292.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginID, etPassword;
    private Button btnSubmit;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginID = (EditText) findViewById(R.id.editTextLoginID);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
        btnSubmit = (Button) findViewById(R.id.buttonSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter username.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter password.", Toast.LENGTH_LONG).show();

                } else {
                    //proceed to authenticate user
                    OnLogin(v);
                }
            }
        });
    }

    private void OnLogin(View v) {
        // Point X - TODO: call doLogin web service to authenticate user
        client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("username", etLoginID.getText().toString());
        params.add("password", etPassword.getText().toString());

        client.post("http://localhost/C302_P09_mCafe/doLogin.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    String authenticate = response.getString("authenticated");
                    Log.d("TAG", response.toString());
                    if (authenticate.equals("true")) {
                        String id = response.getString("id");
                        String apikey = response.getString("apikey");
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("loginID", id);
                        editor.putString("apikey", apikey);
                        editor.commit();

                        startActivity(i);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



