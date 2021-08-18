package com.argando.habittrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.argando.habittrainer.databinding.ActivityMainBinding
import com.argando.habittrainer.db.HabitDBTable
import com.argando.habittrainer.db.HabitTrainerDB

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.tvDescription.text = getString(R.string.drink_water_description)
        binding.tvTitle.text = getString(R.string.drink_water_title)
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.water))*/

        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = HabitsAdapter(HabitDBTable(this).readAllHabits())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_habit -> {
                createHabit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createHabit() {
        startActivity(createLaunchIntent(CreateHabitActivity::class.java))
    }

    private fun createLaunchIntent(c: Class<*>): Intent {
        return Intent(this, c)
    }
}