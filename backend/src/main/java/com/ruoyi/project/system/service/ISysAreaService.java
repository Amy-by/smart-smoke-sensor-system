package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.framework.web.domain.TreeSelect;
import com.ruoyi.project.system.domain.SysArea;

/**
 * 区域管理 服务层
 * 
 * @author ruoyi
 */
public interface ISysAreaService
{
    /**
     * 查询区域管理数据
     * 
     * @param area 区域信息
     * @return 区域信息集合
     */
    public List<SysArea> selectAreaList(SysArea area);

    /**
     * 查询区域树结构信息
     * 
     * @param area 区域信息
     * @return 区域树信息集合
     */
    public List<TreeSelect> selectAreaTreeList(SysArea area);

    /**
     * 构建前端所需要树结构
     * 
     * @param areas 区域列表
     * @return 树结构列表
     */
    public List<SysArea> buildAreaTree(List<SysArea> areas);

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param areas 区域列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildAreaTreeSelect(List<SysArea> areas);

    /**
     * 根据区域ID查询信息
     * 
     * @param areaId 区域ID
     * @return 区域信息
     */
    public SysArea selectAreaById(Long areaId);

    /**
     * 根据ID查询所有子区域（正常状态）
     * 
     * @param areaId 区域ID
     * @return 子区域数
     */
    public int selectNormalChildrenAreaById(Long areaId);

    /**
     * 是否存在区域子节点
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    public boolean hasChildByAreaId(Long areaId);

    /**
     * 查询区域是否存在用户
     * 
     * @param areaId 区域ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkAreaExistUser(Long areaId);
    
    /**
     * 根据角色ID查询区域列表
     * 
     * @param roleId 角色ID
     * @return 区域列表
     */
    public List<Long> selectAreaListByRoleId(Long roleId);

    /**
     * 校验区域名称是否唯一
     * 
     * @param area 区域信息
     * @return 结果
     */
    public boolean checkAreaNameUnique(SysArea area);

    /**
     * 校验区域是否有数据权限
     * 
     * @param areaId 区域id
     */
    public void checkAreaDataScope(Long areaId);

    /**
     * 新增保存区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    public int insertArea(SysArea area);

    /**
     * 修改保存区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    public int updateArea(SysArea area);

    /**
     * 删除区域管理信息
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    public int deleteAreaById(Long areaId);
}
