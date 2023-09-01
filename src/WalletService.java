import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WalletService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/wallet";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "password";

    // 查询用户钱包余额
    public double getBalance(int userId) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM user WHERE id = ?");
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            double balance = rs.getDouble("balance");
            conn.close();
            return balance;
        } else {
            conn.close();
            throw new IllegalArgumentException("User not found");
        }
    }

    // 消费
    public void consume(int userId, double amount) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        conn.setAutoCommit(false);
        try {
            double balance = getBalance(userId);
            if (balance >= amount) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE user SET balance = balance - ? WHERE id = ?");
                stmt.setDouble(1, amount);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("INSERT INTO transaction (user_id, amount, type) VALUES (?, ?, '消费')");
                stmt.setInt(1, userId);
                stmt.setDouble(2, amount);
                stmt.executeUpdate();
                conn.commit();
            } else {
                throw new IllegalArgumentException("Insufficient balance");
            }
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // 退款
    public void refund(int userId, double amount) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        conn.setAutoCommit(false);
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET balance = balance + ? WHERE id = ?");
            stmt.setDouble(1, amount);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            stmt = conn.prepareStatement("INSERT INTO transaction (user_id, amount, type) VALUES (?, ?, '退款')");
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    // 交易历史明细
    public List<Transaction> getTransactionHistory(int userId) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transaction WHERE user_id = ?");
        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();
        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            double amount = rs.getDouble("amount");
            String type = rs.getString("type");
            Timestamp timestamp = rs.getTimestamp("timestamp");
            transactions.add(new Transaction(id, userId, amount, type, timestamp));
        }
        conn.close();
        return transactions;
    }
}