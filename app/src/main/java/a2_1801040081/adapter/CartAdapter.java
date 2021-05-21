package a2_1801040081.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import a2_1801040081.MainActivity;
import a2_1801040081.R;
import a2_1801040081.database.ProductManager;
import a2_1801040081.model.Product;
import a2_1801040081.util.CurrencyFormatter;
import a2_1801040081.util.RecyclerViewItemClick;

import java.io.InputStream;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder>{

    private List<Product> products;
    private RecyclerViewItemClick myClickListener;
    private ProductManager productManager;


    public CartAdapter(List<Product> products, ProductManager productManager) {
        this.products = products;
        this.productManager = productManager;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.cart_rv, parent, false );

        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product product = products.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder{
        private ImageView productImg;
        private TextView productName;
        private TextView productPrice;
        private TextView productQuantity;
        private ImageButton increase;
        private ImageButton decrease;
        private TextView sumPrice;


        public CartHolder(@NonNull View itemView) {
            super(itemView);

            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productName.setWidth(MainActivity.WIDTH /3);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.quantity);
            increase = itemView.findViewById(R.id.increase_quantity);
            decrease = itemView.findViewById(R.id.reduce_quantity);
            sumPrice = itemView.findViewById(R.id.sum_price);
        }

        public void bind(Product product){
            new DownloadImageTask(productImg).execute(product.getImgUrl());
            productName.setText(product.getName());
            productPrice.setText(CurrencyFormatter.format((long) product.getPrice()));
            productQuantity.setText(String.valueOf(product.getQuantity()));
            sumPrice.setText(CurrencyFormatter.format((long) product.getPrice() * product.getQuantity()));
            //handle click event
            increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = product.getQuantity();
                    product.setQuantity(currentQuantity + 1);
                    productManager.update(product);
                    myClickListener.onItemClick(getAdapterPosition(), v);
                    notifyDataSetChanged();
                }
            });
            decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentQuantity = product.getQuantity();
                    if (currentQuantity - 1 == 0){
                        productManager.delete((long) product.getId());
                        products.remove(product);

                    }else {
                        product.setQuantity(currentQuantity - 1);
                        productManager.update(product);
                    }

                    myClickListener.onItemClick(getAdapterPosition(), v);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void onItemClick(RecyclerViewItemClick mclick){
        this.myClickListener = mclick;
    }

    public void updateList(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
