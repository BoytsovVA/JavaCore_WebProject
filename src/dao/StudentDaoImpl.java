package dao;


import config.Config;
import domain.*;
import exception.DaoException;
import java.sql.*;
import java.time.LocalDateTime;

public class StudentDaoImpl implements StudentOrderDao {
    private static final String INSERT_ORDER =
            "INSERT INTO jc_student_order(" +
                    "student_order_status, student_order_date, h_sur_name, h_given_name, h_patronymic, " +
                    "h_date_of_birth, h_passport_seria, h_passport_number, h_passport_date, h_passport_office_id, h_post_index, " +
                    "h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
                    "w_sur_name, w_given_name, w_patronymic, w_date_of_birth, " +
                    "w_passport_seria, w_passport_number, w_passport_date, w_passport_office_id, w_post_index, w_street_code, " +
                    "w_building, w_extension, w_apartment, w_university_id, w_student_number, " +
                    "certificate_id, register_office_id, marriage_date)" +
                    "\tVALUES (?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, " +
                    "?, ?, ?)";

    private static final String INSERT_CHILD =
            "INSERT INTO jc_student_child(" +
                    "student_order_id, c_sur_name, c_given_name, " +
                    "c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date," +
                    "c_register_office_id, c_post_index, c_street_code, c_building," +
                    "c_extension, c_apartment)" +
                    "VALUES (?, ?, ?, ?, " +
                    "?, ?, ?," +
                    "?, ?, ?, ?," +
                    "?, ?);";

    //TODO refactoring - make one method
    private Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(
                Config.getProperty(Config.DB_URL),
                Config.getProperty(Config.DB_LOGIN),
                Config.getProperty(Config.DB_PASSWORD));
        return con;
    }


    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        Long result = -1L;

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[] {"student_order_id"})) {

            con.setAutoCommit(false);
            try {
                //Header
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                //Husband and Wife
                setParamsForAdult(stmt, 3, so.getHusband());
                setParamsForAdult(stmt, 18, so.getWife());

                //Marriage
                stmt.setString(33, so.getMarriageCertificateId());
                stmt.setLong(34, so.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(so.getMarriageDate()));

                stmt.executeUpdate();


                ResultSet gkRs = stmt.getGeneratedKeys();
                if (gkRs.next()) {
                    result = gkRs.getLong(1);
                }
                gkRs.close();

                saveChildren(con, so, result);

                con.commit();
            }catch (SQLException ex) {
                con.rollback();
                throw new DaoException(ex);
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }

        return result;
    }

    private void saveChildren(Connection con, StudentOrder so, Long soId) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
            for (Child child : so.getChildren()) {
                stmt.setLong(1, soId);
                setParamsForChild(stmt, child);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void setParamsForAdult(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4, adult.getPassportSeria());
        stmt.setString(start + 5, adult.getPassportNumber());
        stmt.setDate(start + 6, Date.valueOf(adult.getIssueDate()));
        stmt.setLong(start + 7, adult.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, start + 8, adult);
        stmt.setLong(start + 13, adult.getUnivesity().getUniversityId());
        stmt.setString(start +14, adult.getStudentId());
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, java.sql.Date.valueOf(child.getIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, 9, child);
    }


    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start, person.getSurName());
        stmt.setString(start + 1, person.getGivenName());
        stmt.setString(start + 2, person.getPatronymic());
        stmt.setDate(start + 3, Date.valueOf(person.getDateOfBirth()));
    }
    private void setParamsForAddress(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address h_address = person.getAddress();
        stmt.setString(start, h_address.getPostCode());
        stmt.setLong(start + 1, h_address.getStreet().getStreetCode());
        stmt.setString(start + 2, h_address.getBuilding());
        stmt.setString(start + 3, h_address.getExtension());
        stmt.setString(start + 4, h_address.getApartment());
    }

}
