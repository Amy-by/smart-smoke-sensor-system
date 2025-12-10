# BE-0016-JUnit4到JUnit5迁移问题

## 1. 错误描述

- **错误标题**：JUnit版本不兼容导致单元测试无法执行
- **错误现象**：编写的单元测试类使用了JUnit 4的注解和断言方法，但项目实际使用JUnit 5，导致测试编译失败，执行时显示"Tests run: 0"
- **发生环境**：
  - 操作系统版本：Linux
  - 系统版本号：Spring Boot 2.5.6
  - Maven版本：3.6.3
- **复现步骤**：
  ```
  1. 创建使用JUnit 4注解（@Before、@Test(expected)）的测试类
  2. 编写测试方法使用JUnit 4断言（Assert.assertEquals）
  3. 执行mvn test命令
  4. 观察到编译失败或测试不执行
  ```
- **错误截图**：
  ```
  [ERROR] /var/ctt_wkplace/iot/backend/src/test/java/com/iot/alarm/rule/AlarmRuleEngineTest.java:[4,17] package org.junit does not exist
  [ERROR] /var/ctt_wkplace/iot/backend/src/test/java/com/iot/alarm/rule/AlarmRuleEngineTest.java:[5,17] package org.junit does not exist
  [ERROR] /var/ctt_wkplace/iot/backend/src/test/java/com/iot/alarm/rule/AlarmRuleEngineTest.java:[6,17] package org.junit does not exist
  ```

## 2. 错误分析

- **错误定位**：
  - 文件路径：/var/ctt_wkplace/iot/backend/src/test/java/com/iot/alarm/rule/AlarmRuleEngineTest.java
  - 主要问题：JUnit 4注解和断言方法的使用
- **错误日志**：编译错误提示"package org.junit does not exist"、"cannot find symbol"等
- **可能原因**：
  - 项目依赖的是JUnit 5，但测试类使用了JUnit 4的API
  - JUnit 5与JUnit 4的包结构和注解有较大差异
  - 未正确导入JUnit 5的断言方法
- **影响范围**：
  - 影响的功能模块：报警规则引擎单元测试
  - 影响的用户群体：开发人员
  - 影响的业务流程：测试驱动开发流程

## 3. 解决方案

- **解决思路**：将JUnit 4的测试类迁移到JUnit 5，更新注解和断言方法
- **具体步骤**：
  1. 修改测试类的导入语句，使用JUnit 5的包
  2. 将@Before注解替换为@BeforeEach
  3. 将@Test(expected)替换为assertThrows方法
  4. 更新断言方法的导入，使用org.junit.jupiter.api.Assertions.*
- **代码修改**：
  ```java
  // 错误的JUnit 4导入
  import org.junit.Before;
  import org.junit.Test;
  import static org.junit.Assert.*;
  
  // 修复后的JUnit 5导入
  import org.junit.jupiter.api.BeforeEach;
  import org.junit.jupiter.api.Test;
  import static org.junit.jupiter.api.Assertions.*;
  
  // 错误的测试方法（使用@Test(expected)）
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteInvalidRule() {
      // 测试代码
  }
  
  // 修复后的测试方法（使用assertThrows）
  @Test
  public void testExecuteInvalidRule() {
      assertThrows(IllegalArgumentException.class, () -> {
          // 测试代码
      });
  }
  ```
- **相关依赖**：
  ```xml
  <!-- 项目使用的JUnit 5依赖 -->
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
      <exclusions>
          <exclusion>
              <groupId>org.junit.vintage</groupId>
              <artifactId>junit-vintage-engine</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  ```

## 4. 验证结果

- **验证方法**：执行mvn test命令，检查测试是否正常编译和执行
- **验证环境**：
  - 操作系统版本：Linux
  - 系统版本号：Spring Boot 2.5.6
  - Maven版本：3.6.3
- **验证结果**：
  - 错误是否复现：否
  - 功能是否正常工作：是，测试类正常编译和执行
  - 性能是否受影响：否
  - 其他相关功能是否正常：是，所有报警相关测试均通过
- **测试截图**：
  ```
  [INFO] Running com.iot.alarm.rule.AlarmRuleEngineTest
  [INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.314 s - in com.iot.alarm.rule.AlarmRuleEngineTest
  ```

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2025-12-11 10:30:00
- **解决人**：开发人员
- **解决时间**：2025-12-11 11:00:00
- **严重程度**：中等
- **关联任务**：报警规则引擎开发
- **关联文档**：开发流程计划文档（任务4.2）
- **备注**：此问题提醒在编写单元测试前，应先确认项目使用的JUnit版本，并使用相应的API和注解。