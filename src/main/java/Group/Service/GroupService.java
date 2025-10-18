package Group.Service;

import Group.Main;
import Group.Model.Group;
import Group.Utill.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GroupService {
    /**/
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String URL_BASE = "http://localhost:8080/api/groups";
    private static final Gson GSON = new GsonBuilder().create();
    /**/


    /* --------------------------| CRUD OPERATIONS |--------------------------  */

    /* Create */
    private static void createNewGroup() {
        System.out.print("Enter Group name::");
        String groupName = AppUtils.scanLine.nextLine();
        System.out.print("Enter Group level::");
        int level = AppUtils.scanInt.nextInt();

        Group newGroup = Group.builder()
                .name(groupName)
                .level(level)
                .build();
        String json = GSON.toJson(newGroup);

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            for (int i = 1; i < 100_000 ; i++) {
                var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.statusCode());
            }

            /*if (response.statusCode() == 201) {
                System.out.println("Create command executed");
                Group group = GSON.fromJson(response.body(), Group.class);
                System.out.println(group);
            }
            else System.out.println("External error !");
            System.out.println("-------------------");*/

        } catch (IOException | InterruptedException e) {
            System.out.println("Error on Crete method");
        }
    }
    /* Read */
    private static void showAllGroups() throws IOException, InterruptedException {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .GET()
                    .build();

            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                Group[] groups = GSON.fromJson(response.body(), Group[].class);
                for (Group group : groups) {
                    System.out.println(group);
                }
            } else {
                System.out.println("Error on read all method");
            }
            System.out.println("-------------------");
        } catch (IOException | InterruptedException | JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /* Ready by id */
    private static void showGroupById() throws IOException, InterruptedException {
        System.out.print("Enter group id::");
        long l = AppUtils.scanInt.nextLong();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "/" + l))
                .GET()
                .build();
        var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
            Group group = GSON.fromJson(response.body(), Group.class);
            System.out.println(group);
        } else {
            System.out.println("Error on read by id method");
        }
        System.out.println("-------------------");

    }
    /* Update */
    private static void updateGroup() {
        System.out.print("Enter group id that will be updated::");
        long l = AppUtils.scanInt.nextLong();
        System.out.print("Enter Group name::");
        String groupName = AppUtils.scanLine.nextLine();
        System.out.print("Enter Group level::");
        int level = AppUtils.scanInt.nextInt();

        Group updatedGroup = Group.builder()
                .name(groupName)
                .level(level)
                .build();

        String json = GSON.toJson(updatedGroup);

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + l))
                    .headers("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                Group group = GSON.fromJson(response.body(), Group.class);
                System.out.println(group);
            } else {
                System.out.println("Error on update");
            }
            System.out.println("-------------------");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /* Delete */
    private static void deleteGroup() {
        System.out.print("Enter group id that will be deleted::");
        long l = AppUtils.scanInt.nextLong();

        try  {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + l))
                    .headers("Content-Type", "application/json")
                    .DELETE()
                    .build();

            var response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 204) {
                System.out.println(l + " group is deleted!");
            } else {
                System.out.println("Something went wrong!");
            }

            System.out.println("-------------------");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /* --------------------------| CRUD OPERATIONS |--------------------------  */




    /* Run method to display menu: */
    public static void run() throws IOException, InterruptedException {
        w:
        while (true) {
            System.out.println("""
                    1. Show all groups
                    2. Show group by id
                    3. Create new group
                    4. Update group
                    5. Delete group
                    6. Back
                    """);

            switch (AppUtils.scanInt.nextInt()) {
                case 1 -> {
                    showAllGroups();
                }
                case 2 -> {
                    showGroupById();
                }
                case 3 -> {
                    createNewGroup();
                }
                case 4 -> {
                    updateGroup();
                }
                case 5 -> {
                    deleteGroup();
                }
                case 6 -> {
                    Main.run();
                }
                default -> {
                    System.out.println("No idea!");
                }
            }
        }
    }

}
