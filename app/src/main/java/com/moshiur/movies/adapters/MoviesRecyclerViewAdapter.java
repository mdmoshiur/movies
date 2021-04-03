package com.moshiur.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moshiur.movies.R;
import com.moshiur.movies.models.Results;
import com.moshiur.movies.models.ServerResponse;

import java.util.List;

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesRecyclerViewHolder> {

    private Context mContext;
    private List<Results> listData;
    //interface variable
    //private MyOnItemClickListener myOnItemClickListener;

    public MoviesRecyclerViewAdapter(Context context, List<Results> results) {
        this.listData = results;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MoviesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cards, parent, false);
        return new MoviesRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesRecyclerViewHolder holder, int position) {
        Results results = listData.get(position);

        if (results.getPosterPath() == null) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + results.getPosterPath()).into(holder.imageView);
        }
        holder.titleView.setText(results.getTitle());
        holder.release_date.setText("Release date: " + results.getReleaseDate());
        holder.popularityView.setText("Popularity: "+ results.getPopularity());
        holder.vote_count.setText("Vote counted: " + results.getVoteCount());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class MoviesRecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleView;
        public TextView popularityView, release_date, vote_count;


        public MoviesRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //find all the views
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.title);
            popularityView = itemView.findViewById(R.id.popularity);
            release_date = itemView.findViewById(R.id.release_date_id);
            vote_count = itemView.findViewById(R.id.vote_count_id);

        }
    }

}
