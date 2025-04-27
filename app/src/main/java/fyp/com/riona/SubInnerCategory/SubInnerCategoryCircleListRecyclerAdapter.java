package fyp.com.riona.SubInnerCategory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fyp.com.riona.R;

class SubInnerCategoryCircleListRecyclerAdapter extends RecyclerView.Adapter<SubInnerCategoryCircleListRecyclerAdapter.MyViewHolder> {
    Context context;

//    ArrayList<CategoriesModel> dataModelArrayList;
//
//
//    public SubInnerCategoryCircleListRecyclerAdapter(Context context, ArrayList<CategoriesModel> dataModelArrayList) {
//        this.context = context;
//        this.dataModelArrayList = dataModelArrayList;
//
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_inner_category_circle_recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        // myViewHolder.imageView.setImageResource(R.drawable.test2);
//        Glide.with(context)
//                .load("http://airizo.com/demo/postquam/storage/app/public/"+dataModelArrayList.get(i).getImage()).placeholder(R.drawable.placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .dontAnimate()
//                .into(myViewHolder.imageView);
//    /*    Picasso.get()
//                .load("http://airizo.com/demo/postquam/storage/app/public/"+dataModelArrayList.get(i).getImage())
//                .into(myViewHolder.imageView);
//
//*/
//
//        myViewHolder.textView.setText(dataModelArrayList.get(i).getTitle());
//
//        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
////                Bundle data = new Bundle();
////                data.putString("title",dataModelArrayList.get(i).getTitle());
////                data.putString("id",dataModelArrayList.get(i).getId());
////
////                ProductByCategoryFragment fragment = new ProductByCategoryFragment();
////                fragment.setArguments(data);
////                pushFragment(fragment, context);
//
//
//
//                Bundle data = new Bundle();
//                data.putString("id",dataModelArrayList.get(i).getId());
//                data.putString("title",dataModelArrayList.get(i).getTitle());
//
//                ProductByCategoryFragment fragment = new ProductByCategoryFragment();
//                fragment.setArguments(data);
//                pushSubFragment(fragment, context);
//            }
//
//
//
//        });

    }


    @Override
    public int getItemCount() {
        return 2;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);

//
//            imageView = (ImageView) itemView.findViewById(R.id.product_image25);
//            textView = (TextView) itemView.findViewById(R.id.product_name25);

        }
    }


}
