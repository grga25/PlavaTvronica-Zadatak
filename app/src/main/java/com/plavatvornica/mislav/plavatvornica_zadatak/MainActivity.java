package com.plavatvornica.mislav.plavatvornica_zadatak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plavatvornica.mislav.plavatvornica_zadatak.adapters.RecyclerViewAdapter;
import com.plavatvornica.mislav.plavatvornica_zadatak.database.DatabaseHandler;
import com.plavatvornica.mislav.plavatvornica_zadatak.dialogs.MyDialogFragment;
import com.plavatvornica.mislav.plavatvornica_zadatak.models.Article;
import com.plavatvornica.mislav.plavatvornica_zadatak.models.ResponseBody;
import com.plavatvornica.mislav.plavatvornica_zadatak.rest.ApiEndpointInterface;
import com.plavatvornica.mislav.plavatvornica_zadatak.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements retrofit2.Callback<ResponseBody>, RecyclerViewAdapter.OnItemClickListener {

    private ArrayList<Article> articleList;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private static final String DIALOG_TAG = "dialog";
    private static final String BASE_URL = "https://newsapi.org/";
    DatabaseHandler db;
    ProgressDialog mProgressDialog;
    private static final long FIVE_MINUTES = 1000 * 60 * 5; //ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        db = new DatabaseHandler(this);

        //check if user have internet connection
        boolean haveInternetConntection = Utils.checkInternetConnection(this);

        //sending request
        if ((db.getArticlesCount() == 0 || needUpdate()) && haveInternetConntection) {
            db.deleteAllData();
            sendRequest();
            showProgressDialog();
        }
        //read data from database
        else {
            if (!haveInternetConntection) {
                showAlertDialog(R.string.alert_dialog_no_internet);
            }
            adapter = new RecyclerViewAdapter(this, db.getAllArticles(), this, false);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            articleList = new ArrayList<>();
            articleList = response.body().getArticles();
            adapter = new RecyclerViewAdapter(this, articleList, this, true);
            recyclerView.setAdapter(adapter);

            //save all data to database
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    for (Article article : articleList) {
                        try {
                            db.addArticle(article,
                                    Utils.getBytes(Glide.with(MainActivity.this).load(article.getUrlToImage()).asBitmap().into(360, 200).get()));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            showAlertDialog(R.string.alert_dialog_error);
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        showAlertDialog(R.string.alert_dialog_error);
    }

    private boolean needUpdate() {
        if ((System.currentTimeMillis() - db.getTimeStamp(1)) > FIVE_MINUTES) {
            return true;
        } else {
            return false;
        }
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getText(R.string.progress_dialog_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    void sendRequest() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiEndpointInterface getRequest = retrofit.create(ApiEndpointInterface.class);
        Call<ResponseBody> call = getRequest.getResponse();
        call.enqueue(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void showAlertDialog(int message) {
        MyDialogFragment dialogFragment = MyDialogFragment.newInstance(message);
        dialogFragment.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(this, SingleNewsActivity.class);
        i.putExtra(SingleNewsActivity.KEY_POSITION, position);
        startActivity(i);
    }
}
