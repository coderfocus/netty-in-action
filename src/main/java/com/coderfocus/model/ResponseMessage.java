package com.coderfocus.model;

import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.Charset;

@Data
public class ResponseMessage {

    private int version = 2;
    private int opCode;
    private long streamId;
    private String data;

    public void decode(ByteBuf msg) {
        int version = msg.readInt();
        long streamId = msg.readLong();
        int opCode = msg.readInt();
        String data = msg.toString(Charset.forName("UTF-8"));

        this.setVersion(version);
        this.setOpCode(opCode);
        this.setStreamId(streamId);
        this.setData(data);
    }

    public void encode(ByteBuf byteBuf) {
        byteBuf.writeInt(this.getVersion());
        byteBuf.writeLong(this.getStreamId());
        byteBuf.writeInt(this.getOpCode());
        byteBuf.writeBytes(this.data.getBytes());
    }
}
