
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

    /*************************************************************************************************
        The following information is an understanding of how to swift architecture works.
        We are currently using version 1 (v1.0), which does not include a keystone. This means
        that each time you make a PUT/GET/POST Request, you need to provide it with an Auth-Token
        The Restful API URL has the following structure:
            http://server-name:port/v1/AUTH_{account}/{container}/{object}
        Each account may have multiple containers, each container may contain multiple objects.

        You are not required to input all 3 things (account, container, object). However, you must at
        least include an account. For each additional field you add, container/object, your output
        will differ with it.

        We already have a set of test data ready here:
        Account - companyA
        Container - folder1
        Object - testA
     ***************************************************************************************************/


    /*
      Function:
        - Generates an Auth Token so we can use it later with other RESTFUL API Requests
        - The output of this function are the Metadata that comes as default
      Param:
        - User: User is packaged in this format {account}:{user}. Each account can have multiple users, and thus
                its format is as so
        - Password: The password used to access account information
     */
    public static void authenticate() throws Exception {
        System.out.println("Authenticate needs to run first. Once the Auth Token is received, it is saved as a variable " +
                "for use along with other REST API Commands");
        URL url = new URL("http://hln2329p:8080/auth/v1.0");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("X-Storage-User", "companyA:AdminA");
        con.setRequestProperty("X-Storage-Pass", "password");
        printHeader(con);
    }

    /*
      Send a Get Request
        -The GET Request will list all the containers available within the account
        -If you include the container, it will list all the objects
        -If you only include the account, it will list all the containers available
         within the account
        -If you list the objects as well, it will output the metadata of that object

      Return: The function written here only allows you to provide an account, so it will
          only list the containers within the account
     */
    public static void get(String account) throws Exception {

        String url = "http://hln2329p:8080/v1/AUTH_" + account;

        HttpURLConnection con = conn(url);

        //set method to GET
        con.setRequestMethod("GET");

        //add request Header
        //Setting the Auth Token
        con.setRequestProperty("X-Auth-Token", auth_token);
        con.setRequestProperty("Accept", "text/plain");
        con.setRequestProperty("Accept-Charset", "utf-8");
        con.setRequestProperty("Accept-Language", "en-US");
        responseCode(con, url, "GET");
        System.out.println("Because I only listed account as part of the url, it will display all the countainers within\n" +
                "the account. If you provide the account/container, it will list all the objects within the container");
        printOutput(con);
    }

    /*
      PUT Function differs when used in different levels (Account, Container, Object)
      Account - Request is not available in Account
      Containers - Create container
      Objects - Create or replace object
     */
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

        //Create a stream and write "Resource Content" -> This is placed as the object for
        // this put command "
        OutputStreamWriter out = new OutputStreamWriter(
                con.getOutputStream());
        out.write("Resource content");
        out.close();
        responseCode(con, url, "PUT");
        printHeader(con);
    }

    /*
      POST - Used to update, create, or delete metadata
      Usage:
         Account - Create, update, or delete account metadata
         Containers - Create, update, or delete container metadata
         Objects - Create or update object metadata
      Function:
         - Adds two custom metadata to the object testA
         - You do not always need to create custom metadata. Swift comes with a suit of commonly used Metadata
           as well. For example, X-Delete-At, Bulk-Dete etc.. For more info, visit swift's API Doc
         - The two custom metadata are "X-Object-Meta-Brand : BMW" and "X-Object-Meta-Model : x3"
         - When adding custom metadata, you must add in the following format:
              "X-Object-Meta-{name} : {value}"
     */
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
            //Adding custom metadata here
            con.setRequestProperty((String) mentry.getKey(), (String) mentry.getValue());
        }
        responseCode(con, url, "POST");
        System.out.println("POST is responsible for updating/creating metadata for a container/account/object.\n" +
                "The updated metadata can be seen in the next command, which is head. In this command, I made two custom metadata, 'X-Object-Meta-Brand: BMW' \n" +
                "and 'X-Object-Meta-Model: x3'. Reference the HEAD request to see an update\n");
        printHeader(con);
    }

    /*
      HEAD - Show metadata
      Usage:
         Account - Show Account Metadata
         Containers - Show container Metadata
         Objects - Show object metadata
      Function:
         Earlier in the POST Method, we added 2 new custom metadata, HEAD
         will be able to show that the custom Metadata has been updated
     */
    public static void sendHead(String account, String container, String object) throws Exception{
        String url = formURL(account, container, object);
        HttpURLConnection con = conn(url);
        con.setRequestMethod("HEAD");
        con.setRequestProperty("X-Auth-Token", auth_token);
        responseCode(con, url, "HEAD");
        printHeader(con);
    }

    /*****************************************************************************
      The functions below are merely helper functions. I used them to refactor code.
      For more information, please refer to its official API Document:
              https://developer.openstack.org/api-ref/object-storage
     *****************************************************************************/

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

            //Caching the Auth Token
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
       Establish a HTTP Connection
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

    /***************************************************************************
                           GOOD LUCK, HOPE THIS WAS HELPFUL
     ***************************************************************************/


}




