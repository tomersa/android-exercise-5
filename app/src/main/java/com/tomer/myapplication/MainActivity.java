package com.tomer.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    TextView searchTV;

    private LinearLayoutManager mLayoutManager;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTermEditText = findViewById(R.id.search_url_etv);
        resultTextView = findViewById(R.id.hits_tv);
        mRecyclerView = findViewById(R.id.list_rv);
        searchTV = findViewById(R.id.search_tv);

        initRecycleView();

        searchTermEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ServiceGenerator.currentPage = 1;
                searchTV.setText(getResources().getString(R.string.search));
            }
        });
    }

    private void initRecycleView() {
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    public void searchKeyword(View view) {
        resultTextView.setText(getResources().getText(R.string.status_searching));

        //get search term
        final String searchTerm = searchTermEditText.getText().toString();

        ServiceGenerator.currentPage++;

        searchTV.setText(getResources().getString(R.string.search_next));


        //run request on Pixabay
        PixabayService pixabayService = ServiceGenerator.retrofit.create(PixabayService.class);
        String plusSeparatedSearchTerm = TextUtils.join("+", searchTerm.split(" "));
        Call<ImageSearchResult> call = pixabayService.
                getImageSearchResult(plusSeparatedSearchTerm, ServiceGenerator.currentPage);

        call.enqueue(new Callback<ImageSearchResult>() {
            @Override
            public void onResponse(Call<ImageSearchResult> call, Response<ImageSearchResult> response) {
                if (response.body() == null) {
                    return;
                }

                resultTextView.
                        setText(String.format(getResources().getText(R.string.
                                                                        results_found_status).toString(),
                                              response.body().getTotalHits(),
                                              searchTerm));

                mAdapter.setDataSource(response.body().getHits());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ImageSearchResult> call, Throwable t) {
                resultTextView.setText(getResources().getText(R.string.error_status));
            }
        });
    }
}
