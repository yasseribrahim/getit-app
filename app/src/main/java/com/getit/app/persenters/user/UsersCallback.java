package com.getit.app.persenters.user;

import com.getit.app.models.User;
import com.getit.app.persenters.BaseCallback;

import java.util.List;

public interface UsersCallback extends BaseCallback {
    default void onGetUsersComplete(List<User> users) {
    }

    default void onSaveUserComplete() {
    }
    default void onGetUsersCountComplete(long teacherCount, long studentCount) {
    }

    default void onDeleteUserComplete(int position) {
    }

    default void onSignupUserComplete() {
    }

    default void onSignupUserFail(String message) {
    }

    default void onGetUserComplete(User user) {
    }
}
