/*
 * From AccountManagement, this activity can be prompted for displaying a specific User account's
 * information.
 * Can also be used for resolving password reset requests, etc.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.ReportUserRequest;
import edu.gatech.cs2340.shelterme.model.UnlockRequest;
import edu.gatech.cs2340.shelterme.model.User;

/**
 * A pop-up window to display Account details for selected MessageBoard item.
 */
public class AccountDetails extends AppCompatActivity {
    String accountEmail, messageBody, timeSent;
    Account account;
    boolean accountIsLocked;
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
        accountEmail = intent.getStringExtra("SenderEmail");
        TextView emailText = findViewById(R.id.emailText);
        emailText.setText(accountEmail);

        messageBody = intent.getStringExtra("MessageBody");
        TextView messageText = findViewById(R.id.messageText);
        messageText.setText(messageBody);

        timeSent = intent.getStringExtra("TimeSent");
        TextView timeSentText = findViewById(R.id.timeSentText);
        timeSentText.setText(timeSent);

        account = Model.getAccountByEmail(accountEmail);
        if (account == null) {
            Model.getInstance().displayErrorMessage("No account information found", this);
            return;
        }
        accountIsLocked = account.isAccountLocked();
        TextView statusText = findViewById(R.id.statusText);
        if (accountIsLocked)
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
                boolean shouldUnlockAccount = (message.isAccountUnlockRequested() && accountIsLocked);
                Intent mail;
                if  (shouldUnlockAccount) {
                    mail = ((UnlockRequest)message).resolve();
                    Model.getInstance().displaySuccessMessage("User's account has been " +
                            "unlocked; sending new password to email now.", AccountDetails.this);
//                    message.setAddressed(true);
                    startActivity(mail);
                } else if (!shouldUnlockAccount) {
                    mail = ((ReportUserRequest)message).resolve();
//                    account.setAccountLocked(true);
                    Model.updateUserAccountStatus((User)account, true);
//                    Model.markMessageAsAddressed(this);
                    message.markAsAddressed();
                    Model.getInstance().displaySuccessMessage("User has been banned; account" +
                            " locked.", AccountDetails.this);
                }
            }
        });

    }

}
