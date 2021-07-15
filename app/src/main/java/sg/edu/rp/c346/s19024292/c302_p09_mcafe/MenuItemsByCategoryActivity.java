package sg.edu.rp.c346.s19024292.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MenuItemsByCategoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<MenuCategoryItem> adapter;
    private ArrayList<MenuCategoryItem> list;
    private AsyncHttpClient client;
    String catID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items_by_category);
        Intent i = getIntent();
        String catID = i.getStringExtra("catId");
        String loginId = i.getStringExtra("loginId");
        String apikey = i.getStringExtra("apikey");

        listView = (ListView) findViewById(R.id.listviewMenuItem);
        list = new ArrayList<MenuCategoryItem>();
        adapter = new ArrayAdapter<MenuCategoryItem>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.add("categoryId", catID);
        params.add("loginId", loginId);
        params.add("apikey", apikey);
        client.post("http://localhost/C302_P09_mCafe/getMenuItemsByCategory.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    Log.i("JSON Results: ", response.toString());
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);
                        String id = jsonObj.getString("menu_item_id");
                        String catId = jsonObj.getString("menu_item_category_id");
                        String desc = jsonObj.getString("menu_item_description");
                        Double price = Double.parseDouble(jsonObj.getString("menu_item_unit_price"));


                        MenuCategoryItem menuCategory = new MenuCategoryItem(id, catId, desc, price);
                        list.add(menuCategory);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new ArrayAdapter<MenuCategoryItem>(MenuItemsByCategoryActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.submain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            // TODO: Clear SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().clear().apply();

            // TODO: Redirect back to login screen
            Intent i = new Intent(MenuItemsByCategoryActivity.this, LoginActivity.class);
            startActivity(i);
            Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show();
            finish();

            return true;
        } else {
            Intent i = new Intent(MenuItemsByCategoryActivity.this, AddMenuItemActivity.class);
            i.putExtra("catID", catID);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}