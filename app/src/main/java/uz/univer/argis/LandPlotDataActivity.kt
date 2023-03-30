package uz.univer.argis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.univer.argis.databinding.ActivityLandPlotDataBinding

class LandPlotDataActivity : AppCompatActivity() {
  private lateinit var binding : ActivityLandPlotDataBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding=ActivityLandPlotDataBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}