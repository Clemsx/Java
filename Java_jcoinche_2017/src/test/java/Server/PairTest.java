package Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.netty.channel.ChannelHandlerContext;
import static Server.CardSuit.HEART;
import static org.junit.Assert.*;

public class PairTest {
    protected ChannelHandlerContext ctx;

    protected Player player = new Player("test_id_player", "test_name_player", ctx);
    protected Card card = new Card(1, HEART, 7, 19);

    protected Player playerNotEq = new Player("test_id_player", "test_name_player", ctx);
    protected Card cardNotEq = new Card(1, HEART, 7, 19);

    protected Pair op;

    @Before
    public void setUp() throws Exception {
        op = new Pair(card, player);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCard() throws Exception {
        assertEquals(card, op.getCard());
        assertNotEquals(cardNotEq, op.getCard());
    }

    @Test
    public void getPlayer() throws Exception {
        assertEquals(player, op.getPlayer());
        assertNotEquals(playerNotEq, op.getPlayer());
    }

}