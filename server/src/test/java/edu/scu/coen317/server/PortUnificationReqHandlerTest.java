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
        bufPut.writeInt(3);
        bufPut.writeCharSequence("key", Configuration.CHARSET);
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
    public void testPutAndGetRequest() {
        PortUnificationReqHandler handler = new PortUnificationReqHandler();
        EmbeddedChannel channel = new EmbeddedChannel(handler);

        ByteBuf in = bufPut.duplicate();
        channel.writeInbound(in);

        ByteBuf out = channel.readOutbound();
        assertEquals(MessageType.PUT_REPLY.ordinal(), out.readInt());

        channel = new EmbeddedChannel(new PortUnificationReqHandler());
        in = bufGet.duplicate();
        channel.writeInbound(in);

        out = channel.readOutbound();
        assertEquals(MessageType.GET_REPLY.ordinal(), out.readInt());
        assertEquals(3, out.readInt());
        assertEquals("val", out.readCharSequence(3, Configuration.CHARSET));
        channel.close();
    }

//    @Test
//    public void test() {
//        HashFunction hf = Hashing.md5();
//        HashCode hc = hf.newHasher().putLong(1234).hash();
//        System.out.println(hc.toString());
//        System.out.println(HashFunctions.randomMD5());
//    }
}
