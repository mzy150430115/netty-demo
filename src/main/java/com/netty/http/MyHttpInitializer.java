package com.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * @Author ZhiYuan Ma
 * @Date 2021/5/29 11:35
 * @Version 1.0
 * @motto God bless my code
 */
public class MyHttpInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //先对请求解码，后对响应编码
//        pipeline.addLast("decoder",new HttpRequestDecoder());
//        pipeline.addLast("encoder",new HttpResponseDecoder());
        //直接用这种 省去了上面两步
        pipeline.addLast("codec",new HttpServerCodec());
        //压缩数据
        pipeline.addLast("compressor",new HttpContentCompressor());
        //聚合成完整的信息 参数代表可以处理的最大值 此时是512kb
        pipeline.addLast("aggregator",new HttpObjectAggregator(512*1024));
        pipeline.addLast(new MyHttpHandler());



    }
}
