/*
 * This class implements the "board" needed for a game of Dominion. The class
 * contains all of the decks needed to play the game. Players draw from these
 * and the deck's cards get placed into the players control.
 *  - The trash deck is always at idx 0
 * 
 * Robert Rosenberger
 * Last Modified: 2/3/2017
 */
package org.cs362.dominion;
import java.util.ArrayList;

public class Board {
	
	private ArrayList<Deck> decks;
	
	//constructor
	public Board(){
		decks = new ArrayList<Deck>();
		Deck temp = new Deck("Trash");
		decks.add(temp);
	}
	
	//create deck
	// - takes in card to make deck out of
	// - number cards that should be in the deck
	public void createDeck(Card c, int num){
		Deck temp = new Deck(c.getName());
		for(int j=0; j<num; j++){
			Card tmp = new Card(c);
			temp.addBottom(tmp);
		}
		decks.add(temp);
	}
	
	//gets the name of the deck
	// - takes in idx of deck
	// - throws out of range exception if not valid idx
	public String getName(int idx){
		return decks.get(idx).getName();
	}
	
	//add to deck
	// - takes in card to add to the deck
	// - takes in idx of deck
	// - throws out of range exception if not valid idx
	public void addToDeck(Card c, int idx){
		decks.get(idx).addBottom(c);
	}
	
	//add to trash
	// - takes in card to add to trash deck
	public void addToTrash(Card c){
		addToDeck(c, 0);
	}
	
	//draw from deck, removes one card from the deck
	// - takes idx of deck to draw from
	// - returns card drawn
	// - throws out of range exception if not valid idx
	public Card draw(int idx){
		return decks.get(idx).drawTop();
	}
	
	//returns number of decks
	public int numDecks(){
		return decks.size();
	}
	
	//get number of cards in deck
	// - takes in idx of deck
	// - throws out of range exception if not valid idx
	public int numCardsInDeck(int idx){
		return decks.get(idx).size();
	}
	
	//prints board
	public void print(){
		//TODO
		//PRINT THE DECKS
	}
	
}
