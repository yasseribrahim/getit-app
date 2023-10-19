package com.fully.code.base.persenters.messaging;

import android.util.Log;

import com.fully.code.base.Constants;
import com.fully.code.base.models.ChatId;
import com.fully.code.base.models.Message;
import com.fully.code.base.models.PushMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagingPresenter {
    private static final String TAG = MessagingPresenter.class.getSimpleName();

    private final DatabaseReference reference;

    public MessagingPresenter() {
        reference = FirebaseDatabase.getInstance().getReference().child(Constants.NODE_NAME_MESSAGING).getRef();
    }

    public void sendMessage(ChatId id, final Message message, MessagingCallback callback) {
        String room = id.toString();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PushMessage pushMessage = new PushMessage(message.getSenderId(), message.getSenderName(), message.getReceiveName(), message.getMessage());
                reference.child(room).push().setValue(pushMessage);
                Log.i(TAG, "sendMessageToFirebaseUser: success");
                // send push notification to the receiver
                callback.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());
            }
        });
    }

    public void getMessages(ChatId id, MessagingCallback callback) {
        String room = id.toString();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(room)) {
                    Log.i(TAG, "getMessageFromFirebaseUser: no such room available");
                    callback.onEmptyMessaging();
                    callback.onHideLoading();
                }
                Log.i(TAG, "getMessageFromFirebaseUser: " + room + " exists");
                reference.child(room).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        callback.onGetMessageSuccess(message);
                        callback.onHideLoading();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onGetMessageFailure("Unable to get message: " + databaseError.getMessage());
                        callback.onHideLoading();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onGetMessageFailure("Unable to get message: " + databaseError.getMessage());
                callback.onHideLoading();
            }
        });
    }
}
