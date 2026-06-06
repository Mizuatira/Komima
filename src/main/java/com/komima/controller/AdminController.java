package com.komima.controller;

import com.komima.dto.ApiResponse;
import com.komima.entity.User;
import com.komima.entity.Task;
import com.komima.service.UserService;
import com.komima.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 管理员控制器
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @GetMapping("/users")
    public ApiResponse<List<User>> users() {
        return ApiResponse.success(userService.getAll());
    }

    /**
     * 更新用户角色
     * @param id 用户ID
     * @param role 角色
     * @return 响应结果
     */
    @PutMapping("/user/{id}/role")
    public ApiResponse<Void> updateRole(@PathVariable Integer id, @RequestParam Integer role) {
        userService.updateRole(id, role);
        return ApiResponse.success("角色已更新", null);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 响应结果
     */
    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ApiResponse.success("已删除", null);
    }

    /**
     * 获取所有任务列表
     * @return 任务列表
     */
    @GetMapping("/tasks")
    public ApiResponse<List<Task>> tasks() {
        return ApiResponse.success(taskService.listAll());
    }

    /**
     * 删除任务
     * @param id 任务ID
     * @return 响应结果
     */
    @DeleteMapping("/task/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ApiResponse.success("已删除", null);
    }

    /**
     * 导出用户列表为CSV
     * @param response HTTP响应
     * @throws Exception IO异常
     */
    @GetMapping("/export/users")
    public void exportUsers(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=users.csv");
        PrintWriter w = response.getWriter();
        w.println("\uFEFFID,用户名,昵称,角色,性别,学院,专业,手机,微信,QQ,邮箱,注册时间");
        for (User u : userService.getAll()) {
            w.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    u.getId(), csv(u.getUsername()), csv(u.getNickname()),
                    u.getRole() == 1 ? "管理员" : "普通用户",
                    gender(u.getGender()), csv(u.getCollege()), csv(u.getMajor()),
                    csv(u.getPhone()), csv(u.getWechat()), csv(u.getQq()),
                    csv(u.getEmail()), u.getCreateTime());
        }
        w.flush();
    }

    /**
     * 导出任务列表为CSV
     * @param response HTTP响应
     * @throws Exception IO异常
     */
    @GetMapping("/export/tasks")
    public void exportTasks(HttpServletResponse response) throws Exception {
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=tasks.csv");
        PrintWriter w = response.getWriter();
        w.println("\uFEFFID,标题,内容,发布人ID,状态,分类,有报酬,招募人数,创建时间,更新时间");
        for (Task t : taskService.listAll()) {
            w.printf("%d,%s,%s,%d,%s,%s,%s,%d,%s,%s\n",
                    t.getId(), csv(t.getTitle()), csv(t.getContent()),
                    t.getUserId(), status(t.getStatus()), category(t.getCategory()),
                    t.getHasReward() == 1 ? "是" : "否",
                    t.getRecruitCount(), t.getCreateTime(), t.getUpdateTime());
        }
        w.flush();
    }

    /**
     * CSV字段转义
     * @param s 原始字符串
     * @return 转义后的字符串
     */
    private String csv(String s) { return s == null ? "" : "\"" + s.replace("\"", "\"\"") + "\""; }

    /**
     * 性别编码转文字
     * @param g 性别编码
     * @return 性别文字
     */
    private String gender(Integer g) {
        if (g == null) return "";
        return g == 1 ? "男" : g == 2 ? "女" : "未知";
    }

    /**
     * 任务状态编码转文字
     * @param s 状态编码
     * @return 状态文字
     */
    private String status(Integer s) {
        if (s == null) return "";
        switch (s) {
            case 0: return "待接单";
            case 1: return "进行中";
            case 2: return "已完成";
            case 3: return "已过期";
            default: return "未知";
        }
    }

    /**
     * 任务分类编码转文字
     * @param c 分类编码
     * @return 分类文字
     */
    private String category(Integer c) {
        if (c == null) return "";
        switch (c) {
            case 0: return "其他";
            case 1: return "跑腿";
            case 2: return "学习";
            default: return "未知";
        }
    }
}
