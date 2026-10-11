package utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;

import inventory.Bird;
import inventory.Cat;
import inventory.Dog;
import inventory.Pet;

import users.Customer;

public class RecordManager {

  //FOR PET_LIST.txt
  private static final int PET_ID = 0, PET_NAME = 1, PET_TYPE = 2, PET_BREED = 3, PET_GENDER = 4, PET_AGE = 5, PET_PRICE = 6;
  public static final int PET_FIELD_COUNT = 7;

  //FOR RESERVED.txt
  public static final int AS_TRANSACTION_NUM = 0;
  public static final int AS_FIRST_NAME      = 1;
  public static final int AS_LAST_NAME       = 2;
  public static final int AS_EMAIL           = 3;
  public static final int AS_PET_ID          = 4;
  public static final int AS_PAYMENT_PLAN    = 5;
  public static final int AS_DOWN_PAYMENT    = 6;
  public static final int AS_BALANCE_LEFT    = 7;
  public static final int AS_DATE_REQUESTED  = 8;
  public static final int AS_DATE_CLAIMING   = 9;
  public static final int AS_FIELD_COUNT     = 10;

  //ADOPTED_LIST.txt
  public static final int AD_DATE_FINALIZED = 10;
  public static final int AD_APPROVED_BY = 11;
  public static final int AD_FIELD_COUNT = 12;


  //reader
  public static<T> ArrayList<T> readRecords(String fileName, int expectedFields, Function<String[], T> parser){
    ArrayList<T> records = new ArrayList<>();
    File file = new File(fileName);

    if(!file.exists() || file.length() == 0){
      System.out.println(fileName + " not found or empty.");
      return records;
    }

    try(BufferedReader reader = new BufferedReader(new FileReader(file))){
      String line;
      while((line = reader.readLine()) != null){
        if(line.isBlank()) continue;

        String[] parts = line.split("\\s*\\|\\s*", -1);
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

  public static Pet parsePet(String[] pet){
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

  public static Reservation parseReservation(String[] pet){
    return new Reservation(
      pet[AS_TRANSACTION_NUM].trim(), 
      pet[AS_FIRST_NAME].trim(), 
      pet[AS_LAST_NAME].trim(), 
      pet[AS_EMAIL].trim(), 
      pet[AS_PET_ID].trim(), 
      pet[AS_PAYMENT_PLAN].trim(), 
      Double.parseDouble(pet[AS_DOWN_PAYMENT].trim().replace(",", "")), 
      Double.parseDouble(pet[AS_BALANCE_LEFT].trim().replace(",", "")), 
      LocalDate.parse(pet[AS_DATE_REQUESTED].trim()),
      LocalDate.parse(pet[AS_DATE_CLAIMING].trim()));
  }

  public static ArrayList<Pet> viewAllPetRecords(){
    return readRecords("PET_LIST.txt", PET_FIELD_COUNT, RecordManager::parsePet);
  }

  public static ArrayList<Reservation> viewAllReserveRecords() {
    return readRecords("RESERVED.txt", AS_FIELD_COUNT, RecordManager::parseReservation);
  }

  public static Reservation parseAdoption(String[] parts){
    return parseReservation(parts);
  }

  public static ArrayList<Reservation> viewAllPetAdoptedRecords(){
    return readRecords("ADOPTED_LIST.txt", AD_FIELD_COUNT, RecordManager::parseAdoption);
  }

  public String buildAdoptionRequestRecord(String firstName, String lastName, String emailAddress, Pet selectedPet, String paymentPlan, double downPayment, double balanceLeft, String transactionNum, LocalDate date) {
        String parts[] = new String[RES_FIELD_COUNT - 2];

        parts[AS_TRANSACTION_NUM] = transactionNum;
        parts[AS_FIRST_NAME] = firstName;
        parts[AS_LAST_NAME] = lastName;
        parts[AS_EMAIL] = emailAddress;
        parts[AS_PET_ID] = selectedPet.getPetID();
        parts[AS_PAYMENT_PLAN] = paymentPlan;
        parts[AS_DOWN_PAYMENT] = String.format("%,.2f", downPayment);
        parts[AS_BALANCE_LEFT] = String.format("%,.2f", balanceLeft);
        parts[AS_DATE_REQUESTED] = java.time.LocalDate.now().toString();
        parts[AS_DATE_CLAIMING] = date.toString();

        return String.join("|", parts);
    }

}
