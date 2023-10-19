package com.fully.code.base.persenters.messaging;

import com.fully.code.base.models.Message;
import com.fully.code.base.persenters.BaseCallback;

public interface MessagingCallback extends BaseCallback {
    void onSendMessageSuccess();

    void onSendMessageFailure(String message);

    void onGetMessageSuccess(Message message);

    void onGetMessageFailure(String message);

    void onEmptyMessaging();
}
