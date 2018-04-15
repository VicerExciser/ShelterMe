package edu.gatech.cs2340.shelterme.util;


import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.Shelter;

public class ShelterBuilder {

    private static final int DEFAULT_BED_COUNT = 50;
//    private BedManager bedManager;
    private final ThreadLocal<Shelter> newShelter;


    public ShelterBuilder(final Shelter shelter) {
        this.newShelter = new ThreadLocal<Shelter>() {
            @Override
            protected Shelter initialValue() {
                return shelter;
            }
        };
    }

    public void processRestrictions(String rs) {
//        bedManager = new BedManager(newShelter);
        Shelter shelter = newShelter.get();
        String cp = shelter.getCapacityStr();
        boolean fam;
        boolean anyone;
        boolean exMen;
        boolean exWomen;
        boolean exVets;
        boolean youngAdults;
        int ageFloor = Age.MIN_AGE.toInt();
        int ageCeiling = Age.MAX_AGE.toInt();
        String restrictionString = rs.toLowerCase();
        anyone = restrictionString.contains("anyone");
        if (anyone) {
            ageFloor = Age.MIN_AGE.toInt();
            ageCeiling = Age.MAX_AGE.toInt();
            parseCapacity(cp, true, false, false, ageFloor, ageCeiling, false);
            return;
        } else {
            fam = restrictionString.contains("families");
            exWomen = !fam && restrictionString.contains("women");
            exMen = !fam && !exWomen && restrictionString.contains("men");
        }
        if (exMen) {
            ageFloor = Age.ADULTS_BASE.toInt();
        }
        exVets = restrictionString.contains("veterans");
        if (exVets) {
            ageFloor = Age.ADULTS_BASE.toInt();
            ageCeiling = Age.MAX_AGE.toInt();
        }
        youngAdults = restrictionString.contains("young adults");
        if (restrictionString.contains("children") || restrictionString.contains("newborns")) {
            if (restrictionString.contains("newborns") || restrictionString.contains("under 5")
                    || exWomen || exMen) {
                ageFloor = Age.MIN_AGE.toInt();
                ageCeiling = Age.BABIES.toInt();
            } else {
                ageFloor = Age.CHILDREN_BASE.toInt();
                ageCeiling = Age.CHILDREN_CAP.toInt();
            }
            if (exWomen || exMen) {
                ageCeiling = Age.MAX_AGE.toInt();
                fam = true;
                if (cp.split(" ").length < 2) {
                    cp = (getInt(cp) / 4) + " apartment";
                }
            } else if (youngAdults) {
                ageCeiling = Age.YOUNG_ADULTS_CAP.toInt();
            }
        } else if (youngAdults) {
            ageFloor = Age.YOUNG_ADULTS_BASE.toInt();
            ageCeiling = Age.YOUNG_ADULTS_CAP.toInt();
        }

        parseCapacity(cp, fam, exMen, exWomen, ageFloor, ageCeiling, exVets);
    }



    // parse Capacity strings to extrapolate integers & bed types
    // make this set totalCapacity & call:
    //   addNewBeds(int numberOfSingleBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
    //       Age minAge, Age maxAge, boolean veteranOnly)
    private void parseCapacity(String cp, boolean fm, boolean mo, boolean wo, int mna, int mxa,
                               boolean vo) {
        String cp1 = cp;
        int singleBeds = 0;
        int familyBeds = 0;
        if ((cp1 != null) && !cp1.isEmpty() && !"N/A".equals(cp1)) {
            // split up Capacity strings by spaces
            cp1 = cp1.toLowerCase();
            String[] tokens = cp1.split(" ");
            int length = tokens.length;
            for (int i = 0; i < length; i++) {
                int val = getInt(tokens[i]);
                if (val > 0) {
                    if (length > 1) {
                        for (int j = 1; j <= 3; j++) {
                            if ((i + j) < length) {
                                if (tokens[i + j].contains("famil")) {
                                    familyBeds += val;
                                } else if (tokens[i + j].contains("singl")
                                        || tokens[i + j].contains("bed")) {
                                    singleBeds += val;
                                } else if (tokens[i + j].contains("apartment")) {
                                    familyBeds += val / 2;
                                    singleBeds += val * 2;
                                }
                            }
                        }
                    } else {
                        if (fm) {
                            familyBeds = val;
                        } else {
                            singleBeds = val;
                        }
                    }
                }
            }
        } else {
            if (fm) {
                familyBeds = DEFAULT_BED_COUNT;
            } else {
                singleBeds = DEFAULT_BED_COUNT;
            }
        }
        Age minAge;
        Age maxAge;

        if (mna == Age.MIN_AGE.toInt()) {
            minAge = Age.MIN_AGE;
        } else if (mna == Age.CHILDREN_BASE.toInt()) {
            minAge = Age.CHILDREN_BASE;
        } else if (mna == Age.YOUNG_ADULTS_BASE.toInt()) {
            minAge = Age.YOUNG_ADULTS_BASE;
        } else if (mna == Age.ADULTS_BASE.toInt()) {
            minAge = Age.ADULTS_BASE;
        } else {
            minAge = Age.MIN_AGE;
        }

        if (mxa == Age.BABIES.toInt()) {
            maxAge = Age.BABIES;
        } else if (mxa == Age.CHILDREN_CAP.toInt()) {
            maxAge = Age.CHILDREN_CAP;
        } else if (mxa == Age.YOUNG_ADULTS_CAP.toInt()) {
            maxAge = Age.YOUNG_ADULTS_CAP;
        } else if (mxa == Age.ADULTS_CAP.toInt()) {
            maxAge = Age.ADULTS_CAP;
        } else {
            maxAge = Age.MAX_AGE;
        }

//        this.bedManager = new BedManager(newShelter);
        Shelter shelterTemporary = newShelter.get();
        BedManager bedManager = shelterTemporary.getShelterBedManager();

        //this.setVacancies(singleBeds + familyBeds);
        if (singleBeds > 0) {
            bedManager.addNewBeds(singleBeds, false, mo, wo, minAge, maxAge, vo);
        }
        if (familyBeds > 0) {
            bedManager.addNewBeds(familyBeds, true, mo, wo, minAge, maxAge, vo);
        }
    }


    private int getInt(String text) {
        int val = -1;
        try {
            val = Integer.valueOf(text.trim());
        } catch (NumberFormatException ignored) {
        }
        return val;
    }

//    public BedManager getBedManager() {
//        return this.bedManager;
//    }
}
