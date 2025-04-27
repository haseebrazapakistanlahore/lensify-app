package fyp.com.riona.CategoriesFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fyp.com.riona.HomeFragment.CategoryDataModel;
import fyp.com.riona.R;
import fyp.com.riona.SubCategory.SubCategoryActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class CategoriesTabListProAdapter extends RecyclerView.Adapter<CategoriesTabListProAdapter.MyViewHolder>{

    Context context;
    ArrayList<CategoryDataModel> dataModelArrayList;
    SharedPreferences category_pref;
    SharedPreferences.Editor sharedprefernce_cat_editor ;
    public CategoriesTabListProAdapter(Context context, ArrayList<CategoryDataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

    }
    @NonNull
    @Override
    public CategoriesTabListProAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_all_tab_view , viewGroup , false));

    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesTabListProAdapter.MyViewHolder myViewHolder, final int i) {
        if(dataModelArrayList.get(i).getImage()!=null && !dataModelArrayList.get(i).getImage().isEmpty())
        {
            Glide.with(context)
                    .load(context.getResources().getString(R.string.url)+dataModelArrayList.get(i).getImage()).placeholder(R.drawable.placeholder)
                     .dontAnimate()
                    .into(myViewHolder.imageView);

        }
        if(dataModelArrayList.get(i).getTitle()!=null && !dataModelArrayList.get(i).getTitle().isEmpty())
        {
            myViewHolder.textView.setText(dataModelArrayList.get(i).getTitle());
        }




        myViewHolder.categoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, SubCategoryActivity.class);
                category_pref = context.getSharedPreferences("cate_first_id", MODE_PRIVATE);
                sharedprefernce_cat_editor = category_pref.edit();
                sharedprefernce_cat_editor.putString("category_id",dataModelArrayList.get(i).getId());
                sharedprefernce_cat_editor.putString("category_name",dataModelArrayList.get(i).getTitle());
                sharedprefernce_cat_editor.apply();
                context.startActivity(intent);



            }
        });

    }


    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        CardView categoryCard;

        public MyViewHolder(View itemView) {
            super(itemView);


            imageView = (ImageView) itemView.findViewById(R.id.image_category_tab);
            textView = (TextView) itemView.findViewById(R.id.title_category_tab);
            categoryCard = (CardView) itemView.findViewById(R.id.categories_cardView);

        }
    }

}