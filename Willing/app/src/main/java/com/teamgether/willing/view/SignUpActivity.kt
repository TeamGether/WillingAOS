package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.teamgether.willing.R
import com.teamgether.willing.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : SignUpViewModel() {
    var isDuplicate = false

    @JvmName("setDuplicate1")
    fun setDuplicate(temp: Boolean) {
        this.isDuplicate = temp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        sign_up_check_nickName_btn.setOnClickListener {
            val name = signup_nickName.text.toString()
            Log.d("button", isDuplicate.toString())
            nickNameCheck(name, ::setDuplicate)


            if (name.isBlank()) {
                sign_up_warning_nickName.setText(R.string.sign_up_warning_null)
            } else {
                if (name.contains(" ")) {
                    sign_up_warning_nickName.setText(R.string.sign_up_warning_spacing)
                } else {
                    if (isDuplicate) {
                        sign_up_warning_nickName.setText(R.string.sign_up_warning_nickName)
                    } else {
                        sign_up_warning_nickName.setText(R.string.sign_up_warning_usable)
                    }
                }
            }
        }

        finishSignUpBtn.setOnClickListener {
            val email = signup_email.text.toString()
            val password = signup_pw.text.toString()
            val checkPassword = signup_cpw.text.toString()
            val name = signup_nickName.text.toString()
            val donationName = signup_dona.text.toString()


            if (email.isNotBlank() && password.isNotBlank() && checkPassword.isNotBlank() && name.isNotBlank() && donationName.isNotBlank()
            ) {
                if (password.length >= 6) {
                    sign_up_warning_pwd.text = ""
                    if (password == checkPassword) {
                        sign_up_warning_chkPwd.text = ""
                        createUser(email, password, name, donationName, ::startActivity)
                    } else {
                        sign_up_warning_chkPwd.setText(R.string.sign_up_warning_chkPwd)
                    }
                } else {
                    sign_up_warning_chkPwd.text = ""
                    sign_up_warning_pwd.setText(R.string.sign_up_warning_pwd)
                    //경고문: 6자리 이상
                }
                sign_up_warning_nickName.text = ""
                sign_up_warning_email.text = ""
                sign_up_warning_pwd.text = ""
                sign_up_warning_chkPwd.text = ""
                sign_up_warning_dona.text = ""
            } else {
                if (email.isBlank()) {
                    sign_up_warning_email.setText(R.string.sign_up_warning_null)
                } else {
                    sign_up_warning_email.text = ""
                }
                if (name.isBlank()) {
                    sign_up_warning_nickName.setText(R.string.sign_up_warning_null)
                } else {
                    sign_up_warning_nickName.text = ""
                }
                if (password.isBlank()) {
                    sign_up_warning_pwd.setText(R.string.sign_up_warning_null)
                } else {
                    sign_up_warning_pwd.text = ""
                }
                if (checkPassword.isBlank()) {
                    sign_up_warning_chkPwd.setText(R.string.sign_up_warning_null)
                } else {
                    sign_up_warning_chkPwd.text = ""
                }
                if (donationName.isBlank()) {
                    sign_up_warning_dona.setText(R.string.sign_up_warning_null)
                } else {
                    sign_up_warning_dona.text = ""
                }
            }


            //이메일 발송이 들어가야함.

        }

    }


    fun startActivity() {
        val nextIntent = Intent(this, LoginActivity::class.java)
        startActivity(nextIntent)
    }

    internal fun btn_on(btn: Button) {
        btn.isEnabled = true
    }

    internal fun btn_off(btn: Button) {
        btn.isEnabled = false
    }


}

