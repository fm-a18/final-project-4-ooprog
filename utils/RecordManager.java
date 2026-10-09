package utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;

import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;

public class RecordManager {

  //FOR PET_LIST.txt
  private static final int PET_ID = 0, PET_NAME = 1, PET_TYPE = 2, PET_BREED = 3, PET_GENDER = 4, PET_AGE = 5, PET_PRICE = 6, PET_FIELD_COUNT = 7;

  //FOR RESERVE.txt
  public static final int AS_TRANSACTION_NUM = 0;
  public static final int AS_FIRST_NAME      = 1;
  public static final int AS_LAST_NAME       = 2;
  public static final int AS_EMAIL           = 3;
  public static final int AS_PET_ID          = 4;
  public static final int AS_PAYMENT_PLAN    = 5;
  public static final int AS_DOWN_PAYMENT    = 6;
  public static final int AS_BALANCE_LEFT    = 7;
  public static final int AS_DATE_REQUESTED  = 8;
  public static final int AS_FIELD_COUNT     = 9;


  //reader
  private static<T> ArrayList<T> readRecords(String fileName, int expectedFields, Function<String[], T> parser){
    ArrayList<T> records = new ArrayList<>();
    File file = new File(fileName);

    if(!file.exists() || file.length() == 0){
      System.out.println(fileName + "not found or empty.");
      return records;
    }

    try(BufferedReader reader = new BufferedReader(new FileReader(file))){
      String line;
      while((line = reader.readLine()) != null){
        if(line.isBlank()) continue;

        String[] parts = line.split("\\s*\\|\\s*");
        if(parts.length != expectedFields){
          System.out.println("Invalid record format: " + line);
          continue;
        }

        try{
          T record = parser.apply(parts);
          if(record != null){
            records.add(record);
          }
        }catch(RuntimeException e){
          System.out.println("Invalid data in record: " + line);
        }
      }
    }catch(IOException e){
      System.out.println("Error in reading " + fileName + ".");
    }
    return records;
  }

  private static Pet parsePet(String[] pet){
    String id    = pet[PET_ID].trim();
    String name  = pet[PET_NAME].trim();
    String type  = pet[PET_TYPE].trim();
    String breed = pet[PET_BREED].trim();
    char gender  = pet[PET_GENDER].trim().charAt(0);
    int age      = Integer.parseInt(pet[PET_AGE].trim());
    double price = Double.parseDouble(pet[PET_PRICE].trim());

    return switch(type.toLowerCase()){
      case "dog" -> new Dog(id, name, breed, gender, age, price);
      case "cat" -> new Cat(id, name, breed, gender, age, price);
      case "bird" -> new Bird(id, name, breed, gender, age, price);
      default -> null;
    };
  }

  private static Reservation parseReservation(String[] pet){
    return new Reservation(
      pet[AS_TRANSACTION_NUM].trim(), 
      pet[AS_FIRST_NAME].trim(), 
      pet[AS_LAST_NAME].trim(), 
      pet[AS_EMAIL].trim(), 
      pet[AS_PET_ID].trim(), 
      pet[AS_PAYMENT_PLAN].trim(), 
      Double.parseDouble(pet[AS_DOWN_PAYMENT].trim().replace(",", "")), 
      Double.parseDouble(pet[AS_BALANCE_LEFT].trim().replace(",", "")), 
      LocalDate.parse(pet[AS_DATE_REQUESTED].trim()));
  }

}
