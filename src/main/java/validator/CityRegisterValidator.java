package validator;

import domain.Person;
import domain.register.AnswerCityRegister;
import domain.register.AnswerCityRegisterItem;
import domain.register.CityRegisterResponse;
import domain.Child;
import domain.StudentOrder;
import exception.CityRegisterException;
import exception.TransportException;
import validator.register.CityRegisterChecker;
import validator.register.FakeCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator
{

    public static final String IN_CODE = "NO_GRN";
    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();
            ans.addItem(checkPerson(so.getHusband()));
            ans.addItem(checkPerson(so.getWife()));
            for (Child child : so.getChildren()) {
                ans.addItem(checkPerson(child));
            }

        return ans;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;

        try {
           CityRegisterResponse tmp = personChecker.checkPerson(person);
           status = tmp.isExisting() ?
                   AnswerCityRegisterItem.CityStatus.YES :
                   AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());
        }

        AnswerCityRegisterItem ans = new AnswerCityRegisterItem(status, person, error);
        return ans;
    }
}
