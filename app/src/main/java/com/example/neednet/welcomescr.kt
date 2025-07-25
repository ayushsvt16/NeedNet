package com.example.neednet

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import android.graphics.Color
import android.view.View


class welcomescr : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcomescr)

        auth = FirebaseAuth.getInstance()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT


        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val menuCard = findViewById<CardView>(R.id.menu_card)

        // Open drawer on menu card click
        menuCard.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener(this)

        // Handle padding for insets (status bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cardFindHelp = findViewById<CardView>(R.id.cardFindHelp)
        val cardOfferHelp = findViewById<CardView>(R.id.cardOfferHelp)

        // Apply animation on click for Find Help card
        cardFindHelp.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.click_animation)
            cardFindHelp.startAnimation(animation)
            startActivity(Intent(this, FindHelpActivity::class.java))
        }

        // Apply animation on click for Offer Help card
        cardOfferHelp.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.click_animation)
            cardOfferHelp.startAnimation(animation)
            startActivity(Intent(this, OfferHelpActivity::class.java))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                startActivity(Intent(this, login::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
