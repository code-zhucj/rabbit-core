package core.net;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import core.module.Module;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 网络引擎
 *
 * @author zhuchuanji
 */
public class NetworkEngine implements Module {

    private static final NetworkEngine INSTANCE = new NetworkEngine();

    private ServerBootstrap serverBootstrap;

    private static final int PORT = 6666;

    /**
     * 用于服务器端接受客户端的连接
     */
    EventLoopGroup bossGroup;

    /**
     * 用于网络事件的处理
     */
    EventLoopGroup workGroup;

    private static final Logger log = LoggerFactory.getLogger(NetworkEngine.class);


    public static NetworkEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        log.trace("网络引擎初始化开始...");
        this.serverBootstrap = new ServerBootstrap();
        // 用于服务器端接受客户端的连接
        this.bossGroup = new NioEventLoopGroup();
        // 用于网络事件的处理
        this.workGroup = new NioEventLoopGroup();
        this.serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NetworkEngineChannelHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
        log.trace("网络引擎初始化完成...");
    }

    @Override
    public void execute() {
        try {
            ChannelFuture sync = serverBootstrap.bind(PORT).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("监听端口绑定失败, 网络引擎启动失败", e);
        }
    }

    @Override
    public void destroy() {
        log.trace("网络引擎销毁开始...");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        log.trace("网络引擎销毁完成...");
    }

    private static class NetworkEngineChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            ctx.write("服务器接收到消息:" + msg.toString(CharsetUtil.UTF_8));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }
}
