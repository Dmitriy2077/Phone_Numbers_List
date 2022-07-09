package mingaz.lubinskiy.app.department

import mingaz.lubinskiy.app.employee.Employee
import java.io.Serializable

data class Department(
    var name: String? = null,
    var employees: MutableList<Employee>? = null
): Serializable