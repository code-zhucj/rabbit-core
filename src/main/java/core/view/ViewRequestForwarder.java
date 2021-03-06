package core.view;

import core.constants.Constants;
import core.thread.ThreadManager;
import core.thread.WorkerQueueThreadPoolExecutor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
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
        ViewContext vc = (ViewContext) msg;
        ctx.writeAndFlush(Unpooled.copiedBuffer("服务器已收到请求，正在处理", CharsetUtil.UTF_8));
        WorkerQueueThreadPoolExecutor workerQueueThreadPool = ThreadManager.getInstance().getWorkerQueueThreadPool();
        Attribute<Long> attr = ctx.channel().attr(AttributeKey.valueOf(Constants.CHANNEL_ID));
        workerQueueThreadPool.commit(vc.getClientId(), ViewManager.getViewTaskById(attr.get(), vc.getTargetViewId()));
    }

}
