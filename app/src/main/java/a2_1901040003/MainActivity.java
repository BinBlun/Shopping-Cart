package a2_1901040003;

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

import a2_1901040003.database.ProductManager;
import a2_1901040003.fragment.CartFragment;
import a2_1901040003.fragment.ProductFragment;
import a2_1901040003.model.Product;
import a2_1901040003.util.AsyncResponse;

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
    private final List<Product> shoppingItems = new ArrayList<>();
    private final String ProductsLink = "https://mpr-cart-api.herokuapp.com/products";
    public static int HIGH;
    public static int WIDE;
    private ProductManager manageProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageProduct = ProductManager.getInstance(this);

        JsonTask jsonTask = new JsonTask();
        jsonTask.task = this;
        jsonTask.execute(ProductsLink);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        HIGH = point.y;
        WIDE = point.x;
    }

    @Override
    public void onTaskCompleted() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new ProductFragment(shoppingItems, manageProduct);

        fragmentManager.beginTransaction()
                .replace(R.id.TheFragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.view_the_cart) {
            List<Product> TheCarts = manageProduct.all();

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new CartFragment(TheCarts, manageProduct);

            fragmentManager.beginTransaction()
                    .replace(R.id.TheFragmentContainer, fragment, "VIEW_CART")
                    .addToBackStack("back")
                    .commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu and this will add more items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private class JsonTask extends AsyncTask<String, String, String> {
        private AsyncResponse task;

        @Override
        protected void onPostExecute(String consequences) {
            super.onPostExecute(consequences);
            if (consequences == null) {
                return;
            }
            try {
                JSONArray TheItems = new JSONArray(consequences);

                for (int i = 0; i < TheItems.length(); i++) {
                    JSONObject product = TheItems.getJSONObject(i);
                    Product shoppingMoreItems = new Product();
                    shoppingMoreItems.setId(product.getInt("id"));
                    shoppingMoreItems.setName(product.getString("name"));
                    shoppingMoreItems.setPrice(product.getInt("unitPrice"));
                    shoppingMoreItems.setImageUrl(product.getString("thumbnail"));


                    shoppingItems.add(shoppingMoreItems);
                }

                task.onTaskCompleted();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            URL link;
            HttpURLConnection urlConnection;
            try {
                link = new URL(strings[0]);
                urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.connect();
                InputStream inputTheStream = urlConnection.getInputStream();
                Scanner scanner = new Scanner(inputTheStream);
                StringBuilder consequence = new StringBuilder();
                String lineWord;
                while (scanner.hasNextLine()) {
                    lineWord = scanner.nextLine();
                    consequence.append(lineWord);
                }
                Log.i("RESULT", "" + consequence);
                return consequence.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

//public class MainActivity extends AppCompatActivity implements AsyncResponse {
//    private List<Product> shoppingProducts = new ArrayList<>();
//    private final String productUrl = "https://mpr-cart-api.herokuapp.com/products";
//    public static int WIDTH;
//    public static int HEIGHT;
//    private ProductManager productManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        productManager = ProductManager.getInstance(this);
//
//        JsonTask jsonTask = new JsonTask();
//        jsonTask.task = this;
//        jsonTask.execute(productUrl);
//
//
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        WIDTH = size.x;
//        HEIGHT = size.y;
//    }
//
//    @Override
//    public void onTaskCompleted() {
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment fragment = new ProductFragment(shoppingProducts, productManager);
//
//        manager.beginTransaction()
//                .replace(R.id.TheFragmentContainer, fragment)
//                .commit();
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.view_the_cart){
//            List<Product> carts = productManager.all();
//
//            FragmentManager manager = getSupportFragmentManager();
//            Fragment fragment = new CartFragment(carts, productManager);
//
//            manager.beginTransaction()
//                    .replace(R.id.TheFragmentContainer, fragment, "VIEW_CART" )
//                    .addToBackStack("back")
//                    .commit();
//
//            return true;
//        }
//
//        return false;
//    }
//
//    private class JsonTask extends AsyncTask<String, String, String> {
//        private AsyncResponse task;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            URL url;
//            HttpURLConnection urlConnection;
//            try {
//                url = new URL(strings[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.connect();
//                InputStream is = urlConnection.getInputStream();
//                Scanner sc = new Scanner(is);
//                StringBuilder result = new StringBuilder();
//                String line;
//                while (sc.hasNextLine()) {
//                    line = sc.nextLine();
//                    result.append(line);
//                }
//                Log.i("RESULT", "" + result);
//                return result.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (result == null) {
//                return;
//            }
//            try {
//                JSONArray products = new JSONArray(result);
//
//                for(int i = 0; i < products.length(); i++){
//                    JSONObject product = products.getJSONObject(i);
//                    Product shoppingProduct = new Product();
//
//                    shoppingProduct.setId(product.getInt("id"));
//                    shoppingProduct.setImageUrl(product.getString("thumbnail"));
//                    shoppingProduct.setName(product.getString("name"));
//                    shoppingProduct.setPrice(product.getInt("unitPrice"));
//
//                    shoppingProducts.add(shoppingProduct);
//                }
//
//                task.onTaskCompleted();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}