package utils;

import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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

    public static ArrayList<String> filterRecords(String fileName, int index, String value) {
        ArrayList<String> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > index && parts[index].equalsIgnoreCase(value)) {
                    results.add(line);
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
            }
        } catch (IOException e){
            System.out.println("Error in reading file.");
        }
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
