package validator;

import domain.AnswerStudent;
import domain.StudentOrder;

public class StudentValidator
{
    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("Студенты проверяются");
        return new AnswerStudent();
    }
}
