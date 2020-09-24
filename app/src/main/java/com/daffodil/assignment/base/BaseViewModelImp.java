package com.daffodil.assignment.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class BaseViewModelImp extends AndroidViewModel  {
    public BaseViewModelImp(@NonNull Application application) {
        super(application);
    }

    public interface ViewModelCallback<T> {

        void onSuccess(T data);

        void onError(ErrorResponse errorResponse);
    }

}
