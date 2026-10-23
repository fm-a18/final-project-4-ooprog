package payment;

public interface PaymentPlan {
  String getName();
  double computeDownPayment(double petPrice);
}
