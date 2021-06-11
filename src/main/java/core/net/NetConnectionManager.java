package core.net;

import core.constants.Constants;
import core.unique.UniqueManager;
import core.util.CollectionUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * 网络连接管理器
 *
 * @author zhuchuanji
 */
@ChannelHandler.Sharable
public class NetConnectionManager extends ChannelInboundHandlerAdapter {

    private Map<Long, ChannelHandlerContext> connections = CollectionUtils.newConcurrentHashMap();

    private static final NetConnectionManager connectionManager = new NetConnectionManager();

    private static final Logger logger = LogManager.getLogger(NetConnectionManager.class);

    public static NetConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        long channelId = UniqueManager.generateChannelId();
        Attribute<Long> attr = ctx.channel().attr(AttributeKey.newInstance(Constants.CHANNEL_ID));
        attr.set(channelId);
        connections.put(channelId, ctx);
        logger.info(String.format("client: %s 已成功建立连接%n", channelId));
    }

    public void write(long id, String object) {
        ChannelHandlerContext channelHandlerContext = connections.get(id);
        if (channelHandlerContext != null) {
            channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(object, CharsetUtil.UTF_8));
        }
    }

    public void writeAll(String read) {
        connections.values().forEach(v -> v.writeAndFlush(Unpooled.copiedBuffer(read, CharsetUtil.UTF_8)));
    }
}
