import java.util.ArrayList;
import java.util.Random;

/**
 * Created by j on 8/16/16.
 */
public class Monopoly {
    Random rand = new Random();

    public static void main(String args[]) {
        Monopoly mono = new Monopoly();
    }

    public Monopoly(){
        // setup game board
        Board mBoard = new Board();
        for(int i=0; i<10000000; ++i){
            mBoard.advance(diceRoll(), rand);
        }
        mBoard.displayStats();
    }

    private int diceRoll(){
        return rand.nextInt(12)+1;
    }

}