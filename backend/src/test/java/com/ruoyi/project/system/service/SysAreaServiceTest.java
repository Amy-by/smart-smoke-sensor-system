package com.ruoyi.project.system.service;

import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.system.domain.SysArea;
import com.ruoyi.project.system.service.impl.SysAreaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 区域管理服务测试类
 * 
 * @author ruoyi
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysAreaServiceTest {

    @Autowired
    private SysAreaServiceImpl areaService;

    @Test
    public void testSelectAreaList() {
        // 测试查询区域列表
        SysArea area = new SysArea();
        List<SysArea> areaList = areaService.selectAreaList(area);
        assertNotNull(areaList);
        assertTrue(areaList.size() > 0);
    }

    @Test
    public void testSelectAreaById() {
        // 测试根据区域ID查询区域信息
        Long areaId = 1L;
        SysArea area = areaService.selectAreaById(areaId);
        assertNotNull(area);
        assertEquals(areaId, area.getAreaId());
    }

    @Test
    public void testSelectAreaListByRoleId() {
        // 测试根据角色ID查询区域列表
        Long roleId = 1L;
        List<Long> areaIds = areaService.selectAreaListByRoleId(roleId);
        // 角色可能没有关联区域，所以只需要验证结果不为空
        assertNotNull(areaIds);
    }

    @Test
    public void testSelectAreaTreeList() {
        // 测试查询区域树列表
        SysArea area = new SysArea();
        List<TreeSelect> areaTreeList = areaService.selectAreaTreeList(area);
        assertNotNull(areaTreeList);
        // 至少应该有一个根节点
        assertTrue(areaTreeList.size() > 0);
    }

    @Test
    public void testCheckAreaNameUnique() {
        // 测试区域名称唯一性校验
        SysArea area = new SysArea();
        area.setAreaName("总部");
        area.setAreaId(1L);
        boolean result = areaService.checkAreaNameUnique(area);
        // 总部区域已存在，所以应该返回true（唯一）
        assertTrue(result);
    }

    @Test
    public void testHasChildByAreaId() {
        // 测试检查区域是否有子区域
        Long areaId = 1L; // 总部
        boolean result = areaService.hasChildByAreaId(areaId);
        // 总部应该有子区域
        assertTrue(result);
    }
}
