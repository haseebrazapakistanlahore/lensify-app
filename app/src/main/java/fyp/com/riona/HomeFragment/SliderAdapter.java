package fyp.com.riona.HomeFragment;

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

import java.util.ArrayList;


public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<BannerDataModel> homeSliderModels;


    public SliderAdapter(Context context, ArrayList<BannerDataModel> homeSliderModels) {
        this.context = context;
        this.homeSliderModels = homeSliderModels;
    }

    @Override
    public int getCount() {
        return homeSliderModels.size();
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
        ImageView  imageView = (ImageView) view.findViewById(R.id.image_slider);

        Glide.with(context)
                .load(context.getResources().getString(R.string.url)+homeSliderModels.get(position).getImageUrl()).placeholder(R.drawable.placeholder)
                .dontAnimate()
                .fitCenter()
                .centerCrop()
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
