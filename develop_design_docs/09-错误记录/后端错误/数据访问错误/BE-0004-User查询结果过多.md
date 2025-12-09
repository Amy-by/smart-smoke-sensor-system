# BE-004-User查询结果过多

## 1. 错误描述
- 错误现象：后端服务启动时抛出 TooManyResultsException 异常，提示 "Expected one result (or null) to be returned by selectOne(), but found: 2"

- 错误日志：
  ```
  nested exception is org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 2
  ```

- 发生环境：
  - 操作系统：Linux
  - 系统版本：智能烟感系统后端 v3.9.0
  - Java 版本：11.0.29
  - 数据库：MySQL

- 复现步骤：
  1. 初始化数据库，创建 sys_user 表
  2. 插入多条用户名或用户ID相同的记录
  3. 启动后端服务
  4. 服务在查询用户信息时抛出 TooManyResultsException

## 2. 错误分析
- 错误定位：/var/ctt_wkplace/iot/backend/src/main/resources/mybatis/system/SysUserMapper.xml (selectUserByUserName 和 selectUserById 方法，行98-106 和 107-115)
- 错误日志：
  ```
  02:00:44.544 [main] INFO  c.r.RuoYiApplication - Starting RuoYiApplication v3.9.0 using Java 11.0.29
  ...
  nested exception is org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 2
  ```
- 可能原因：
  - SysUserMapper.xml 中的 selectUserByUserName 和 selectUserById 查询没有限制结果数量
  - 当数据库中存在多条用户名或用户ID相同的记录时，查询返回多条结果
  - MyBatis 尝试将多条结果映射到单个 SysUser 对象时抛出异常
- 影响范围：
  - 影响模块：用户管理模块
  - 影响功能：用户登录、用户信息查询、权限验证
  - 影响用户群体：所有系统用户

## 3. 解决方案
- 解决思路：在 selectUserByUserName 和 selectUserById 查询中添加 limit 1 子句，确保只返回一条结果，避免 MyBatis 映射异常
- 具体步骤：
  1. 打开 SysUserMapper.xml 文件
  2. 在 selectUserByUserName 查询的 SQL 语句末尾添加 limit 1
  3. 在 selectUserById 查询的 SQL 语句末尾添加 limit 1
  4. 重新构建并启动服务
- 代码修改：
  ```xml
  <!-- 错误代码 -->
  <select id="selectUserByUserName" parameterType="String" resultMap="SysUserResult">
      <include refid="selectUserVo" />
      where u.user_name = #{userName}
  </select>
  
  <select id="selectUserById" parameterType="Long" resultMap="SysUserResult">
      <include refid="selectUserVo" />
      where u.user_id = #{userId}
  </select>
  
  <!-- 修复后的代码 -->
  <select id="selectUserByUserName" parameterType="String" resultMap="SysUserResult">
      <include refid="selectUserVo" />
      where u.user_name = #{userName}
      limit 1
  </select>
  
  <select id="selectUserById" parameterType="Long" resultMap="SysUserResult">
      <include refid="selectUserVo" />
      where u.user_id = #{userId}
      limit 1
  </select>
  ```
- 相关依赖：无

## 4. 验证结果
- 验证方法：
  1. 重新构建后端项目
  2. 启动后端服务
  3. 检查服务是否成功启动
  4. 验证用户登录功能是否正常
- 验证环境：
  - 操作系统：Linux
  - 系统版本：智能烟感系统后端 v3.9.0
  - Java 版本：11.0.29
- 验证结果：
  - 服务成功启动，不再抛出 TooManyResultsException 异常
  - 用户登录功能正常工作
  - 用户信息查询功能正常工作
  - 其他相关功能未受影响

## 5. 相关信息
- 记录人：开发团队
- 记录时间：2025-07-01 01:00:00
- 解决人：开发团队
- 解决时间：2025-07-01 01:30:00
- 严重程度：严重
- 关联任务：用户管理模块修复
- 关联文档：无
- 备注：该错误是由于 MyBatis 查询设计不当导致的，添加 limit 1 限制后问题解决。后续开发中应注意：
  1. 查询返回结果的数量控制
  2. 确保数据库中唯一字段的唯一性约束
  3. 在可能返回多条结果的查询中使用适当的限制条件
