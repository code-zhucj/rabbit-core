package core.net.coder;

import core.view.ViewContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description: 解码器
 * @author: zhuchuanji
 * @create: 2021-05-07 20:18
 */
@ChannelHandler.Sharable
public class Decoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("将数据开始解码");
        System.out.println(((ByteBuf) msg).toString(CharsetUtil.UTF_8));
        super.channelRead(ctx, new ViewContext());
    }
}
