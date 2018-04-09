/*
 * This activity displays the Admin message queue of pending requests from Users (locked accounts)
 * and Employees (reporting malicious user accounts)
 *
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.MessageBoardModel;

public class MessageBoard extends AppCompatActivity {

    private ListView messageListView;
    private CheckBox showAddressedCheckBox;

    private ArrayAdapter<Message> allMessageArrayAdapter;
    private ArrayAdapter<Message> unaddressedMessageArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        messageListView = (ListView) findViewById(R.id._messageListView);
        showAddressedCheckBox = (CheckBox) findViewById(R.id._showAddressedCheckBox);

        allMessageArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1,
                MessageBoardModel.messages);
        unaddressedMessageArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1,
                MessageBoardModel.unaddressedMessages);

        messageListView.setAdapter(allMessageArrayAdapter);
    }

    public void onCheckBoxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if (isChecked) {
            showUnaddressedOnly();
        } else {
            showAddressed();
        }
    }

    private void showAddressed() {
        messageListView.setAdapter(allMessageArrayAdapter);
    }

    private void showUnaddressedOnly() {
        messageListView.setAdapter(unaddressedMessageArrayAdapter);
    }

}
