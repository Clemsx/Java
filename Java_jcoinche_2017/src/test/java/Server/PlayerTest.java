package Server;

import io.netty.channel.ChannelHandlerContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    protected ChannelHandlerContext ctx;
    protected Player op;

    @Before
    public void setUp() throws Exception {
        op = new Player("test_id", "test_name", ctx);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getName() throws Exception {
        assertEquals("test_name", op.getName());
        assertNotEquals("blabla", op.getName());
    }

    @Test
    public void getId() throws Exception {
        assertEquals("test_id", op.getId());
        assertNotEquals("blabla", op.getId());
    }

    @Test
    public void getCtx() throws Exception {
        assertEquals(ctx, op.getCtx());
    }

    @Test
    public void getPlayerCards() throws Exception {
    }

    @Test
    public void getCardWon() throws Exception {
    }

}