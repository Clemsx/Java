package Server;

enum CardSuit {
    HEART, SPADE, DIAMOND, CLUB, UNDEFINED
}

public class Card {
    private int number;
    private CardSuit suit;
    private int valueAsset;
    private int valueNoAsset;

    public Card(int number, CardSuit suit, int valueAsset, int valueNoAsset) {
        this.number = number;
        this.suit = suit;
        this.valueAsset = valueAsset;
        this.valueNoAsset = valueNoAsset;
    }

    public int getCardNumber() {
        return this.number;
    }

    public CardSuit getCardSuit() {
        return this.suit;
    }

    public int getValueAsset() {
        return this.valueAsset;
    }

    public int getValueNoAsset() {
        return this.valueNoAsset;
    }

    public String getCardString() {
        String str;

        str = getCardName(number) + " of " + suit.toString().toLowerCase();
        return  str;
    }

    private String getCardName(int value) {
        String str;

        switch (value) {
            case 1: str = "As";
            break;
            case 7: str = "Seven";
                break;
            case 8: str = "Eight";
                break;
            case 9: str = "Nine";
                break;
            case 10: str = "Ten";
                break;
            case 11: str = "Jack";
                break;
            case 12: str = "Queen";
                break;
            case 13: str = "King";
                break;
            default: str = "?";
                break;
        }
        return str;
    }
}
