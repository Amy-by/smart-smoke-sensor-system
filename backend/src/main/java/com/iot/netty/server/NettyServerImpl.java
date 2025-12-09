package com.iot.netty.server;

import com.iot.data.forward.DataForwarder;
import com.iot.protocol.adapter.ProtocolAdapter;
import com.iot.protocol.codec.ProtocolCodec;
import com.iot.protocol.model.SensorData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Netty服务器实现类
 *
 * 说明：实现Netty服务器接口，用于接收传感器数据
 * 设计原则：单一职责原则 - 专注于Netty服务器的实现
 *
 * @author IoT架构组
 * @date 2025-01-27
 */
@Component
public class NettyServerImpl implements NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerImpl.class);

    /** 主线程组，用于处理连接请求 */
    private EventLoopGroup bossGroup;

    /** 工作线程组，用于处理客户端数据 */
    private EventLoopGroup workerGroup;

    /** 服务器端口 */
    private int port;

    /** 服务器运行状态 */
    private volatile boolean running = false;

    /** 协议适配器映射 */
    @Autowired
    private Map<String, ProtocolAdapter> protocolAdapterMap;

    /** 协议编解码器映射 */
    @Autowired
    private Map<String, ProtocolCodec> protocolCodecMap;
    
    /** 数据转发器 */
    @Autowired
    private DataForwarder dataForwarder;

    @Override
    public void start(int port) throws Exception {
        if (running) {
            throw new IllegalStateException("Netty服务器已经在运行中");
        }

        this.port = port;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器启动器
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 配置通道处理器
                            ch.pipeline()
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new SensorDataHandler());
                        }
                    });

            // 绑定端口并启动服务器
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Netty服务器已启动，监听端口：{}", port);
            running = true;

            // 等待服务器关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅关闭线程组
            shutdown();
        }
    }

    @Override
    public void stop() throws Exception {
        shutdown();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * 关闭服务器
     */
    private void shutdown() {
        if (!running) {
            return;
        }

        running = false;

        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        logger.info("Netty服务器已关闭");
    }

    /**
     * 传感器数据处理器
     */
    private class SensorDataHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try {
                String rawData = (String) msg;
                logger.info("接收到传感器数据：{}", rawData);

                // 这里应该根据协议类型选择对应的适配器或编解码器
                // 目前暂时使用Cat1ProtocolAdapter进行处理
                ProtocolAdapter cat1Adapter = protocolAdapterMap.get("cat1ProtocolAdapter");
                if (cat1Adapter != null) {
                    SensorData sensorData = cat1Adapter.adapt(rawData);
                    logger.info("解析后的传感器数据：{}", sensorData);

                    // 调用数据转发器，将解析后的数据转发到相应的业务模块
                    dataForwarder.forward(sensorData);
                }
            } catch (Exception e) {
                logger.error("处理传感器数据失败：{}", e.getMessage(), e);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("Netty服务器异常：{}", cause.getMessage(), cause);
            ctx.close();
        }
    }
}