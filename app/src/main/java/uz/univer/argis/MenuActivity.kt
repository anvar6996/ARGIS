package uz.univer.argis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import uz.univer.argis.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
  private lateinit var bind:ActivityMenuBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind=ActivityMenuBinding.inflate(layoutInflater)
    setContentView(bind.root)


  }
}