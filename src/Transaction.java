import java.sql.Timestamp;

public class Transaction {
    private int id; // 交易ID
    private int userId; // 用户ID
    private double amount; // 交易金额
    private String type; // 交易类型
    private Timestamp timestamp; // 交易时间戳

    public Transaction(int id, int userId, double amount, String type, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}