package com.moshiur.movies.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.moshiur.movies.R;
import com.moshiur.movies.adapters.MoviesRecyclerViewAdapter;
import com.moshiur.movies.interfaces.ApiInterface;

import com.moshiur.movies.models.Results;
import com.moshiur.movies.models.ServerResponse;
import com.moshiur.movies.network.RetrofitApiClient;
import com.moshiur.movies.utils.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private Button nextPageButton, previousPageButton;

    List<Results> results = new ArrayList<>();
    private RecyclerView moviesRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MoviesRecyclerViewAdapter moviesRecyclerViewAdapter;

    private String API_KEY = "c90baf00024b8a6d772153aa5d02e15d";
    private int totalPages;
    private int page = 1;
    private String  LANGUAGE = "en-US";
    private String category = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding the views
        nextPageButton = findViewById(R.id.next_page_button);
        previousPageButton = findViewById(R.id.previous_page_button);

        makeNetworkCall(category, API_KEY, LANGUAGE, page);

        setMoviesRecyclerView();

        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( page < totalPages) {
                    page = page + 1;
                    makeNetworkCall(category, API_KEY, LANGUAGE, page);
                }

            }
        });

        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page > 1){
                    page = page - 1;
                    makeNetworkCall(category, API_KEY, LANGUAGE, page);
                }
            }
        });

    }

    private void makeNetworkCall(String category, String API_KEY, String LANGUAGE, int page){
        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        Call<ServerResponse> call = apiInterface.getMovies(category, API_KEY, LANGUAGE, page);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                ServerResponse serverResponse = response.body();
                if(response.code() == 200 && serverResponse != null){
                    totalPages = serverResponse.getTotalPages();
                    results = serverResponse.getResults();
                    setMoviesRecyclerView();
                    // moviesRecyclerViewAdapter.notifyDataSetChanged();
                    Log.d("on response", "onResponse: "+ results.size());
                } else {
                    Log.d("on response", "onResponse: "+ "i am in else statement");
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("on failure", "onFailure: ");
            }
        });
    }

    private void setMoviesRecyclerView() {
        //find recyclerView
        moviesRecyclerView = findViewById(R.id.movies_recyclerview);
        moviesRecyclerView.setHasFixedSize(true); //it increases performance

        //use a linear layout manager
        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //add item decoration
        moviesRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        //set adapter
        moviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getApplicationContext(), results);
        moviesRecyclerView.setAdapter(moviesRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.popular_movies)
        {
            category = "popular";
            page = 1;
            makeNetworkCall(category, API_KEY, LANGUAGE, page);
            return  true;
        } else if( id == R.id.top_rated_movies) {
            category = "top_rated";
            page = 1;
            makeNetworkCall(category, API_KEY, LANGUAGE, page);
            return true;
        } else if( id == R.id.upcoming_movies) {
            category = "upcoming";
            page = 1;
            makeNetworkCall(category, API_KEY, LANGUAGE, page);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}