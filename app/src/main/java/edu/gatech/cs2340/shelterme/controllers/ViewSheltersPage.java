package edu.gatech.cs2340.shelterme.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Account;
import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;
import edu.gatech.cs2340.shelterme.model.User;

public class ViewSheltersPage extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private HashMap<String, Shelter> shelters;
    private Model model = Model.getInstance();
//    private boolean critChecked;
    private AgeRange selectedAgeRange = AgeRange.ANYONE;
    private GenderAccepted selectedGender = GenderAccepted.ANY;
    private ListView shelterList;
    private User currentUser;


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

        shelterList = findViewById(R.id.shelterList);
        currentUser = ((User)model.getCurrUser());

        //default to registered criteria
//        final Button criteriaCheck = (Button) findViewById(R.id.yesCrit);
//        final Button noCrit = (Button) findViewById(R.id.noCrit);
        final Spinner ageSpin = (Spinner) findViewById(R.id.ageSpinner);
        final Spinner genderSpin = (Spinner) findViewById(R.id.genderSpinner);
        final CheckBox familyCheck = (CheckBox) findViewById(R.id.FamilyCheck);
//        final TextView text = (TextView) findViewById(R.id.inferCriteria);
//        ageSpin.setVisibility(View.GONE);
//        genderSpin.setVisibility(View.GONE);
//        familyCheck.setVisibility(View.GONE);


        //filling spinners
        ArrayAdapter<String> adapterAge = new ArrayAdapter(this,android.R.layout.simple_spinner_item, AgeRange.values());
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpin.setAdapter(adapterAge);

        ArrayAdapter<String> adapterGen = new ArrayAdapter(this,android.R.layout.simple_spinner_item, GenderAccepted.values());
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(adapterGen);

        // Omit shelters where this user is ineligible to stay
        shelters = new HashMap<>();
        for(Shelter s : Model.getShelterListPointer()) {
            if(currentUser != null && s.hasOpenBed(currentUser.generateKey()))
                shelters.put(s.getShelterName(), s);
            else if (currentUser == null && s.getVacancies() > 0) {
                Log.e("ViewShelterPage", "Current User returned NULL!");
                shelters.put(s.getShelterName(), s);
//                model.displayErrorMessage("Must be a registered user to do this!", this);
            }
        }
//        updateSearch(familyCheck.isChecked());
//        updateResults();

