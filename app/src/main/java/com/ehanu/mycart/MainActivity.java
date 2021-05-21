package com.ehanu.mycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.ehanu.mycart.database.ProductManager;
import com.ehanu.mycart.fragment.CartFragment;
import com.ehanu.mycart.fragment.ProductFragment;
import com.ehanu.mycart.model.Product;
import com.ehanu.mycart.util.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private List<Product> shoppingProducts = new ArrayList<>();
    private final String productUrl = "https://mpr-cart-api.herokuapp.com/products";
    public static int WIDTH;
    public static int HEIGHT;
    private ProductManager productManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productManager = ProductManager.getInstance(this);

        JsonTask jsonTask = new JsonTask();
        jsonTask.task = this;
        jsonTask.execute(productUrl);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WIDTH = size.x;
        HEIGHT = size.y;
    }

    @Override
    public void onTaskCompleted() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = new ProductFragment(shoppingProducts, productManager);

        manager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_cart){
            List<Product> carts = productManager.all();

            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = new CartFragment(carts, productManager);

            manager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, "VIEW_CART" )
                    .addToBackStack("back")
                    .commit();

            return true;
        }

        return false;
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        private AsyncResponse task;

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    result.append(line);
                }
                Log.i("RESULT", "" + result);
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                return;
            }
            try {
                JSONArray products = new JSONArray(result);

                for(int i = 0; i < products.length(); i++){
                    JSONObject product = products.getJSONObject(i);
                    Product shoppingProduct = new Product();

                    shoppingProduct.setId(product.getInt("id"));
                    shoppingProduct.setImgUrl(product.getString("thumbnail"));
                    shoppingProduct.setName(product.getString("name"));
                    shoppingProduct.setPrice(product.getInt("unitPrice"));

                    shoppingProducts.add(shoppingProduct);
                }

                task.onTaskCompleted();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}