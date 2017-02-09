import java.io.*;
import java.util.*;

/*
Class Game
    Variables:
        Private:

        Public:
            ArrayList   <Deck>bank;
            int         numPlayers;
            ArrayList   <Player>players;

    Functions:
        Private:

        Public:
            Game()
            Game()
            Game(String ... names)
            printBank       ()
            playerTurn      (int playerNumber):


        Game()
            Game(String ... names)
            void        printBank();
            void        playerTurn(int playerNumber);


        Game()
            Description:
            Input:
            Output:
            Potential Errors:
                -

        Game(String ... names):
            Description:
            Input:
            Output:
            Potential Errors:
                -

        printBank:
            Description:
            Input:
            Output:
            Potential Errors:
                -

        playerTurn:
            Description:
            Input:
            Output:
            Potential Errors:
                -


        Number of cards:
        Gold:   30
        Silver: 40
        Copper: 60

        Actions: 10

        Estate:     24
        Dutchy:     12
        Province:   12

        Curse:
        2 players: 10
        3 players: 20
        4 players: 30
*/

public class Game {
    public static void main(String[] args) {
        System.out.print("Do you want to run this program with pretty printing? (yes/no): ");
        String pretty = scanner.nextLine();
        pretty = pretty.toLowerCase();

        if (pretty.equals("yes")) {
            setSleepTime(500);
            setPrintTime(40);
        }
        else {
            setSleepTime(0);
            setPrintTime(0);
        }

        //creates three players
        Game dominion = new Game("Connor", "Billy", "Lily");

        //prints an empty line
        System.out.println();
        //prints the whole bank for testing
        // dominion.printBank();

        for (int x = 0; x < dominion.numPlayers(); x++) {
            dominion.getPlayer(x).draw(5);
        }

        for (int x = 0; x < 20; x++) {
            dominion.playerTurn(0);
        }


        // for (int x = 0; x < 3; x++) {
        //     dominion.playerTurn(x);
        // }
    }

    //array of all decks the game owns
    ArrayList<Deck> bank;
    public Deck trash;
    int numPlayers;
    ArrayList<Player> players;
    static int printTime;
    static int sleepTime;

    public static void setPrintTime(int number) {
        printTime = number;
    }

    public static void setSleepTime(int number) {
        sleepTime = number;
    }

    public Game() {
        bank = new ArrayList<Deck>();
        players = new ArrayList<Player>();
        trash = new Deck();
        //treasure
        bank.add(new Deck(30, new Card("gold")));
        bank.add(new Deck(40, new Card("silver")));
        bank.add(new Deck(60, new Card("copper")));

        //victory
        bank.add(new Deck(12, new Card("province")));
        bank.add(new Deck(12, new Card("dutchy")));
        bank.add(new Deck(24, new Card("estate")));

        //action
        //String[] decksToImplement = {"village", "smithy", "adventurer", "curse", "witch", "cellar", "market"};

        String[] decksToImplement = {"adventurer", "ambassador", "baron", "council room", "cutpurse", "embargo", "feast", "gardens", "great hall", "mine", "curse"};

        for (String deckName: decksToImplement) {
            bank.add(new Deck(10, new Card(deckName)));
        }
    }

    public Game(String ... names) {
        bank = new ArrayList<Deck>();
        trash = new Deck();
        //treasure
        bank.add(new Deck(30, new Card("gold")));
        bank.add(new Deck(40, new Card("silver")));
        bank.add(new Deck(60, new Card("copper")));

        //victory
        bank.add(new Deck(12, new Card("province")));
        bank.add(new Deck(12, new Card("dutchy")));
        bank.add(new Deck(24, new Card("estate")));

        //build the action decks
        String[] decksToImplement = {"adventurer", "ambassador", "baron", "council room", "cutpurse", "embargo", "feast", "gardens", "great hall", "mine", "curse", "smithy", "village", "witch", "cellar"};

        for (String deckName: decksToImplement) {
            bank.add(new Deck(10, new Card(deckName)));
        }

        //build the players
        players = new ArrayList<Player>();
        for (String s: names) {
            players.add(new Player(s));
        }
        numPlayers = names.length;
    }

