package mingaz.lubinskiy.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.databinding.ActivityLoginBinding
import mingaz.lubinskiy.app.department.DepartmentsListActivity
import mingaz.lubinskiy.app.employee.EmployeesListActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var userList = ArrayList<Login>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val database = Firebase.database(getString(R.string.db_url))
        val ref = database.getReference("login")
        onChangeListener(ref)*/
        startActivity(Intent(this, DepartmentsListActivity::class.java))
        binding.login.setOnClickListener {
            onClick(binding.username.text.toString(), binding.password.text.toString())
        }

    }

    private fun onClick(username: String, password: String) = with(binding) {
        val context = this@LoginActivity
        val intent = Intent(context, DepartmentsListActivity::class.java)
        /*if (username.isNotEmpty() && password.isNotEmpty()) {
            for (user in userList) {
                if (username == user.name && password.toInt() == user.password) {*/
                    startActivity(intent)
                /*} else if (username != user.name || password.toInt() != user.password){
                    Toast.makeText(context, "not authorised user", Toast.LENGTH_SHORT)
                        .show()
                //TODO: Toast calls too many times
                }
            }
        } else {
            Toast.makeText(context, "username or password isEmpty", Toast.LENGTH_SHORT)
                .show()
        }*/
    }

    private fun onChangeListener(ref: DatabaseReference) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(Login::class.java)
                    if (user != null) userList.add(user)
                    /*Log.d("Snapshots", "Key is ${it.key.toString()}")
                    Log.d("Snapshots", "Value is ${it.value.toString()}")*/
                }
            }

            // Failed to read value
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, error.toException().message, Toast.LENGTH_SHORT)
                    .show()
                Log.d("Mingaz", "Failed to read value.", error.toException())
            }

        })
    }

}
