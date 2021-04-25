package core.net;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;

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
        client.start();
    }

    public void start() throws InterruptedException {
        logger.info("启动客户端...");
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(IP, PORT))
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ClientChannelHandler());
        try {
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    private static class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("netty rocks!", CharsetUtil.UTF_8));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("客户端收到消息：" + msg.toString(CharsetUtil.UTF_8));
        }
    }

}
