package validator.register;

import domain.CityRegisterCheckerResponse;
import domain.Person;
import exception.CityRegisterException;
import validator.register.CityRegisterChecker;

public class RealCityRegisterChecker implements CityRegisterChecker {

    public CityRegisterCheckerResponse checkPerson(Person person) throws CityRegisterException {
        return null;
    }
}
