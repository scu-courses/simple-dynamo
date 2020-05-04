package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ClientRequestEncoderTest {

    private static final ClientRequest request = new ClientRequest(MessageType.GET, "key");

    @Test
    public void testEncode() {
        EmbeddedChannel channel = new EmbeddedChannel(new ClientRequestEncoder());
        channel.writeOutbound(request);

        ByteBuf in = channel.readOutbound();
        assertEquals(in.readableBytes(), 11);
        assertEquals(in.readInt(), MessageType.GET.ordinal());
        assertEquals(in.readInt(), 3);
        assertEquals(in.readCharSequence(3, Configuration.CHARSET), "key");
    }
}
