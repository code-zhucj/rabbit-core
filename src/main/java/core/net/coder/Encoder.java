package core.net.coder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @description: 编码器
 * @author: zhuchuanji
 * @create: 2021-05-07 20:21
 */
@ChannelHandler.Sharable
public class Encoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("将数据开始编码");
        super.write(ctx, msg, promise);
    }
}
