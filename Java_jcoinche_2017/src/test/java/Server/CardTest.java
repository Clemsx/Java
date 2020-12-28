package Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static Server.CardSuit.*;
import static org.junit.Assert.*;

public class CardTest {
    protected Card op;
    protected Card op2;
    protected Card op3;
    protected Card op4;

    @Before
    public void setUp() throws Exception {
        op = new Card(1, HEART, 7, 19);
        op2 = new Card(1, SPADE, 7, 19);
        op3 = new Card(1, CLUB, 7, 19);
        op4 = new Card(1, DIAMOND, 7, 19);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getCardNumber() throws Exception {
        assertEquals(1, op.getCardNumber());
        assertEquals(1, op2.getCardNumber());
        assertEquals(1, op3.getCardNumber());
        assertEquals(1, op4.getCardNumber());
    }

    @Test
    public void getCardSuit() throws Exception {
        assertEquals(HEART, op.getCardSuit());
        assertEquals(SPADE, op2.getCardSuit());
        assertEquals(CLUB, op3.getCardSuit());
        assertEquals(DIAMOND, op4.getCardSuit());
    }

    @Test
    public void getValueAsset() throws Exception {
        assertEquals(7, op.getValueAsset());
        assertEquals(7, op2.getValueAsset());
        assertEquals(7, op3.getValueAsset());
        assertEquals(7, op4.getValueAsset());
    }

    @Test
    public void getValueNoAsset() throws Exception {
        assertEquals(19, op.getValueNoAsset());
        assertEquals(19, op2.getValueNoAsset());
        assertEquals(19, op3.getValueNoAsset());
        assertEquals(19, op4.getValueNoAsset());
    }

    @Test
    public void getCardString() throws Exception {
    }

}