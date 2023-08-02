package com.app.amtadminapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.app.amtadminapp.R
import kotlinx.android.synthetic.main.activity_get_start.*

class StartActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)
        initializeView()
    }

    override fun initializeView() {

        txtSignUp.setOnClickListener(this)
        LLcardButtonLogin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.LLcardButtonLogin -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
//                finish()
            }
            R.id.txtSignUp -> {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
//                finish()

            }
        }
    }
}