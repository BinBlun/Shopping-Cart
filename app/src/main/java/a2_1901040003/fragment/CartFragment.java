package a2_1901040003.fragment;

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

import a2_1901040003.R;
import a2_1901040003.adapter.CartAdapter;
import a2_1901040003.database.ProductManager;
import a2_1901040003.model.Product;
import a2_1901040003.util.CurrencyFormatter;
import a2_1901040003.util.RecyclerViewItemClick;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView cartRecyclerView;
    private CartAdapter theCartAdapter;
    private TextView totalMoney;
    private final List<Product> carts;
    private final ProductManager manageTheProduct;

    public CartFragment(List<Product> carts, ProductManager manageTheProduct) {
        this.carts = carts;
        this.manageTheProduct = manageTheProduct;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.cart_fragment, container, false);

        cartRecyclerView = v.findViewById(R.id.cart_recyclerview);
//        TextView total = v.findViewById(R.id.total);
        totalMoney = v.findViewById(R.id.total_money);
        MaterialButton checkoutButton = v.findViewById(R.id.checkout_button);

        totalMoney.setText(CurrencyFormatter.format((long) calculateTheTotalMoney()));


        theCartAdapter = new CartAdapter(carts, manageTheProduct);
        cartRecyclerView.setAdapter(theCartAdapter);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageTheProduct.deleteAll();

                carts.clear();

                Toast toast = Toast.makeText(view.getContext(), "Thank you for the purchase!", Toast.LENGTH_SHORT);
                toast.show();
                theCartAdapter.notifyDataSetChanged();
                refresh();
            }
        });

        return v;
    }

    private int calculateTheTotalMoney() {
        int totalMoney = 0;

        for (Product product : carts) {
            totalMoney += product.getPrice() * product.getQuantity();
        }

        return totalMoney;
    }

    @Override
    public void onResume() {
        super.onResume();
        theCartAdapter.onItemClick(new RecyclerViewItemClick() {
            @Override
            public void onProductClick(int position, View v) {
                totalMoney.setText(CurrencyFormatter.format((long) calculateTheTotalMoney()));
            }
        });
    }

    public void refresh() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
}

