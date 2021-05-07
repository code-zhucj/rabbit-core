package core.net;

import core.util.CollectionUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网络连接管理器
 *
 * @author zhuchuanji
 */
@ChannelHandler.Sharable
public class NetConnectionManager extends ChannelInboundHandlerAdapter {

    private Map<Integer, ChannelHandlerContext> connections = CollectionUtils.newConcurrentHashMap();
    //TODO 修改为使用分布式id
    private AtomicInteger unique = new AtomicInteger();
    private static final NetConnectionManager connectionManager = new NetConnectionManager();

    public static NetConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connections.put(unique.incrementAndGet(), ctx);
        System.out.printf("id: %s 已成功建立连接%n", unique.get());
    }

    public void write(int id, String object) {
        ChannelHandlerContext channelHandlerContext = connections.get(id);
        if (channelHandlerContext != null) {
            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(object, CharsetUtil.UTF_8));
        }
    }

    public void writeAll(String read) {
        connections.values().forEach(v -> v.writeAndFlush(Unpooled.copiedBuffer(read, CharsetUtil.UTF_8)));
    }
}
