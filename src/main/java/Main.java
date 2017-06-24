import static spark.Spark.*;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        SQLiteJDBC s = new SQLiteJDBC();

        get("/:query", (request, response) -> {
            ArrayList<String> parameters = processRequest(
                    request.params(":query"));

            ArrayList<Integer> targetingList = s
                    .baseTargeting(parameters.get(0), parameters.get(1));
            if (targetingList.size() < 1) {
                return "No Companies Passed from Targeting";
            }
            // printlist(targetingList);

            ArrayList<Integer> budgetList = s.budgetCheck();
            if (budgetList.size() < 1) {
                return "No Companies Passed from Budget";
            }
            // printlist(budgetList);

            ArrayList<Integer> bidList = s
                    .bidCheck(Integer.valueOf(parameters.get(2)));
            if (bidList.size() < 1) {
                return "No Companies Passed from BaseBid check";
            }
            // printlist(bidList);

            int winner = Winner(bidList.get(0));
            s.reduceBudget(winner);
            return winner;
        });

        // s.databaseScreenshot(); // to get Database screenshoot
        // Log log =new Log();
        // log.logScreenshoot(); // to get Log screenshoot

    }

    public static int Winner(int x) throws IOException {
        Log log = new Log();
        // write the Winner company to the Log
        log.writeFile("Winner = " + x + "\n***************\n");
        return x; // response the winner company's id
    }

    ////////////////////////////////////////////////////
    // splite request tokens
    public static ArrayList<String> processRequest(String str) {
        ArrayList<String> tokens = new ArrayList<String>();
        String[] strs = str.split("&");
        tokens.add(strs[0]);
        tokens.add(strs[1]);
        tokens.add(strs[2]);
        return getParameters(tokens);
    }

    // Get the parameters Country, Category, baseBid
    public static ArrayList<String> getParameters(ArrayList<String> tokens) {
        ArrayList<String> parameters = new ArrayList<String>();
        for (String token : tokens) {
            String[] strs = token.split("=");
            parameters.add(strs[1]);
        }
        return parameters;
    }

    //////////////////////////////////////////////////
    // Debug
    public static void printlist(ArrayList<Integer> list) {
        for (int item : list) {
            System.out.println(item);
        }
        System.out.println("////////////////");
    }

}
