package validator.register;

import domain.CityRegisterCheckerResponse;
import domain.Person;
import exception.CityRegisterException;

public interface CityRegisterChecker {
    CityRegisterCheckerResponse checkPerson(Person person) throws CityRegisterException;
}
