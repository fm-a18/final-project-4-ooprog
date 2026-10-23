package payment;

public class FullPayment implements PaymentPlan {
  @Override 
  public String getName(){
    return "Full Payment";
  }

  @Override public double computeDownPayment(double petPrice){
    return petPrice;
  }
}
