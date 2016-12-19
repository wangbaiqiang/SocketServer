package com.pq.mina;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MinaServer {
    /**
     * mina 封装了jdk 中难用的nio的api
     *
     */
    public static void main(String[] args) {
        /**
         * mina的用法 四步 基本用法就是四部 还可以加一些配置操作
         */

        try {
            //1
            NioSocketAcceptor acceptor=new NioSocketAcceptor();
            //2 mina 网络管理和消息管理进行分离
            MyServerHandler handler=new MyServerHandler();
            //交给handler去处理消息的收发
            acceptor.setHandler(handler);
            //3.mina做了一个责任链模式实现的拦截器 所有的消息都通过这些拦截器之后才能发送和接收
            //获得所有的拦截器  mina默认的一种对文本进行操作的编解码一行一行的读 TextLineCodecFactory
            //那么要想读json或者xml的数据就需要我们自己写(定制)自己的CodecFactory
            //自己定制3 我们不写复杂的先模仿mina给的我们自己写一个简单的也是读取一行一行的数据 即自定义编解码
//             acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory()));
            //用我们自己定义的编解码器
             acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new MyTextLineFactory()));
                //配置idel 进入空闲的时间 参数1: 多长时间没有读消息 写消息 BOTH是两者都起作用 参数2: 秒
            //功能就是服务端心跳保持客户端在线
                acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);

            //4
            acceptor.bind(new InetSocketAddress(9898));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
