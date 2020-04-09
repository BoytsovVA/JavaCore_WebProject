package dao;

import domain.CountryArea;
import domain.PassportOffice;
import domain.RegisterOffice;
import domain.Street;
import exception.DaoException;

import java.util.List;

public interface DictionaryDao {
    List<Street> findStreet(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException;
}
