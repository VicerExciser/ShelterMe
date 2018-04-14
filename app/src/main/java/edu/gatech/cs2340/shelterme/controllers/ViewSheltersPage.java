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

import java.util.AbstractMap;
import java.util.HashMap;

import edu.gatech.cs2340.shelterme.R;
import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.GenderAccepted;
import edu.gatech.cs2340.shelterme.model.Model;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class ViewSheltersPage extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private AbstractMap<String, Shelter> shelters;
//    private Model model = Model.getInstance();
    private boolean showAll = true;
    private AgeRange selectedAgeRange = AgeRange.ANYONE;
    private GenderAccepted selectedGender = GenderAccepted.ANY;
    private ListView shelterList;

    private CheckBox familyCheck;
    private CheckBox showAllCheck;

    private int updateCounter = 0;
    private final int updatesForInit = 2;
    // signifies initialization is complete (to be compared against updateCounter)
    private boolean saclAttached = false;
    // signifies initialization is complete (showAllCheck listener attached)
    private boolean ignoreUpdate = false;
    // to avoid unnecessary subroutine calls

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

        final Spinner ageSpin = findViewById(R.id.ageSpinner);
        final Spinner genderSpin = findViewById(R.id.genderSpinner);
        familyCheck = findViewById(R.id.FamilyCheck);
        showAllCheck = findViewById(R.id.showAll);

        showAllCheck.setChecked(true);
        showAllCheck.setOnCheckedChangeListener(null);

        //filling spinners
        //noinspection unchecked,unchecked
        ArrayAdapter<String> adapterAge = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, AgeRange.values());
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpin.setAdapter(adapterAge);

        //noinspection unchecked,unchecked
        ArrayAdapter<String> adapterGen = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, GenderAccepted.values());
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpin.setAdapter(adapterGen);

        // Omit shelters where this user is ineligible to stay  <- nah
        shelters = new HashMap<>(Model.getShelterListPointer());


        adapter = new ArrayAdapter<>(ViewSheltersPage.this, R.layout.list_item,
                R.id.shelter_name,
                shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetailView(position);
            }
        });

        ageSpin.setOnItemSelectedListener(ageItemSelect);
        genderSpin.setOnItemSelectedListener(genderItemSelect);

        SearchView inputSearch = findViewById(R.id.inputSearch);
        inputSearch.setQueryHint("Search by Name");
