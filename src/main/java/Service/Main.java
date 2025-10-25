package Group;

import Group.Service.GroupService;
import Group.Service.StudentService;
import Group.Utill.AppUtils;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        run();
        System.out.println("Program has finished!");
    }

    public static void run() throws IOException, InterruptedException {
        while (true) {
            System.out.println("""
                    ::Pick your choice::
                    1.Group Service
                    2.Student Service
                    3.Exit
                    """);
            switch (AppUtils.scanInt.nextInt()) {
                case 1 -> {
                    GroupService.run();
                }
                case 2 -> {
                    StudentService.run();
                }
                case 3 -> {
                    System.out.println("Bye bro:");
                    return;
                }
                default -> System.out.println("No idea!");
            }
        }
    }
}
