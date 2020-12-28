package Server;

import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;

public class Player {

    private String name;
    private String id;
    private ChannelHandlerContext ctx;
    private ArrayList<Card> playerCards = new ArrayList<Card>();
    private ArrayList<Card> cardWon = new ArrayList<Card>();

    public Player(String id, String name, ChannelHandlerContext ctx) {
        this.id = id;
        this.name = name;
        this.ctx = ctx;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public ChannelHandlerContext getCtx() {
        return this.ctx;
    }

    public ArrayList<Card> getPlayerCards() {
        return this.playerCards;
    }

    public ArrayList<Card> getCardWon() {
        return cardWon;
    }
}
