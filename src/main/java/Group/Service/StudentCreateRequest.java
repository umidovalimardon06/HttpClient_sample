package Group.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateRequest {
    private String name;
    private Integer age;
    private Double gpa;
    private Long groupId;  //
}