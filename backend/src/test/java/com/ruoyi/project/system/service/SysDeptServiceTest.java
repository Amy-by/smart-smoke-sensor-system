package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.impl.SysDeptServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 部门管理服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysDeptServiceTest {

    @Autowired
    private SysDeptServiceImpl deptService;

    @Test
    public void testSelectDeptList() {
        // 测试查询部门列表
        SysDept dept = new SysDept();
        dept.setDeptName("系统管理");
        dept.setParentId(0L);
        dept.setDeptId(1L);
        boolean result = deptService.checkDeptNameUnique(dept);
        // 系统管理部门已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testSelectDeptById() {
        // 测试根据部门ID查询部门信息
        Long deptId = 1L;
        SysDept dept = deptService.selectDeptById(deptId);
        assertNotNull(dept);
        assertEquals(deptId, dept.getDeptId());
    }

    @Test
    public void testHasChildByDeptId() {
        // 测试是否存在部门子节点
        Long deptId = 1L;
        boolean result = deptService.hasChildByDeptId(deptId);
        // 系统管理部门下应该有子节点
        assertTrue(result);
    }

    @Test
    public void testCheckDeptNameUnique() {
        // 测试部门名称唯一性校验
        SysDept dept = new SysDept();
        dept.setDeptName("系统管理");
        dept.setParentId(0L);
        dept.setDeptId(1L);
        boolean result = deptService.checkDeptNameUnique(dept);
        // 系统管理部门已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testSelectNormalChildrenDeptById() {
        // 测试查询正常状态的子部门数量
        Long deptId = 1L;
        int count = deptService.selectNormalChildrenDeptById(deptId);
        // 系统管理部门下应该有子部门
        assertTrue(count > 0);
    }
}