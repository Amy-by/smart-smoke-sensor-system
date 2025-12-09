# BE-0011-实体类BaseEntity导入错误

## 1. 错误描述

- **错误标题**：实体类BaseEntity导入路径错误
- **错误现象**：在编译和运行单元测试时，出现"package com.ruoyi.common.core.domain does not exist"和"cannot find symbol BaseEntity"的编译错误
- **发生环境**：
  - 操作系统版本：Linux
  - 系统版本号：智能烟感系统开发版
- **复现步骤**：
  ```
  1. 创建继承自BaseEntity的Device和DeviceStatus实体类
  2. 使用错误的BaseEntity导入路径：com.ruoyi.common.core.domain.BaseEntity
  3. 运行mvn test命令
  4. 观察编译错误
  ```
- **错误截图**：无

## 2. 错误分析

- **错误定位**：
  - 文件路径：/var/ctt_wkplace/iot/backend/src/main/java/com/ruoyi/project/device/domain/Device.java
  - 文件路径：/var/ctt_wkplace/iot/backend/src/main/java/com/ruoyi/project/device/domain/DeviceStatus.java
  - 行号：第3行（import语句）
  - 函数/方法名：无
- **错误日志**：
  ```
  [ERROR] /var/ctt_wkplace/iot/backend/src/main/java/com/ruoyi/project/device/domain/Device.java:[3,42] package com.ruoyi.common.core.domain does not exist
  [ERROR] /var/ctt_wkplace/iot/backend/src/main/java/com/ruoyi/project/device/domain/Device.java:[7,35] cannot find symbol
  [ERROR]   symbol: class BaseEntity
  ```
- **可能原因**：
  - 对若依框架的BaseEntity类路径不熟悉
  - 错误地使用了其他项目的BaseEntity导入路径
  - toString()方法错误地使用了@Override注解（BaseEntity类并没有重写toString()方法）
- **影响范围**：
  - 影响的功能模块：设备实体类开发
  - 影响的用户群体：开发团队
  - 影响的业务流程：设备管理模块开发

## 3. 解决方案

- **解决思路**：
  1. 查找项目中正确的BaseEntity类路径
  2. 修正Device.java和DeviceStatus.java中的导入语句
  3. 移除toString()方法上错误的@Override注解
- **具体步骤**：
  1. 使用search_codebase工具查找BaseEntity类的正确路径
  2. 修改Device.java和DeviceStatus.java中的import语句
  3. 移除toString()方法上的@Override注解
  4. 重新运行单元测试验证修复效果
- **代码修改**：
  ```java
  // 错误代码 (Device.java)
  import com.ruoyi.common.core.domain.BaseEntity;
  
  public class Device extends BaseEntity {
      // ...
      @Override
      public String toString() {
          return "Device{" +
                  "sensorId='" + sensorId + '\'' +
                  ", materialCode='" + materialCode + '\'' +
                  ", installationLocation='" + installationLocation + '\'' +
                  ", deviceType='" + deviceType + '\'' +
                  ", manufacturer='" + manufacturer + '\'' +
                  ", modelNumber='" + modelNumber + '\'' +
                  ", firmwareVersion='" + firmwareVersion + '\'' +
                  ", hardwareVersion='" + hardwareVersion + '\'' +
                  ", serialNumber='" + serialNumber + '\'' +
                  ", status='" + status + '\'' +
                  ", latitude=" + latitude +
                  ", longitude=" + longitude +
                  ", altitude=" + altitude +
                  ", batteryLevel=" + batteryLevel +
                  ", signalStrength=" + signalStrength +
                  ", lastOnlineTime=" + lastOnlineTime +
                  ", installationDate=" + installationDate +
                  ", maintenanceDate=" + maintenanceDate +
                  ", remark='" + remark + '\'' +
                  '}';
      }
  }
  
  // 修复后的代码 (Device.java)
  import com.ruoyi.framework.web.domain.BaseEntity;
  
  public class Device extends BaseEntity {
      // ...
      public String toString() {
          return "Device{" +
                  "sensorId='" + sensorId + '\'' +
                  ", materialCode='" + materialCode + '\'' +
                  ", installationLocation='" + installationLocation + '\'' +
                  ", deviceType='" + deviceType + '\'' +
                  ", manufacturer='" + manufacturer + '\'' +
                  ", modelNumber='" + modelNumber + '\'' +
                  ", firmwareVersion='" + firmwareVersion + '\'' +
                  ", hardwareVersion='" + hardwareVersion + '\'' +
                  ", serialNumber='" + serialNumber + '\'' +
                  ", status='" + status + '\'' +
                  ", latitude=" + latitude +
                  ", longitude=" + longitude +
                  ", altitude=" + altitude +
                  ", batteryLevel=" + batteryLevel +
                  ", signalStrength=" + signalStrength +
                  ", lastOnlineTime=" + lastOnlineTime +
                  ", installationDate=" + installationDate +
                  ", maintenanceDate=" + maintenanceDate +
                  ", remark='" + remark + '\'' +
                  '}';
      }
  }
  ```
- **相关依赖**：无

## 4. 验证结果

- **验证方法**：
  1. 运行mvn test -Dtest=DeviceTest,DeviceStatusTest命令
  2. 检查测试是否通过
- **验证环境**：
  - 操作系统版本：Linux
  - 系统版本号：智能烟感系统开发版
- **验证结果**：
  - 错误是否复现：否
  - 功能是否正常工作：是
  - 性能是否受影响：否
  - 其他相关功能是否正常：是
- **测试截图**：无

## 5. 相关信息

- **记录人**：开发人员
- **记录时间**：2023-10-01 15:30:00
- **解决人**：开发人员
- **解决时间**：2023-10-01 15:45:00
- **严重程度**：轻微
- **关联任务**：任务3.1 设备实体类开发
- **关联文档**：无
- **备注**：无