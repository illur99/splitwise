import java.time.LocalDateTime;
import java.util.List;

public class Expense {
    private final String id;
    private final String description;
    private final User paidBy;
    private final double amount;
    private final List<Split> split;
    private final LocalDateTime timestamp;

    public Expense(ExpenseBuilder builder)
    {
        this.id = builder.id;
        this.description = builder.description;
        this.paidBy = builder.paidBy;
        this.amount = builder.amount;
        this.split = builder.splitStrategy.calculateSplit(builder.amount, builder.paidBy, builder.participants, builder.splitValues);
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public double getAmount() {
        return amount;
    }

    public List<Split> getSplit() {
        return split;
    }

    public static class ExpenseBuilder
    {
        private String id;
        private String description;
        private User paidBy;
        private double amount;
        private List<Double> splitValues;
        private List<User> participants;
        private SplitStrategy splitStrategy;

        public void setId(String id) {
            this.id = id;
        }

        public ExpenseBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ExpenseBuilder setPaidBy(User paidBy) {
            this.paidBy = paidBy;
            return this;
        }

        public ExpenseBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public ExpenseBuilder setSplitValues(List<Double> splitValues) {
            this.splitValues = splitValues;
            return this;
        }

        public ExpenseBuilder setParticipants(List<User> participants) {
            this.participants = participants;
            return this;
        }

        public ExpenseBuilder setSplitStrategy(SplitStrategy splitStrategy) {
            this.splitStrategy = splitStrategy;
            return this;
        }
        public Expense build()
        {
            if(splitStrategy == null)
            {
                throw new IllegalArgumentException("Split strategy must be provided");
            }
            return new Expense(this);
        }

    }
}
