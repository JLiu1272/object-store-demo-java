
import java.net.HttpURLConnection;

import java.net.*;
import java.io.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.net.HttpURLConnection;

/**
 * Created by LIUJE3 on 6/26/2017.
 */
public class connect {

    private static final String GET_URL = "";
    private static String auth_token = "";


    public static void main(String[] args)throws Exception {

        //Generate Metadata
        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap.put("X-Object-Meta-Brand", "BMW");
        hmap.put("X-Object-Meta-Model", "x3");

        authenticate();
        System.out.println("\n");
        get("companyA");
        System.out.println("\n");
        sendPut("companyA","folder1","testA");
        System.out.println("\n");
        sendPost(hmap, "companyA", "folder1", "testA");
        System.out.println("\n");
        sendHead("companyA", "folder1", "testA");
    }

    public static void authenticate() throws Exception {
        System.out.println("Authenticate needs to run first. Once the Auth Token is received, it is saved as a variable " +
                "for use along with other REST API Commands");
        URL url = new URL("http://hln2329p:8080/auth/v1.0");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-Storage-User", "companyA:AdminA");
        con.setRequestProperty("X-Storage-Pass", "password");
        printHeader(con);
    }

    public static void get(String account) throws Exception {

        String url = "http://hln2329p:8080/v1/AUTH_" + account;

        HttpURLConnection con = conn(url);

        //set method to GET
        con.setRequestMethod("GET");

        //add request Header
        con.setRequestProperty("X-Auth-Token", auth_token);
        con.setRequestProperty("Accept", "text/plain");
        con.setRequestProperty("Accept-Charset", "utf-8");
        con.setRequestProperty("Accept-Language", "en-US");
        responseCode(con, url, "GET");
        System.out.println("Because I only listed account as part of the url, it will display all the countainers within\n" +
                "the account. If you provide the account/container, it will list all the objects within the container");
        printOutput(con);
    }

    // HTTP POST request
    public static void sendPut(String account, String container, String object) throws Exception {

        String url = formURL(account, container, object);
        HttpURLConnection con = conn(url);

        //add reuqest header
        con.setRequestMethod("PUT");

        con.setRequestProperty("X-Auth-Token", auth_token);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(
                con.getOutputStream());
        out.write("Resource content");
        out.close();
        responseCode(con, url, "PUT");
        printHeader(con);
    }

    // Post Head Show Metadata
    public static void sendPost(HashMap<String, String> metadata, String account, String container, String object) throws Exception {

        String url = formURL(account, container, object);
        HttpURLConnection con = conn(url);

        con.setRequestMethod("POST");
        con.setRequestProperty("X-Auth-Token", auth_token);

        //Iterating through the set of metadata
        Set set = metadata.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Map.Entry mentry = (Map.Entry) iterator.next();
            con.setRequestProperty((String) mentry.getKey(), (String) mentry.getValue());
        }
        responseCode(con, url, "POST");
        System.out.println("POST is responsible for updating/creating metadata for a container/account/object.\n" +
                "The updated metadata can be seen in the next command, which is head. In this command, I made two custom metadata, 'X-Object-Meta-Brand: BMW' \n" +
                "and 'X-Object-Meta-Model: x3'. Reference the HEAD request to see an update\n");
        printHeader(con);
    }

    //Send Head Request
    public static void sendHead(String account, String container, String object) throws Exception{
        String url = formURL(account, container, object);
        HttpURLConnection con = conn(url);
        con.setRequestMethod("HEAD");
        con.setRequestProperty("X-Auth-Token", auth_token);
        responseCode(con, url, "HEAD");
        printHeader(con);
    }

    /*
      Print the response output
      Param:
         con (HttpURLConnection)
     */
    public static void printOutput(HttpURLConnection con) throws Exception{
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine + "\n");
        }
        in.close();
        //print result
        System.out.println(response.toString());
    }

    /*
      Print Header Fields
      Param:
        con (HttpURLConnection)
     */
    public static void printHeader(HttpURLConnection con){
        StringBuilder builder = new StringBuilder();
        Map<String, List<String>> map = con.getHeaderFields();
        int i = 0;
        for (Map.Entry<String, List<String>> entry : map.entrySet())
        {
            if (entry.getKey() == null)
                continue;
            builder.append( entry.getKey())
                    .append(": ");
            if(entry.getKey().equals("X-Auth-Token")){
                auth_token = (String) entry.getValue().get(0);

            }

            List<String> headerValues = entry.getValue();
            Iterator<String> it = headerValues.iterator();
            if (it.hasNext()) {
                builder.append(it.next());

                while (it.hasNext()) {
                    builder.append(", ")
                            .append(it.next());
                }
            }

            builder.append("\n");
        }

        System.out.println(builder);
    }

    /*
       Prints Response Code
       Param:
          Con (HttpURLConnection) - an already opened connection
     */
    public static void responseCode(HttpURLConnection con, String url, String method) throws IOException {
        int responseCode = con.getResponseCode();
        System.out.println("\nSending '" + method + "' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
    }

    /*
       Establish an HTTP Connection
       Param:
           url (String) - the url in string
       Return:
            con (HttpURLConnection) - an opened session based on URL
     */
    public static HttpURLConnection conn(String url) throws Exception {
        URL obj = new URL(url);
        return (HttpURLConnection) obj.openConnection();
    }

    /*
      A Helper function for creating a URL based on whether container or
      object exist
      Param:
        account - User account
        container - Swift Container
        object - Swift Object
       Return:
         A String of a completed URL
     */
    public static String formURL(String account, String container, String object){
        String url = "http://hln2329p:8080/v1/AUTH_" + account + "/";
        if (!container.isEmpty()){
            url += container + "/";
        }
        if (!object.isEmpty()){
            url += object;
        }
        return url;
    }


}




