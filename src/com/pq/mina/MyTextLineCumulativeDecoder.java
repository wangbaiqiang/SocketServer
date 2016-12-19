package com.pq.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *针对数据丢失，也就是被chain拦截器拦截掉了，本质就是缓冲区中我们没有把这部分主句write写入到流 给handler
 */
public class MyTextLineCumulativeDecoder extends CumulativeProtocolDecoder {

    /**
     * 当且仅当数据已经明确知道读取完成了，并且开启新的读取，返回true。如果没有读取完成，希望下次一并读取时就返回false
     *
     * @param ioSession
     * @param ioBuffer
     * @param out
     * @return  如果认为本次读取完了就返回true 没读取完 希望下次读取一次读取完就false
     * @throws Exception
     */
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput out) throws Exception {
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
                return true;
            }
        }
        ioBuffer.position(startPosition);
        return false;
    }
}
