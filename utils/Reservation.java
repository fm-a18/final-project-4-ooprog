package utils;

import java.time.LocalDate;

public class Reservation {
  private final String transactionNum, firstName, lastName, email, petID, paymentPlan;
  private final double downPayment, balanceLeft;
  private final LocalDate dateRequested;

  public Reservation(String transactionNum, String firstName, String lastName, String email, String petID, String paymentPlan, double downPayment, double balanceLeft, LocalDate dateRequested){
    this.transactionNum = transactionNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.petID = petID;
        this.paymentPlan = paymentPlan;
        this.downPayment = downPayment;
        this.balanceLeft = balanceLeft;
        this.dateRequested = dateRequested;
  }

  public String getTransactionNum(){return this.transactionNum;}
  public String getFirstName(){return this.firstName;}
  public String getLastName(){return this.lastName;}
  public String getEmail(){return this.email;}
  public String getPetID(){return this.petID;}
  public String getPaymentPlan(){return this.paymentPlan;}
  public double getDownPayment(){return this.downPayment;}
  public double getBalanceLeft(){return this.balanceLeft;}
  public LocalDate getDateRequested(){return this.dateRequested;}

  @Override public String toString(){
    return transactionNum + " | " + firstName + " " + lastName + " | " + email
             + " | " + petID + " | " + paymentPlan
             + " | " + String.format("%,.2f", downPayment)
             + " | " + String.format("%,.2f", balanceLeft)
             + " | " + dateRequested;
  }
}
