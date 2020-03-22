package validator;

import domain.AnswerChildren;
import domain.StudentOrder;

public class ChildrenValidator
{
    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("Children Check is running");
        return new AnswerChildren();
    }
}
