package com.priyanshumaurya8868.realworld.io.conduit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.priyanshumaurya8868.realworld.io.api.model.entites.User
import com.priyanshumaurya8868.realworld.io.conduit.databinding.NavHeaderMainBinding
import com.priyanshumaurya8868.realworld.io.conduit.extentions.loadImageInCircleView

class MainActivity : AppCompatActivity() {
    var navHeaderMainBinding : NavHeaderMainBinding? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var authViewModel: AuthViewModel
    private var temp: NavigationView? = null
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val FILE_AUTH = "file_auth"
        const val KEY_TOKEN = "key_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Conduit)
        setContentView(R.layout.activity_main)
        navHeaderMainBinding = NavHeaderMainBinding.inflate(layoutInflater)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)
        temp = navView
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        AppCompatDelegate.getDefaultNightMode()
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPreferences = getSharedPreferences(FILE_AUTH, Context.MODE_PRIVATE)
        //if already signed in
        sharedPreferences.getString(KEY_TOKEN, null)?.let {
            Log.d("auth", " token from shared pref :-> $it ")
            authViewModel.autoSignin(it)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menu_nav_feed,
                R.id.menu_nav_my_feed,
                R.id.menu_nav_auth,
                R.id.menu_nav_settings
            ), drawerLayout
        )
        //inn mei add krne se navigation drawer ka  hamburger milega varna back arrow k  sign
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val view = navView.getHeaderView(0) //there is only header view in our project
        navHeaderMainBinding = view?.let { NavHeaderMainBinding.bind(it) }

        //for auto selecting an item in our navDrawer on startup
        navView.menu.getItem(0).isChecked = true;

        authViewModel.user.observe({ lifecycle }) { user ->
            updateUser(user)
            user?.token?.let {
                sharedPreferences.edit {
                    Log.d("auth", " token stored into shared pref :-> $it ")
                    putString(KEY_TOKEN, it)
                }
                Toast.makeText(this, "Logged in as " + user.username + "!", Toast.LENGTH_LONG)
                    .show()
            } ?: run {
                sharedPreferences.edit {
                    remove(KEY_TOKEN)
                }
                Toast.makeText(this, "Log Out successfully...!", Toast.LENGTH_SHORT).show()

            }
            navController.navigateUp()
            setUpNavHeader(user)
        }


    }

    private fun setUpNavHeader(user : User?){
        //TODO : Profile preview
        navHeaderMainBinding?.apply {
                profileUsername.text = user?.username?: "Username"
             profileImageView.loadImageInCircleView(user?.image,R.mipmap.ic_launcher_round)
        }?:Log.d("navHeader", "Binding is null")
    }

    private fun updateUser(it: User?) {
        when (it) {
            is User -> {
                temp?.menu?.clear()
                temp?.inflateMenu(R.menu.activity_main_drawer_user)
            }
            else -> {
                temp?.menu?.clear()
                temp?.inflateMenu(R.menu.activity_main_drawer_guest)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_signout -> {
                authViewModel.logOut()
            }
        }
        return super.onOptionsItemSelected(item)

    }
}