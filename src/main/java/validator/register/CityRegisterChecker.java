package validator.register;

import domain.register.CityRegisterResponse;
import domain.Person;
import exception.CityRegisterException;
import exception.TransportException;

public interface CityRegisterChecker {
    CityRegisterResponse checkPerson(Person person)
            throws CityRegisterException, TransportException;
}
