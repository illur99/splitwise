import java.util.ArrayList;
import java.util.List;

public class EqualSplitStrategy implements SplitStrategy
{
    @Override
    public List<Split> calculateSplit(double amount, User paidBy, List<User> participents, List<Double> splitValues) {
        List<Split> splits = new ArrayList<>();
        double equalAmount = amount/participents.size();
        for(User participant : participents)
        {
            splits.add(new Split(participant, equalAmount));
        }
        return splits;

    }
}
