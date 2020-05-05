package edu.scu.coen317.server;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class PortUnificationReqHandlerTest {

    private static ByteBuf bufPut;
    private static ByteBuf bufGet;

    @BeforeClass
    public static void setup() {
        bufPut = Unpooled.buffer();
        bufPut.writeInt(MessageType.PUT.ordinal());
        bufPut.writeInt(4);
        bufPut.writeCharSequence("test", Configuration.CHARSET);
        bufPut.writeInt(3);
        bufPut.writeCharSequence("val", Configuration.CHARSET);

        bufGet = Unpooled.buffer();
        bufGet.writeInt(MessageType.GET.ordinal());
        bufGet.writeInt(3);
        bufGet.writeCharSequence("key", Configuration.CHARSET);
    }

    @Test
    public void testGetRequest() {
        PortUnificationReqHandler handler = new PortUnificationReqHandler();
        EmbeddedChannel channel = new EmbeddedChannel(handler);

        ByteBuf in = bufGet.duplicate();
        channel.writeInbound(in.retain());

        ByteBuf out = channel.readOutbound();
        assertEquals(out.readInt(), MessageType.GET_REPLY.ordinal());

        channel.close();
    }

    @Test
    public void testPutRequest() {
        PortUnificationReqHandler handler = new PortUnificationReqHandler();
        EmbeddedChannel channel = new EmbeddedChannel(handler);

        ByteBuf in = bufPut.duplicate();
        channel.writeInbound(in.retain());

        ByteBuf out = channel.readOutbound();
        assertEquals(out.readInt(), MessageType.PUT_REPLY.ordinal());

        channel.close();
    }
}
