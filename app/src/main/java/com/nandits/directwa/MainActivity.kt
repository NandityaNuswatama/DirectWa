package com.nandits.directwa

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.nandits.directwa.databinding.ActivityMainBinding
import com.rejowan.cutetoast.CuteToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val viewModel: MainViewModel by viewModels()
        
        viewModel.enable.observe(this, {
            isEnable(it)
        })
        
        with(binding){
            inputNumber.doAfterTextChanged {
                if (it.toString().length > 9) viewModel.setNumber(it.toString())
                if (it.toString().length < 10) viewModel.setNumber("")
            }
            btnCopy.setOnClickListener { copyLink(inputNumber.text.toString()) }
            btnChat.setOnClickListener { goToWhatsapp(inputNumber.text.toString()) }
        }
    }
    
    private fun toast(message: String){
        CuteToast.ct(this, message, CuteToast.LENGTH_SHORT, CuteToast.HAPPY, true).show()
    }
    
    private fun goToWhatsapp(number: String) {
        var url = ""
        if (number.take(1) == "0"){
            val format = number.replaceFirst("0", "+62")
            url = "https://api.whatsapp.com/send?phone=$format"
        }
        if (number.take(1) == "6"){
            val format = number.replaceFirst("6", "+6")
            url = "https://api.whatsapp.com/send?phone=$format"
        }
        try {
            packageManager?.getPackageInfo(
                "com.whatsapp",
                PackageManager.GET_ACTIVITIES
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            toast("Terima kasih telah menggunakan aplikasi ini")
            startActivity(intent)
        } catch (e: PackageManager.NameNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")
                )
            )
        }
    }
    
    private fun copyLink(number: String) {
        var link = ""
        val clipManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (number.take(1) == "0"){
            val format = number.replaceFirst("0", "+62")
            link = "wa.me/$format"
        }
        clipManager.apply {
            setPrimaryClip(
                ClipData.newPlainText(
                    "WA Link",
                    link
                )
            )
        }
        toast("Tautan berhasil disalin!")
    }
    
    private fun isEnable(boolean: Boolean){
        with(binding){
            btnChat.isEnabled = boolean
            btnCopy.isEnabled = boolean
        }
    }
}