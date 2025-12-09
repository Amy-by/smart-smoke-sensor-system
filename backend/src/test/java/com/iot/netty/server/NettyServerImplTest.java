package com.iot.netty.server;

import com.iot.protocol.adapter.Cat1ProtocolAdapter;
import com.iot.protocol.adapter.DataParseException;
import com.iot.protocol.adapter.ProtocolAdapter;
import com.iot.protocol.codec.ProtocolCodec;
import com.iot.protocol.model.SensorData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Netty服务器单元测试类
 *
 * 说明：测试Netty服务器的基本功能
 *
 * @author IoT架构组
 * @date 2025-01-27
 */
public class NettyServerImplTest {

    private NettyServerImpl nettyServer;

    @Mock
    private Cat1ProtocolAdapter cat1ProtocolAdapter;

    private Map<String, ProtocolAdapter> protocolAdapterMap;
    private Map<String, ProtocolCodec> protocolCodecMap;

    private Thread serverThread;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // 创建NettyServerImpl实例
        nettyServer = new NettyServerImpl();

        // 初始化协议适配器映射
        protocolAdapterMap = new HashMap<>();
        protocolAdapterMap.put("cat1ProtocolAdapter", cat1ProtocolAdapter);

        // 初始化协议编解码器映射
        protocolCodecMap = new HashMap<>();

        // 通过反射设置字段
        java.lang.reflect.Field adapterField = NettyServerImpl.class.getDeclaredField("protocolAdapterMap");
        adapterField.setAccessible(true);
        adapterField.set(nettyServer, protocolAdapterMap);

        java.lang.reflect.Field codecField = NettyServerImpl.class.getDeclaredField("protocolCodecMap");
        codecField.setAccessible(true);
        codecField.set(nettyServer, protocolCodecMap);

        // 模拟Cat1ProtocolAdapter的行为
        SensorData mockSensorData = new SensorData();
        mockSensorData.setDeviceId("1234567890");
        mockSensorData.setProtocolType("CAT1");
        mockSensorData.setSmokeConcentration(100);
        mockSensorData.setTemperature(25);
        mockSensorData.setBatteryLevel(80);
        
        when(cat1ProtocolAdapter.adapt(anyString())).thenReturn(mockSensorData);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // 停止Netty服务器
        if (nettyServer.isRunning()) {
            nettyServer.stop();
        }
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }

    @Test
    public void testStartAndStop() throws Exception {
        // 启动Netty服务器在单独的线程中
        serverThread = new Thread(() -> {
            try {
                nettyServer.start(8888);
            } catch (Exception e) {
                // 忽略中断异常
                if (!(e instanceof InterruptedException)) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();

        // 等待服务器启动
        Thread.sleep(1000);

        // 验证服务器是否正在运行
        assertTrue(nettyServer.isRunning());

        // 停止服务器
        nettyServer.stop();

        // 等待服务器停止
        Thread.sleep(1000);

        // 验证服务器是否已停止
        assertFalse(nettyServer.isRunning());
    }

    @Test
    public void testServerAcceptConnection() throws Exception {
        // 启动Netty服务器在单独的线程中
        serverThread = new Thread(() -> {
            try {
                nettyServer.start(8889);
            } catch (Exception e) {
                // 忽略中断异常
                if (!(e instanceof InterruptedException)) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();

        // 等待服务器启动
        Thread.sleep(1000);

        // 创建客户端连接
        Socket socket = new Socket("localhost", 8889);
        OutputStream outputStream = socket.getOutputStream();

        // 发送测试数据
        String testData = "{\"deviceId\":\"1234567890\",\"data\":{\"smokeConcentration\":100,\"temperature\":25,\"batteryLevel\":80},\"reportTime\":\"2025-01-27 12:00:00\"}";
        outputStream.write(testData.getBytes());
        outputStream.flush();

        // 关闭客户端连接
        outputStream.close();
        socket.close();

        // 验证是否调用了协议适配器
        verify(cat1ProtocolAdapter, timeout(2000)).adapt(anyString());
    }

    @Test
    public void testStartAlreadyRunningServer() throws Exception {
        // 启动Netty服务器在单独的线程中
        serverThread = new Thread(() -> {
            try {
                nettyServer.start(8890);
            } catch (Exception e) {
                // 忽略中断异常
                if (!(e instanceof InterruptedException)) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();

        // 等待服务器启动
        Thread.sleep(1000);

        // 尝试再次启动服务器，应该抛出异常
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            nettyServer.start(8890);
        });
        assertEquals("Netty服务器已经在运行中", exception.getMessage());
    }
}
