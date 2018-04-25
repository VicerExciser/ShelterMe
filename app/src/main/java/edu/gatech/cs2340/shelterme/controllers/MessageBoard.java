/*
 * This activity displays the Admin message queue of pending requests from Users (locked accounts)
 * and Employees (reporting malicious user accounts)
 *
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Message;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.util.MessageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The type Message board.
 */
public class MessageBoard extends AppCompatActivity {

    private RecyclerView messageRecyclerView;
    private Map<String, Message> dataSet;
    MessageAdapter messageAdapter;
//    int filterUpdatesBeforeChange = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        messageRecyclerView = findViewById(R.id.recycler_message);
        Model.initMessages();

        // Just for testing adding a Message to DB
//        Model model = Model.getInstance();
//        model.testMessage();

        dataSet = Model.getMessageListPointer();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        messageRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager messageLayoutManager = new LinearLayoutManager(this);
        messageLayoutManager.setReverseLayout(true);
        messageLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(messageLayoutManager);

        // specify an adapter
//        messageAdapter = new MessageAdapter(dataSet);
//        messageRecyclerView.setAdapter(messageAdapter);
//        Model.maintainMessages(messageRecyclerView, messageAdapter);

//--------------------------------------------------------------------------------------------------

        CheckBox showAddressedCheckBox = findViewById(R.id._showAddressedCheckBox);
//        showAddressedCheckBox.setChecked(true);
        showAddressedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    showUnaddressedOnly();
//                    MessageBoard.this.messageAdapter.filter("Addressed", "False");
//                    if (filterUpdatesBeforeChange <= 0)
                        updateFilter("Addressed", "False");
                } else {
//                    showAddressed();
//                    MessageBoard.this.messageAdapter.filter("Addressed", "True");
                    updateFilter("Addressed", "True");
                }
            }
        });

        SearchView messageSearchView = findViewById(R.id.message_search);
        messageSearchView.setQueryHint("Search by Sender's Email");
//        messageSearchView.setIconifiedByDefault(false);
        messageSearchView.setSelected(false);
        messageSearchView.setImeOptions(1);
        messageSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                MessageBoard.this.messageAdapter.filter("Search", query);
                updateFilter("Search", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
//                    MessageBoard.this.messageAdapter.filter(null, null);
                    updateFilter(null, null);
                } else {
//                    MessageBoard.this.messageAdapter.filter("Search", newText);
                    updateFilter("Search", newText);
                }
                return false;
            }
        });

//        MessageBoard.this.messageAdapter.filter(null, null);

//        updateFilter(null, null);
//        messageAdapter.notifyDataSetChanged();

        dataSet = Model.getMessageListPointer();
        Message[] dataArray = new Message[dataSet.size()];
        List<Message> dataList = new ArrayList<>(dataSet.values());
        Collections.sort(dataList, Collections.reverseOrder()); // display in descending order
        dataArray = dataList.toArray(dataArray);
        messageAdapter = new MessageAdapter(dataArray);
        messageRecyclerView.setAdapter(messageAdapter);
        Model.maintainMessages(messageRecyclerView, messageAdapter);
        messageAdapter.notifyDataSetChanged();
    }

    private void updateFilter(String mode, String searchText) {
        Message[] dataArray = new Message[dataSet.size()];
        List<Message> dataList = new ArrayList<>(dataSet.values());
        if ((mode == null) || (searchText == null)) {
            dataSet = Model.getMessageListPointer();
        } else if ("Search".equals(mode)) {
            Log.e("'Search' filter", "Now showing only results containing " + searchText);
//            dataArray = new Message[dataSet.size()];
//            dataList = new ArrayList<>(dataSet.values());
            Iterator<Message> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                String email = message.getSenderEmail();
                if (!email.contains(searchText)) {
                    iterator.remove();
                }
            }
        } else if ("Addressed".equals(mode) && "False".equals(searchText)) {
            Log.e("'Addressed' filter", "Now showing only unaddressed messages");
//            dataArray = new Message[dataSet.size()];
//            dataList = new ArrayList<>(dataSet.values());
            Iterator<Message> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                Message message = iterator.next();
                if (message.isAddressed()) {
                    iterator.remove();
                }
            }
        } else {
            dataSet = Model.getMessageListPointer();
        }
        Collections.sort(dataList, Collections.reverseOrder()); // display in descending order
        dataArray = dataList.toArray(dataArray);
        messageAdapter = new MessageAdapter(dataArray);
        messageRecyclerView.setAdapter(messageAdapter);
        Model.maintainMessages(messageRecyclerView, messageAdapter);
        messageAdapter.notifyDataSetChanged();
//        filterUpdatesBeforeChange--;
    }


//    private void showAddressed() {
////        messageListView.setAdapter(allMessageArrayAdapter);
//        messageAdapter.filter("Addressed", "True");
//    }
//
//    private void showUnaddressedOnly() {
////        messageListView.setAdapter(unaddressedMessageArrayAdapter);
//        messageAdapter.filter("Addressed", "False");
//    }

}
