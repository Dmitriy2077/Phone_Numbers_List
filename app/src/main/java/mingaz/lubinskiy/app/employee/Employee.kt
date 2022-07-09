package mingaz.lubinskiy.app.employee

import mingaz.lubinskiy.app.employee_info.EmployeeInfo
import java.io.Serializable

data class Employee(
    var name: String? = null,
    var info: EmployeeInfo? = null
) :Serializable
