/*
 * This activity displays the Admin message queue of pending requests from Users (locked accounts)
 * and Employees (reporting malicious user accounts)
 *
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.MessageBoardModel;

public class MessageBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        ListView messageListView = (ListView) findViewById(R.id._messageListView);

        ArrayAdapter<Message> allMessageArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1,
                MessageBoardModel.messages);

        messageListView.setAdapter(allMessageArrayAdapter);

        ArrayAdapter<Message> unaddressedMessageArrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_1,
                MessageBoardModel.unaddressedMessages);
    }

}
