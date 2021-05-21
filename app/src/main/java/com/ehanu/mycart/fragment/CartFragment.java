package com.ehanu.mycart.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ehanu.mycart.MainActivity;
import com.ehanu.mycart.R;
import com.ehanu.mycart.adapter.CartAdapter;
import com.ehanu.mycart.database.ProductManager;
import com.ehanu.mycart.model.Product;
import com.ehanu.mycart.util.RecyclerViewItemClick;

import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView cartRv;
    private CartAdapter cartAdapter;
    private TextView totalPrice;
    private List<Product> carts;
    private ProductManager productManager;

    public CartFragment(List<Product> carts, ProductManager productManager){
        this.carts = carts;
        this.productManager = productManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        cartRv = view.findViewById(R.id.cart_rv);
        TextView total = view.findViewById(R.id.total);
        totalPrice = view.findViewById(R.id.total_price);
        TextView checkoutText = view.findViewById(R.id.checkout_text);
        ImageView img = view.findViewById(R.id.img_credit_cart);

        total.setWidth(MainActivity.WIDTH / 3);
        totalPrice.setWidth(MainActivity.WIDTH /3);
        totalPrice.setText(String.valueOf(calculateTotalPrice()) + "đ");
        checkoutText.setWidth(MainActivity.WIDTH /3);
        img.setMaxWidth(MainActivity.WIDTH/3);

        cartAdapter = new CartAdapter(carts, productManager);
        cartRv.setAdapter(cartAdapter);

        cartRv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    private int calculateTotalPrice(){
        int totalPrice = 0;

        for (Product product: carts){
            totalPrice += product.getPrice() * product.getQuantity();
        }

        return totalPrice;
    }

    @Override
    public void onResume(){
        super.onResume();
        cartAdapter.onItemClick(new RecyclerViewItemClick() {
            @Override
            public void onItemClick(int position, View v) {
                totalPrice.setText(String.valueOf(calculateTotalPrice()) + "đ");
            }
        });
    }
}
