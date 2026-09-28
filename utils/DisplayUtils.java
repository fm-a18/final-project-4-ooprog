package utils;

import inventory.Pet;

import java.util.ArrayList;

public class DisplayUtils {
    public static final int TABLE_WIDTH = 100;
    public static final int MENU_WIDTH = 40;

    private static void centeredTitle(String tableName, int width) {
        System.out.println("=".repeat(width));
        int padding = (width - tableName.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + tableName);
        System.out.println("=".repeat(width));
    }

    public static void staffPetHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);

        System.out.printf(
                "%-10s | %-20s | %-15s | %-15s | %-10s | %-5s | %-12s | %-20s | %-20s%n",
                "Pet ID",
                "Pet Name",
                "Type",
                "Breed",
                "Gender",
                "Age",
                "Price",
                "Adoption History",
                "Medical History"
        );

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void customerPetHeader(String tableName) {
        centeredTitle(tableName, TABLE_WIDTH);

        System.out.printf(
                "%-20s | %-15s | %-15s | %-10s | %-5s | %-15s | %-20s | %-20s%n",
                "Pet Name",
                "Type",
                "Breed",
                "Gender",
                "Age",
                "Adoption Status",
                "Adoption History",
                "Medical History"
        );

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displayPetsForStaff(ArrayList<Pet> pets) {

        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        staffPetHeader("LIST OF PETS");

        for (Pet pet : pets) {
            System.out.printf(
                    "%-10s | %-20s | %-15s | %-15s | %-10s | %-5d | %-12s | %-20s | %-20s%n",
                    pet.getPetID(),
                    pet.getPetName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getGender(),
                    pet.getAge(),
                    String.format("Php %,.2f", pet.getPrice()),
                    pet.getAdoptionHistory(),
                    pet.getMedicalHistory()
            );
        }

        System.out.println("-".repeat(TABLE_WIDTH));
    }

    public static void displayPetsForCustomer(ArrayList<Pet> pets) {

        if (pets.isEmpty()) {
            System.out.println("No pets found.");
            return;
        }

        customerPetHeader("AVAILABLE PETS");

        for (Pet pet : pets) {

            System.out.printf(
                    "%-20s | %-15s | %-15s | %-10s | %-5d | %-15s | %-20s | %-20s%n",
                    pet.getPetName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getGender(),
                    pet.getAge(),
                    pet.getAdoptionStatus(),
                    pet.getAdoptionHistory(),
                    pet.getMedicalHistory()
            );
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

    public static void printMenu(String tableName, String... options) {
        centeredTitle(tableName, MENU_WIDTH);

        for (int i = 0; i < options.length; i++) {
            System.out.printf(" [%d] %s%n", i + 1, options[i]);
        }
        System.out.println("=".repeat(MENU_WIDTH));
    }
}
