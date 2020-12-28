package Server;

import java.util.ArrayList;
import java.util.Collections;

import static Server.CardSuit.*;

public class Deck {
    private ArrayList<Card> deck = new ArrayList<Card>();

    public Deck() {
        this.deck.add(new Card(1, HEART, 7, 19));
        this.deck.add(new Card(7, HEART, 0, 0));
        this.deck.add(new Card(8, HEART, 0, 0));
        this.deck.add(new Card(9, HEART, 9, 0));
        this.deck.add(new Card(10, HEART, 5, 10));
        this.deck.add(new Card(11, HEART, 11, 2));
        this.deck.add(new Card(12, HEART, 2, 3));
        this.deck.add(new Card(13, HEART, 3, 4));

        this.deck.add(new Card(1, SPADE, 7, 19));
        this.deck.add(new Card(7, SPADE, 0, 0));
        this.deck.add(new Card(8, SPADE, 0, 0));
        this.deck.add(new Card(9, SPADE, 9, 0));
        this.deck.add(new Card(10, SPADE, 5, 10));
        this.deck.add(new Card(11, SPADE, 11, 2));
        this.deck.add(new Card(12, SPADE, 2, 3));
        this.deck.add(new Card(13, SPADE, 3, 4));

        this.deck.add(new Card(1, DIAMOND, 7, 19));
        this.deck.add(new Card(7, DIAMOND, 0, 0));
        this.deck.add(new Card(8, DIAMOND, 0, 0));
        this.deck.add(new Card(9, DIAMOND, 9, 0));
        this.deck.add(new Card(10, DIAMOND, 5, 10));
        this.deck.add(new Card(11, DIAMOND, 11, 2));
        this.deck.add(new Card(12, DIAMOND, 2, 3));
        this.deck.add(new Card(13, DIAMOND, 3, 4));

        this.deck.add(new Card(1, CLUB, 7, 19));
        this.deck.add(new Card(7, CLUB, 0, 0));
        this.deck.add(new Card(8, CLUB, 0, 0));
        this.deck.add(new Card(9, CLUB, 9, 0));
        this.deck.add(new Card(10, CLUB, 5, 10));
        this.deck.add(new Card(11, CLUB, 11, 2));
        this.deck.add(new Card(12, CLUB, 2, 3));
        this.deck.add(new Card(13, CLUB, 3, 4));
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card popCard() {
        Card ret = deck.get(0);
        deck.remove(0);
        return ret;
    }

    public void add(Card card) {
        deck.add(card);
    }
}
