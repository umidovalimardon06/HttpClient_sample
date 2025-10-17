package Group.Service;

import Group.MainService;
import Group.Model.Group;
import Group.Utill.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class GroupService {
    public static void run() throws IOException, InterruptedException {
        w: while(true) {
            System.out.println("""
                    1. Show all groups
                    2. Show group by id
                    3. Create new group
                    4. Update group
                    5. Delete group
                    6. Back
                    """);

            switch (AppUtils.scanInt.nextInt()){
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
                    MainService.run();
                }
                default -> {
                    System.out.println("No idea!");
                }
            }
        }
    }

    private static void deleteGroup() {
        System.out.print("Enter group id that will be deleted::");
        long l = AppUtils.scanInt.nextLong();

        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/groups/"+l))
                    .headers("Content-Type","application/json")
                    .DELETE()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==204){
                System.out.println(l+" group is deleted!");
            } else {
                System.out.println("Something went wrong!");
            }

            System.out.println("-------------------");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

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

        Gson gson = new GsonBuilder()
                .create();

        String json = gson.toJson(updatedGroup);

        String url = "http://localhost:8080/api/groups/"+l;
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .headers("Content-Type","application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Group group = gson.fromJson(response.body(),Group.class);
            System.out.println(group);

            System.out.println("-------------------");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createNewGroup() {
        System.out.print("Enter Group name::");
        String groupName = AppUtils.scanLine.nextLine();
        System.out.print("Enter Group level::");
        int level = AppUtils.scanInt.nextInt();

        Group newGroup = Group.builder()
                .name(groupName)
                .level(level)
                .build();

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(newGroup);
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/groups"))
                    .headers("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Group group = gson.fromJson(response.body(),Group.class);
            System.out.println(group);

            System.out.println("-------------------");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void showGroupById() throws IOException, InterruptedException {
        System.out.print("Enter group id::");
        long l = AppUtils.scanInt.nextLong();
        String url = "http://localhost:8080/api/groups/"+l;
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder()
                    .create();
            Group groups = gson.fromJson(response.body(), Group.class);

            if (groups == null){
                System.out.println("Not found-!");
            } else {
                System.out.println(groups);
            }

            System.out.println("-------------------");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private static void showAllGroups() throws IOException, InterruptedException {
        try (var client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/groups"))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder()
                    .create();

            Group[] groups = gson.fromJson(response.body(), Group[].class);
            for (Group group : groups) {
                System.out.println(group);
            }
            System.out.println("-------------------");
        }
    }
}
