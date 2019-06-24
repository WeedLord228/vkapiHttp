package Main;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] arrgs) throws IOException, AccesClosedException, PrivateProfileException, TooManyRequestsException, InterruptedException {
        String userToken = "ee460cc8652d97a6b60b99fb3228cbd53255e8157970eeb52d16c885972ab44b5b17eb6503e4adbaaac3f";
        Integer userid = 77201136;
        int requestsCount = 1000;

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
        int max = Integer.MIN_VALUE;
        Integer curId = 0;
        int counter = 0;

        for (Integer ffid : friendsOfFriendsIds) {
            Thread.sleep(900);
            try {
                if (counter >= requestsCount) break;
                curCount = HTTPWorker.getMutualFriendsList(userid + "", ffid + "", userToken).size();
                if (curCount > max) {
                    max = curCount;
                    curId = ffid;
                }
                counter++;
            } catch (AccesClosedException e) {

            } catch (PrivateProfileException e) {

            } catch (TooManyRequestsException e) {
                throw e;
            }
        }

        System.out.println("The user with the maximum count of common friends is:");
        System.out.println(HTTPWorker.getUserFullName(curId + "", userToken));
        System.out.println("And the count is: " + max);
    }

}
