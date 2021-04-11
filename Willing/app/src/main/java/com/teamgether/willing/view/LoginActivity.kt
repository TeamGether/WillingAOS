package com.teamgether.willing.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : LoginViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var isChecked: Boolean = false

        finishLoginBtn.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_pwd.text.toString()

//            login_warning_email.text = ""
//            login_warning_pwd.text = ""


            if(email.isNotBlank()&&email.isNotEmpty()&&password.isNotEmpty()&&password.isNotBlank()){
//                login_warning_email.text = ""
//                login_warning_pwd.text = ""
                login(email, password)
            }else{
                if (email.isEmpty() or email.isBlank()) {
//                    login_warning_email.setText(R.string.login_warning_null)
                }else{
//                    login_warning_email.text = ""
                }
                if (password.isEmpty() or password.isBlank()) {
//                    login_warning_pwd.setText(R.string.login_warning_null)
                }else{
//                    login_warning_pwd.text = ""
                }
            }


        }
        gotoSignUpBtn.setOnClickListener {
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }


    }
}