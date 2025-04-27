package fyp.com.riona.SubCategory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import fyp.com.riona.R;
import fyp.com.riona.SubInnerCategory.SubInnerCategoryActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SubCategoryCircleAdapter extends RecyclerView.Adapter<SubCategoryCircleAdapter.MyViewHolder>{

    Context context;
    ArrayList<SabCatDataModel> dataModelArrayList;
    int product_availble_flag ;

    SharedPreferences category_pref;
    SharedPreferences.Editor sharedprefernce_cat_editor ;


    public SubCategoryCircleAdapter(Context context, ArrayList<SabCatDataModel> dataModelArrayList , int product_availble_flag) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        this.product_availble_flag = product_availble_flag;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(product_availble_flag == 0) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_large_recycler_view_item, viewGroup, false));
        }else {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_small_recycler_view_item, viewGroup, false));

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final  int i) {
        if(dataModelArrayList.get(i).getImage()!=null && !dataModelArrayList.get(i).getImage().isEmpty())
        {
            Glide.with(context)
                    .load(context.getResources().getString(R.string.url)+dataModelArrayList.get(i).getImage()).placeholder(R.drawable.placeholder)

                    .dontAnimate()
                    .into(myViewHolder.imageView);

        }
        if(dataModelArrayList.get(i).getTitle()!=null || !dataModelArrayList.get(i).getTitle().isEmpty())
        {
            myViewHolder.textView.setText(dataModelArrayList.get(i).getTitle());
        }


        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SubInnerCategoryActivity.class);

                category_pref = context.getSharedPreferences("cate_second_id", MODE_PRIVATE);
                sharedprefernce_cat_editor = category_pref.edit();
                sharedprefernce_cat_editor.putString("sub_category_id",dataModelArrayList.get(i).getId());
                sharedprefernce_cat_editor.putString("sub_category_name",dataModelArrayList.get(i).getTitle());
                sharedprefernce_cat_editor.apply();
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

        ImageView  imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.category_image);
            textView = (TextView) itemView.findViewById(R.id.category_title);
        }
    }



}
