package com.komima.config;

import com.komima.entity.Task;
import com.komima.entity.User;
import com.komima.mapper.ApplicationMapper;
import com.komima.mapper.TaskMapper;
import com.komima.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Statement;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public void run(String... args) throws Exception {
        initTables();
        if (!userMapper.selectAll().isEmpty()) return;

        User u1 = insertUser("admin", "admin", 1, "系统管理员");
        User u2 = insertUser("xiaoming", "123456", 0, "小明");
        User u3 = insertUser("xiaohong", "123456", 0, "小红");
        User u4 = insertUser("xiaogang", "123456", 0, "小刚");

        insertTask("代取快递", "菜鸟驿站中通快递，取件码8-3-5021，送到12号宿舍楼301室。", u2.getId(), 0, 1, 1, 1, 0);
        insertTask("图书馆代占座", "明天早上7:30在图书馆三楼东侧靠窗占两个座位。", u3.getId(), 0, 1, 0, 1, 1);
        insertTask("高数线上辅导", "大一高数第二章导数部分没听懂，求辅导，周三下午或周五晚。", u4.getId(), 0, 2, 1, 1, 2);
        insertTask("帮忙取外卖", "今晚6点半南门取美团外卖两份，送教学楼B栋201。", u2.getId(), 1, 1, 1, 1, 0);
        insertTask("二手课本求购", "求购《线性代数》第六版，八成新以上，20元左右。", u3.getId(), 2, 1, 1, 1, 0);
        insertTask("招募项目队友", "Java课设招募两名队友，需熟练Spring Boot，速联系。", u2.getId(), 0, 0, 0, 2, 0);
        insertTask("招募篮球比赛选手", "周日下午友谊赛招募两名外线球员。", u3.getId(), 0, 0, 1, 2, 0);
        insertTask("急求家教", "初三数学家教，每周两次，时薪80元。", u4.getId(), 0, 2, 1, 1, 0);
    }

    private void initTables() throws Exception {
        Statement s = dataSource.getConnection().createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL, password VARCHAR(100) NOT NULL," +
                "role INT DEFAULT 0, nickname VARCHAR(50), gender INT DEFAULT 0," +
                "college VARCHAR(100), major VARCHAR(100), phone VARCHAR(20)," +
                "wechat VARCHAR(50), qq VARCHAR(20), email VARCHAR(100)," +
                "create_time DATETIME NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        s.execute("CREATE TABLE IF NOT EXISTS task (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100) NOT NULL, content TEXT NOT NULL," +
                "user_id INT NOT NULL, status INT DEFAULT 0," +
                "category INT DEFAULT 0, has_reward INT DEFAULT 0, recruit_count INT DEFAULT 1," +
                "create_time DATETIME NOT NULL, update_time DATETIME NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        s.execute("CREATE TABLE IF NOT EXISTS application (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, task_id INT NOT NULL, applicant_id INT NOT NULL," +
                "status INT DEFAULT 0, confirm_publisher INT DEFAULT 0, confirm_applicant INT DEFAULT 0," +
                "create_time DATETIME NOT NULL," +
                "FOREIGN KEY (task_id) REFERENCES task(id), FOREIGN KEY (applicant_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        s.execute("CREATE TABLE IF NOT EXISTS evaluate (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, task_id INT NOT NULL," +
                "from_user_id INT NOT NULL, to_user_id INT NOT NULL," +
                "score INT NOT NULL, content TEXT, create_time DATETIME NOT NULL," +
                "FOREIGN KEY (task_id) REFERENCES task(id)," +
                "FOREIGN KEY (from_user_id) REFERENCES user(id)," +
                "FOREIGN KEY (to_user_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        s.close();
        s.getConnection().close();
    }

    private User insertUser(String username, String password, int role, String nickname) {
        User u = new User()
                .setUsername(username).setPassword(password)
                .setRole(role).setNickname(nickname).setCreateTime(LocalDateTime.now());
        userMapper.insert(u);
        return u;
    }

    private void insertTask(String title, String content, int userId, int status, int category, int hasReward, int recruitCount, int delayDays) {
        LocalDateTime now = LocalDateTime.now();
        Task t = new Task()
                .setTitle(title).setContent(content).setUserId(userId)
                .setStatus(status).setCategory(category).setHasReward(hasReward)
                .setRecruitCount(recruitCount)
                .setCreateTime(now.minusDays(delayDays)).setUpdateTime(now.minusDays(delayDays));
        taskMapper.insert(t);
    }
}
