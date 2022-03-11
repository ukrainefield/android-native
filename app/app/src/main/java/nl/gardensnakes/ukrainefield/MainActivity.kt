package nl.gardensnakes.ukrainefield

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationbar: BottomNavigationView
    private lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationbar = findViewById(R.id.bottomNav)
        fragment = NewsFeedFragment()

        setupBottomNavigationBar()

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun setupBottomNavigationBar() {
        bottomNavigationbar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.newsfeed -> {
                    fragment = NewsFeedFragment()
                }
                R.id.ukraine_map -> {
                    fragment = MapFragment()
                }
                R.id.bookmarks -> {
                    fragment = BookmarkFragment()
                }
                R.id.settings -> {
                    fragment = SettingsFragment()
                }
            }

            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            true
        }
    }
}
