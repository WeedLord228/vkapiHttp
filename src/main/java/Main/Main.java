package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
            throws IOException, AccesClosedException, PrivateProfileException, TooManyRequestsException, InterruptedException {
//        if (args.length == 0 || args.length != 4) {
//            System.out.println("Usage: ");
//            System.out.println("[YOUR_ID] [YOUR_USER_TOKEN] [TIMEOUT] [REQUESTS_COUNT]");
//            return;
//        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write your User Token");
        String userToken = scanner.nextLine();//        "ee460cc8652d97a6b60b99fb3228cbd53255e8157970eeb52d16c885972ab44b5b17eb6503e4adbaaac3f"
        System.out.println("Write your User ID");
        Integer userid = scanner.nextInt(); //77201136
        System.out.println("Write how many requests need to be send:");
        int requestsCount = scanner.nextInt();
        System.out.println("Write timeout of ecach mutual request (900 min)");
        int timeOut = scanner.nextInt();//900
        scanner.close();

        ArrayList<Integer> friendsIDList = HTTPWorker.getFriendsList(userid + "", HTTPWorker.serviceToken);
        ArrayList<Integer> friendsOfFriendsIds = new ArrayList<Integer>();
        for (Integer id : friendsIDList) {
            try {
                ArrayList<Integer> tempList = HTTPWorker.getFriendsList(id + "", HTTPWorker.serviceToken);
                if (tempList.size() <= 250)
                    for (Integer id2 : tempList) {
                        if (!friendsIDList.contains(id2) && !friendsOfFriendsIds.contains(id2) && !id2.equals(userid))
                            friendsOfFriendsIds.add(id2);
                    }
            } catch (AccesClosedException e) {
            } catch (PrivateProfileException e) {
            } catch (TooManyRequestsException e) {
                throw e;
            }
        }
        System.out.println("Friends of friends count: " + friendsOfFriendsIds.size());

        int curCount = 0;
        int counter = 0;
        ArrayList<Pair> resultList = new ArrayList<Pair>();

        for (Integer ffid : friendsOfFriendsIds) {
            Thread.sleep(timeOut);
            try {
                if (counter >= requestsCount) break;
                curCount = HTTPWorker.getMutualFriendsList(userid + "", ffid + "", userToken).size();
                resultList.add(new Pair(ffid + "", curCount));
                counter++;
            } catch (AccesClosedException e) {

            } catch (PrivateProfileException e) {

            } catch (TooManyRequestsException e) {
                throw e;
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("Ten people with the most count of common friends:");
        resultList.sort(Comparator.<Pair>reverseOrder());
        for (int i = 0; i < 10; i++) {
            System.out.println(HTTPWorker.getUserFullName(resultList.get(i).name, HTTPWorker.serviceToken) +
                    ": " + resultList.get(i).value);
        }
    }

}
