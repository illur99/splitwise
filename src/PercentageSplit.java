import java.util.ArrayList;
import java.util.List;

public class PercentageSplit implements SplitStrategy {

    @Override
    public List<Split> calculateSplit(double amount, User paidBy, List<User> participents, List<Double> splitValues) {
        if(participents.size() != splitValues.size())
        {
            throw new IllegalArgumentException("Number of participants and split values must be the same.");
        }
        double totalPercentage =0;
        for(double value: splitValues)
        {
            totalPercentage += value;
        }
        if(totalPercentage != 100)
        {
            throw new IllegalArgumentException("Total of split percentages must be equal to 100.");
        }
        List<Split> splits = new ArrayList<>();
        for(int i=0;i<participents.size();i++)
        {
          User user = participents.get(i);
          double percentage = splitValues.get(i);

          double splitAmount = amount * (percentage/ 100);
          splits.add(new Split(user,splitAmount));
        }
        return splits;
    }
}
