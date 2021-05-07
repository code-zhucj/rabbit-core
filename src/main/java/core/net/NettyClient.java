package core.net;


import core.thread.ThreadManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 本类用于测试客户端与服务器连接
 * netty client
 *
 * @author zhuchuanji
 */
public class NettyClient {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6666;

    private static final Logger logger = LogManager.getLogger(NettyClient.class);

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();
        client.start(Integer.parseInt(args[0]));
    }

    public void start(int localPort) throws InterruptedException {
        logger.info("启动客户端...");
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ClientChannelHandler clientChannelHandler = new ClientChannelHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(IP, PORT))
                .localAddress(localPort)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(clientChannelHandler);
                        ch.pipeline().addLast(new ClientSenderChannelHandler());
                    }
                });
        try {
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    @ChannelHandler.Sharable
    private static class ClientSenderChannelHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("客户端：" + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
            ctx.writeAndFlush(msg);
        }
    }

    @ChannelHandler.Sharable
    private static class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

        private ChannelHandlerContext ctx;

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("netty rocks!", CharsetUtil.UTF_8));
            this.ctx = ctx;
            ThreadManager.getInstance().system.execute(new ConsoleThread(ctx));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("客户端收到消息：" + msg.toString(CharsetUtil.UTF_8));
        }

        public ChannelHandlerContext getCtx() {
            return ctx;
        }
    }

    /**
     * 控制台线程，用于获取控制太输入后推送到服务器端
     */
    private static class ConsoleThread implements Runnable {

        private final ChannelHandlerContext ctx;

        public ConsoleThread(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNext()) {
                    String input = scanner.next();
                    ctx.writeAndFlush(Unpooled.copiedBuffer(input, CharsetUtil.UTF_8));
                }
            }
        }
    }
}
