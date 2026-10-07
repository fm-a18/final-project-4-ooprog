package utils;

import inventory.Pet;

import java.util.ArrayList;

public class DisplayUtils {
    public static final int TABLE_WIDTH = 100;
    public static final int MENU_WIDTH = 60;

    public static void centeredTitle(String tableName, int width) {
        System.out.println("=".repeat(width));
        int padding = (width - tableName.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + tableName);
        System.out.println("=".repeat(width));
    }

    public static void staffPetHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);

        System.out.printf(
                "%-10s | %-20s | %-15s | %-15s%n | %-10%n",
                "Pet ID",
                "Pet Name",
                "Type",
                "Status",
                "Price");

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void customerPetHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);

        System.out.printf(
                "%-20s | %-15s | %-15s | %-5s | %-15s | %-10%n",
                "Pet Name",
                "Type",
                "Breed",
                "Age",
                "Status",
                "Price");

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displayPetsForStaff(ArrayList<Pet> pets) { // ID, Name, Type, Adoption Status, Medical History, Adoption History

        staffPetHeader("LIST OF PETS");

        for (Pet pet : pets) {
            System.out.printf(
                    "%-10s | %-20s | %-15s | %-15s%n | %,10.2f%n | %-25s | %-25s%n",
                    pet.getPetID(),
                    pet.getPetName(),
                    pet.getType(),
                    pet.getAdoptionStatus(),
                    pet.getPrice(),
                    pet.getMedicalHistory(),
                    pet.getAdoptionHistory());

        }

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displayPetsForCustomer(ArrayList<Pet> pets) { // Name, Type, Breed, Age, Adoption Status, Price

        customerPetHeader("AVAILABLE PETS");

        for (Pet pet : pets) {
            System.out.printf(
                    "%-20s | %-15s | %-15s | %-5d | %-15s%n | %,10.2f%n",
                    pet.getPetName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getAdoptionStatus(),
                    pet.getPrice());
        }

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void printSummaryBox(String tableName, String[][] rows) {
        int labelWidth = 0;
        for (String[] row : rows) {
            labelWidth = Math.max(labelWidth, row[0].length());
        }

        centeredTitle(tableName, MENU_WIDTH);
        for (String[] row : rows) {
            System.out.printf(" %-" + labelWidth + "s : %s%n", row[0], row[1]);
        }
        System.out.println("=".repeat(MENU_WIDTH));
    }

    public static void displayPetDetails(Pet pet) {

        printSummaryBox("PET DETAILS",
                new String[][] {
                        { "Pet ID", pet.getPetID() },
                        { "Name", pet.getPetName() },
                        { "Type", pet.getType() },
                        { "Breed", pet.getBreed() },
                        { "Gender", String.valueOf(pet.getGender()) },
                        { "Age", String.valueOf(pet.getAge()) },
                        { "Price", String.format("Php %,.2f", pet.getPrice()) },
                        { "Status", pet.getAdoptionStatus() },
                        { "Adoption History", pet.getAdoptionHistory() },
                        { "Medical History", pet.getMedicalHistory() }
                });
    }

    public static void printDivider(String tableName) {
        String divider = " " + tableName + " ";
        int dashCount = (MENU_WIDTH - divider.length()) / 2;

        System.out.println(
                "-".repeat(Math.max(0, dashCount))
                        + divider
                        + "-".repeat(Math.max(0,
                                MENU_WIDTH - dashCount - divider.length())));
    }

    public static void printMenu(String tableName, String... options) {
        centeredTitle(tableName, MENU_WIDTH);
        int ctr = 1;

        for (String option : options) {
            if (option.startsWith("#")) {
                System.out.println();
                printDivider(option.substring(1));
                continue;
            }
            System.out.printf(" [%d] %s%n", ctr++, option);
        }
        System.out.println(" [0] Back");
        System.out.println("=".repeat(MENU_WIDTH));
    }
}
