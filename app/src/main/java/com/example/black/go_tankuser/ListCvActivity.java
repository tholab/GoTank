package com.example.black.go_tankuser;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.black.go_tankuser.adapter.CompanyAdapter;
import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.service.CompanyService;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCvActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Company> companies = new ArrayList<>();
    private CompanyService companyService = ApiUtils.getCompanyService();

    private CompanyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cv);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadData();
    }

    private void loadData(){
        companyService.all().enqueue(new Callback<WrappedListResponse<Company>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<Company>> call, Response<WrappedListResponse<Company>> response) {
                if (response.isSuccessful()){
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        companies = body.getData();
                        recyclerView.setAdapter(new CompanyAdapter(companies, ListCvActivity.this));
                    }else {
                        //Belum ada data
                        Toast.makeText(ListCvActivity.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("Gotank", response.message());
                    Toast.makeText(ListCvActivity.this, "Failed to retrieve data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<Company>> call, Throwable t) {
                Log.d("Gotank", t.getMessage());
                Toast.makeText(ListCvActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Cari Jasa Sedot WC...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 0){
                    search(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().length() == 0){
                    loadData();
                }
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false,false);
        return true;
    }

    private void search(String query){
        companyService.search(query).enqueue(new Callback<WrappedListResponse<Company>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<Company>> call, Response<WrappedListResponse<Company>> response) {
                if (response.isSuccessful()){
                    WrappedListResponse body = response.body();
                    if (body != null && body.getStatus() == 1){
                        companies = body.getData();
//                        recyclerView.setAdapter(new CompanyAdapter(companies, ListCvActivity.this));
                        mAdapter = new CompanyAdapter(companies,ListCvActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    }else {
                        //Belum ada data
                        Toast.makeText(ListCvActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d("Gotank", response.message());
                    Toast.makeText(ListCvActivity.this, "Failed to retrieve data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<Company>> call, Throwable t) {
                Log.d("Gotank", t.getMessage());
                Toast.makeText(ListCvActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