    public void playerTurn(int num) {

        /*******************************************************/
        /*************** Variable initialization ***************/
        /*******************************************************/

        String name = getPlayer(num).getName(); //players name
        String cardToPlay;                      //card the user will want to play

        //award the player one action and one buy to start the turn
        getPlayer(num).starterPoints();


        /*******************************************************/
        /******************** Begin actions ********************/
        /*******************************************************/

        clearScreen();
        printLineDelay(name + ", the game is drawing 5 cards...");
        // getPlayer(num).draw(5);
        clearAndShowHand(num, sleepTime);

        /*******************************************************/
        /******************** Action phase ********************/
        /*******************************************************/

        //if the player has action points and they have action cards in their hand
        while (getPlayer(num).hasActions()) {

            printLineDelay("Please play an action card:\n");
            //print the number of actions, buys and coins the player has
            System.out.println(getPlayer(num).getMoves());

            System.out.println("\nCards you can play:");
            //print only the action cards in their hand
            getPlayer(num).printHandType("action");
            System.out.print("Select your card: ");

            cardToPlay = scanner.nextLine();

            // getPlayer(num).playCard(cardToPlay);

            playCard(num, cardToPlay);


            if (cardToPlay.equals("skip")) {
                getPlayer(num).skipActionsPhase();
            }
            else {
                System.out.println("");
                getPlayer(num).showHand();
            }
        }
        //if player did not have an action card
        if (!getPlayer(num).handContainsType("action")) {
            printLineDelay("You do not have an action card...");
        }
        else {
            printLineDelay("You do not have any more action points...");
        }
        printLineDelay("\nMoving on to buying phase...\n");
        printLineDelay("Press enter to continue: ");
        scanner.nextLine();


        /*******************************************************/
        /******************** Buying Phase ********************/
        /*******************************************************/

        //tally up the total treasure value
        getPlayer(num).sumTreasure();

        //set the loop to exit by default
        boolean done = true;
        boolean previousInputError = false;
        do {
            clearAndShowHand(num, sleepTime);
            System.out.println("");
            //print the moves
            printLineDelay(getPlayer(num).getMoves());
            System.out.println("\n\nHere are all the cards you can afford in the bank: ");
            //print only items in the bank that the player can afford
            printBank(getPlayer(num).getValues());

            if (!previousInputError) {
                printLineDelay("\nPlease enter a card you want to buy (or you may skip): ");
            } else {
                printLineDelay("\nThat is not a valid card. Please enter a card you want to buy (or you may skip): ");
            }
            String purchaseCard = scanner.nextLine();
            purchaseCard = purchaseCard.toLowerCase();

            /*******#### HERE IS _BUG_ ####*******/
            /*******####
                The buy function does not check the value of the card before buying
            ####*******/
            //if the getDeck function couldn't find the deck, it will return null
            //if it didn't return null, it means it found the deck.
            if (getDeck(purchaseCard) != null) {
                getPlayer(num).buy(getDeck(purchaseCard));
                System.out.println("Purchase sucessfull");
                pause(sleepTime);
                done = true;
            }
            else if (purchaseCard.equals("skip")) {
                printLineDelay("Skipped. ");
                printLineDelay("Your turn will now end...\n");
                getPlayer(num).endTurn();
                done = true;
            }
            else {
                printLineDelay("Sorry, that is not a valid card. Try again.\n");
                printLineDelay("....");
                done = false;
                previousInputError = true;
            }
        } while (!done && (getPlayer(num).getValues() > 0));

        getPlayer(num).discardAll();
        printLineDelay("You may see the deck, or just press enter to end your turn: ");

        String request = scanner.nextLine();
        request = request.toLowerCase();
        if (request.equals("deck")) {
            printBank();
            printLineDelay("Press enter to end your turn: ");
            scanner.nextLine();
        }
        getPlayer(num).draw(5);
    }

    public boolean playCard(int num, String cardName) {
        if (getPlayer(num).hand.indexOf(cardName) >= 0) {
            Card temp = new Card(cardName);
            if (!temp.isType("action")) {
                System.out.println("Error. " + cardName + " is not an action card.");
                return false;
            }
            else {
                applyCardActions(getPlayer(num), num, temp);
                if (! (cardName.equals("feast")) ) {
                    getPlayer(num).discard(cardName);
                }
                getPlayer(num).modifyActions(-1);
                return true;
            }
        }
        else {
            System.out.println("Error. That card does not exist");
            return false;
        }
    }

