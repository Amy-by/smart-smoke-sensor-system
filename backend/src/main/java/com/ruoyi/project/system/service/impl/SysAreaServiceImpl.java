package com.ruoyi.project.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.system.domain.SysArea;
import com.ruoyi.project.system.domain.SysRole;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysAreaMapper;
import com.ruoyi.project.system.mapper.SysRoleMapper;
import com.ruoyi.project.system.service.ISysAreaService;

/**
 * 区域管理 服务实现
 * 
 * @author ruoyi
 */
@Service
public class SysAreaServiceImpl implements ISysAreaService
{
    @Autowired
    private SysAreaMapper areaMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询区域管理数据
     * 
     * @param area 区域信息
     * @return 区域信息集合
     */
    @Override
    @DataScope(deptAlias = "a")
    public List<SysArea> selectAreaList(SysArea area)
    {
        return areaMapper.selectAreaList(area);
    }
    
    /**
     * 查询区域树结构信息
     * 
     * @param area 区域信息
     * @return 区域树信息集合
     */
    @Override
    public List<TreeSelect> selectAreaTreeList(SysArea area)
    {
        List<SysArea> areas = SpringUtils.getAopProxy(this).selectAreaList(area);
        return buildAreaTreeSelect(areas);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param areas 区域列表
     * @return 树结构列表
     */
    @Override
    public List<SysArea> buildAreaTree(List<SysArea> areas)
    {
        List<SysArea> returnList = new ArrayList<SysArea>();
        List<Long> tempList = areas.stream().map(SysArea::getAreaId).collect(Collectors.toList());
        for (SysArea area : areas)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(area.getParentId()))
            {
                recursionFn(areas, area);
                returnList.add(area);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = areas;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param areas 区域列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildAreaTreeSelect(List<SysArea> areas)
    {
        List<SysArea> areaTrees = buildAreaTree(areas);
        return areaTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据区域ID查询信息
     * 
     * @param areaId 区域ID
     * @return 区域信息
     */
    @Override
    public SysArea selectAreaById(Long areaId)
    {
        return areaMapper.selectAreaById(areaId);
    }

    /**
     * 根据ID查询所有子区域（正常状态）
     * 
     * @param areaId 区域ID
     * @return 子区域数
     */
    @Override
    public int selectNormalChildrenAreaById(Long areaId)
    {
        return areaMapper.selectNormalChildrenAreaById(areaId);
    }

    /**
     * 是否存在子节点
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    @Override
    public boolean hasChildByAreaId(Long areaId)
    {
        int result = areaMapper.hasChildByAreaId(areaId);
        return result > 0;
    }

    /**
     * 校验区域名称是否唯一
     * 
     * @param area 区域信息
     * @return 结果
     */
    @Override
    public boolean checkAreaNameUnique(SysArea area)
    {
        Long areaId = StringUtils.isNull(area.getAreaId()) ? -1L : area.getAreaId();
        SysArea info = areaMapper.checkAreaNameUnique(area.getAreaName(), area.getParentId());
        if (StringUtils.isNotNull(info) && info.getAreaId().longValue() != areaId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    
    /**
     * 查询区域是否存在用户
     * 
     * @param areaId 区域ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkAreaExistUser(Long areaId)
    {
        int result = areaMapper.checkAreaExistUser(areaId);
        return result > 0;
    }

    /**
     * 校验区域是否有数据权限
     * 
     * @param areaId 区域id
     */
    @Override
    public void checkAreaDataScope(Long areaId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotNull(areaId))
        {
            SysArea area = new SysArea();
            area.setAreaId(areaId);
            List<SysArea> areas = SpringUtils.getAopProxy(this).selectAreaList(area);
            if (StringUtils.isEmpty(areas))
            {
                throw new ServiceException("没有权限访问区域数据！");
            }
        }
    }
    
    /**
     * 根据角色ID查询区域列表
     * 
     * @param roleId 角色ID
     * @return 区域列表
     */
    @Override
    public List<Long> selectAreaListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return areaMapper.selectAreaListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 新增保存区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    @Override
    public int insertArea(SysArea area)
    {
        SysArea info = areaMapper.selectAreaById(area.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!StringUtils.isNull(info) && StringUtils.equals(UserConstants.AREA_DISABLE, info.getStatus()))
        {
            throw new ServiceException("区域停用，不允许新增");
        }
        area.setAncestors(info.getAncestors() + "," + info.getAreaId());
        return areaMapper.insertArea(area);
    }

    /**
     * 修改保存区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    @Override
    public int updateArea(SysArea area)
    {
        SysArea newArea = areaMapper.selectAreaById(area.getAreaId());
        SysArea oldArea = areaMapper.selectAreaById(area.getParentId());
        if (!StringUtils.isNull(newArea) && !StringUtils.isNull(oldArea))
        {
            String newAncestors = oldArea.getAncestors() + "," + oldArea.getAreaId();
            String oldAncestors = newArea.getAncestors();
            area.setAncestors(newAncestors);
            updateAreaChildren(newArea.getAreaId(), newAncestors, oldAncestors);
        }
        int result = areaMapper.updateArea(area);
        if (StringUtils.equals(UserConstants.AREA_NORMAL, area.getStatus()) && StringUtils.isNotEmpty(area.getAncestors()))
        {
            // 如果该区域是启用状态，则启用该区域的所有上级区域
            updateParentAreaStatusNormal(area);
        }
        return result;
    }

    /**
     * 修改子元素关系
     * 
     * @param areaId 被修改的区域ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateAreaChildren(Long areaId, String newAncestors, String oldAncestors)
    {
        List<SysArea> children = areaMapper.selectChildrenAreaById(areaId);
        for (SysArea child : children)
        {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            areaMapper.updateAreaChildren(children);
        }
    }

    /**
     * 修改区域状态
     * 
     * @param area 区域信息
     * @return 结果
     */
    public int updateAreaStatus(SysArea area)
    {
        return areaMapper.updateArea(area);
    }

    /**
     * 删除区域管理信息
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    @Override
    public int deleteAreaById(Long areaId)
    {
        return areaMapper.deleteAreaById(areaId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysArea> list, SysArea t)
    {
        // 得到子节点列表
        List<SysArea> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysArea tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysArea> getChildList(List<SysArea> list, SysArea t)
    {
        List<SysArea> tlist = new ArrayList<SysArea>();
        Iterator<SysArea> it = list.iterator();
        while (it.hasNext())
        {
            SysArea n = (SysArea) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getAreaId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysArea> list, SysArea t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 递归更新上级区域状态为正常
     * 
     * @param area 当前区域
     */
    private void updateParentAreaStatusNormal(SysArea area)
    {
        String ancestors = area.getAncestors();
        Long[] areaIds = Convert.toLongArray(ancestors);
        areaMapper.updateAreaStatusNormal(areaIds);
    }
}
