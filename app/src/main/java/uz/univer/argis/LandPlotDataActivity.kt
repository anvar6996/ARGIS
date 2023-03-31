package uz.univer.argis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.univer.argis.databinding.ActivityLandPlotDataBinding

class LandPlotDataActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLandPlotDataBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLandPlotDataBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnNext.setOnClickListener {
      startActivity(Intent(this, EstateDataActivity::class.java))
    }
    binding.btnBack.setOnClickListener {
      finish()
    }

  }
}