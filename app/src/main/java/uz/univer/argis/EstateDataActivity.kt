package uz.univer.argis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.univer.argis.databinding.ActivityEstateDataBinding

class EstateDataActivity : AppCompatActivity() {
  private lateinit var binding:ActivityEstateDataBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding=ActivityEstateDataBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }
}