    public void applyCardActions(Player player, int num, Card card) {
        player.modifyActions(card.getActions());
        player.modifyValues(card.getValue());
        player.modifyBuys(card.getBuys());

        if (card.getCards() > 0) {
            player.draw(card.getCards());
        }


        //Section to apply special actions from cards

        if (card.isType("special action")) {

            /*Custom Cards:
                Adventurer -
                    Reveal cards from your deck until you reveal 2 treasure cards.
                    Put those Treasure cards into your hand and discard the other reveal cards.
                Ambassador -
                    Reveal a card from your hand.
                    Return up to 2 copies of it from your hand to the Supply.
                    Then each other player gains a copy of it.
                Baron -
                    You may discard an Estate card.
                    If you do, +4 Coins. Otherwise, gain an Estate card.
                Council room -
                    +4 Cards, +1 Buy, Each other player draws a card
                Cutpurse -
                    Each other player discards a copper card (or reveals a hand with no copper)
                Embargo -
                    Trash for +2 coins. Put an Embargo token on top of a Supply pile.
                    A player gains a Curse card per Embargo token on that pile when a card is bought.
                Feast -
                    Trash this card. Gain a card costing up to 5 coins
                Gardens -
                    Worth 1 victory for every 10 cards in your deck(rounded down)
                Great hall -
                    Lets the player draw an extra card and have an extra action
                Mine -
                    Trash a treasure card from your hand.
                    Gain a treasure card costing up to 3 coins more and put that card in your hand
            */

            /*Custom Cards:
                DONE
                Ambassador -
                    Reveal a card from your hand.
                    Return up to 2 copies of it from your hand to the Supply.
                    Then each other player gains a copy of it.
                Embargo -
                    Trash for +2 coins. Put an Embargo token on top of a Supply pile.
                    A player gains a Curse card per Embargo token on that pile when a card is bought.
                Gardens -
                    Worth 1 victory for every 10 cards in your deck(rounded down)
            */
            //if the card is an adventurer
            if (card.getName().equals("adventurer")) {
                //number of treasures drawn so far is 0
                int numTreasures = 0;
                //the loop stops when we draw two treasure cards
                while (numTreasures < 2) {
                    //if we draw a treasure, increment the treasure counter
                    if (player.draw().isType("treasure")) {
                        numTreasures++;
                    }
                    //otherwise, discard that card we just drew
                    else {
                        player.discardAtIndex(player.hand.numCards() - 1);
                    }
                }
            }
            else if (card.getName().equals("baron")) {
                String discard;
                System.out.print("Do you want to discard an estate card? ");
                discard = scanner.nextLine();
                if (discard.toLowerCase().equals("yes")) {
                    player.discard("estate");
                    player.modifyValues(4);
                }
                else if (discard.toLowerCase().equals("no")) {
                    //draw from the estate deck
                    player.buy(getDeck("estate"));
                    System.out.println("You gained an estate card");
                }
            }
            else if (card.getName().equals("witch")) {
                for (int x = 0; x < players.size(); x++) {
                    if (x != num) {
                        getPlayer(x).buy(getDeck("curse"));
                    }
                }
            }
            else if (card.getName().equals("cellar")) {
                String discard;
                int numDraw = 0;
                do {
                    System.out.print("Select a card to discard or type \"done\": ");
                    discard = scanner.nextLine();
                    discard = discard.toLowerCase();
                    if (player.hand.hasCard(discard)) {
                        player.discard(discard);
                        numDraw++;
                        player.showHand();
                    }
                    else {
                        System.out.println("You don't have that card in your hand");
                    }
                    System.out.printf("You have discarded %d cards\n", numDraw);
                } while (!discard.equals("done"));
                player.draw(numDraw);
            }
            else if (card.getName().equals("council room")) {
                for (int x = 0; x < players.size(); x++) {
                    if (x != num) {
                        getPlayer(x).draw();
                    }
                }
                System.out.println("Each other player drew a card");
            }
            else if (card.getName().equals("cutpurse")) {
                for (int x = 0; x < players.size(); x++) {
                    if (x != num) {
                        if (getPlayer(x).hand.indexOf("copper") >= 0) {
                            System.out.printf("\n%s has discarded a copper", getPlayer(x).getName());
                            getPlayer(x).discardAtIndex(getPlayer(x).hand.indexOf("copper"));
                        }
                    }
                }
            }
            else if (card.getName().equals("feast")) {
                trash.addCard(player.hand.drawCard("feast"));
                System.out.println("Trashed the feast card");
                printBank(5);
                System.out.print("Select your card: ");
                String cardToBuy;
                cardToBuy = scanner.nextLine();
                cardToBuy.toLowerCase();
                player.buy(getDeck(cardToBuy));
            }
            else if (card.getName().equals("mine")) {
                String treasureName;
                boolean again = false;
                do {
                    System.out.print("Kind of treasure card: ");
                    treasureName = scanner.nextLine();
                    treasureName.toLowerCase();
                    if (treasureName.equals("copper")) {
                        trash.addCard(player.hand.drawCard("copper"));
                        player.hand.addCard(getDeck("silver").drawCard());
                        again = false;
                    }
                    else if (treasureName.equals("silver")) {
                        trash.addCard(player.hand.drawCard("silver"));
                        player.hand.addCard(getDeck("gold").drawCard());
                        again = false;
                    }
                    else if (treasureName.equals("gold")) {
                        System.out.println("Gold cards are the highest value cards. You can not trade them up to a higher value. Try again: ");
                        again = true;
                    }
                    else {
                        System.out.println("That is not a valid card. Try again: ");
                        again = true;
                    }
                } while (again);
            }
            else if (card.getName().equals("ambassador")) {
                String supply;
                System.out.print("Card you would like to discard: ");
                supply = scanner.nextLine();
                supply.toLowerCase();
                
            }
        }
    }

