package com.naztec.storesy.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naztec.storesy.Models.MultiLayoutModel;
import com.naztec.storesy.R;

import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public class MultiLayoutAdapter extends RecyclerView.Adapter {

    ArrayList<MultiLayoutModel> layoutList;

    protected final int BANNERS_LAYOUT = 0;
    protected final int HORIZONTAL_PRODS_LAYOUT = 1;

    public MultiLayoutAdapter(ArrayList<MultiLayoutModel> layoutsList) {
        this.layoutList = layoutsList;
    }

    @Override
    public int getItemViewType(int position) {

        switch (layoutList.get(position).getLayoutID()) {
            case 0:
                return BANNERS_LAYOUT;
            case 1:
                return HORIZONTAL_PRODS_LAYOUT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case BANNERS_LAYOUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_banners, parent, false);
                return new BannerViewHolder(view);
            case HORIZONTAL_PRODS_LAYOUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_horizontal_prod_list, parent, false);
                return new HorizontalViewHolder(view);
            default:
                //noinspection ConstantConditions
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MultiLayoutModel data = layoutList.get(position);

        switch (data.getLayoutID()) {
            case BANNERS_LAYOUT:
                ((BannerViewHolder) holder).setData();
                break;
            case HORIZONTAL_PRODS_LAYOUT:
                ((HorizontalViewHolder) holder).setData(data.getLayoutTitle(), data.getProductIds());
                break;
        }


    }

    protected static class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void setData() {
        }

    }

    protected static class HorizontalViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RecyclerView rvHorizontalProducts;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hor_prod_list_title);
            rvHorizontalProducts = itemView.findViewById(R.id.rv_horizontal_prod);
            rvHorizontalProducts.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));

        }

        @SuppressLint("NotifyDataSetChanged")
        private void setData(String txtTitle, ArrayList<String> products) {
            title.setText(txtTitle);
            HorizontalProductsAdapter adapter = new HorizontalProductsAdapter(products);
            rvHorizontalProducts.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public int getItemCount() {
        return layoutList.size();
    }
}
