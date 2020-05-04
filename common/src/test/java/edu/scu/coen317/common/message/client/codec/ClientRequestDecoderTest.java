package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ClientRequestDecoderTest {

    private static ByteBuf bufPut;
    private static ByteBuf bufGet;

    @BeforeClass
    public static void setup() {
        bufPut = Unpooled.buffer();
        bufPut.writeInt(0);
        bufPut.writeInt(4);
        bufPut.writeCharSequence("test", Configuration.CHARSET);
        bufPut.writeInt(3);
        bufPut.writeCharSequence("val", Configuration.CHARSET);

        bufGet = Unpooled.buffer();
        bufGet.writeInt(2);
        bufGet.writeInt(3);
        bufGet.writeCharSequence("key", Configuration.CHARSET);
    }

    @Test
    public void testDecode() {
        // test write messages
        ByteBuf in = bufPut.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new ClientRequestDecoder());

        assertTrue(channel.writeInbound(in.retain()));
        assertTrue(channel.finish());

        // test read messages
        ClientRequest request = channel.readInbound();
        assertEquals(request.getType(), MessageType.PUT);
        assertEquals(request.getKey(), "test");
        assertEquals(request.getVal(), "val");

        // test another message
        in = bufGet.duplicate();
        channel = new EmbeddedChannel(new ClientRequestDecoder());

        assertTrue(channel.writeInbound(in.retain()));
        assertTrue(channel.finish());

        request = channel.readInbound();
        assertEquals(request.getType(), MessageType.GET);
        assertEquals(request.getKey(), "key");
    }
}
