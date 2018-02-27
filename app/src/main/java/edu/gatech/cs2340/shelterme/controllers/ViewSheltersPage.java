package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.HashMap;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class ViewSheltersPage extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private HashMap<String, Shelter> shelters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shelters_page);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewSheltersPage.this, HomePage.class));
            }
        });

        shelters = new HashMap<>();
        for (Shelter s : Model.getShelterListPointer()) {
            shelters.put(s.getShelterName(), s);
        }

        ListView shelterList = findViewById(R.id.shelterList);
        SearchView inputSearch = findViewById(R.id.inputSearch);
        adapter = new ArrayAdapter<>(ViewSheltersPage.this, R.layout.list_item, R.id.shelter_name,
                 shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetailView(position);
            }
        });
        inputSearch.setQueryHint("Search by Name");
//        inputSearch.setSelected(true);
        inputSearch.setIconifiedByDefault(false);
        inputSearch.setSelected(false);
        inputSearch.setImeOptions(1);
        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ViewSheltersPage.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ViewSheltersPage.this.adapter.getFilter().filter(newText);
                return false;
            }
        });
        // Enabling the search filter
//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // When user changed the Text
//                ViewSheltersPage.this.adapter.getFilter().filter(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void displayDetailView(int position) {
        Shelter selected = shelters.get(adapter.getItem(position));
        Intent intent = new Intent(ViewSheltersPage.this, ShelterDetailsPage.class);
        intent.putExtra("Shelter", selected);
        startActivity(intent);
    }
}
