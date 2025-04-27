package fyp.com.riona.Notification;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fyp.com.riona.R;

import java.util.ArrayList;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
    ArrayList<NotificationDataModel> dataModelArrayList;
    Context context;
    public NotificationRecyclerAdapter(Context context, ArrayList<NotificationDataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

    }
    @NonNull
    @Override
    public NotificationRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_recycler_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.MyViewHolder myViewHolder, int i) {
        if(dataModelArrayList.get(i).getContent()!=null && !dataModelArrayList.get(i).getContent().isEmpty())
        {
            myViewHolder.notification_tv_name.setText(dataModelArrayList.get(i).getContent());
        }

        if(dataModelArrayList.get(i).getCreatedAt()!=null && !dataModelArrayList.get(i).getCreatedAt().isEmpty())
        {
            myViewHolder.notification_tv_time.setText(dataModelArrayList.get(i).getCreatedAt());
        }

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView notification_tv_time, notification_tv_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notification_tv_time = (TextView) itemView.findViewById(R.id.notification_item_time);
            notification_tv_name = (TextView) itemView.findViewById(R.id.notification_item_text);
        }
    }
}