//        String[] listItems = new String[shelters.keySet().size()];
//        shelters.keySet().toArray(listItems);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
//        shelterList.setAdapter(adapter);

        adapter = new ArrayAdapter<>(ViewSheltersPage.this, R.layout.list_item, R.id.shelter_name,
                shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetailView(position);
            }
        });

        /* ~ Would implement if we wanted to use the User's registered attributes ~
        noCrit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ageSpin.setVisibility(View.VISIBLE);
                genderSpin.setVisibility(View.VISIBLE);
                //familyCheck.setVisibility(View.VISIBLE);
                criteriaCheck.setVisibility(View.GONE);
                noCrit.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                critChecked = false;

                selectedGender

                updateSearch((((User)model.getCurrUser()).getIsFamily()));
            }
        }); */

        /*
        // removing all shelters w/ no vacancies from the listing
        criteriaCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                critChecked = true;
                List<String> list = new ArrayList<>(shelters.keySet());
                for (int i = 0; i < shelters.size(); i++) {
                    if (((User) model.getCurrUser()) != null) {
                        if (!shelters.get(list.get(i)).hasOpenBed(((User) model.getCurrUser()).generateKey())) {
                            shelters.remove(shelters.get(list.get(i)));
                        }
                    }
                }
            }
        }); */

        familyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO: Incorporate other search criteria to be applied
                updateSearch(isChecked);
            }
        });

        ageSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedAgeRange = AgeRange.ANYONE;
                        break;
                    case 1:
                        selectedAgeRange = AgeRange.FAMWITHYOUNG;
                        break;
                    case 2:
                        selectedAgeRange = AgeRange.CHILDREN;
                        break;
                    case 3:
                        selectedAgeRange = AgeRange.YOUNGADULTS;
                        break;
                }
                updateSearch(familyCheck.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        genderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedGender = GenderAccepted.ANY;
                        break;
                    case 1:
                        selectedGender = GenderAccepted.MEN;
                        break;
                    case 2:
                        selectedGender = GenderAccepted.WOMEN;
                        break;
                }
                updateSearch(familyCheck.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        SearchView inputSearch = findViewById(R.id.inputSearch);
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

    private void updateSearch(boolean isChecked) {
        shelters.clear();
        for (Shelter s : Model.getShelterListPointer()) {
            for (String key : s.getBeds().keySet()) {
                if (familyChoiceMatchesKey(key, isChecked) && s.getVacancies() > 0)
                    if (genderChoiceMatchesKey(key) && ageRangeChoiceMatchesKey(key))
                        // Probably can omit the following if statement:
                        try {
                            if (s.hasOpenBed(currentUser.generateKey()))
                                shelters.put(s.getShelterName(), s);
                        } catch (NullPointerException npe) {
                            model.displayErrorMessage("Must be a registered user to do this!", this);
                            Log.e("Shelter Search Update", npe.getMessage());
                            npe.printStackTrace();
                        }
            }
        }
        updateResults();
    }

    private void updateResults() {
        adapter = new ArrayAdapter<>(ViewSheltersPage.this, R.layout.list_item, R.id.shelter_name,
                shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetailView(position);
            }
        });
    }

    private boolean familyChoiceMatchesKey(String key, boolean famChecked) {
        if (famChecked && key.charAt(0) == 'T')
            return true;
        else if (!famChecked && key.charAt(0) == 'F')
            return true;
        return false;
    }

    private boolean genderChoiceMatchesKey(String key) {
        if ((selectedGender.compareTo(GenderAccepted.MEN) == 0 && key.charAt(1) == 'T')
                || (selectedGender.compareTo(GenderAccepted.WOMEN) == 0 && key.charAt(2) == 'T'))
            return true;
        else if (selectedGender.compareTo(GenderAccepted.ANY) == 0
                && key.charAt(1) == 'F' && key.charAt(2) == 'F')
            return true;
        return false;

    }

    private boolean ageRangeChoiceMatchesKey(String key) {
        String min = key.substring(3,6) + "_";
        String max = key.substring(7,10) + "_";
        if ((selectedAgeRange.compareTo(AgeRange.ANYONE) == 0
                || selectedAgeRange.compareTo(AgeRange.FAMWITHYOUNG) == 0)
                && min.equals(Age.MINAGE)
                && max.equals(Age.MAXAGE))
            return true;
        else if (selectedAgeRange.compareTo(AgeRange.CHILDREN) == 0
                && min.equals(Age.CHILDREN_BASE)
                && max.equals(Age.CHILDREN_CAP))
            return true;
        else if (selectedAgeRange.compareTo(AgeRange.YOUNGADULTS) == 0
                && min.equals(Age.YOUNGADULTS_BASE)
                && max.equals(Age.YOUNGADULTS_CAP))
            return true;
        return false;
    }

    private void displayDetailView(int position) {
        Shelter selected = shelters.get(adapter.getItem(position));
        Intent intent = new Intent(ViewSheltersPage.this, ShelterDetailsPage.class);
        intent.putExtra("Shelter", selected);
        startActivity(intent);
    }

    private enum AgeRange {
        ANYONE("Any age"),
        FAMWITHYOUNG("Family w/ children under 5"),
        CHILDREN("Children (age 6 - 15)"),
        YOUNGADULTS("Young adults (age 16 - 25)");


        private final String _msg;
        AgeRange(String msg) { _msg = msg; }
        public String toString() { return _msg; }
    }

    private enum GenderAccepted {
        ANY("Any gender"),
        MEN("Men only"),
        WOMEN("Women only");

        private final String _msg;
        GenderAccepted(String msg) { _msg = msg; }
        public String toString() { return _msg; }
    }
/*
    private enum Accepting {
        ANY("Anyone"),
        MEN("Men only"),
        WOMEN("Women only"),
        CHILD("Children (age 6 - 15"),
        YA("Young Adults (age 16-25)"),
        WAC("Women & Children"),
        CAYA("Children & Young Adults"),
        VET("Veterans"),
        FAM("Families"),
        FAMCHILD("Families with Children under 5"),
        FAMNEWB("Families with Newborns");

    }
*/
}
