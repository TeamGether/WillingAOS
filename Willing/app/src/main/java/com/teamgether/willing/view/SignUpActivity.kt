package com.teamgether.willing.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.teamgether.willing.R
import com.teamgether.willing.viewmodels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

class SignUpActivity : SignUpViewModel() {
    var isDuplicate = false
    @JvmName("setDuplicate1")
    fun setDuplicate(temp: Boolean) {
        this.isDuplicate = temp
        if (isDuplicate) {
            sign_up_warning_nickName.setText(R.string.sign_up_warning_nickName)
            sign_up_warning_nickName.setTextColor(getColor(R.color.red))
            btn_off(R.id.finishSignUpBtn)
        } else {
            sign_up_warning_nickName.setText(R.string.sign_up_warning_usable)
            sign_up_warning_nickName.setTextColor(getColor(R.color.green))
            btn_on(R.id.finishSignUpBtn)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sign_up_check_nickName_btn.setOnClickListener {
            val name = signup_nickName.text.toString()
            Log.d("button", isDuplicate.toString())


            if (name.isBlank()) {
                sign_up_warning_nickName.setText(R.string.sign_up_warning_null)
                sign_up_warning_nickName.setTextColor(getColor(R.color.red))
            } else {
                if (name.contains(" ")) {
                    sign_up_warning_nickName.setText(R.string.sign_up_warning_spacing)
                    sign_up_warning_nickName.setTextColor(getColor(R.color.red))
                } else {
                    nickNameCheck(name, ::setDuplicate)
                }
            }
        }

        finishSignUpBtn.setOnClickListener {
            val email = signup_email.text.toString()
            val password = signup_pw.text.toString()
            val checkPassword = signup_cpw.text.toString()
            val name = signup_nickName.text.toString()
            val profileImg = "profile/default_profile.jpeg"


            if (email.isNotBlank() && password.isNotBlank() && checkPassword.isNotBlank() && name.isNotBlank()
            ) {
                if (password.length >= 6) {
                    sign_up_warning_pwd.text = ""
                    if (password == checkPassword) {
                        sign_up_warning_chkPwd.text = ""
                        btn_on(R.id.finishSignUpBtn)
                        createUser(email, password, name, profileImg, ::startActivity)
                    } else {
                        sign_up_warning_chkPwd.setText(R.string.sign_up_warning_chkPwd)
                        sign_up_warning_chkPwd.setTextColor(getColor(R.color.red))

                    }
                } else {
                    sign_up_warning_chkPwd.text = ""
                    sign_up_warning_pwd.setText(R.string.sign_up_warning_pwd)
                    sign_up_warning_pwd.setTextColor(getColor(R.color.red))
                    //경고문: 6자리 이상
                }
                sign_up_warning_nickName.text = ""
                sign_up_warning_email.text = ""
                sign_up_warning_pwd.text = ""
                sign_up_warning_chkPwd.text = ""
              } else {
                if (email.isBlank()) {
                    sign_up_warning_email.setText(R.string.sign_up_warning_null)
                    sign_up_warning_email.setTextColor(getColor(R.color.red))

                } else {
                    sign_up_warning_email.text = ""
                }
                if (name.isBlank()) {
                    sign_up_warning_nickName.setText(R.string.sign_up_warning_null)
                    sign_up_warning_nickName.setTextColor(getColor(R.color.red))
                } else {
                    sign_up_warning_nickName.text = ""
                }
                if (password.isBlank()) {
                    sign_up_warning_pwd.setText(R.string.sign_up_warning_null)
                    sign_up_warning_pwd.setTextColor(getColor(R.color.red))

                } else {
                    sign_up_warning_pwd.text = ""
                }
                if (checkPassword.isBlank()) {
                    sign_up_warning_chkPwd.setText(R.string.sign_up_warning_null)
                    sign_up_warning_chkPwd.setTextColor(getColor(R.color.red))

                } else {
                    sign_up_warning_chkPwd.text = ""
                }
            }
            //이메일 발송이 들어가야함.
        }

    }


    fun startActivity() {
        val nextIntent = Intent(this, LoginActivity::class.java)
        startActivity(nextIntent)
    }

    private fun btn_on(btn: Int) {
        findViewById<Button>(btn).isEnabled = true
    }

    internal fun btn_off(btn: Int) {
        findViewById<Button>(btn).isEnabled = false
    }



}

