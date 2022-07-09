package mingaz.lubinskiy.app.employee_info

import java.io.Serializable

data class EmployeeInfo(
    var position: String? = null,
    var numbers: HashMap<String, Long>? = null
): Serializable


