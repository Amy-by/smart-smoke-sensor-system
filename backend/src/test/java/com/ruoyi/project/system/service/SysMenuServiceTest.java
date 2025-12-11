package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysMenu;
import com.ruoyi.project.system.service.impl.SysMenuServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * 菜单管理服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysMenuServiceTest {

    @Autowired
    private SysMenuServiceImpl menuService;

    @Test
    public void testSelectMenuList() {
        // 测试根据管理员用户查询菜单列表
        Long adminUserId = 1L;
        List<SysMenu> menuList = menuService.selectMenuList(adminUserId);
        assertNotNull(menuList);
        assertTrue(menuList.size() > 0);
    }

    @Test
    public void testSelectMenuPermsByUserId() {
        // 测试根据用户ID查询权限
        Long adminUserId = 1L;
        Set<String> permsSet = menuService.selectMenuPermsByUserId(adminUserId);
        assertNotNull(permsSet);
        assertTrue(permsSet.size() > 0);
    }

    @Test
    public void testSelectMenuById() {
        // 测试根据菜单ID查询菜单信息
        Long menuId = 1L;
        SysMenu menu = menuService.selectMenuById(menuId);
        assertNotNull(menu);
        assertEquals(menuId, menu.getMenuId());
    }

    @Test
    public void testHasChildByMenuId() {
        // 测试是否存在菜单子节点
        Long menuId = 1L;
        boolean result = menuService.hasChildByMenuId(menuId);
        // 系统管理菜单下应该有子节点
        assertTrue(result);
    }

    @Test
    public void testCheckMenuNameUnique() {
        // 测试菜单名称唯一性校验
        SysMenu menu = new SysMenu();
        menu.setMenuName("系统管理");
        menu.setParentId(0L);
        menu.setMenuId(1L);
        boolean result = menuService.checkMenuNameUnique(menu);
        // 系统管理菜单已存在，所以应该返回true（唯一）
        assertTrue(result);
    }
}