import javax.management.relation.RelationNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by j on 8/16/16.
 */
public class Board {
    private int positionOnBoard;
    private int jailRollNumber;
    private ArrayList<Integer> stats;
    private static final int BOARDSIZE = 40;

    public Board(){
        // constructor
        positionOnBoard = 0;
        jailRollNumber = 0;
        stats = new ArrayList<>(Collections.nCopies(BOARDSIZE, 0));
    }

    public void advance(int numToAdvance, Random inputRand){
        boolean wasGoToJail = false;
        int originalPosition = positionOnBoard;
        if(jailRollNumber==0){
            // not in jail
            positionOnBoard += numToAdvance;
            positionOnBoard %= BOARDSIZE; // ensuring not off board
            if(positionOnBoard == 7 || positionOnBoard == 22 || positionOnBoard == 36){
                // checking chance cards
                chanceCardCalculate(inputRand);
            }
            else if(positionOnBoard == 2 || positionOnBoard == 17 || positionOnBoard == 33){
                // checking community chest cards
                communityChestCalculate(inputRand);
            }
            else if(positionOnBoard == 30){
                // go to jail
                positionOnBoard = 10;
                jailRollNumber = 1;
                wasGoToJail = true;
            }
        }
        else {
            // in jail
            jailRollNumber += 1;
            if(jailRollNumber == 3){
                jailRollNumber = 0; // leaving jail after 2nd roll but moving on next roll
            }
        }
        if(positionOnBoard != originalPosition){
            // if position moved then record the statistic
            if(wasGoToJail){
                statistics(30); // saving fact that landed on position 30 on board
            }else{
                statistics(positionOnBoard);
            }
        }
    }

    public void displayStats(){
        ArrayList<Integer> copyOfOriginal = new ArrayList<>(BOARDSIZE);
        for(int i=0; i<BOARDSIZE; ++i){
            copyOfOriginal.add(i, stats.get(i));
        }
        Collections.sort(copyOfOriginal);
        System.out.println("Board position and hits out of 1 million");
        for(int i=0; i<copyOfOriginal.size(); ++i){
            // looping over sorted list
            for(int j=0; j<stats.size(); ++j){
                if(copyOfOriginal.get(i) == stats.get(j)){
                    System.out.println("Pos " + j + " is: " + copyOfOriginal.get(i));
                }
            }
        }

    }

    private void statistics(int position){
        int oldAmount = stats.get(position);
        stats.remove(position);
        stats.add(position,++oldAmount);
    }

    private void communityChestCalculate(Random inputRand){
        int communityCardNumber = (inputRand.nextInt()%16)+1;
        if(communityCardNumber==10){
            // move to Go
            positionOnBoard=0;
        }
        else if(communityCardNumber==16){
            // move to Jail
            positionOnBoard=10;
            jailRollNumber = 1;
        }
    }

    private void chanceCardCalculate(Random inputRand){
        int chanceCardNumber = (inputRand.nextInt()%16)+1;
        if(chanceCardNumber==1){
            positionOnBoard = 0; // go to Go
        }
        else if(chanceCardNumber==3){
            positionOnBoard -= 3; // move back 3 spaces
        }
        else if(chanceCardNumber==4){
            // moving to next utility
            if(positionOnBoard==7 || positionOnBoard==36){
                positionOnBoard = 12;
            }else if(positionOnBoard==22){
                positionOnBoard = 28;
            }
        }
        else if(chanceCardNumber==5){
            positionOnBoard = 10; // go to jail
            jailRollNumber = 1;
        }
        else if(chanceCardNumber==7){
            positionOnBoard = 11; // go to st.charles place
        }
        else if(chanceCardNumber==9 || chanceCardNumber==11){
            // move to nearest railroad
            if(positionOnBoard==7){
                positionOnBoard = 15;
            }
            else if(positionOnBoard==22){
                positionOnBoard = 25;
            }
            else if(positionOnBoard == 36){
                positionOnBoard = 5;
            }
        }
        else if(chanceCardNumber==10){
            positionOnBoard = 5; // move to reading railroad
        }
        else if(chanceCardNumber==12){
            positionOnBoard = 39; // move to boardwalk
        }
        else if(chanceCardNumber==14){
            positionOnBoard = 24; // move to illinois ave
        }
    }
}
