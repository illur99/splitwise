import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitwiseService {
    private static SplitwiseService instance;

    private Map<String,User> users = new HashMap<>();
    private Map<String,Group> groups = new HashMap<>();

    private SplitwiseService()
    {}

    public static synchronized SplitwiseService getInstance()
    {
        if(instance == null)
        {
            instance = new SplitwiseService();
        }
        return instance;
    }

    public User addUser(String name,String email)
    {
        User user = new User(name,email);
        users.put(user.getId(),user);
        return user;
    }
    public Group addGroup(String groupName, List<User> members)
    {
        Group group = new Group(groupName,members);
        groups.put(group.getGroupId(),group);
        return group;
    }

    public User getUserById(String userId)
    {
        return users.get(userId);
    }
    public Group getGroupById(String groupId)
    {
        return groups.get(groupId);
    }

    public synchronized void createExpense(Expense.ExpenseBuilder builder) {
        Expense expense = builder.build();
        User paidBy = expense.getPaidBy();
        for (Split split : expense.getSplit()) {
            User particiant = split.getUser();
            double amount = split.getAmount();
            if (!paidBy.equals(particiant)) {
                paidBy.getBalanceSheet().adjustBalance(particiant, amount);
                particiant.getBalanceSheet().adjustBalance(paidBy, -amount);
            }
        }
        System.out.println("Expense created " + expense.getDescription());
    }
    public synchronized void settleUp(String payerId,String payeeId,double amount)
    {
        User payer = users.get(payerId);
        User payee = users.get(payeeId);
        payee.getBalanceSheet().adjustBalance(payer,-amount);
        payer.getBalanceSheet().adjustBalance(payee,amount);
    }
    public void showBalanceSheet(String userId)
    {
        User user = users.get(userId);
        user.getBalanceSheet().showBalances();
    }
    public List<Transaction> simplifyGroupDebts(String groupId)
    {
        Group group = groups.get(groupId);
        if(group == null)
        {
            throw new IllegalArgumentException("Group not found");
        }
        Map<User, Double> netBalances = new HashMap<>();
        for(User user : group.getMembers())
        {
            double balance =0;
            for(Map.Entry<User,Double> entry : user.getBalanceSheet().getBalances().entrySet())
            {
               User otherUser = entry.getKey();
               double amount = entry.getValue();
               if(group.getMembers().contains(otherUser))
               {
                   balance+= amount;
               }
            }
            netBalances.put(user,balance);
        }
        List<User> creditors = new ArrayList<>();
        List<User> debitors = new ArrayList<>();
        for(User user : group.getMembers()) {
            double balance = netBalances.get(user);
            if (balance > 0) {
                creditors.add(user);
            } else if (balance < 0) {
                debitors.add(user);
            }
        }
            List<Transaction> transactions = new ArrayList<>();
            int i=0; int j=0;
            while(i< creditors.size() && j< debitors.size())
            {
                  User creditor = creditors.get(i);
                  User debitor = debitors.get(j);

                  double creditorAmount = netBalances.get(creditor);
                  double debitorAmount = -netBalances.get(debitor);

                  double amount = Math.min(creditorAmount,debitorAmount);
                  transactions.add(new Transaction(debitor,creditor,amount));

                  netBalances.put(creditor,creditorAmount - amount);
                  netBalances.put(debitor,netBalances.get(debitor)+amount);

                  if(netBalances.get(creditor) == 0)
                  {
                      i++;
                  }
                  if(netBalances.get(debitor) == 0)
                  {
                      j++;
                  }
            }

        return transactions;
    }


}
