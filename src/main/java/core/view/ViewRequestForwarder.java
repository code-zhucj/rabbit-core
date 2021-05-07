package core.view;

import core.net.NetConnectionManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @description: 视图请求转发器
 * @author: zhuchuanji
 * @create: 2021-05-07 20:58
 */
public class ViewRequestForwarder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("视图转发器已收到，开始处理业务逻辑");
        ViewCoderParam viewMsg = (ViewCoderParam) msg;
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务器已收到请求，正在处理", CharsetUtil.UTF_8));
        new Thread(()-> {
            try {
                Thread.sleep(2000);
                NetConnectionManager.getConnectionManager().write(1, "我这边处理完成了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(5000);
    }

}
