package com.tomer.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tomer.myapplication.rest.ImageSearchResult;
import com.tomer.myapplication.rest.PixabayService;
import com.tomer.myapplication.rest.ServiceGenerator;
import com.tomer.myapplication.ui.ImageAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText searchTermEditText;
    TextView resultTextView;
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTermEditText = findViewById(R.id.search_url_etv);
        resultTextView = findViewById(R.id.hits_tv);
        mRecyclerView = findViewById(R.id.list_rv);

        initRecycleView();
    }

    private void initRecycleView() {
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void searchKeyword(View view) {
        //get search term
        String searchTerm = searchTermEditText.getText().toString();

        //run request on Pixabay
        PixabayService pixabayService = ServiceGenerator.retrofit.create(PixabayService.class);
        String plusSeparatedSearchTerm = TextUtils.join("+", searchTerm.split(" "));
        Call<ImageSearchResult> call = pixabayService.
                getImageSearchResult(
                        ServiceGenerator.PIXABAY_KEY,
                        plusSeparatedSearchTerm,
                        ServiceGenerator.IMAGE_TYPE
                );

        call.enqueue(new Callback<ImageSearchResult>() {
            @Override
            public void onResponse(Call<ImageSearchResult> call, Response<ImageSearchResult> response) {
                if (response.body() == null) {
                    return;
                }

                mAdapter.setDataSource(response.body().getHits());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ImageSearchResult> call, Throwable t) {

            }
        });
    }
}
