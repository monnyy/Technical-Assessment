// FINAL VERSION OF THE CODE

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CodingExercise {

    public static void main(String[] args) throws IOException {

        // total number of days for flights allowed
        final int totalDays = 2;

        // total number of boxes per flight allowed
        final int totalBoxes = 20;

        // store the content of the json file into a string
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/coding-assigment-orders.json")));

        // creating JSONObject from source file
        JSONObject obj = new JSONObject(jsonContent);

        if (obj.names() == null) {
            // just in case the json file is empty
            System.out.print("\nNothing to display!");

        } else {
            // obtain all the keys(orders) into one single string
            String orderStr = obj.names().toString();
            // remove the extra [] and convert the content into an array of keys
            String[] orderArr = orderStr.substring(1, orderStr.length() - 1).split(",");

            // for some reason, the order in which the orders were provided is changed
            // so, sort is applied to the array to recover the order in which it was originally provided
            Arrays.sort(orderArr);

            // helps to keep track of the ongoing count of boxes and days
            int[] currDayCount = {1, 1, 1};
            int[] currBoxCount = {0, 0, 0};

            // iterate through the object and process the orders accordingly
            for (String key : orderArr) {
                // get the current order given a key and remove the extra "
                JSONObject currOrder = obj.getJSONObject(key.replaceAll("\"", ""));

                // get the destination of the current order
                String currDest = currOrder.get("destination").toString();

                // holds the index of the desired flight number
                // the initial value -1 represents unrecognized flight codes
                int i = -1;

                // holds the output string
                // this initial value is printed in case the flight code is unrecognized,
                // or the max order per flights is reached for total flight days
                String printToConsole = String.format("order: %s, flightNumber: not scheduled", key);

                // assign index according to flight codes
                switch (currDest) {
                    case "YYZ":
                        i = 0; break;
                    case "YYC":
                        i = 1; break;
                    case "YVR":
                        i = 2; break;
                    default:
                        break;
                }

                // if a scheduled destination is found,
                if (i != -1) {
                    // increment the box count in case proper flight code is recognized
                    ++currBoxCount[i];

                    // if the current day count for each flight is less than the maximum,
                    // and the current box count per flight is less than the maximum,
                    // then continue accepting more boxes accordingly
                    if (currBoxCount[i] <= totalBoxes && currDayCount[i] <= totalDays) {
                        // prepare the output string depending on the destination code
                        printToConsole = String.format("order: %s, flightNumber: %d, departure: YUL, arrival: %s, day: %d",
                                key, i + 1, currDest, currDayCount[i]);

                        // reset the box count when max capacity per flight is reached
                        // and send the remaining boxes to the following day
                        if ((currBoxCount[i] % totalBoxes) == 0) {
                            ++currDayCount[i];
                            currBoxCount[i] = 0;
                        }
                    }
                }

                // print the desired output to console
                System.out.println(printToConsole);
            }
        }
    }
}