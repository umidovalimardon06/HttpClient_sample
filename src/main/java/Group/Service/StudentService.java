package Group.Service;

import Group.Model.Student;
import Group.Utill.AppUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import static java.lang.System.out;
import static java.net.http.HttpRequest.BodyPublishers.ofString;
import static java.net.http.HttpResponse.BodyHandlers;

public class StudentService {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String URL_BASE = "http://localhost:8080/api/students";
    private static final Gson GSON = new GsonBuilder().create();

    /* --------------------------| CRUD OPERATIONS |--------------------------  */

    /* Create */
    private static void createNewStudent() {
        out.print("Enter Student name:: ");
        String name = AppUtils.scanLine.nextLine();
        out.print("Enter Student age:: ");
        Integer age = AppUtils.scanInt.nextInt();
        out.print("Enter Student GPA:: ");
        Double gpa = AppUtils.scanInt.nextDouble();
        out.print("Enter Student group ID:: ");
        Long groupId = AppUtils.scanInt.nextLong();

        StudentCreateRequest request = StudentCreateRequest.builder()
                .name(name)
                .age(age)
                .gpa(gpa)
                .groupId(groupId)
                .build();

        String json = GSON.toJson(request);
        out.println("\nSending JSON: " + json);

        try {
            var httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .header("Content-Type", "application/json")
                    .POST(ofString(json))
                    .build();

            var response = HTTP_CLIENT.send(httpRequest, BodyHandlers.ofString());

            out.println("Response Status: " + response.statusCode());
            out.println("Response Body: " + response.body());

            switch (response.statusCode()) {
                case 201 -> {
                    // ✅ Parse response as Student (with nested group)
                    Student student = GSON.fromJson(response.body(), Student.class);
                    out.println("\n✓ Student created successfully!");
                    out.println(student);
                }
                case 400 -> {
                    out.println("\n✗ Bad Request - Invalid data format");
                    out.println("Check your input values");
                }
                case 500 -> {
                    out.println("\n✗ Server Error - Group ID " + groupId + " not found!");
                    out.println("Please create a group first or use an existing group ID");
                }
                default -> {
                    out.println("\n✗ Unexpected status: " + response.statusCode());
                    out.println("Response: " + response.body());
                }
            }
            out.println("-------------------");

        } catch (IOException e) {
            out.println("\n✗ Cannot connect to server at " + URL_BASE);
            out.println("Make sure the server is running");
        } catch (InterruptedException e) {
            out.println("\n✗ Request interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            out.println("\n✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* Read All */
    private static void showAllStudents() {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .GET()
                    .build();

            var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Student[] students = GSON.fromJson(response.body(), Student[].class);

                if (students.length == 0) {
                    out.println("No students found.");
                } else {
                    out.println("\n=== All Students ===");
                    for (Student student : students) {
                        out.println(student);
                    }
                }
            } else {
                out.println("✗ Error fetching students. Status: " + response.statusCode());
            }
            out.println("-------------------");

        } catch (IOException | InterruptedException e) {
            out.println("✗ Error: " + e.getMessage());
        }
    }

    /* Read by ID */
    private static void showStudentById() {
        out.print("Enter student ID:: ");
        long studentId = AppUtils.scanInt.nextLong();

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + studentId))
                    .GET()
                    .build();

            var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Student student = GSON.fromJson(response.body(), Student.class);
                out.println("\n=== Student Details ===");
                out.println(student);
            } else if (response.statusCode() == 404) {
                out.println("✗ Student with ID " + studentId + " not found.");
            } else {
                out.println("✗ Error fetching student. Status: " + response.statusCode());
            }
            out.println("-------------------");

        } catch (IOException | InterruptedException e) {
            out.println("✗ Error: " + e.getMessage());
        }
    }

    /* Update */
    private static void updateStudent() {
        out.print("Enter Student ID to update:: ");
        long studentId = AppUtils.scanInt.nextLong();

        AppUtils.scanLine.nextLine(); // Clear buffer

        out.print("Enter Student name:: ");
        String name = AppUtils.scanLine.nextLine();
        out.print("Enter Student age:: ");
        int age = AppUtils.scanInt.nextInt();
        out.print("Enter Student GPA:: ");
        double gpa = AppUtils.scanInt.nextDouble();
        out.print("Enter Student group ID:: ");
        long groupId = AppUtils.scanInt.nextLong();

        StudentCreateRequest updateRequest = StudentCreateRequest.builder()
                .name(name)
                .age(age)
                .gpa(gpa)
                .groupId(groupId)
                .build();

        String json = GSON.toJson(updateRequest);

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + studentId))
                    .header("Content-Type", "application/json")
                    .PUT(ofString(json))
                    .build();

            var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> {
                    Student student = GSON.fromJson(response.body(), Student.class);
                    out.println("\n✓ Student updated successfully!");
                    out.println(student);
                }
                case 404 -> out.println("\n✗ Student with ID " + studentId + " not found.");
                case 500 -> out.println("\n✗ Server Error - Group ID " + groupId + " not found!");
                default -> out.println("\n✗ Failed to update. Status: " + response.statusCode());
            }
            out.println("-------------------");

        } catch (IOException | InterruptedException e) {
            out.println("✗ Error updating student: " + e.getMessage());
        }
    }

    /* Delete */
    private static void deleteStudent() {
        out.print("Enter student ID to delete:: ");
        long studentId = AppUtils.scanInt.nextLong();

        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + studentId))
                    .DELETE()
                    .build();

            var response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 204 -> out.println("✓ Student " + studentId + " deleted successfully!");
                case 404 -> out.println("✗ Student with ID " + studentId + " not found.");
                default -> out.println("✗ Failed to delete. Status: " + response.statusCode());
            }
            out.println("-------------------");

        } catch (IOException | InterruptedException e) {
            out.println("✗ Error deleting student: " + e.getMessage());
        }
    }

    /* --------------------------| CRUD OPERATIONS |--------------------------  */

    /* Run method to display menu */
    public static void run() {
        boolean running = true;

        while (running) {
            out.println("""
                    
                    ===== Student Management =====
                    1. Show all students
                    2. Show student by ID
                    3. Create new student
                    4. Update student
                    5. Delete student
                    6. Back to Main Menu
                    ==============================
                    Enter your choice: """);

            try {
                int choice = AppUtils.scanInt.nextInt();

                switch (choice) {
                    case 1 -> showAllStudents();
                    case 2 -> showStudentById();
                    case 3 -> createNewStudent();
                    case 4 -> updateStudent();
                    case 5 -> deleteStudent();
                    case 6 -> {
                        out.println("Returning to main menu...");
                        running = false;
                    }
                    default -> out.println("Invalid choice! Please select 1-6.");
                }
            } catch (java.util.InputMismatchException e) {
                out.println("Invalid input! Please enter a number.");
                AppUtils.scanInt.nextLine();
            }
        }
    }
}