import java.util.*;

public class RollResult { // Added missing '{'
    int total;
    int modifier;
    Vector<Integer> rolls; // Uncommented the rolls field

    private RollResult(int total, // Corrected constructor name from Roll_Result to RollResult
                      int modifier,
                      Vector<Integer> rolls) {
        this.total = total; // Fixed typo 'thistotal' to 'this.total'
        this.modifier = modifier;
        this.rolls = rolls;
    }

    public RollResult(int bonus) {
        this.total = bonus;
        this.modifier = bonus;
        rolls = new Vector<Integer>();
    }

    public void addResult(int res) {
        total += res; // Added missing semicolon
        rolls.add(res);
    }

    public RollResult andThen(RollResult r2) {
        int total = this.total + r2.total;
        Vector<Integer> rolls = new Vector<Integer>();
        rolls.addAll(this.rolls);
        rolls.addAll(r2.rolls);
        return new RollResult(total,
                this.modifier + r2.modifier,
                rolls);
    }

    public String toString() {
        return total + "  <= " + rolls.toString();
	}
}