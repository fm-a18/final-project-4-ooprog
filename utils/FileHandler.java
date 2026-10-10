package utils;

import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;
import inventory.AdoptionStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

import static inventory.AdoptionStatus.PENDING_REVIEW;

public class FileHandler {
    public static String findRecord(String fileName, String searchFor, int colIndex) {
        try (Scanner read = new Scanner(new File(fileName))) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > colIndex && parts[colIndex].equalsIgnoreCase(searchFor)) {
                    return line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
        }
        return null;
    }

    public static void printRecords(ArrayList<Pet> records) {
        if (records.isEmpty()) {
            System.out.println("No matching records.");
            return;
        }
        for (String record : records) {
            System.out.println(record);
        }
    }

    public static ArrayList<Pet> viewAllPetRecords(){
        ArrayList<Pet> pets = new ArrayList<>();
        File file = new File("PET_LIST.txt");

        if(!file.exists() || file.length() == 0){
            System.out.println("File not found or empty.");
            return pets;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader("PET_LIST.txt"))) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split("\\s*\\|\\s*");
                if(parts.length == 7) {
                    try{
                        String petID = parts[0].trim();
                        String petName = parts[1].trim();
                        String petType = parts[2].trim();
                        String petBreed = parts[3].trim();
                        char petGender = parts[4].trim().charAt(0);
                        int petAge = Integer.parseInt(parts[5].trim());
                        double petPrice = Double.parseDouble(parts[6].trim());

                        Pet pet = switch (petType.toLowerCase()) {
                            case "dog" -> new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
                            case "cat" -> new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
                            case "bird" -> new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
                            default -> null;
                        };

                        if (pet != null) {
                            pets.add(pet);
                        }
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        System.out.println("Invalid number format in record: " + line);
                    }
                    
                }else{
                    System.out.println("Invalid record format: " + line);
                }
            }
        } catch(FileNotFoundException e){
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("Error in reading file.");
        }

        return pets;
    }

    private static Pet parse(String line){
        String[] parts = line.split("\\s*\\|\\s*");
        if(parts.length < 8) {
            return null;
        }

        try{
            String petID = parts[0];
            String petName = parts[1];
            String petType = parts[2];
            String petBreed = parts[3];
            char petGender = parts[4].charAt(0);
            int petAge = Integer.parseInt(parts[5]);
            double petPrice = Double.parseDouble(parts[6]);
            AdoptionStatus adoptionStatus = AdoptionStatus.valueOf(parts[7].toUpperCase());

            Pet pet;
            switch(petType.toLowerCase()){
                case "dog" -> pet = new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
                case "cat" -> pet = new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
                case "bird" -> pet = new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
                default -> pet = null;
            }

            assert pet != null;
            pet.setAdoptionStatus(PENDING_REVIEW);
            return pet;
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static ArrayList<Pet> filterRecords(String fileName, Object value, Function <Pet, ?> getters) {
        ArrayList<Pet> results = new ArrayList<>();
        String target = String.valueOf(value);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pet pet = parse(line);
                if (pet != null) continue;

                String field = String.valueOf(getters.apply(pet));
                if (field.equalsIgnoreCase(target)) {
                    results.add(pet);
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
        }
        return results;
    }

    public static void appendRecord(String fileName, String record){
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(record + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error in writing to file.");
        }
    }

    public static boolean removeRecord(String fileName, String searchFor, int colIndex) {
        File file = new File(fileName);
        StringBuilder updated = new StringBuilder();
        boolean recordFound = false;

        try (Scanner read = new Scanner(file)) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > colIndex && parts[colIndex].trim().equalsIgnoreCase(searchFor)) {
                    recordFound = true;
                    continue;
                }
                updated.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
            return false;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(updated.toString());
        } catch (IOException e) {
            System.out.println("Error in writing file.");
            return false;
        }
        return recordFound;
    }

    public static boolean updateRecord(String fileName, String searchFor, int index, String newLine) {
        File file = new File(fileName);
        StringBuilder updated = new StringBuilder();
        boolean found = false;

        try (Scanner read = new Scanner(file)) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > index && parts[index].trim().equalsIgnoreCase(searchFor)) {
                    updated.append(newLine).append(System.lineSeparator());
                    found = true;
                } else {
                    updated.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
            return false;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(updated.toString());
        } catch (IOException e) {
            System.out.println("Error in writing file.");
            return false;
        }
        return found;
    }

    public static ArrayList<Pet> loadPets(String fileName, int choice) {
        ArrayList<Pet> pets = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()){
            return pets;
        }

        try (Scanner read = new Scanner(file)){
            while(read.hasNextLine()){
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if(parts.length < 10) {
                    continue;
                }
                Pet pet = getPet(choice, parts);
                if(pet != null){
                    pets.add(pet);
                }
                return pets;
            }
        } catch (IOException e){
            System.out.println("Error in reading file.");
        }
        return null;
    }

    private static Pet getPet(int choice, String[] parts) {
        String petID = parts[0];
        String petName = parts[1];
        String petType = parts[2];
        String petBreed = parts[3];
        char petGender = parts[4].charAt(0);
        int petAge = Integer.parseInt(parts[5]);
        double petPrice = Double.parseDouble(parts[6]);

        Pet pet = switch(choice){
            case 1 -> new Dog(petID, petName, petBreed, petGender, petAge, petPrice);
            case 2 -> new Cat(petID, petName, petBreed, petGender, petAge, petPrice);
            case 3 -> new Bird(petID, petName, petBreed, petGender, petAge, petPrice);
            default -> null;
        };
        return pet;
    }

}
