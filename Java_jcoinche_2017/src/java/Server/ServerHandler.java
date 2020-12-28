package Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.Iterator;

import static Server.CardSuit.UNDEFINED;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    public enum gameState {
        WAITING, BIDTURN, INGAME
    }

    //General variables
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static gameState gs = gameState.WAITING;
    private static Deck deck = new Deck();
    private static Player playerTurn = null;

    //Bid turn variables
    private static int contractValue = 0;
    private static CardSuit contractSuit = UNDEFINED;

    //In game variables
    private static ArrayList<Pair> table = new ArrayList<Pair>();
    private static int scoreTeam1 = 0;
    private static int scoreTeam2 = 0;

    //Error variables
    private static int error = 0;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        Player newPlayer;

        if (players.size() == 4) {
            disconnectPlayer(ctx);
        } else {
            newPlayer = addPlayerToList(ctx);
            sendWhoJoinedToAllPlayer(ctx, newPlayer);
            sendRoomStatusToAllPlayer();
            startGameIfEnoughPlayer();
        }
    }

    //Channel active functions

    private void disconnectPlayer(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush("[Server]: Sorry you are late :(\nCome back later :)\n");
        ctx.close();
    }

    private Player addPlayerToList(ChannelHandlerContext ctx) {
        String playerName = "Player ";
        String playerId;
        Player player;

        channels.add(ctx.channel());
        playerName += channels.size();
        playerId = ctx.channel().id().toString();
        player = new Player(playerId, playerName, ctx);
        players.add(player);
        return player;
    }

    private void sendWhoJoinedToAllPlayer(ChannelHandlerContext ctx, Player newPlayer) {
        for (Channel c : channels) {
            if (c != ctx.channel()) {
                c.writeAndFlush("[Server]: " + newPlayer.getName() + " joined the room\n");
            } else {
                c.writeAndFlush("[Server]: you are " + newPlayer.getName() + "\n");
            }
        }
    }

    private void sendRoomStatusToAllPlayer() {
        if (players.size() < 4) {
            for (Channel c : channels) {
                c.writeAndFlush("[Server]: waiting for " + (4 - players.size()) + " more player(s) to connect\n");
            }
        } else {
            for (Channel c : channels) {
                c.writeAndFlush("[Server]: 4 players are connected, starting game...\n");
            }
        }
    }

    private void printPlayerHand(Player player, Boolean index) {
        int i = 0;
        String msg;
        Boolean first = true;

        writeToPlayer(player, "[Coinche]: Your current hand:\n");
        for (Card c : player.getPlayerCards()) {
            if (!first) {
                writeToPlayer(player, " - ");
            }
            if (index) {
                msg = "(" + i + ") " + c.getCardString();
            } else {
                msg = c.getCardString();
            }
            writeToPlayer(player, msg);
            i++;
            first = false;
        }
        writeToPlayer(player, "\n");
    }

    private void askContractValue() {
        String msg = "[Coinche]: Choose between the following value for your contract (\"<value>\"):\n" +
                "80 - 90 - 100 - 110 - 120 - 130\n";

        writeToPlayer(playerTurn, msg);
    }

    private void bidTurn() {
        gs = gameState.BIDTURN;
        playerTurn = players.get(0);

        //Send bid turn to all client
        for (Channel c : channels) {
            if (c.id().toString().equals(playerTurn.getId())) {
                c.writeAndFlush("[Coinche]: Contract turn !\n");
            } else {
                c.writeAndFlush("[Coinche]: " + playerTurn.getName() + " is filling the contract\n");
            }
        }
        printPlayerHand(playerTurn, false);
        askContractValue();
    }

    private void startGameIfEnoughPlayer() {
        if (players.size() == 4) {
            giveCardToPlayer();
            bidTurn();
        }
    }

    //ChannelRead0 functions

    private void disconnectCommand(ChannelHandlerContext ctx) {
        Iterator<Player> it = players.iterator();
        Player instance = null;
        Player obj;

        while (it.hasNext()) {
            obj = it.next();
            if (obj.getId().equals(ctx.channel().id().toString())) {
                instance = obj;
                break;
            }
        }
        if (instance != null)
            players.remove(instance);
        ctx.close();
    }

    private void dumpCommand(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush("Contract value: " + contractValue + "\n");
        ctx.channel().writeAndFlush("Contract suit: " + contractSuit.toString() + "\n");
    }

    private Boolean isContractValueValid() {
        return contractValue == 80 ||
                contractValue == 90 ||
                contractValue == 100 ||
                contractValue == 110 ||
                contractValue == 120 ||
                contractValue == 130;
    }

    private void writeToPlayer(Player player, String msg) {
        player.getCtx().channel().writeAndFlush(msg);
    }

    private void askContractSuit() {
        playerTurn.getCtx().channel().writeAndFlush("[Coinche]: Choose between the following suit for your contract (\"<string>\"):\n" +
                "Heart - Spade - Diamond - Club\n");
    }

    private void setContractValue(String msg) {
        try {
            contractValue = Integer.parseInt(msg);
            if (!isContractValueValid()) {
                writeToPlayer(playerTurn, "[Coinche]: Invalid input\n");
                contractValue = 0;
                askContractValue();
            } else {
                askContractSuit();
            }
        } catch (NumberFormatException e) {
            error = 1;
            throw e;
        }
    }

    private void displayContractToAll() {
        for (Channel c : channels) {
            c.writeAndFlush("[Coinche]: Contract has been set by " + playerTurn.getName() + ":\n");
            c.writeAndFlush("Score: " + contractValue + " Suit: " + contractSuit.toString().toLowerCase() + "\n");
        }
    }

    private void askPlayerToPlay(Player player) {
        printPlayerHand(player, true);
        writeToPlayer(player, "[Coinche]: Enter the number according to the card you want to play (\"<value>\"):\n");
    }

    private void printTable(Player player) {
        String str;

        writeToPlayer(player, "Table:\n");
        if (table.isEmpty()) {
            writeToPlayer(player, "Empty\n");
        } else {
            for (Pair p : table) {
                str = p.getCard().getCardString() + "\t(" + p.getPlayer().getName() + ")\n";
                writeToPlayer(player, str);
            }
        }
    }

    private void startCoincheTurn() {
        printTable(playerTurn);
        askPlayerToPlay(playerTurn);
    }

    private void printTurn() {
        String str;

        writeToPlayer(playerTurn, "[Coinche]: It's your turn !\n");
        for (Player p : players) {
            if (p != playerTurn) {
                str = "[Coinche]: " + playerTurn.getName() + " is picking a card\n";
                writeToPlayer(p, str);
            }
        }
    }

    private void startGame() {
        gs = gameState.INGAME;
        for (Channel c : channels) {
            c.writeAndFlush("[Coinche]: Starting coinche game !\n");
        }
        printTurn();
        startCoincheTurn();
    }

    private void setContractSuit(String msg) {
        switch (msg.toLowerCase()) {
            case "heart":
                contractSuit = CardSuit.HEART;
                break;
            case "spade":
                contractSuit = CardSuit.SPADE;
                break;
            case "diamond":
                contractSuit = CardSuit.DIAMOND;
                break;
            case "club":
                contractSuit = CardSuit.CLUB;
                break;
            default:
                contractSuit = CardSuit.UNDEFINED;
                break;
        }
        if (contractSuit == CardSuit.UNDEFINED) {
            writeToPlayer(playerTurn, "[Coinche]: Invalid input\n");
            askContractSuit();
        } else {
            displayContractToAll();
            startGame();
        }
    }

    private void setContract(String msg) {
        if (contractValue == 0) {
            setContractValue(msg);
        } else if (contractSuit == CardSuit.UNDEFINED) {
            setContractSuit(msg);
        }
    }

    private void putPlayerCardOnTable(String msg) {
        int cardIndex;
        Card cardPicked;
        ArrayList<Card> playerCards = playerTurn.getPlayerCards();

        cardIndex = Integer.parseInt(msg);
        cardPicked = playerCards.get(cardIndex);
        playerCards.remove(cardPicked);
        table.add(new Pair(cardPicked, playerTurn));
        for (Player p : players)
            printTable(p);
    }

    private int sumAllCard(int index) {
        Card card;
        int score = 0;

        while (!players.get(index).getCardWon().isEmpty()) {
            card = players.get(index).getCardWon().get(0);
            players.get(index).getCardWon().remove(card);
            if (card.getCardSuit().equals(contractSuit)) {
                score += card.getValueAsset();
            } else {
                score += card.getValueNoAsset();
            }
            deck.add(card);
        }
        return score;
    }

    private void displayTeamScore() {
        for (Channel c : channels) {
            c.writeAndFlush("[Coinche]: Score of team Player 1 - Player 3: " + scoreTeam1 + "\n");
            c.writeAndFlush("[Coinche]: Score of team Player 2 - Player 4: " + scoreTeam2 + "\n");
        }
    }

    private void calculateWinnerRound() {
        int i = 0;

        while (i < 4) {
            if (i % 2 == 0) { //teamscore1
                scoreTeam1 = +sumAllCard(i);
            } else { //teamscore2
                scoreTeam2 = +sumAllCard(i);
            }
            i++;
        }
        contractValue = 0;
        contractSuit = CardSuit.UNDEFINED;
        displayTeamScore();
    }

    private void giveCardToPlayer() {
        deck.shuffle();
        for (Player p : players) {
            p.getPlayerCards().add(deck.popCard());
            p.getPlayerCards().add(deck.popCard());
            p.getPlayerCards().add(deck.popCard());
        }
        for (Player p : players) {
            p.getPlayerCards().add(deck.popCard());
            p.getPlayerCards().add(deck.popCard());
        }
        for (Player p : players) {
            p.getPlayerCards().add(deck.popCard());
            p.getPlayerCards().add(deck.popCard());
            p.getPlayerCards().add(deck.popCard());
        }
    }

    private void startNewTurn() {
        if (playerTurn.getPlayerCards().isEmpty()) {
            calculateWinnerRound();
            gs = gameState.BIDTURN;
            giveCardToPlayer();
            bidTurn();
        } else {
            printTurn();
            startCoincheTurn();
        }
    }

    private void setNextPlayerTurn() {
        Iterator<Player> it = players.iterator();

        while (it.hasNext()) {
            if (playerTurn.equals(it.next())) {
                break;
            }
        }
        if (!it.hasNext()) {
            playerTurn = players.iterator().next();
        } else {
            playerTurn = it.next();
        }
    }

    private void calculateWinnerTurn() {
        Pair winner = table.get(0);
        Player pwin;
        int i = 1;
        Pair tmp;

        while (i < 4) {
            if (table.get(i).getCard().getCardSuit().equals(contractSuit) && !winner.getCard().getCardSuit().equals(contractSuit)) {
                winner = table.get(i);
            } else if (!table.get(i).getCard().getCardSuit().equals(contractSuit) && !winner.getCard().getCardSuit().equals(contractSuit)) {
                if (table.get(i).getCard().getValueNoAsset() >= winner.getCard().getValueNoAsset()) {
                    winner = table.get(i);
                }
            } else if (table.get(i).getCard().getCardSuit().equals(contractSuit) && winner.getCard().getCardSuit().equals(contractSuit)) {
                if (table.get(i).getCard().getValueAsset() >= winner.getCard().getValueAsset()) {
                    winner = table.get(i);
                }
            }
            i++;
        }
        pwin = winner.getPlayer();
        while (!table.isEmpty()) {
            tmp = table.get(0);
            table.remove(tmp);
            pwin.getCardWon().add(tmp.getCard());
        }
        for (Channel c : channels) {
            if (c != pwin.getCtx().channel()) {
                c.writeAndFlush("[Coinche]: " + pwin.getName() + " has won the turn !\n");
            } else {
                c.writeAndFlush("[Coinche]: You have won the turn !\n");
            }
        }
        table.clear();
        playerTurn = pwin;
    }

    private void getPlayerCardPick(String msg) {
        try {
            putPlayerCardOnTable(msg);
            if (table.size() == 4) {
                calculateWinnerTurn();
                startNewTurn();
            } else {
                setNextPlayerTurn();
                printTurn();
                startCoincheTurn();
            }
        } catch (NumberFormatException e) {
            error = 2;
            throw e;
        }
    }

    private void handleGameTurnForPlayersTurn(String msg) {
        if (gs.equals(gameState.WAITING)) {

        } else if (gs.equals(gameState.BIDTURN)) {
            setContract(msg);
        } else if (gs.equals(gameState.INGAME)) {
            getPlayerCardPick(msg);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if ("disconnect".equals(msg.toLowerCase())) {
            disconnectCommand(ctx);
        }
        if ("dump".equals(msg.toLowerCase())) {
            dumpCommand(ctx);
        }

        //Read msg only from the player turn
        try {
            if (playerTurn.getCtx().equals(ctx)) {
                handleGameTurnForPlayersTurn(msg);
            }
        } catch (NumberFormatException e) {
            writeToPlayer(playerTurn, "[Coinche]: Invalid input\n");
            if (error == 1) {
                error = 0;
                askContractValue();
            } else if (error == 2) {
                error = 0;
                startCoincheTurn();
            }
        } catch (IndexOutOfBoundsException e) {
            writeToPlayer(playerTurn, "[Coinche]: Invalid input\n");
            askPlayerToPlay(playerTurn);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
