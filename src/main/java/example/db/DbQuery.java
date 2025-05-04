package example.db;

import java.sql.Timestamp;
import java.util.List;

public class DbQuery {
    public static void main(String[] args) {

        EmployeeDao dao = new EmployeeDao();

        // Пример получения всех сотрудников и вывода их в консоль
        List<EmployeeDao.Employee> employees = dao.getAllEmployees();
        for (EmployeeDao.Employee e : employees) {
            System.out.println(e);
        }

        //Пример добавления нового сотрудника в таблицу
       EmployeeDao.Employee newEmp = new EmployeeDao.Employee();
       newEmp.isActive = true;
       newEmp.createTimestamp = new Timestamp(System.currentTimeMillis());
       newEmp.changeTimestamp = new Timestamp(System.currentTimeMillis());
       newEmp.firstName = "Василий";
       newEmp.lastName= "Авдеев";
       newEmp.middleName= "Иванович";
       newEmp.phone= "911-358-55-77";
       newEmp.email= "avdvi@mail.com";
       newEmp.birthdate= java.sql.Date.valueOf("1995-05-03");
       newEmp.avatarUrl= null;
       newEmp.companyId= 800;
       boolean added = dao.addEmployee(newEmp);
       System.out.println("Добавлен новый сотрудник: " + added);

    }
}