package utils;

import inventory.Pet;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

public class FileHandler {

    public static String resolveDataDir() {
        String[] candidates = { "data", "final-project-4-ooprog/data" };
        for (String c : candidates) {
            if (new File(c).isDirectory()) {
                return c + "/";
            }
        }
        new File("data").mkdirs(); // nothing found, so create it
        return "data/";
    }

    public static boolean ensureFilesExist() {
        File dir = new File(RecordManager.DATA_DIR);

        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Could not create data folder: " + dir.getAbsolutePath());
            return false;
        }
        if (!dir.isDirectory()) {
            System.out.println("Data path is not a directory: " + dir.getAbsolutePath());
            return false;
        }

        String[] files = {
                RecordManager.FILE_USER_LIST, RecordManager.FILE_PET_LIST, RecordManager.FILE_INCOMING_LIST,
                RecordManager.FILE_RESERVED, RecordManager.FILE_ADOPTED,
                RecordManager.FILE_CANCELLATION_REQUEST, RecordManager.FILE_CANCELLED_LIST,
                RecordManager.FILE_RETURN_REQUEST, RecordManager.FILE_RETURNED_LIST
        };

        for (String fileName : files) {
            File file = new File(fileName);

            try {
                if (!file.exists() && !file.createNewFile()) {
                    System.out.println("Could not create file: " + fileName);
                }
            } catch (IOException e) {
                System.out.println("Could not create " + fileName + ": " + e.getMessage());
            }
        }
        return true;
    }

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
            System.out.println("Error in reading file." + e.getMessage());
        }
        return null;
    }

    public static ArrayList<Pet> viewAllPetRecords() {
        return RecordManager.viewAllPetRecords();
    }

    public static ArrayList<Pet> filterRecords(String fileName, Object value, Function<Pet, ?> getters) {
        ArrayList<Pet> results = new ArrayList<>();
        String target = String.valueOf(value);

        ArrayList<Pet> pets = RecordManager.readRecords(fileName, RecordManager.PET_FIELD_COUNT,
                RecordManager::parsePet);

        for (Pet pet : pets) {
            if (String.valueOf(getters.apply(pet)).equalsIgnoreCase(target)) {
                results.add(pet);
            }
        }

        return results;
    }

    public static void appendRecord(String fileName, String record) {
        boolean needsNewLine = !endsWithNewLine(fileName);
        try (FileWriter writer = new FileWriter(fileName, true)) {
            if (needsNewLine) {
                writer.write(System.lineSeparator());
            }
            writer.write(record);
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error in writing to file.");
        }
    }

    private static boolean endsWithNewLine(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return true;
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(file.length() - 1);
            int last = raf.read();
            return last == '\n' || last == '\r';
        } catch (IOException e) {
            return true;
        }
    }

    public static void removeRecord(String fileName, String searchFor, int colIndex) {
        File file = new File(fileName);
        StringBuilder updated = new StringBuilder();

        try (Scanner read = new Scanner(file)) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > colIndex && parts[colIndex].trim().equalsIgnoreCase(searchFor)) {
                    continue;
                }
                updated.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
            return;
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(updated.toString());
        } catch (IOException e) {
            System.out.println("Error in writing file.");
        }
    }

    public static boolean updateRecord(String fileName, String searchFor, int index, String newLine) { // for updating
                                                                                                       // pet medical
                                                                                                       // history
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

    public static int highestNumber(String fileName, int colIndex, String prefix) {
        int max = 0;
        File file = new File(fileName);
        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s*\\|\\s*", -1);
                if (parts.length > colIndex && parts[colIndex].startsWith(prefix)) {
                    try {
                        max = Math.max(max, Integer.parseInt(parts[colIndex].substring(prefix.length())));
                    } catch (NumberFormatException e) {
                        // skip IDs that don't end in a number
                        System.out.println("Invalid ID format in " + fileName + ". Skipping this ID.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading " + fileName + ".");
        }
        return max;
    }

    public static boolean savePets(ArrayList<Pet> pets) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RecordManager.FILE_PET_LIST))) {
            for (Pet pet : pets) {
                writer.write(pet.toFileString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error in writing file.");
            return false;
        }
    }
}
