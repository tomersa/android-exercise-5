package com.tomer.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomer.myapplication.rest.ImageSearchResult;
import com.tomer.myapplication.rest.ImageSearchResult;
import com.tomer.myapplication.rest.PixabayService;
import com.tomer.myapplication.rest.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText searchTermEditText;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTermEditText = findViewById(R.id.search_url_etv);
        resultTextView = findViewById(R.id.hits_tv);
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

                ImageSearchResult data = response.body();
                Toast.makeText(getApplicationContext(), Integer.toString(data.getTotal()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ImageSearchResult> call, Throwable t) {

            }
        });

        //add result to result_tv
    }
}
