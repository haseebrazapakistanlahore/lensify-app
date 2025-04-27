package fyp.com.riona.ProductDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import fyp.com.riona.R;

import java.util.List;



public class ProductColorsSpinnerAdapter extends BaseAdapter {

    Context context;
    List<ColorsModel> colorsNames;

    public ProductColorsSpinnerAdapter(Context context, List<ColorsModel> colorsNames) {
        this.context = context;
        this.colorsNames = colorsNames;
    }

    @Override
    public int getCount() {
        return colorsNames.size();
    }

    @Override
    public ColorsModel getItem(int i) {
        return colorsNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.product_spinner_colors_view,viewGroup,false);
        TextView names = (TextView) view.findViewById(R.id.color_name);
        ImageView my_img = view.findViewById(R.id.color_image);
        names.setText(colorsNames.get(i).getName());
        Glide.with(context)
                .load(context.getResources().getString(R.string.url)+colorsNames.get(i).getImageUrl()).placeholder(R.drawable.placeholder)

                .dontAnimate()
                .into(my_img);
        return view;
    }
}