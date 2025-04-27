package fyp.com.riona.MyOrders;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fyp.com.riona.OrderDetail.OrderDetailActivity;
import fyp.com.riona.R;

import java.util.ArrayList;

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.MyViewHolder> {
    ////////////////Declare Variable//////////////////
    private Context context;
    ArrayList<OrderDataModel> dataModelArrayList;
    public MyOrdersRecyclerAdapter(Context context, ArrayList<OrderDataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_orders_recycler_item , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final  int i) {

        String order_id=dataModelArrayList.get(i).getInvoiceId();
        String date=dataModelArrayList.get(i).getCreatedAt();
        String total=dataModelArrayList.get(i).getNetTotal();
        String status=dataModelArrayList.get(i).getOrderStatus();
        if(order_id!=null && !order_id.isEmpty())
        {
            myViewHolder.tv_order_id.setText("Order Id: "+order_id);
        }
        if(date!=null && !date.isEmpty())
        {
            myViewHolder.tv_date.setText("Date: "+date);
        }
        if(total!=null && !total.isEmpty())
        {
            myViewHolder.tv_total.setText("Total: "+total);
        }
        if(status!=null && !status.isEmpty())
        {
            myViewHolder.tv_status.setText("Order Status: "+status);
        }
      myViewHolder.img_details.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, OrderDetailActivity.class);
              String id =dataModelArrayList.get(i).getId();
              intent.putExtra("order_id",id);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_id,tv_status,tv_total,tv_date;
        ImageView img_details;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_id = itemView.findViewById(R.id.order_id_list);
            tv_status = itemView.findViewById(R.id.order_status_list);
            tv_total = itemView.findViewById(R.id.order_total_list);
            tv_date = itemView.findViewById(R.id.order_date_list);
            img_details = itemView.findViewById(R.id.order_details_list);

        }
    }
}
