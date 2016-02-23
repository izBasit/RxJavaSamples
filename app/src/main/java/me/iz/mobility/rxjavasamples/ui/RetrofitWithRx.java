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
 *  @date 2/23/16 3:09 PM
 *  @modified 2/23/16 3:09 PM
 */

package me.iz.mobility.rxjavasamples.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import hugo.weaving.DebugLog;
import me.iz.mobility.rxjavasamples.R;
import me.iz.mobility.rxjavasamples.models.Contributor;
import me.iz.mobility.rxjavasamples.rest.GitHubService;
import me.iz.mobility.rxjavasamples.rest.RestConnection;
import me.iz.mobility.rxjavasamples.utils.RxUtils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class RetrofitWithRx extends AppCompatActivity {

    private GitHubService gitHubService = RestConnection.createGitHubService();

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_with_rx);


//        getContributors();

//        getContributorsWithLambda();
        getAllUsers();

//        getNonNullUsers();
    }

    @DebugLog
    private void getContributors() {
        subscriptions.add(
                gitHubService.contributors("netflix", "rxjava")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Contributor>>() {
                            @Override
                            public void onCompleted() {
                                Timber.d("Retrofit call 1 completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e, "woops we got an error while getting the list of contributors");
                            }

                            @Override
                            public void onNext(List<Contributor> contributors) {
                                for (Contributor c : contributors) {
                                    Timber.v(c.login + '\t' + c.contributions);
                                }
                            }
                        })
        );
    }

    @DebugLog
    private void getContributorsWithLambda() {

        subscriptions.add(
                gitHubService.contributors("netflix", "rxjava")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(contributors -> {
                            for (Contributor c : contributors) {
                                Timber.d(c.login + '\t' + c.contributions);
                            }
                        })
        );
    }

    @DebugLog
    private void getAllUsers() {

//        subscriptions.add(
        gitHubService.contributors("netflix", "rxjava")
                .flatMap(contributors -> Observable.from(contributors))
                .flatMap(contributor -> gitHubService.user(contributor.login))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .forEach(user -> Timber.d(user.name + '\t' + user.email));
    }

    @DebugLog
    private void getNonNullUsers() {

//        subscriptions.add(
        gitHubService.contributors("netflix", "rxjava")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(contributors -> Observable.from(contributors))
                .flatMap(contributor ->gitHubService.user(contributor.login).subscribeOn(Schedulers.io()))
                .filter(user -> user.name != null)
                .forEach(user -> Timber.d(user.name + '\t' + user.email));
    }


    @Override
    public void onResume() {
        super.onResume();
        subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscriptions);
    }


}
