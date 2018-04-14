/*
 * This activity lists User accounts for an Admin to manage.
 */
package edu.gatech.cs2340.shelterme.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;

import java.util.AbstractMap;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Model;

@SuppressWarnings("ALL")  // This activity has not been finished (ignore for inspection)
public class AccountManagement extends AppCompatActivity {

    // Customize this activity to suit full purposes; integrate Firebase realtime updates for
    // RecyclerView data

    // --Commented out by Inspection (4/13/2018 6:17 PM):private AccountAdapter accountAdapter;
    private AbstractMap<String, Account> dataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        RecyclerView accountRecyclerView = findViewById(R.id.recycler_account);

        dataSet = Model.getAccountListPointer();
        accountRecyclerView.setHasFixedSize(true);

        LinearLayoutManager accountLayoutManager = new LinearLayoutManager(this);
        accountLayoutManager.setReverseLayout(true);
        accountLayoutManager.setStackFromEnd(true);
        accountRecyclerView.setLayoutManager(accountLayoutManager);


        CheckBox showLockedAccounts = findViewById(R.id._showLockedCheckBox);
        showLockedAccounts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        SearchView accountSearchView = findViewById(R.id.account_search);
        accountSearchView.setQueryHint("Search by User Account");
        accountSearchView.setSelected(false);
        accountSearchView.setImeOptions(1);
        accountSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                updateFilter("Search", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
//                    updateFilter(null, null);
                } else {
//                    updateFilter("Search", newText);
                }
                return false;
            }
        });

//        updateFilter(null, null);
    }

// --Commented out by Inspection START (4/13/2018 6:17 PM):
//    private void updateFilter(String mode, String searchText) {
//        Account[] dataArray = new Account[dataSet.size()];
//        List<Account> dataList = new ArrayList<>(dataSet.values());
////        if (mode == null || searchText == null) {
////            dataSet = Model.getAccountListPointer();
////        } else if (mode.equals("Search")) {
////            Log.e("'Search' filter", "Now showing only results containing " + searchText);
////            Iterator<Message> iterator = dataList.iterator();
////            while (iterator.hasNext()) {
////                Account account = iterator.next();
////                if (!account.getEmail().contains(searchText) || ... ) {
////                    iterator.remove();
////                }
////            }
////        } else if (mode.equals("Locked") && searchText.equals("True")) {
////            Log.e("'Locked' filter", "Now displaying locked accounts as well");
////            Iterator<Message> iterator = dataList.iterator();
////            while (iterator.hasNext()) {
////                Message message = iterator.next();
////                if (message.isAccountLocked()) {
////                    iterator.remove();
////                }
////            }
////        } else {
////            dataSet = Model.getAccountListPointer();
////        }
////        Account[] dataArray = new Account[dataSet.size()];
////        List<Account> dataList = new ArrayList<>(dataSet.values());
////        Collections.sort(dataList, Collections.reverseOrder()); // display in descending order
////        dataArray = (Account[])(dataList.toArray(dataArray));
////        accountAdapter = new AccountAdapter(dataArray);
////        accountRecyclerView.setAdapter(accountAdapter);
////        Model.maintainAccounts(accountRecyclerView, accountAdapter);
//    }
// --Commented out by Inspection STOP (4/13/2018 6:17 PM)
}
