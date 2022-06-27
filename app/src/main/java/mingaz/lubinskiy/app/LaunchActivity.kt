package mingaz.lubinskiy.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import mingaz.lubinskiy.app.department.DepartmentsListActivity


class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    fun onClick(v: View?) {
        val intent = Intent(this, DepartmentsListActivity::class.java)
        startActivity(intent)
    }
}