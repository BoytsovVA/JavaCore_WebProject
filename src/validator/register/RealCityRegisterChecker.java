package validator.register;

import domain.register.CityRegisterResponse;
import domain.Person;
import exception.CityRegisterException;
import exception.TransportException;

public class RealCityRegisterChecker implements CityRegisterChecker {

    public CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException {
        return null;
    }
}
