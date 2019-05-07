package com.example.gitsearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gitsearch.Model.User;
import com.example.gitsearch.R;

import java.util.ArrayList;
import java.util.List;

public class FollowersRecyclerAdapter extends RecyclerView.Adapter<FollowersRecyclerAdapter.ViewHolder> {

    private List<User> mFollowerList = new ArrayList<>();
    private Context mContext;

    public FollowersRecyclerAdapter(List<User> mFollowerList, Context mContext) {
        this.mFollowerList = mFollowerList;
        this.mContext = mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mUserNameTextView;
        ImageView mAvatarImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatarImageView = itemView.findViewById(R.id.recycler_follower_avatar);
            mUserNameTextView = itemView.findViewById(R.id.recycler_folloer_userName);
        }
    }

    @NonNull
    @Override
    public FollowersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new FollowersRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersRecyclerAdapter.ViewHolder viewHolder, int i) {
        User user = mFollowerList.get(i);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_github_logo)
                .error(R.drawable.ic_github_logo);

        Glide.with(mContext)
                .load(user.getmAvatarUrl())
                .apply(options)
                .into(viewHolder.mAvatarImageView);

        viewHolder.mUserNameTextView.setText(user.getmUserName());

    }

    @Override
    public int getItemCount() {
        Log.d("mmmm", String.valueOf(mFollowerList.size()));
        return mFollowerList.size();
    }
}
