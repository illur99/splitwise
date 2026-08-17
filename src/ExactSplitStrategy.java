import java.util.ArrayList;
import java.util.List;

public class ExactSplitStrategy implements SplitStrategy{

    @Override
    public List<Split> calculateSplit(double amount, User paidBy, List<User> participents, List<Double> splitValues) {
        if(participents.size() != splitValues.size())
        {
            throw new IllegalArgumentException("Number of participants and split values must be the same.");
        }
        double total = 0;
        for(double value: splitValues)
        {
            total += value;
        }
        if(total !=amount)
        {
            throw new IllegalArgumentException("Total of split values must be equal to the amount.");
        }
        List<Split> splits = new ArrayList<>();
        for(int i=0;i<participents.size();i++)
        {
            splits.add(new Split(participents.get(i), splitValues.get(i)));
        }
        return splits;
    }
}
