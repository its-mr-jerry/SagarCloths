package com.example.sagarcloths

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sagarcloths.databinding.ActivityUserInfoBinding

class UserInfo : AppCompatActivity() {
      lateinit var binding: ActivityUserInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}