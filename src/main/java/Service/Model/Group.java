package Group.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Group {
    private Long id;
    private String name;
    private Integer level;

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    @AllArgsConstructor
    @Data
    @Builder
    public static class Student {
        private String name;
        private int age;
        private double gpa;
        private long groupId;

        @Override
        public String toString() {
            return "Student{" +
                    "groupId=" + groupId +
                    ", gpa=" + gpa +
                    ", age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
