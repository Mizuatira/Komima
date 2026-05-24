
CREATE DATABASE IF NOT EXISTS komima_task DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE komima_task;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    create_time DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    user_id INT NOT NULL,
    status INT DEFAULT 0,
    receiver_id INT DEFAULT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS evaluate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    user_id INT NOT NULL,
    score INT NOT NULL,
    content TEXT,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (task_id) REFERENCES task(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO user (username, create_time) VALUES 
('张三', NOW()),
('李四', NOW()),
('王五', NOW());

INSERT INTO task (title, content, user_id, status, create_time, update_time) VALUES 
('代取快递', '请帮我到菜鸟驿站取一个快递，包裹号是SF1234567890，送到教学楼A栋301室。', 1, 0, NOW(), NOW()),
('图书馆占座', '早上8点前帮我在图书馆三楼自习室占一个座位，需要靠窗的位置。', 2, 1, NOW(), NOW()),
('作业辅导', '需要有人帮忙辅导高等数学作业，主要是微积分部分，时间可以商量。', 3, 2, NOW(), NOW());
