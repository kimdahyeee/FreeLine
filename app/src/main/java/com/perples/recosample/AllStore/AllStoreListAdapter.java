package com.perples.recosample.AllStore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.perples.recosample.R;

import java.util.List;

/**
 * Created by dahye on 2016-09-27.
 */
public class AllStoreListAdapter extends RecyclerView.Adapter<AllStoreListAdapter.RecyclerViewHolders> {

    private List<AllStoreListObject> storeList;
    private Context context;

    public AllStoreListAdapter(Context context, List<AllStoreListObject> storeList){
        this.storeList = storeList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_store_list_adapter, parent, false);
        //View layoutView = LayoutInflater.inflate(R.layout.all_store_list,parent, false);//오빤 false로함 차이 알아보기
        return new RecyclerViewHolders(layoutView);
    }

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.storeName.setText(storeList.get(position).getName());
        Glide.with(context).load(storeList.get(position).getImage()).into(holder.storeImage);
        holder.storeCategory.setText(storeList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return this.storeList.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView storeName;
        public ImageView storeImage;
        public TextView storeCategory;


        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            storeName = (TextView)itemView.findViewById(R.id.store_name);
            storeImage = (ImageView)itemView.findViewById(R.id.store_image);
            storeCategory = (TextView)itemView.findViewById(R.id.store_category);
        }

        @Override
        public void onClick(View view){
            Context context =view.getContext();
            int position = getAdapterPosition();
            Intent intent = new Intent(context, AllStoreDetailActivity.class);
            intent.putExtra("storeName", storeList.get(position).getName());
            intent.putExtra("storeImage", storeList.get(position).getImage());
            intent.putExtra("storeTel", storeList.get(position).getTel());
            intent.putExtra("storeIntroduction", storeList.get(position).getIntroduction());
            intent.putExtra("storeLocation", storeList.get(position).getLocation());
            intent.putExtra("storeCategory", storeList.get(position).getCategory());
            Toast.makeText(view.getContext(), "클릭됐어" + storeList.get(position).getName(), Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        }
    }
}
