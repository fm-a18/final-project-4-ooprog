package utils;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
    public static String findRecord(String fileName, String searchFor, int index) {
        try (Scanner read = new Scanner(new File(fileName))) {
            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length > index && parts[index].equalsIgnoreCase(searchFor)) {
                    return line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file.");
        }
        return null;
    }
}
