
CREATE DATABASE IF NOT EXISTS komima_task DEFAULT CHARACTER SET utf8mb4;

USE komima_task;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role INT DEFAULT 0,
    nickname VARCHAR(50),
    gender INT DEFAULT 0,
    college VARCHAR(100),
    major VARCHAR(100),
    phone VARCHAR(20),
    wechat VARCHAR(50),
    qq VARCHAR(20),
    email VARCHAR(100),
    create_time DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    user_id INT NOT NULL,
    status INT DEFAULT 0,
    receiver_id INT,
    category INT DEFAULT 0,
    has_reward INT DEFAULT 0,
    publisher_confirm INT DEFAULT 0,
    receiver_confirm INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS application (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    applicant_id INT NOT NULL,
    status INT DEFAULT 0,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (task_id) REFERENCES task(id),
    FOREIGN KEY (applicant_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS evaluate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    from_user_id INT NOT NULL,
    to_user_id INT NOT NULL,
    score INT NOT NULL,
    content TEXT,
    create_time DATETIME NOT NULL,
    FOREIGN KEY (task_id) REFERENCES task(id),
    FOREIGN KEY (from_user_id) REFERENCES user(id),
    FOREIGN KEY (to_user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
