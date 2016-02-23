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
 *  @date 2/23/16 12:21 PM
 *  @modified 2/23/16 12:21 PM
 */

package me.iz.mobility.rxjavasamples.rest;

import java.util.List;

import me.iz.mobility.rxjavasamples.models.Contributor;
import me.iz.mobility.rxjavasamples.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GitHubService {


    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(@Path("owner") String owner,
                                               @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributorsWithoutRx(@Path("owner") String owner,
                                               @Path("repo") String repo);

    @GET("/users/{user}")
    Observable<User> user(@Path("user") String user);
}