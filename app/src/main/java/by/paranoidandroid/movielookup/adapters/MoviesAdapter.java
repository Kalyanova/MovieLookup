package by.paranoidandroid.movielookup.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import by.paranoidandroid.movielookup.R;
import by.paranoidandroid.movielookup.model.entities.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movies;
    private LayoutInflater layoutInflater;

    public MoviesAdapter(Context context) {
        movies = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.list_item_movie, parent, false));

    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        Picasso.get()
                .load(Uri.parse(movie.getPoster()))
                .placeholder(R.drawable.movie_poster_placeholder) // download placeholder
                .error(R.drawable.movie_poster_placeholder)       // error placeholder
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setItems(List<Movie> items) {
        movies.clear();
        movies = items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imv_movie);
            title = itemView.findViewById(R.id.tv_title);
        }
    }
}