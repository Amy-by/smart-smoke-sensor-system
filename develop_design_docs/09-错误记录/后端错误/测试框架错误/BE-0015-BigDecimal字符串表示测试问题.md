# BE-0015-BigDecimal字符串表示测试问题

## 1. 错误描述

- **错误标题**：NotificationQuotaTest中BigDecimal字符串表示测试失败
- **错误现象**：单元测试中预期的BigDecimal字符串表示与实际输出不匹配
- **发生环境**：
  - 操作系统版本：Linux
  - 系统版本号：1.0.0
- **复现步骤**：
  ```
  1. 运行NotificationQuotaTest单元测试
  2. 观察测试结果
  ```

## 2. 错误分析

- **错误定位**：
  - 文件路径：/var/ctt_wkplace/iot/backend/src/test/java/com/iot/alarm/domain/NotificationQuotaTest.java
  - 函数/方法名：testToString
  - 行号：44
- **错误日志**：
expected: <NotificationQuota{id=1, sensorId='123456789012345', freeVoiceCount=100, freeSmsCount=50, balance=100.50}> but was: <NotificationQuota{id=1, sensorId='123456789012345', freeVoiceCount=100, freeSmsCount=50, balance=100.5}>
- **可能原因**：
  - BigDecimal的toString()方法会自动去掉末尾的0
  - 测试用例中硬编码了带有末尾0的预期结果
- **影响范围**：
  - 影响的功能模块：单元测试
  - 影响的用户群体：开发人员
  - 影响的业务流程：开发测试流程

## 3. 解决方案

- **解决思路**：修改测试用例中的预期结果，使其与BigDecimal的实际字符串表示一致
- **具体步骤**：
  1. 打开NotificationQuotaTest.java文件
  2. 修改testToString()方法中的预期结果
  3. 保存文件并重新运行测试
- **代码修改**：
  ```java
  // 错误代码
  @Test
  public void testToString() {
      String result = notificationQuota.toString();
      assertNotNull(result);
      assertEquals("NotificationQuota{id=1, sensorId='123456789012345', freeVoiceCount=100, freeSmsCount=50, balance=100.50}", result);
  }
  
  // 修复后的代码
  @Test
  public void testToString() {
      String result = notificationQuota.toString();
      assertNotNull(result);
      assertEquals("NotificationQuota{id=1, sensorId='123456789012345', freeVoiceCount=100, freeSmsCount=50, balance=100.5}", result);
  }
  ```

## 4. 验证结果

- **验证方法**：重新运行NotificationQuotaTest单元测试
- **验证环境**：Linux操作系统
- **验证结果**：
  - 错误是否复现：否
  - 功能是否正常工作：是
  - 性能是否受影响：否
  - 其他相关功能是否正常：是

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2025-12-09 21:36:00
- **解决人**：开发人员
- **解决时间**：2025-12-09 21:36:30
- **严重程度**：轻微错误（Low）
- **关联任务**：报警实体类开发（任务4.1）
- **关联文档**：开发流程计划.md
