# BE-0014-MockMultipartFile版本兼容性问题

## 1. 错误描述

- **错误标题**：MockMultipartFile版本兼容性问题
- **错误现象**：在运行DeviceServiceImplTest单元测试时，编译失败，提示"cannot find symbol"错误，找不到MockMultipartFile的builder()方法
- **发生环境**：
  - 操作系统版本：Linux
  - 系统版本号：1.0
- **复现步骤**：
  ```
  1. 进入项目根目录：cd /var/ctt_wkplace/iot/backend
  2. 运行测试：mvn test -Dtest=DeviceServiceImplTest#testBatchImportDevices
  3. 观察编译错误
  ```
- **错误截图**：无

## 2. 错误分析

- **错误定位**：
  - 文件路径：/var/ctt_wkplace/iot/backend/src/test/java/com/ruoyi/project/device/service/impl/DeviceServiceImplTest.java
  - 方法名：testBatchImportDevices、testBatchImportDevicesEmptyFile
  - 行号：358-368、376-381
- **错误日志**：
  ```
  [ERROR] /var/ctt_wkplace/iot/backend/src/test/java/com/ruoyi/project/device/service/impl/DeviceServiceImplTest.java:[358,42] cannot find symbol
  [ERROR]   symbol:   method builder()
  [ERROR]   location: class org.springframework.mock.web.MockMultipartFile
  [ERROR] /var/ctt_wkplace/iot/backend/src/test/java/com/ruoyi/project/device/service/impl/DeviceServiceImplTest.java:[376,42] cannot find symbol
  [ERROR]   symbol:   method builder()
  [ERROR]   location: class org.springframework.mock.web.MockMultipartFile
  ```
- **可能原因**：
  - 依赖版本问题：项目使用的Spring版本较低，不支持MockMultipartFile.builder()方法（该方法是Spring 5.0+才引入的）
  - 代码兼容性问题：测试代码使用了较新版本的API，但项目依赖的是旧版本
- **影响范围**：
  - 影响的功能模块：设备服务实现测试模块
  - 影响的用户群体：开发人员
  - 影响的业务流程：单元测试执行流程

## 3. 解决方案

- **解决思路**：将测试用例中的MockMultipartFile.builder()方法改为使用构造函数创建对象，以兼容旧版本的Spring
- **具体步骤**：
  1. 打开DeviceServiceImplTest.java文件
  2. 定位到testBatchImportDevices方法
  3. 将MockMultipartFile.builder()创建文件的方式改为使用构造函数
  4. 定位到testBatchImportDevicesEmptyFile方法
  5. 同样将MockMultipartFile.builder()创建文件的方式改为使用构造函数
  6. 保存文件并重新运行测试
- **代码修改**：
  ```java
  // 错误代码
  MockMultipartFile file = MockMultipartFile.builder()
      .name("file")
      .originalFilename("test.xlsx")
      .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      .content(excelBytes)
      .build();

  // 修复后的代码
  MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.xlsx",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      excelBytes
  );
  ```
- **相关依赖**：
  - org.springframework.mock:spring-mock

## 4. 验证结果

- **验证方法**：重新运行DeviceServiceImplTest单元测试
- **验证环境**：
  - 操作系统版本：Linux
  - 系统版本号：1.0
- **验证结果**：
  - 错误是否复现：否
  - 功能是否正常工作：是
  - 性能是否受影响：否
  - 其他相关功能是否正常：是
- **测试截图**：无

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2025-12-09 02:00:00
- **解决人**：开发人员
- **解决时间**：2025-12-09 02:05:00
- **严重程度**：轻微错误
- **关联任务**：任务3.3 设备服务批量导入功能开发
- **关联文档**：
  - 接口设计文档.md
  - 开发流程计划.md
- **备注**：此问题是由于Spring版本兼容性导致的，使用旧版本构造函数创建MockMultipartFile对象解决了该问题。