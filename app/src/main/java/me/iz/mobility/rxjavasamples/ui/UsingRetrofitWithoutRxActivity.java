/*
 * Copyright 2016 CarIQ Technologies Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *  @author Basit Parkar
 *  @date 2/23/16 1:27 PM
 *  @modified 2/23/16 1:27 PM
 */

package me.iz.mobility.rxjavasamples.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import hugo.weaving.DebugLog;
import me.iz.mobility.rxjavasamples.R;
import me.iz.mobility.rxjavasamples.models.Contributor;
import me.iz.mobility.rxjavasamples.rest.GitHubService;
import me.iz.mobility.rxjavasamples.rest.RestConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UsingRetrofitWithoutRxActivity extends AppCompatActivity {

    private GitHubService gitHubService = RestConnection.createGitHubService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_retrofit_without_rx);

        new MyAsyncTask().execute();

        AsyncCall();
    }


    private class MyAsyncTask extends AsyncTask<Void,Void,Void> {

        @DebugLog
        @Override
        protected Void doInBackground(Void... params) {



            Call<List<Contributor>> contributorCall = gitHubService.contributorsWithoutRx("netflix", "rxjava");
            Response<List<Contributor>> contributorList = null;
            try {
                contributorList = contributorCall.execute();
                for (Contributor c : contributorList.body()) {
                    Timber.v(c.login + '\t' + c.contributions);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            return null;

        }
    }

    @DebugLog
    private void AsyncCall() {

        Call<List<Contributor>> contributorCall = gitHubService.contributorsWithoutRx("netflix", "rxjava");
        contributorCall.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> contributorList) {
                Timber.d("--------------------------------------------------------------------------");
                for (Contributor c : contributorList.body()) {
                    Timber.i(c.login + '\t' + c.contributions);
                }

            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                Timber.w(t.getMessage());

            }
        });
    }
}
