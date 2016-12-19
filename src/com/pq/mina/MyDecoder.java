package com.pq.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MyDecoder implements ProtocolDecoder {
    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
        /**
         * 客户端发送一条消息就调用这个方法
         * 这里我们一个一个字节读取然后转换编码成字符
         */
        int startPosition=ioBuffer.position();
        while (ioBuffer.hasRemaining()){
            //每次循环读取一个字节，遇到'\n'就结束做处理
            //这里注意每次get之后position都会++
            byte b=ioBuffer.get();
            if(b=='\n') {
                //记录当前位置
                int currentPosition=ioBuffer.position();
                //记录总长度
                int limit=ioBuffer.limit();
                ioBuffer.position(startPosition);
                ioBuffer.limit(currentPosition);
                //截取到我们的数据
                IoBuffer buf = ioBuffer.slice();
                //把iobuffer变换成字符串
                byte[] dest=new byte[buf.limit()];
                buf.get(dest);
                String str=new String(dest);
                //写出  我们写入消息->才会走 handler中的messageReceive方法
                out.write(str);
                //然后还原操作
                ioBuffer.position(currentPosition);
                ioBuffer.limit(limit);
            }
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        System.out.println("finishDecode");
    }

    /**
     * dispose处理,安排
     * @param ioSession
     * @throws Exception
     */
    @Override
    public void dispose(IoSession ioSession) throws Exception {
        System.out.println("dispose");
    }
}
