package fyp.com.riona.OrderDetail;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fyp.com.riona.ProductDetails.ProductDetailsActivity;

import fyp.com.riona.R;

import java.util.List;


public class OrderDetailProductsAdapter extends RecyclerView.Adapter<OrderDetailProductsAdapter.MyViewHolder> {
    Context context;
    List<OrderDetailsProduct> dataModelArrayList;

    public OrderDetailProductsAdapter(Context context, List<OrderDetailsProduct> dataModelArrayList) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myorder_product_detail_view, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.tv_name.setText(dataModelArrayList.get(i).getTitle());
        myViewHolder.tv_price.setText("Rs:" + " " + dataModelArrayList.get(i).getPrice());
        myViewHolder.tv_quantity.setText("Quantity:" + " " + dataModelArrayList.get(i).getOrderQuantity());
        Picasso.get()
                .load("http://cablefor.wwwmi3-ls7.a2hosted.com/dev/postquam/storage/app/public/" + dataModelArrayList.get(i).getThumbnail())
                .into(myViewHolder.img);
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                String id = dataModelArrayList.get(i).getId();
                intent.putExtra("product_id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView tv_name, tv_price, tv_quantity;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView_order_detail);
            tv_name = itemView.findViewById(R.id.textViewName_detail_order);
            tv_price = itemView.findViewById(R.id.textViewVersion_price);
            cardView = itemView.findViewById(R.id.card_view_detail_product);
            tv_quantity = itemView.findViewById(R.id.textViewVersion_quantity);
        }
    }
}
