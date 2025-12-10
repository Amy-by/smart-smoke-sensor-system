# BE-0017-依赖重复声明和MySQL驱动重定位警告

## 1. 错误描述

- **错误标题**：Maven编译时依赖重复声明和MySQL驱动重定位警告
- **错误现象**：在执行`mvn compile`命令编译后端代码时，出现依赖重复声明和MySQL驱动重定位的警告信息
- **发生环境**：
  - 操作系统版本：Linux
  - 网络环境：本地开发环境
  - 系统版本号：开发中版本
- **复现步骤**：
  ```
  1. 进入后端项目目录：cd /var/ctt_wkplace/iot/backend
  2. 执行编译命令：mvn compile
  3. 观察编译输出信息
  ```
- **错误截图**：无

## 2. 错误分析

- **错误定位**：
  - 文件路径：/var/ctt_wkplace/iot/backend/pom.xml
  - 函数/方法名：无
  - 行号：无
- **错误日志**：
  ```
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: com.alibaba:druid:jar -> version 1.2.11 vs 1.2.14 @ line 351, column 17
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: com.baomidou:mybatis-plus-boot-starter:jar -> version 3.5.2 vs 3.5.3.1 @ line 358, column 17
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: com.baomidou:mybatis-plus-generator:jar -> version 3.5.2 vs 3.5.3.1 @ line 372, column 17
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: com.baomidou:mybatis-plus-extension:jar -> version 3.5.2 vs 3.5.3.1 @ line 365, column 17
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: org.apache.velocity:velocity-engine-core:jar -> version 2.3 vs 2.3.1 @ line 386, column 17
  [WARNING] 'dependencies.dependency.(groupId:artifactId:type:classifier)' must be unique: mysql:mysql-connector-java:jar -> version 8.0.30 vs 8.0.31 @ line 393, column 17
  [WARNING] mysql:mysql-connector-java:8.0.31 is relocated to com.mysql:mysql-connector-j:8.0.31
  ```
- **可能原因**：
  - pom.xml文件中重复声明了相同的依赖，但版本不同
  - MySQL驱动的groupId发生了变化，从mysql:mysql-connector-java变为com.mysql:mysql-connector-j
- **影响范围**：
  - 影响的功能模块：整个后端项目编译过程
  - 影响的用户群体：开发人员
  - 影响的业务流程：开发编译流程

## 3. 解决方案

- **解决思路**：
  1. 移除pom.xml中重复声明的依赖，只保留一个版本
  2. 更新MySQL驱动的groupId和artifactId到最新规范
- **具体步骤**：
  1. 查看pom.xml文件中重复声明的依赖
  2. 选择合适的版本保留
  3. 删除重复的依赖声明
  4. 更新MySQL驱动的groupId和artifactId
- **代码修改**：
  ```xml
  <!-- 错误代码示例（重复声明） -->
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.11</version>
  </dependency>
  <!-- 其他依赖 -->
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.14</version>
  </dependency>
  
  <!-- MySQL驱动旧版本 -->
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.31</version>
  </dependency>
  
  <!-- 修复后的代码 -->
  <!-- 只保留一个版本的依赖 -->
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.2.14</version>
  </dependency>
  
  <!-- MySQL驱动新版本 -->
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <version>8.0.31</version>
  </dependency>
  ```
- **相关依赖**：无

## 4. 验证结果

- **验证方法**：执行`mvn compile`命令重新编译项目
- **验证环境**：
  - 操作系统版本：Linux
  - 网络环境：本地开发环境
- **验证结果**：
  - 错误是否复现：否（警告消失）
  - 功能是否正常工作：是
  - 性能是否受影响：否
  - 其他相关功能是否正常：是
- **测试截图**：无

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2025-12-10 14:30:00
- **解决人**：开发人员
- **解决时间**：2025-12-10 14:45:00
- **严重程度**：轻微错误（Low）
- **关联任务**：任务4.3（报警处理器开发）
- **关联文档**：开发流程计划.md
- **备注**：这些警告不影响代码编译和运行，但为了代码质量，建议修复这些依赖问题