package com.example.tusharagarwal.githubprofile.controller;

import android.app.ProgressDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import android.widget.SearchView;

import android.widget.Toast;

import com.example.tusharagarwal.githubprofile.ItemAdapter;
import com.example.tusharagarwal.githubprofile.R;
import com.example.tusharagarwal.githubprofile.api.Client;
import com.example.tusharagarwal.githubprofile.api.Service;
import com.example.tusharagarwal.githubprofile.model.Item;
import com.example.tusharagarwal.githubprofile.model.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ProgressDialog pd;

    String text="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initViews(){
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching Github Users...");
        pd.setCancelable(true);
        pd.show();

        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();
    }
                                         //getting the data from api response//

    private void loadJSON(){



        try{
            Client Client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<ItemResponse> call = apiService.getItems( "/search/users?q="+text);
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    if(items.size()==0)
                    {
                        Toast.makeText(MainActivity.this,"No  such user",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {

                        recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));
                        recyclerView.smoothScrollToPosition(0);
                    }

                    pd.hide();
                }

                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    pd.hide();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            pd.hide();
        }



    }

    /*
        Search bar is created after oncreate method
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem menuItem=menu.findItem(R.id.main_search);
        final SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search users");                  //setting the placeholder
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                 text=s;
                initViews();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        return true;
    }

}