package Main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTPWorker {
    static final String serviceToken = "b894cd4ab894cd4ab894cd4aadb8fff3a5bb894b894cd4ae598c742e94a963cefff8079";

    //    ArrayList<Integer>
    public static ArrayList<Integer> getFriendsList(String id, String token)
            throws IOException, AccesClosedException, PrivateProfileException, TooManyRequestsException {
        final String method = "friends.get";
        final String canonicalId = getUserId(id, serviceToken);
        final String stringUrl = "https://api.vk.com/method/" + method + "?user_id=" + canonicalId + "&v=5.95&" +
                "access_token=" + token;

        JSONObject obj = getJsonObj(stringUrl);
        try {
            obj = obj.getJSONObject("response");
        } catch (JSONException e) {
            Integer errorCode = Integer.parseInt(obj.getJSONObject("error").get("error_code").toString());
            checkException(errorCode);
        }
        JSONArray jsonArray = obj.getJSONArray("items");
        ArrayList<Integer> resultArr = new ArrayList<Integer>();
        for (Object o : jsonArray) {
            resultArr.add((Integer) o);
        }

        return resultArr;
    }

    public static ArrayList<Integer> getMutualFriendsList(String sourceId, String targetId, String userToken)
            throws IOException, TooManyRequestsException, PrivateProfileException, AccesClosedException {
        final String method = "friends.getMutual";
        final String canonicalSourceId = getUserId(sourceId, userToken);
        final String canonicalTargetId = getUserId(targetId, userToken);
        String stringUrl = "https://api.vk.com/method/" + method + "?v=9.95" + "&source_uid=" +
                canonicalSourceId + "&access_token=" + userToken + "&target_uid=" + canonicalTargetId;

        JSONObject obj = getJsonObj(stringUrl);
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = obj.getJSONArray("response");
        } catch (JSONException e) {
            Integer errorCode = Integer.parseInt(obj.getJSONObject("error").get("error_code").toString());
            checkException(errorCode);
        }
        ;
        ArrayList<Integer> resultArr = new ArrayList<Integer>();
        for (Object o : jsonArray) {
            resultArr.add((Integer) o);
        }

        return resultArr;
    }

    public static String getUserId(String userId, String token)
            throws IOException, AccesClosedException, TooManyRequestsException, PrivateProfileException {
        final String method = "users.get";
        final String stringUrl = "https://api.vk.com/method/" + method + "?user_ids=" + userId +
                "&v=5.95&" + "access_token=" + token;

        JSONObject jsonObject = getJsonObj(stringUrl);
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            arr = jsonObject.getJSONArray("response");
            obj = arr.getJSONObject(0);
            return obj.get("id").toString();
        } catch (JSONException e) {
            Integer errorCode = Integer.parseInt(jsonObject.getJSONObject("error").get("error_code").toString());
            checkException(errorCode);
        }
        return obj.toString();
    }

    public static String getUserFullName(String userId, String token) throws IOException, AccesClosedException, TooManyRequestsException, PrivateProfileException {
        final String method = "users.get";
        final String stringUrl = "https://api.vk.com/method/" + method + "?user_ids=" + userId +
                "&v=5.95&" + "access_token=" + token;

        JSONObject jsonObject = getJsonObj(stringUrl);
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        String result = "";
        try {
            arr = jsonObject.getJSONArray("response");
            obj = arr.getJSONObject(0);
            result += obj.get("first_name").toString() + " ";
            result += obj.get("last_name").toString();

        } catch (JSONException e) {
            Integer errorCode = Integer.parseInt(jsonObject.getJSONObject("error").get("error_code").toString());
            checkException(errorCode);
        }
        return result;
    }


    public static JSONObject getJsonObj(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine = "";
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    public static void checkException(Integer errorCode)
            throws PrivateProfileException, AccesClosedException, TooManyRequestsException {
        if (errorCode == 30)
            throw new PrivateProfileException();
        if (errorCode == 15)
            throw new AccesClosedException();
        if (errorCode == 6)
            throw new TooManyRequestsException();
    }

}
