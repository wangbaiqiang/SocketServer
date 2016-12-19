package com.pq.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MyEncoder implements ProtocolEncoder{
    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {
        String s=null;
        if(message instanceof String) {
            s=(String)message;
        }
        if(s!=null) {
            //这种写法是有问题的，每次都要去new， 获取系统默认的编码
//            CharsetEncoder charsetEncoder= Charset.defaultCharset().newEncoder();
            CharsetEncoder charsetEncoder= (CharsetEncoder) ioSession.getAttribute("encoder");
            if(charsetEncoder==null) {
                charsetEncoder=Charset.defaultCharset().newEncoder();
                ioSession.setAttribute("encoder",charsetEncoder);
            }
            //开始转码操作
            //开辟内存 减少多次物理读取的次数，缓存区在创建时就被分配内存，内存区域一直被重用，减少动态分配和回收内存的次数
            IoBuffer ioBuffer=IoBuffer.allocate(s.length());
            //内存可以自动扩展
            ioBuffer.setAutoExpand(true);
            //put进缓存
            ioBuffer.putString(s,charsetEncoder);
            //为了写出到流做准备，limit=position position=0 并重置mark
            ioBuffer.flip();
            //最后写出数据  那么里面的写出二进制就由mina帮我们完成
            out.write(ioBuffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
