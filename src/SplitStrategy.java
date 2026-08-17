import java.util.List;

public interface SplitStrategy {
    public List<Split> calculateSplit(double amount,User paidBy,List<User> participents,List<Double> splitValues);
}
