package fyp.com.riona.ProductDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import fyp.com.riona.R;

import java.util.List;



public class ProductImagesSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ProductImagesModel> productImagesModelList;
    public ProductImagesSliderAdapter(Context context , List<ProductImagesModel> productImagesModelList) {
        productImagesModelList.remove(0);
        this.context = context;
        this.productImagesModelList = productImagesModelList;
    }

    @Override
    public int getCount() {
        return productImagesModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_view,container,false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_slider);

        Glide.with(context)
                .load(context.getResources().getString(R.string.url)+productImagesModelList.get(position).getImageUrl()).placeholder(R.drawable.placeholder)

                .dontAnimate()
                .into(imageView);
     container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
