CREATE TABLE user (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(50) NOT NULL,
                      balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE transaction (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             user_id INT NOT NULL,
                             amount DECIMAL(10, 2) NOT NULL,
                             type ENUM('充值', '消费', '退款', '提现') NOT NULL,
                             timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (user_id) REFERENCES user(id)
);