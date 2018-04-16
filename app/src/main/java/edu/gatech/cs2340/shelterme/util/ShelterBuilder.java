package edu.gatech.cs2340.shelterme.util;


import edu.gatech.cs2340.shelterme.model.Age;
import edu.gatech.cs2340.shelterme.model.CapacityStruct;
import edu.gatech.cs2340.shelterme.model.Shelter;

/**
 * The type Shelter builder.
 */
public class ShelterBuilder {

    private static final int DEFAULT_BED_COUNT = 50;
//    private BedManager bedManager;
    private final ThreadLocal<Shelter> newShelter;


    /**
     * Instantiates a new Shelter builder.
     *
     * @param shelter the shelter
     */
    public ShelterBuilder(final Shelter shelter) {
        this.newShelter = new ThreadLocal<Shelter>() {
            @Override
            protected Shelter initialValue() {
                return shelter;
            }
        };
    }


    /**
     * Process shelter restrictions.
     *
     * @param rs the restrictions string
     */
    public void processRestrictions(String rs) {
//        bedManager = new BedManager(newShelter);
        Shelter shelter = newShelter.get();
        CapacityStruct parameters = new CapacityStruct();
        parameters.shelterName = shelter.getShelterName();
        String cp = shelter.getCapacityStr();
        boolean fam;
        boolean exMen;
        boolean exWomen;
        boolean exVets;
        boolean youngAdults;
        int ageFloor = Age.MIN_AGE.toInt();
        int ageCeiling = Age.MAX_AGE.toInt();
        String restrictionString = rs.toLowerCase();
        if (restrictionString.contains("anyone")) {
            parameters.cap = cp;
            parameters.family = true;
            parameters.menOnly = false;
            parameters.vetsOnly = false;
            parameters.womenOnly = false;
            parameters.minimumAge = ageFloor;
            parameters.maximumAge = ageCeiling;
            parseCapacity(parameters);
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

        parameters.cap = cp;
        parameters.family = fam;
        parameters.menOnly = exMen;
        parameters.vetsOnly = exVets;
        parameters.womenOnly = exWomen;
        parameters.minimumAge = ageFloor;
        parameters.maximumAge = ageCeiling;
        parseCapacity(parameters);
//        parseCapacity(cp, fam, exMen, exWomen, ageFloor, ageCeiling, exVets);
    }



    // parse Capacity strings to extrapolate integers & bed types
    // make this set totalCapacity & call:
    //   addNewBeds(int numberOfSingleBeds, boolean isFamily, boolean menOnly, boolean womenOnly,
    //       Age minAge, Age maxAge, boolean veteranOnly)
//    private void parseCapacity(String cp, boolean fm, boolean mo, boolean wo, int mna, int mxa,
//                               boolean vo) {
    private void parseCapacity(CapacityStruct p) {
        String cp = p.cap;
        if ((cp != null) && !cp.isEmpty() && !"N/A".equals(cp)) {
            // split up Capacity strings by spaces
            cp = cp.toLowerCase();
            String[] tokens = cp.split(" ");
            int length = tokens.length;
            for (int i = 0; i < length; i++) {
                int val = getInt(tokens[i]);
                if (val > 0) {
                    if (length > 1) {
                        for (int j = 1; j <= 3; j++) {
                            if ((i + j) < length) {
                                if (tokens[i + j].contains("famil")) {
                                    p.familyBeds += val;
                                } else if (tokens[i + j].contains("singl")
                                        || tokens[i + j].contains("bed")) {
                                    p.singleBeds += val;
                                } else if (tokens[i + j].contains("apartment")) {
                                    p.familyBeds += val / 2;
                                    p.singleBeds += val * 2;
                                }
                            }
                        }
                    } else {
                        if (p.family) {
                            p.familyBeds = val;
                        } else {
                            p.singleBeds = val;
                        }
                    }
                }
            }
        } else {
            if (p.family) {
                p.familyBeds = DEFAULT_BED_COUNT;
            } else {
                p.singleBeds = DEFAULT_BED_COUNT;
            }
        }

        decipherMinAge(p);
        decipherMaxAge(p);

        Shelter shelterTemporary = newShelter.get();
        BedManager bedManager = shelterTemporary.getShelterBedManager();

        //this.setVacancies(singleBeds + familyBeds);
//        if (singleBeds > 0) {
//            bedManager.addNewBeds(singleBeds, false, mo, wo, minAge, maxAge, vo);

//        }
//        if (familyBeds > 0) {
////            bedManager.addNewBeds(familyBeds, true, mo, wo, minAge, maxAge, vo);
//        }
        if (p.singleBeds > 0) {
            bedManager.addNewBeds(p, p.singleBeds);
        }
        if (p.familyBeds > 0) {
            bedManager.addNewBeds(p, p.familyBeds);
        }
    }

    private void decipherMinAge(CapacityStruct p) {
        if (p.minimumAge == Age.CHILDREN_BASE.toInt()) {
            p.fromAge = Age.CHILDREN_BASE;
        } else if (p.minimumAge == Age.YOUNG_ADULTS_BASE.toInt()) {
            p.fromAge = Age.YOUNG_ADULTS_BASE;
        } else if (p.minimumAge == Age.ADULTS_BASE.toInt()) {
            p.fromAge = Age.ADULTS_BASE;
        } else {
            p.fromAge = Age.MIN_AGE;
        }
    }

    private void decipherMaxAge(CapacityStruct p) {
        if (p.maximumAge == Age.BABIES.toInt()) {
            p.toAge = Age.BABIES;
        } else if (p.maximumAge  == Age.CHILDREN_CAP.toInt()) {
            p.toAge = Age.CHILDREN_CAP;
        } else if (p.maximumAge  == Age.YOUNG_ADULTS_CAP.toInt()) {
            p.toAge = Age.YOUNG_ADULTS_CAP;
        } else if (p.maximumAge  == Age.ADULTS_CAP.toInt()) {
            p.toAge = Age.ADULTS_CAP;
        } else {
            p.toAge = Age.MAX_AGE;
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
