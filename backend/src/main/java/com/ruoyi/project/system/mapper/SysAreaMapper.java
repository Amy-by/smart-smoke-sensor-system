package com.ruoyi.project.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.project.system.domain.SysArea;

/**
 * 区域管理 数据层
 * 
 * @author ruoyi
 */
public interface SysAreaMapper
{
    /**
     * 查询区域管理数据
     * 
     * @param area 区域信息
     * @return 区域信息集合
     */
    public List<SysArea> selectAreaList(SysArea area);

    /**
     * 根据区域ID查询信息
     * 
     * @param areaId 区域ID
     * @return 区域信息
     */
    public SysArea selectAreaById(Long areaId);

    /**
     * 根据ID查询所有子区域
     * 
     * @param areaId 区域ID
     * @return 区域列表
     */
    public List<SysArea> selectChildrenAreaById(Long areaId);

    /**
     * 根据ID查询所有子区域（正常状态）
     * 
     * @param areaId 区域ID
     * @return 子区域数
     */
    public int selectNormalChildrenAreaById(Long areaId);

    /**
     * 是否存在子节点
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    public int hasChildByAreaId(Long areaId);

    /**
     * 查询区域是否存在用户
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    public int checkAreaExistUser(Long areaId);

    /**
     * 校验区域名称是否唯一
     * 
     * @param areaName 区域名称
     * @param parentId 父区域ID
     * @return 结果
     */
    public SysArea checkAreaNameUnique(@Param("areaName") String areaName, @Param("parentId") Long parentId);

    /**
     * 新增区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    public int insertArea(SysArea area);

    /**
     * 修改区域信息
     * 
     * @param area 区域信息
     * @return 结果
     */
    public int updateArea(SysArea area);

    /**
     * 修改所在区域正常状态
     * 
     * @param areaIds 区域ID组
     */
    public void updateAreaStatusNormal(Long[] areaIds);

    /**
     * 删除区域管理信息
     * 
     * @param areaId 区域ID
     * @return 结果
     */
    public int deleteAreaById(Long areaId);

    /**
     * 修改子元素关系
     * 
     * @param areas 子元素
     * @return 结果
     */
    public int updateAreaChildren(@Param("areas") List<SysArea> areas);
    
    /**
     * 根据角色ID查询区域列表
     * 
     * @param roleId 角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 区域列表
     */
    public List<Long> selectAreaListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);
}
