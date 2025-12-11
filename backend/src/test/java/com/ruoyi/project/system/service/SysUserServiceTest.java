package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.impl.SysUserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 用户管理服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserServiceTest {

    @Autowired
    private SysUserServiceImpl userService;

    @Test
    public void testSelectUserByUserName() {
        // 测试根据用户名查询用户
        String userName = "admin";
        SysUser user = userService.selectUserByUserName(userName);
        assertNotNull(user);
        assertEquals(userName, user.getUserName());
    }

    @Test
    public void testSelectUserById() {
        // 测试根据用户ID查询用户
        Long userId = 1L;
        SysUser user = userService.selectUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @Test
    public void testCheckUserNameUnique() {
        // 测试用户名唯一性校验
        SysUser user = new SysUser();
        user.setUserName("admin");
        user.setUserId(1L);
        boolean result = userService.checkUserNameUnique(user);
        // admin用户名已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testCheckPhoneUnique() {
        // 测试手机号码唯一性校验
        SysUser user = new SysUser();
        user.setPhonenumber("13800138000");
        user.setUserId(1L);
        boolean result = userService.checkPhoneUnique(user);
        // 该手机号码已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testCheckEmailUnique() {
        // 测试邮箱唯一性校验
        SysUser user = new SysUser();
        user.setEmail("admin@example.com");
        user.setUserId(1L);
        boolean result = userService.checkEmailUnique(user);
        // 该邮箱已存在，所以应该返回true（唯一）
        assertTrue(result);
    }
}