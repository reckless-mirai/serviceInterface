import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        WalletService ws = new WalletService();

        // 查询用户钱包余额
        double balance = ws.getBalance(1);// 用户id，设为1,下同
        System.out.println("用户钱包余额：" + balance);

        // 用户消费100元
        ws.consume(1, 100);
        System.out.println("用户消费成功");

        // 用户退款20元
        ws.refund(1, 20);
        System.out.println("用户退款成功");

        // 查询用户钱包金额变动明细
        List<Transaction> transactions = ws.getTransactionHistory(1);
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}