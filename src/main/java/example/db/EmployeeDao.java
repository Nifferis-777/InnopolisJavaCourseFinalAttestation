package example.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class EmployeeDao {
        private final DbConnect dbConnect;

        public EmployeeDao() {
            this.dbConnect = new DbConnect();
        }

        public static class Employee {
            public int id;
            public boolean isActive;
            public Timestamp createTimestamp;
            public Timestamp changeTimestamp;
            public String firstName;
            public String lastName;
            public String middleName;
            public String phone;
            public String email;
            public Date birthdate;
            public String avatarUrl;
            public int companyId;

            @Override
            public String toString() {
                return "Employee{" +
                        "id=" + id +
                        ", isActive=" + isActive +
                        ", createTimestamp=" + createTimestamp +
                        ", changeTimestamp=" + changeTimestamp +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", middleName='" + middleName + '\'' +
                        ", phone='" + phone + '\'' +
                        ", email='" + email + '\'' +
                        ", birthdate=" + birthdate +
                        ", avatarUrl='" + avatarUrl + '\'' +
                        ", companyId=" + companyId +
                        '}';
            }
        }

        // Метод для получения информации о всех сотрудниках из таблицы
        public List<Employee> getAllEmployees() {
            List<Employee> employees = new ArrayList<>();
            String sql = "SELECT id, is_active, create_timestamp, change_timestamp, first_name, last_name, middle_name, phone, email, birthdate, avatar_url, company_id FROM employee";

            try (Connection conn = dbConnect.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.id = rs.getInt("id");
                    emp.isActive = rs.getBoolean("is_active");
                    emp.createTimestamp = rs.getTimestamp("create_timestamp");
                    emp.changeTimestamp = rs.getTimestamp("change_timestamp");
                    emp.firstName = rs.getString("first_name");
                    emp.lastName = rs.getString("last_name");
                    emp.middleName = rs.getString("middle_name");
                    emp.phone = rs.getString("phone");
                    emp.email = rs.getString("email");
                    emp.birthdate = rs.getDate("birthdate");
                    emp.avatarUrl = rs.getString("avatar_url");
                    emp.companyId = rs.getInt("company_id");

                    employees.add(emp);
                }

            } catch (SQLException e) {
                System.err.println("Ошибка выполнения запроса: " + e.getMessage());
            }

            return employees;
        }

        // Метод для добавления сотрудника
        public boolean addEmployee(Employee employee) {
            String sql = "INSERT INTO employee (is_active, create_timestamp, change_timestamp, first_name, last_name, middle_name, phone, email, birthdate, avatar_url, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = dbConnect.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setBoolean(1, employee.isActive);
                pstmt.setTimestamp(2, employee.createTimestamp);
                pstmt.setTimestamp(3, employee.changeTimestamp);
                pstmt.setString(4, employee.firstName);
                pstmt.setString(5, employee.lastName);
                pstmt.setString(6, employee.middleName);
                pstmt.setString(7, employee.phone);
                pstmt.setString(8, employee.email);
                pstmt.setDate(9, employee.birthdate);
                pstmt.setString(10, employee.avatarUrl);
                pstmt.setInt(11, employee.companyId);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;

            } catch (SQLException e) {
                System.err.println("Ошибка добавления сотрудника: " + e.getMessage());
                return false;
            }
        }
    }

