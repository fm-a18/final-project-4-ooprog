package payment;

public class SeventyFivePercentDP implements PaymentPlan{
  @Override 
  public String getName(){
    return "75% Down Payment";
  }

  @Override public double computeDownPayment(double petPrice){
    return petPrice * 0.75;
  }
}
