package com.daffodil.assignment.base;


import com.daffodil.assignment.network.ApiService;

public class BaseRepoImpl {
    protected ApiService mApiService;

    public BaseRepoImpl(ApiService apiService) {
        this.mApiService = apiService;
    }

}
