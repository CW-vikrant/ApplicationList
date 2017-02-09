package com.applicationlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applicationlist.R;
import com.applicationlist.activities.MovieDetailsActivity;
import com.applicationlist.pojo.Contact;
import com.applicationlist.pojo.Result;

import java.util.List;

/**
 * Created by Vikrant Chauhan on 10/17/2016.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    private List<Result> moviesList;
    private Context mContext;


    public MoviesAdapter(Context context,List<Result> moviesList){
        this.mContext = context;
        this.moviesList = moviesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie,parent,false);
        return new MoviesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.movieTitle.setText(moviesList.get(position).getTitle());
        holder.data.setText(moviesList.get(position).getRelease_date());
        holder.movieDescription.setText(moviesList.get(position).getOverview());
        holder.rating.setText(moviesList.get(position).getVote_average().toString());
    }

    @Override
    public int getItemCount() {
        return moviesList!=null ? moviesList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;


        public ViewHolder(View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.subtitle);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);

            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int id = moviesList.get(getAdapterPosition()).getId();
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.ID,id);
            mContext.startActivity(intent);
        }
    }

}
