package payment;

public class FiftyPercentDP implements PaymentPlan{
  @Override 
  public String getName(){
    return "50% Down Payment";
  }

  @Override public double computeDownPayment(double petPrice){
    return petPrice * 0.5;
  }
}
