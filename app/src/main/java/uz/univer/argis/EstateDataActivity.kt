package uz.univer.argis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.univer.argis.databinding.ActivityEstateDataBinding
import uz.univer.argis.domain.MapsActivity

class EstateDataActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEstateDataBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEstateDataBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnNext.setOnClickListener {
      startActivity(Intent(this, MapsActivity::class.java))
    }
    binding.btnBack.setOnClickListener {
      finish()
    }
  }
}