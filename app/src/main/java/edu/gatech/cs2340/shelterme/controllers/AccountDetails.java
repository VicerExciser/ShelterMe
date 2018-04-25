/*
 * From AccountManagement, this activity can be prompted for displaying a specific User account's
 * information.
 * Can also be used for resolving password reset requests, etc.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.UnlockRequest;
import edu.gatech.cs2340.shelterme.model.User;

/**
 * A pop-up window to display Account details for selected MessageBoard item.
 */
public class AccountDetails extends AppCompatActivity {
    String accountEmail, messageBody, timeSent;
    Account account;
    boolean status;
    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int)(dm.widthPixels * 0.9);
        int height = (int)(dm.heightPixels * 0.6);

        getWindow().setLayout(width, height);

        Intent intent = getIntent();
//        accountEmail = savedInstanceState.getString("SenderEmail");
        accountEmail = intent.getStringExtra("SenderEmail");
        TextView emailText = findViewById(R.id.emailText);
        emailText.setText(accountEmail);

//        messageBody = savedInstanceState.getString("MessageBody");
        messageBody = intent.getStringExtra("MessageBody");
        TextView messageText = findViewById(R.id.messageText);
        messageText.setText(messageBody);

//        timeSent = savedInstanceState.getString("TimeSent");
        timeSent = intent.getStringExtra("TimeSent");
        TextView timeSentText = findViewById(R.id.timeSentText);
        timeSentText.setText(timeSent);

        account = Model.getAccountByEmail(accountEmail);
        if (account == null) {
            Model.getInstance().displayErrorMessage("No account information found", this);
            return;
        }
        status = account.isAccountLocked();
        TextView statusText = findViewById(R.id.statusText);
        if (status)
            statusText.setText(R.string.account_status_locked);
        else
            statusText.setText(R.string.account_status_unlocked);

        message = Model.getMessageListPointer().get(timeSent);
        if (message == null) {
            Model.getInstance().displayErrorMessage("No message information found", this);
            return;
        }

        Button resolveButton = findViewById(R.id.resolveButton);
        resolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status) {
//                    UnlockRequest unlockReq = (UnlockRequest) message;
//                    Intent mail = unlockReq.resolve();
                    Intent mail = ((UnlockRequest)message).resolve();
                    Model.getInstance().displaySuccessMessage("User's account has been " +
                            "unlocked; sending new password to email now.", AccountDetails.this);
                    message.setAddressed(true);
//                    Model.getMessageListPointer().remove(timeSent);
//                    Intent.createChooser(mail, "Send email...");
                    startActivity(mail);
                } else {
//                    account.setAccountLocked(true);
                    Model.updateUserAccountStatus((User)account, true);
                    Model.getInstance().displaySuccessMessage("User has been banned; account" +
                            " locked.", AccountDetails.this);
                }
            }
        });

    }

}
