package mingaz.lubinskiy.app

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

    fun onClick(v: View?) {
        val intent = Intent(this, DepartmentListActivity::class.java)
        startActivity(intent)
    }
}