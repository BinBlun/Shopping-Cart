package a2_1801040081.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import a2_1801040081.MainActivity;
import a2_1801040081.R;
import a2_1801040081.adapter.CartAdapter;
import a2_1801040081.database.ProductManager;
import a2_1801040081.model.Product;
import a2_1801040081.util.CurrencyFormatter;
import a2_1801040081.util.RecyclerViewItemClick;
import com.google.android.material.button.MaterialButton;

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
        MaterialButton checkout = view.findViewById(R.id.checkout_btn);

        totalPrice.setText(CurrencyFormatter.format((long) calculateTotalPrice()));


        cartAdapter = new CartAdapter(carts, productManager);
        cartRv.setAdapter(cartAdapter);

        cartRv.setLayoutManager(new LinearLayoutManager(this.getContext()));

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productManager.deleteAll();

                carts.clear();

                Toast toast= Toast.makeText(view.getContext(),"Thank you for the purchase!",Toast.LENGTH_SHORT);
                toast.show();
                cartAdapter.notifyDataSetChanged();
                refresh();
            }
        });

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
                totalPrice.setText(CurrencyFormatter.format((long) calculateTotalPrice()));
            }
        });
    }

    public void refresh(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
}
