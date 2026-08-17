import java.util.Arrays;
import java.util.List;

public class SplitWiseDemo {
    public static void main(String[] args)
    {
       SplitwiseService service =SplitwiseService.getInstance();

       User arpita = service.addUser("Arpita","arpita@gmail.com");
       User alok = service.addUser("Alok","alok@gmail.com");
       User ragu = service.addUser("Ragu","ragu@gmail.com");
       User megha = service.addUser("Megha","megha@gmail.com");

       Group GoaTrip = service.addGroup("Goa Trip", List.of(arpita,alok,ragu,megha));

       System.out.println("use case 1 : EQUAL SPLIT");
       service.createExpense(new Expense.ExpenseBuilder()
               .setDescription("Dinner")
               .setAmount(5000)
               .setPaidBy(arpita)
                .setParticipants(List.of(arpita,alok,ragu,megha))
               .setSplitStrategy(new EqualSplitStrategy())
               );

       service.showBalanceSheet(arpita.getId());
       service.showBalanceSheet(alok.getId());

        System.out.println("--- Use Case 2: Exact Split ---");
        service.createExpense(new Expense.ExpenseBuilder()
                .setDescription("Movie Tickets")
                .setAmount(370)
                .setPaidBy(alok)
                .setParticipants(List.of(ragu, megha))
                .setSplitStrategy(new ExactSplitStrategy())
                .setSplitValues(List.of(120.0, 250.0))
        );

        service.showBalanceSheet(alok.getId());
        service.showBalanceSheet(megha.getId());
        System.out.println();

        // 5. Use Case 3: Percentage Split
        System.out.println("--- Use Case 3: Percentage Split ---");
        service.createExpense(new Expense.ExpenseBuilder()
                .setDescription("Groceries")
                .setAmount(500)
                .setPaidBy(megha)
                .setParticipants(Arrays.asList(alok, arpita, ragu))
                .setSplitStrategy(new PercentageSplit())
                .setSplitValues(Arrays.asList(40.0, 30.0, 30.0)) // 40%, 30%, 30%
        );

        System.out.println("--- Balances After All Expenses ---");
        service.showBalanceSheet(arpita.getId());
        service.showBalanceSheet(ragu.getId());

        service.showBalanceSheet(megha.getId());
        service.showBalanceSheet(alok.getId());

        System.out.println();

        System.out.println("--- Use Case 4: Simplify Group Debts for 'Goa Trip' ---");
        List<Transaction> simplifiedDebts = service.simplifyGroupDebts(GoaTrip.getGroupId());
        if (simplifiedDebts.isEmpty()) {
            System.out.println("All debts are settled within the group!");
        } else {
            simplifiedDebts.forEach(System.out::println);
        }
        System.out.println();

        service.showBalanceSheet(alok.getId());


        System.out.println("--- Use Case 5: Partial Settlement ---");

        service.settleUp(alok.getId(), arpita.getId(), 100);

        System.out.println("--- Balances After Partial Settlement ---");
        service.showBalanceSheet(alok.getId());
        service.showBalanceSheet(arpita.getId());



    }
}
