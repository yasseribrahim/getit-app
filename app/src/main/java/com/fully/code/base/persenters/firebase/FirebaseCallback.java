package com.fully.code.base.persenters.firebase;

import com.fully.code.base.persenters.BaseCallback;

public interface FirebaseCallback extends BaseCallback {
    default void onSaveTokenComplete() {
    }

    default void onGetTokenComplete(String token) {
    }
}
