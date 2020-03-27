package dao;

import domain.StudentOrder;
import exception.DaoException;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder so) throws DaoException;
}
