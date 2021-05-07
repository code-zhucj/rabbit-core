package core.net;

import core.module.Module;
import core.net.coder.Decoder;
import core.net.coder.Encoder;
import core.view.ViewRequestForwarder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

    private static final Logger log = LogManager.getLogger(NetworkEngine.class);


    public static NetworkEngine getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        log.info("网络引擎配置初始化开始...");
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
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        channelInboundHandlers().forEach(pipeline::addLast);
                        channelOutboundHandlers().forEach(pipeline::addLast);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
        log.info("网络引擎配置初始化完成");
    }

    /**
     * ChannelInboundHandlers 队列
     *
     * @return ChannelHandler列表
     */
    private List<ChannelInboundHandler> channelInboundHandlers() {
        List<ChannelInboundHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(NetConnectionManager.getConnectionManager());
        channelHandlers.add(new Decoder());
        channelHandlers.add(new ViewRequestForwarder());
        return channelHandlers;
    }

    /**
     * ChannelInboundHandlers 队列
     *
     * @return ChannelHandler列表
     */
    private List<ChannelOutboundHandler> channelOutboundHandlers() {
        List<ChannelOutboundHandler> channelHandlers = new ArrayList<>();
        channelHandlers.add(new Encoder());
        return channelHandlers;
    }

    @Override
    public void execute() {
        try {
            log.info("网络引擎当前监听端口：" + PORT);
            ChannelFuture sync = serverBootstrap.bind(PORT).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("监听端口绑定失败, 网络引擎启动失败", e);
        }
    }

    @Override
    public void destroy() {
        log.info("网络引擎销毁开始...");
        try {
            bossGroup.shutdownGracefully().sync();
            workGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("网络引擎销毁异常...");
            return;
        }
        log.info("网络引擎销毁完成...");
    }


    @ChannelHandler.Sharable
    private static class NetworkEngineChannelHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String read = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
            System.out.println("服务器：" + read);
            NetConnectionManager.getConnectionManager().writeAll(read);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
//                    .addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
