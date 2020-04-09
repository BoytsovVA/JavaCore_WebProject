package dao;

import domain.StudentOrder;
import exception.DaoException;

import java.util.List;

public interface StudentOrderDao {
    Long saveStudentOrder(StudentOrder so) throws DaoException;
    List <StudentOrder> getStudentOrder() throws DaoException;
}
