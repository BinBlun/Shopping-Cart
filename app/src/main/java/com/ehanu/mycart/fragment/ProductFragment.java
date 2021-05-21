package com.ehanu.mycart.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehanu.mycart.R;
import com.ehanu.mycart.adapter.ProductAdapter;
import com.ehanu.mycart.database.ProductManager;
import com.ehanu.mycart.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductFragment extends Fragment {
    private RecyclerView productRv;
    private List<Product> shoppingProducts;
    private TextView searchProducts;
    private ImageButton searchBtn;
    private ProductAdapter productAdapter;
    private ProductManager productManager;

    public ProductFragment(List<Product> shoppingProducts, ProductManager productManager){
        this.shoppingProducts = shoppingProducts;
        this.productManager = productManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        searchProducts = view.findViewById(R.id.search_text);
        searchBtn = view.findViewById(R.id.search_icon);

        productRv = view.findViewById(R.id.product_rv);

        productAdapter = new ProductAdapter(shoppingProducts, productManager);
        productRv.setAdapter(productAdapter);


        productRv.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(searchProducts.getText().toString());
            }
        });

        return view;
    }

    private void filter(String text){
        List<Product> temp = new ArrayList();
        for(Product product: shoppingProducts){
            if(product.getName().toLowerCase().contains(text)){
                temp.add(product);
            }
        }
        //update recyclerview
        productAdapter.updateList(temp);
    }

}
