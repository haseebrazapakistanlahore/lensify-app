package fyp.com.riona.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import fyp.com.riona.R;
import fyp.com.riona.SubCategory.SubCategoryActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragmentCategoriesRecyclerAdapter extends RecyclerView.Adapter<HomeFragmentCategoriesRecyclerAdapter.MyViewHolder> {

    private Context context;
    ArrayList<CategoryDataModel> dataModelArrayList;
    SharedPreferences category_pref;
    SharedPreferences.Editor sharedprefernce_cat_editor ;
    public HomeFragmentCategoriesRecyclerAdapter(Context context, ArrayList<CategoryDataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_fragment_categories_recycler_item , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        Glide.with(context)
                .load(context.getResources().getString(R.string.url)+dataModelArrayList.get(i).getImage()).placeholder(R.drawable.placeholder)
                .dontAnimate()
                .into(myViewHolder.imageView);
        myViewHolder.textView.setText(dataModelArrayList.get(i).getTitle());

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SubCategoryActivity.class);

                category_pref = context.getSharedPreferences("cate_first_id", MODE_PRIVATE);
                sharedprefernce_cat_editor = category_pref.edit();
                sharedprefernce_cat_editor.putString("category_id",dataModelArrayList.get(i).getId());
                sharedprefernce_cat_editor.putString("category_name",dataModelArrayList.get(i).getTitle());
                sharedprefernce_cat_editor.apply();
                context.startActivity(intent);                }
        });

    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.category_image);
            textView = (TextView) itemView.findViewById(R.id.category_title);
            linearLayout = itemView.findViewById(R.id.linearlayout1);

        }
    }
}
