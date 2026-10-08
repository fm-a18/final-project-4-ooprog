package users;

import inventory.AdoptionStatus;
import inventory.Pet;
import utils.FileHandler;
import utils.PetManager;
import utils.DataValidation;
import utils.DisplayUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
//import java.util.function.Function;


public class Customer extends User{
    public Customer(String firstName, String lastName, String emailAddress,
                 String address, String birthDay, String phoneNumber, String password){
        super(firstName, lastName, emailAddress,
                address,birthDay, phoneNumber, password);
    }

    @Override
    public String getRole(){
        return "Customer";
    }

    public void showMenu(Scanner sc){
        boolean isDone = false;

        while(!isDone){
            DisplayUtils.printMenu("CUSTOMER MENU",
                    "#Browse Pets",
                    "View All Available Pets",
                    "Search by Category",
                    "Search Specific Pets",
                    "View Specific Pet Details",
                    "Filter by Price",

                    "#Adoption",
                    "Request Adoption",
                    "Cancel Adoption Request",

                    "#Return",
                    "Request Pet Return"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 6, 7, 0);

            switch(choice) {
                case 1 -> viewAllPets();
                case 2 -> searchByCategory();
                case 3 -> findSpecificPets();
                case 4 -> filterByPrice();
                case 5 -> requestAdoption();
                case 6 -> cancelAdoption();
                case 7 -> requestReturn();
                case 0 -> isDone = true;
            }
        }
    }

    ArrayList<Pet> pets = PetManager.getInstance().getAllPets();

