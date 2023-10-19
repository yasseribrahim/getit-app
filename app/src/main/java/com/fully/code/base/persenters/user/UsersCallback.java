package com.fully.code.base.persenters.user;

import com.fully.code.base.models.User;
import com.fully.code.base.persenters.BaseCallback;

import java.util.List;

public interface UsersCallback extends BaseCallback {
    default void onGetUsersComplete(List<User> users) {
    }

    default void onSaveUserComplete() {
    }
    default void onGetUsersCountComplete(long teacherCount, long studentCount) {
    }

    default void onDeleteUserComplete(User user) {
    }

    default void onSignupUserComplete() {
    }

    default void onSignupUserFail(String message) {
    }

    default void onGetUserComplete(User user) {
    }
}
