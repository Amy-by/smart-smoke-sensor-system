package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysRole;
import com.ruoyi.project.system.service.impl.SysRoleServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 角色管理服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleServiceImpl roleService;

    @Test
    public void testSelectRoleList() {
        // 测试查询角色列表
        SysRole role = new SysRole();
        List<SysRole> roleList = roleService.selectRoleList(role);
        assertNotNull(roleList);
        assertTrue(roleList.size() > 0);
    }

    @Test
    public void testSelectRoleById() {
        // 测试根据角色ID查询角色信息
        Long roleId = 1L;
        SysRole role = roleService.selectRoleById(roleId);
        assertNotNull(role);
        assertEquals(roleId, role.getRoleId());
    }

    @Test
    public void testCheckRoleNameUnique() {
        // 测试角色名称唯一性校验
        SysRole role = new SysRole();
        role.setRoleName("管理员");
        role.setRoleId(1L);
        boolean result = roleService.checkRoleNameUnique(role);
        // 管理员角色已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testCheckRoleKeyUnique() {
        // 测试角色权限字符串唯一性校验
        SysRole role = new SysRole();
        role.setRoleKey("admin");
        role.setRoleId(1L);
        boolean result = roleService.checkRoleKeyUnique(role);
        // admin角色权限字符串已存在，所以应该返回true（唯一）
        assertTrue(result);
    }
}