//        inputSearch.setSelected(true);
        inputSearch.setIconifiedByDefault(false);
        inputSearch.setSelected(false);
        inputSearch.setImeOptions(1);
        inputSearch.setOnQueryTextListener(queryListener);

        familyCheck.setOnCheckedChangeListener(famCheckListener);

        // To display all shelters by default:
        showAllCheck.post(new Runnable() {
            @Override
            public void run() {
                if (!showAllCheck.isChecked()) {
                    showAllCheck.setChecked(true);
                }
                showAllCheck.setOnCheckedChangeListener(showAllCheckListener);
                saclAttached = true;
                Log.e("ViewShelters runnable", "showAll should be checked");
            }
        });
    }

    private final AdapterView.OnItemSelectedListener ageItemSelect
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ignoreUpdate = true;
            if (showToggle()) {
                Log.e("ageSpinItemSelect", "showAllCheck toggled to "
                        +showAllCheck.isChecked() +", showAll = "+showAll);
            }
            switch (position) {
                case 0:
                    selectedAgeRange = AgeRange.ANYONE;
                    break;
                case 1:
                    selectedAgeRange = AgeRange.FAM_WITH_YOUNG;
                    break;
                case 2:
                    selectedAgeRange = AgeRange.CHILDREN;
                    break;
                case 3:
                    selectedAgeRange = AgeRange.YOUNG_ADULTS;
                    break;
                case 4:
                    selectedAgeRange = AgeRange.ADULTS;
                    break;
            }
            ignoreUpdate = false;
            updateSearch(familyCheck.isChecked());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    private final AdapterView.OnItemSelectedListener genderItemSelect
            = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ignoreUpdate = true;
            if (showToggle()) {
                Log.e("genderSpinItemSelect", "showAllCheck toggled to "
                        +showAllCheck.isChecked());
            }
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
            ignoreUpdate = false;
            updateSearch(familyCheck.isChecked());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    private final SearchView.OnQueryTextListener queryListener
            = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            ViewSheltersPage.this.adapter.getFilter().filter(query);
            if (showToggle()) {
                Log.e("onQueryTextSubmit", "showAllCheck toggled to "+
                        showAllCheck.isChecked());
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            ViewSheltersPage.this.adapter.getFilter().filter(newText);
            return false;
        }
    };

    private final CompoundButton.OnCheckedChangeListener famCheckListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                ignoreUpdate = true;
                if (showToggle()) {
                    Log.e("familyCheckListener", "showAllCheck toggled to "
                            + showAllCheck.isChecked() + ", showAll = " + showAll);
                }
                ignoreUpdate = false;
            }
            updateSearch(isChecked);
        }
    };

    private final CompoundButton.OnCheckedChangeListener showAllCheckListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            showAll = (updateCounter >= updatesForInit) ? isChecked : showAll;
            Log.e("showAllCheckListener", "showAll checked = "+showAll);
            if (isChecked && familyCheck.isChecked()) {
                familyCheck.toggle();
            } else {
                updateSearch(familyCheck.isChecked());
            }
        }
    };

    private boolean showToggle() {
        if (showAllCheck.isChecked() && saclAttached && (updateCounter >= updatesForInit)) {
            showAllCheck.toggle();
            return true;
        }
        return false;
    }


    private void updateSearch(boolean isChecked) {
        if (!ignoreUpdate) {
            shelters.clear();
            updateCounter++;
            Log.e("updateSearch", String.format("  update #%d  showAll = %b",
                    updateCounter, showAll));
            if (showAll) {
                for (Shelter s : Model.getShelterListPointer().values()) {
                    shelters.put(s.getShelterName(), s);
                }
            } else {
                for (Shelter s : Model.getShelterListPointer().values()) {
                    for (String key : s.getBeds().keySet()) {
                        if (key.length() > 1) {
//                        Log.e("ViewShelters", "key = " + key);
                            if (familyChoiceMatchesKey(key, isChecked) && (s.getVacancies() > 0)) {
                                if (genderChoiceMatchesKey(key) && ageRangeChoiceMatchesKey(key)) {
                                    shelters.put(s.getShelterName(), s);

                                }
                            }
                        }
                    }
                }
            }
            updateResults();
        }
    }

    private void updateResults() {
        adapter = new ArrayAdapter<>(ViewSheltersPage.this, R.layout.list_item,
                R.id.shelter_name,
                shelters.keySet().toArray(new String[shelters.keySet().size()]));
        shelterList.setAdapter(adapter);
        shelterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayDetailView(position);
            }
        });
    }

    private boolean familyChoiceMatchesKey(CharSequence key, boolean famChecked) {
        boolean match = false;
        if (famChecked && (key.charAt(0) == 'T')) {
            match = true;
        }
        else if ((!famChecked) && (key.charAt(0) == 'F'))
            //key.charAt(1) == 'F' &&  key.charAt(2) == 'F') // FFF000_200_F
        {
            match = true;
        }
        return match;
    }

    private boolean genderChoiceMatchesKey(CharSequence key) {
//        Log.e("ViewShelters", key);
        boolean match = false;
        if ((selectedGender.equals(GenderAccepted.MEN) && (key.charAt(1) == 'T'))
                ^ (selectedGender.equals(GenderAccepted.WOMEN) && (key.charAt(2) == 'T'))) {
            match = true;
        }
        else if (selectedGender.equals(GenderAccepted.ANY)
                /*||*/ && ((key.charAt(1) == 'F') && (key.charAt(2) == 'F'))) {
            match = true;
        }
        return match;

    }

    private boolean ageRangeChoiceMatchesKey(String key) {
        boolean match = false;
        int min = Integer.valueOf(key.substring(3,6));
        int max = Integer.valueOf(key.substring(7,10));
        if ((selectedAgeRange.equals(AgeRange.ANYONE))) {
            if ((min == Age.MIN_AGE.toInt()) && (max == Age.MAX_AGE.toInt())) {
                match = true;
            }
        }
        else if ((selectedAgeRange.equals(AgeRange.FAM_WITH_YOUNG))
                && (min == Age.MIN_AGE.toInt()) && (max >= Age.BABIES.toInt())) {
            match = true;
        }
        else if (selectedAgeRange.equals(AgeRange.CHILDREN)
                && ((min <= Age.CHILDREN_BASE.toInt())
                && (max >= Age.CHILDREN_CAP.toInt()))){
            match = true;}
        else if (selectedAgeRange.equals(AgeRange.YOUNG_ADULTS)
                && ((min <= Age.YOUNG_ADULTS_BASE.toInt())
                && (max >= Age.YOUNG_ADULTS_CAP.toInt()))) {
            match = true;
        }
        else if (selectedAgeRange.equals(AgeRange.ADULTS)
                && ((min <= Age.ADULTS_BASE.toInt())
                && (max >= Age.ADULTS_CAP.toInt()))) {
            match = true;
        }
        return match;
    }

    private void displayDetailView(int position) {
        Shelter selected = shelters.get(adapter.getItem(position));
        Intent intent = new Intent(ViewSheltersPage.this, ShelterDetailsPage.class);
        intent.putExtra("Shelter", selected);
        startActivity(intent);
    }

    private enum AgeRange {
        ANYONE("Any age"),
        FAM_WITH_YOUNG("Children under 5"),
        CHILDREN("Children (age 6 - 15)"),
        YOUNG_ADULTS("Young adults (age 16 - 25)"),
        ADULTS("Adults (25+)");


        private final String _msg;
        AgeRange(String msg) { _msg = msg; }
        public String toString() { return _msg; }
    }

//    private enum GenderAccepted {
//        ANY("Any gender"),
//        MEN("Men only"),
//        WOMEN("Women only");
//
//        private final String _msg;
//        GenderAccepted(String msg) { _msg = msg; }
//        public String toString() { return _msg; }
//    }

}
