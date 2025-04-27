package fyp.com.riona.SaleFragment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import fyp.com.riona.R;

import java.util.ArrayList;


public class SalesRecycerAdapter extends RecyclerView.Adapter<SalesRecycerAdapter.MyViewHolder> {
    Context context;
    ArrayList<DiscountsModel> discountsModelArrayList;

    public SalesRecycerAdapter(Context context, ArrayList<DiscountsModel> discountsModelArrayList) {
        this.context = context;
        this.discountsModelArrayList = discountsModelArrayList;
    }

    @NonNull
    @Override
    public SalesRecycerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_recycler_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull SalesRecycerAdapter.MyViewHolder myViewHolder, int i) {

        Glide.with(context)
                .load(context.getResources().getString(R.string.url) + discountsModelArrayList.get(i).getImage()).placeholder(R.drawable.placeholder)

                .dontAnimate()
                .into(myViewHolder.sales_image);



    }

    @Override
    public int getItemCount() {
        return discountsModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView sales_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            sales_image = (ImageView) itemView.findViewById(R.id.sales_offers_imageView);

        }
    }
}
