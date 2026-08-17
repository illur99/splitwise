import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BalanceSheet {
    private final User owner;
    private final Map<User,Double> balances = new ConcurrentHashMap<>();

    public BalanceSheet(User owner) {
        this.owner = owner;
    }

    public Map<User,Double> getBalances() {
        return balances;
    }

    public synchronized void adjustBalance(User otherUser, double amount)
    {
        if(otherUser.equals(owner))
        {
            throw new IllegalArgumentException("Cannot adjust balance with self");
        }
        double oldBalance = balances.getOrDefault(otherUser,0.0);
        balances.put(otherUser,oldBalance + amount);
    }

    public void showBalances()
    {
        System.out.println("Balance Sheet for " + owner.getName() + ":");
        if(balances.isEmpty())
        {
            System.out.println("All settled up!!");
            return;
        }
        for(Map.Entry<User,Double> entry : balances.entrySet())
        {
           User otherUser = entry.getKey();
           double amount = entry.getValue();
           if(amount > 0)
           {
               System.out.println(otherUser.getName() + " owes " + owner.getName() + " Rs" + amount);
           }
           else if(amount < 0)
           {
               System.out.println(owner.getName() + " owes " + otherUser.getName() + " Rs" + (-amount));
           }
        }

    }



}
