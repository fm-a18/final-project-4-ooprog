package utils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;

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
}
