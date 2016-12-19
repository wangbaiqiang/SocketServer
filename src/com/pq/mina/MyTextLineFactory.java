package com.pq.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 自定义编解码  加密解密的东西
 */
public class MyTextLineFactory implements ProtocolCodecFactory{
    private MyEncoder myEncoder;
    private MyDecoder myDecoder;
    //防止数据丢失的多 可以下一次一次性读取数据的decoder

    private MyTextLineCumulativeDecoder myTextLineCumulativeDecoder;
    public MyTextLineFactory(){
        myEncoder=new MyEncoder();
        myDecoder=new MyDecoder();
        myTextLineCumulativeDecoder=new MyTextLineCumulativeDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return myEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
//        return myDecoder;
        return myTextLineCumulativeDecoder;
    }
}