    public void printBank() {
        for (int x = 0; x < bank.size(); x++) {
            System.out.printf("%-15s# remaining: %d\tCost: %d\tDescription: %s\n", bank.get(x).cardInfo(0).getName(), bank.get(x).numCards(), bank.get(x).cardInfo(0).getCost(), bank.get(x).cardInfo(0).getDescription());
        }
    }

    public void printBank(int coinLimit) {
        for (int x = 0; x < bank.size(); x++) {
            if (bank.get(x).cardInfo(0).getCost() <= coinLimit) {
                System.out.printf("%-15s# remaining: %d\tCost: %d\tDescription: %s\n", bank.get(x).cardInfo(0).getName(), bank.get(x).numCards(), bank.get(x).cardInfo(0).getCost(), bank.get(x).cardInfo(0).getDescription());
            }
        }
    }

    public Player getPlayer(int number) {
        return players.get(number);
    }

    public Deck getDeck(String name) {
        for(int x = 0; x < bank.size(); x++) {
            String deckName = bank.get(x).cardInfo(0).getName();
            if (deckName.equals(name)) {
                return bank.get(x);
            }
        }
        return null;
    }

    public void printAllDecks(int num) {
        String name = getPlayer(num).getName();
        System.out.println("\n" + name + "'s draw deck: ");
        getPlayer(num).drawDeck.printDeck();
        System.out.println("\n" +  name + "'s hand:");
        getPlayer(num).showHand();
        System.out.println("\n" + name + "'s discarded deck: ");
        getPlayer(num).discard.printDeck();
    }

    public int numPlayers() {
        return numPlayers;
    }

    //building a scanner class that will be used for keyboard input
    private static Scanner scanner = new Scanner(System.in);

    /******* Text formatting *******/

    public void clearScreen() {
        System.out.print("\033[2J\033[K\033[H");
    }

    public void pause(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {}
    }

    public void clearAndShowHand(int playerNumber, int sleepTime) {
        clearScreen();
        System.out.println(getPlayer(playerNumber).getName() + "'s hand: ");
        getPlayer(playerNumber).showHand();
        pause(sleepTime);
    }

    public void printLineDelay(String text) {
        char[] charArr = text.toCharArray();
        for(int i = 0; i <= charArr.length-1; i++) {
            System.out.print(charArr[i]);
            pause(printTime);
        }
        pause(sleepTime);
    }

    public void printLineDelay(String text, int printTime) {
        char[] charArr = text.toCharArray();
        printTime = printTime/(charArr.length-1);
        for(int i = 0; i <= charArr.length-1; i++) {
            System.out.print(charArr[i]);
            pause(printTime);
        }
        pause(sleepTime);
    }
}
