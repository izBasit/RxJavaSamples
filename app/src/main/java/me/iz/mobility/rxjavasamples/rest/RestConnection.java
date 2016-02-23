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
 *  @date 2/23/16 12:10 PM
 *  @modified 1/25/16 1:02 PM
 */

package me.iz.mobility.rxjavasamples.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Create Rest Connection object to call rest API's from server
 */
public class RestConnection {

    private static Retrofit restAdapter = null;

    private final static String baseUrl = "https://api.github.com";

    /**
     * Create rest Adapter object to call CarIQ Api Server
     *
     * @return RestAdapter Object
     */
    private static Retrofit getRestAdapter() {

        if (restAdapter == null) {
            restAdapter = connectGitHub();
        }
        return restAdapter;
    }

    public static GitHubService createGitHubService() {
        return getRestAdapter().create(GitHubService.class);
    }


    /**
     * This method internally called by getRestAdapter method
     *
     * @return RestAdapter Object
     */
    private static Retrofit connectGitHub() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .disableHtmlEscaping().create();

        final GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

}