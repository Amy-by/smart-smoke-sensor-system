# BE-003-SysConfig查询结果过多

## 1. 错误描述
- 错误现象：后端服务启动时抛出 TooManyResultsException 异常，提示 "Expected one result (or null) to be returned by selectOne(), but found: 2"

### 错误现象
后端服务启动时抛出异常：
```
nested exception is org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 2
```

- 发生环境：
  - 操作系统：Linux
  - 系统版本：智能烟感系统后端 v3.9.0
  - Java 版本：11.0.29
  - 数据库：MySQL

- 复现步骤：
  1. 初始化数据库，创建 sys_config 表
  2. 插入多条配置数据
  3. 启动后端服务
  4. 服务在初始化 sysConfig 时抛出 TooManyResultsException

## 2. 错误分析
- 错误定位：/var/ctt_wkplace/iot/backend/src/main/resources/mybatis/system/SysConfigMapper.xml (selectConfig 方法，行37-40)
- 错误日志：
  ```
  02:00:44.544 [main] INFO  c.r.RuoYiApplication - Starting RuoYiApplication v3.9.0 using Java 11.0.29
  ...
  nested exception is org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 2
  ```
- 可能原因：
  - SysConfigMapper.xml 中的 selectConfig 查询没有限制结果数量
  - 当数据库中存在多条 sys_config 记录时，查询返回多条结果
  - MyBatis 尝试将多条结果映射到单个 SysConfig 对象时抛出异常
- 影响范围：
  - 影响模块：系统配置模块
  - 影响功能：系统初始化、配置加载
  - 影响用户群体：所有系统用户

## 3. 解决方案
- 解决思路：在 selectConfig 查询中添加 limit 1 子句，确保只返回一条结果，避免 MyBatis 映射异常
- 具体步骤：
  1. 打开 SysConfigMapper.xml 文件
  2. 在 selectConfig 查询的 SQL 语句末尾添加 limit 1
  3. 重新构建并启动服务
- 代码修改：
  ```xml
  <!-- 错误代码 -->
  <select id="selectConfig" parameterType="SysConfig" resultMap="SysConfigResult">
      <include refid="selectConfigVo"/>
      <include refid="sqlwhereSearch"/>
  </select>
  
  <!-- 修复后的代码 -->
  <select id="selectConfig" parameterType="SysConfig" resultMap="SysConfigResult">
      <include refid="selectConfigVo"/>
      <include refid="sqlwhereSearch"/>
      limit 1
  </select>
  ```
- 相关依赖：无

## 4. 验证结果
- 验证方法：
  1. 重新构建后端项目
  2. 启动后端服务
  3. 检查服务是否成功启动
  4. 验证系统配置功能是否正常
- 验证环境：
  - 操作系统：Linux
  - 系统版本：智能烟感系统后端 v3.9.0
  - Java 版本：11.0.29
- 验证结果：
  - 服务成功启动，不再抛出 TooManyResultsException 异常
  - 系统配置功能正常工作
  - 其他相关功能未受影响

## 5. 相关信息
- 记录人：开发团队
- 记录时间：2025-07-01 00:00:00
- 解决人：开发团队
- 解决时间：2025-07-01 00:30:00
- 严重程度：致命
- 关联任务：系统初始化修复
- 关联文档：无
- 备注：该错误是由于 MyBatis 查询设计不当导致的，添加 limit 1 限制后问题解决。后续开发中应注意查询返回结果的数量控制，避免类似问题再次发生。