    private void viewAllPets() {
        if(pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(pets);
    }

    private void searchByCategory() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("SEARCH BY CATEGORY",
                    "#Select Category",
                    "Pet Name",
                    "Type",
                    "Breed",
                    "Gender",
                    "Age"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 4, 5, 0);

            switch(choice){
                case 1: //SEARCH PET BY NAME
                    String name = DataValidation.validateString("Enter Pet Name: ");

                    ArrayList<Pet> petNames = FileHandler.filterRecords("PETS_LIST.txt", name, Pet::getPetName);

                    if(petNames.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petNames);
                    break;
                case 2: //VIEW PET BY PET TYPE
                    ArrayList<Pet> petTypes = getSpecificPetDetailsPerType();
                    if(petTypes.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petTypes);
                    break;
                case 3: //SEARCH PET BY PET BREED
                    String breed = DataValidation.validateString("Enter Pet Breed: ");

                    ArrayList<Pet> petBreeds = FileHandler.filterRecords("PETS_LIST.txt", breed, Pet::getBreed);
                    if(petBreeds.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petBreeds);
                    break;
                case 4: //SEARCH PET BY GENDER
                    DisplayUtils.printMenu("SEARCH BY PET GENDER",
                            "#Select Gender",
                            "Male",
                            "Female"
                    );
                    int choiceGender = DataValidation.intChoiceValidation("Select Option",
                            1, 2, 0);

                    char gender;
                    if(choiceGender == 1){gender = 'M';}
                    else{gender = 'F';}

                    ArrayList<Pet> petGender = FileHandler.filterRecords("PETS_LIST.txt", String.valueOf(gender), Pet::getGender);
                    if(petGender.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petGender);
                    break;
                case 5: //SEARCH PET BY AGE
                    int age = DataValidation.validatePetAgeInput("Enter Pet Age: ");

                    ArrayList<Pet> petAge = FileHandler.filterRecords("PETS_LIST.txt", String.valueOf(age), Pet::getAge);
                    if(petAge.isEmpty()){
                        System.out.println("No pets found");
                        return;
                    }

                    DisplayUtils.displayPetsForCustomer(petAge);
                    break;
                case 0: isDone = true; break;
            }
        }
    }

    private ArrayList<Pet> getSpecificPetDetailsPerType(){
        String type = "";
        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("SEARCH BY PET TYPE",
                "#Select Pet Type",
                "Dog",
                "Cat",
                "Bird"
            );
            int choiceType = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 0);

            switch(choiceType){
                case 1 -> type = "dog";
                case 2 -> type = "cat";
                case 3 -> type = "bird";
                case 0 -> isDone = true;
            }
        }

        return FileHandler.filterRecords("PETS_LIST.txt", type, Pet::getType);
    }

    
    private void findSpecificPets() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        ArrayList<Pet> petTypes = getSpecificPetDetailsPerType();

        if(petTypes.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        DisplayUtils.displayPetsForCustomer(petTypes);

        char viewDetailsChoice = DataValidation.charChoiceValidation("Do you want to view specific pet details? (y/n): ", 'y', 'n');



        if(viewDetailsChoice == 'y'){
            String type = petTypes.get(0).getType();
            specificPetDetails(type, petTypes);
        }else{
            return;
        }
    }

    private void specificPetDetails(String type, ArrayList<Pet> petTypes) { // EDIT THIS
        String petID = DataValidation.validatePetIdInput("Enter Pet ID: ", type);

        for(Pet pet : petTypes){
            if(pet.getPetID().equalsIgnoreCase(petID)){
                DisplayUtils.displayPetDetails(pet);
                return;
            }
        }
        System.out.println("No pet found with the given ID.");
    }

    private void filterByPrice() {
        if(pets.isEmpty()){
            System.out.println("No pets found.");
            return;
        }

        boolean isDone = false;
        while(!isDone){
            DisplayUtils.printMenu("FILTER BY PRICE",
                    "#Select Option",
                    "Ascending Price",
                    "Descending Price",
                    "Price Range"
            );
            int choice = DataValidation.intChoiceValidation("Select Option",
                    1, 2, 3, 0);

            switch(choice){
                case 1:
                    ArrayList<Pet> sortedPetsAscending = sortByPrice(1, pets);
                    if(sortedPetsAscending.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsAscending);
                    break;
                case 2:
                    ArrayList<Pet> sortedPetsDescending = sortByPrice(2, pets);
                    if(sortedPetsDescending.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(sortedPetsDescending);
                    break;
                case 3:
                    ArrayList<Pet> filteredPets = filterByPriceRange();

                    if(filteredPets.isEmpty()){
                        System.out.println("No pets found.");
                        return;
                    }
                    DisplayUtils.displayPetsForCustomer(filteredPets);
                    break;
                case 0: isDone = true; break;
            }
        }
    }

    private ArrayList<Pet> sortByPrice(int order, ArrayList<Pet> pets) {
    ArrayList<Pet> sortPrice = new ArrayList<>(pets);

      Comparator<Pet> comparator = Comparator.comparingDouble(Pet::getPrice);

      if(order == 2){
        comparator = comparator.reversed();
      }

      sortPrice.sort(comparator);
      return sortPrice;
    }

    private ArrayList<Pet> filterByPriceRange() {
        ArrayList<Pet> filteredPets = new ArrayList<>();
        double startingPrice = DataValidation.validatePriceInput("Enter Starting Price: ");
        double endingPrice = DataValidation.validatePriceInput("Enter Ending Price: ");

        for(Pet pet : pets){
            if(pet.getPrice() >= startingPrice && pet.getPrice() <= endingPrice){
                filteredPets.add(pet);
            }
        }
        return filteredPets;
    }

    private void requestAdoption() {  
        //FILTER TO SHOW ONLY AVAILABLE PETS viewAllPets();

        String petID = DataValidation.validatePetID("Enter Pet ID to request adoption: ", "^(?i)D|C|B\\d{4}$");

        Pet selectedPet = null;
    
        for(Pet pet : pets){
           if(pet.idExists(petID)){
            selectedPet = pet;
            break;
           }
        }

        if(selectedPet == null) {
            System.out.println("Pet ID: " + petID + " not found.");
            return;
        }

        selectedPet.setAdoptionStatus(AdoptionStatus.PENDING_REVIEW);
        //MAKE A TRANSACTION NUMBER AND SAVE TO RESERVE.TXT
        String transactionNum = DataValidation.transactionNumGenerator();

        //APPEND INFORMATION FROM USER_LIST.TXT AND PET_LIST.TXT TO RESERVE.TXT

        //String adoptionStatusRecord = buildAdoptionStatusRecord();
        
    }

    private static final int RES_TRANSACTION_ID   = 0;
    private static final int RES_FIRST_NAME       = 1;
    private static final int RES_LAST_NAME        = 2;
    private static final int RES_EMAIL            = 3;
    private static final int RES_PET_ID           = 4;
    private static final int RES_PAYMENT_TYPE     = 5;
    private static final int RES_DOWN_PAYMENT     = 6;
    private static final int RES_BALANCE_LEFT     = 7;
    private static final int RES_DATE_REQUESTED   = 8;

    private String buildAdoptionStatusRecord() {

        return String.join("|",
                parts[RES_TRANSACTION_ID], parts[RES_FIRST_NAME], parts[RES_LAST_NAME],
                parts[RES_EMAIL], parts[RES_PET_ID], parts[RES_PAYMENT_TYPE],
                parts[RES_DOWN_PAYMENT], parts[RES_BALANCE_LEFT],
                parts[RES_DATE_REQUESTED], dateFinalized, approvedByEmail
        );
    }

    private void cancelAdoption() {
    }

    private void requestReturn() {
    }
}
