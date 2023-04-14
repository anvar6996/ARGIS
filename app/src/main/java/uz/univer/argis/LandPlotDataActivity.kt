package uz.univer.argis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uz.univer.argis.databinding.ActivityLandPlotDataBinding
import uz.univer.argis.domain.MapsActivity
import uz.univer.argis.models.LandPlotData
import uz.univer.argis.models.export_data.StaticValue

class LandPlotDataActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLandPlotDataBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLandPlotDataBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btnNext.setOnClickListener {
      loadLandPlotData()
    }
    binding.btnBack.setOnClickListener {
      finish()
    }
    binding.btnMap.setOnClickListener {
      startActivity(Intent(this@LandPlotDataActivity, MapsActivity::class.java))
      finish()
    }
  }

  private fun loadLandPlotData() {
    binding.apply {
      if (kadastr.text.toString().isEmpty()) {
        kadastr.error = "Kadastrni kiriting"
        return
      }
      kadastr.error = null
      if (obyektNomi.text.toString().isEmpty()) {
        obyektNomi.error = "Obyek nomini kiriting"
        return
      }
      obyektNomi.error = null
      if (obyektManzili.text.toString().isEmpty()) {
        obyektManzili.error = "Obyek manzilini kiriting"
        return
      }
      obyektManzili.error = null
      if (xuquqTuri.text.toString().isEmpty()) {
        xuquqTuri.error = "Xuquq turini kiriting"
        return
      }
      xuquqTuri.error = null
      if (registerDate.text.toString().isEmpty()) {
        registerDate.error = "Ruyxatga olingan sanani kiriting kiriting"
        return
      }
      registerDate.error = null
      if (maydon.text.toString().isEmpty()) {
        maydon.error = "Maydoni kiring kiriting"
        return
      }
      maydon.error = null
      if (maqsad.text.toString().isEmpty()) {
        maqsad.error = "Maqsadingizni kiriting"
        return
      }
      maqsad.error = null
      if (soliq.text.toString().isEmpty()) {
        soliq.error = "Soliq miqdorini kiriting"
        return
      }
      soliq.error = null
      if (cost.text.toString().isEmpty()) {
        cost.error = "Qiymatini kiriting"
        return
      } else {
        cost.error = null
        StaticValue.landPlotData = LandPlotData(
          kadastr = kadastr.text.toString(),
          obyektNomi = obyektNomi.text.toString(),
          obyektManzili = obyektManzili.text.toString(),
          xuquqTuri = xuquqTuri.text.toString(),
          ruyxatSanasi = registerDate.text.toString(),
          maydon = maydon.text.toString(),
          maqsad = maqsad.text.toString(),
          soliq = soliq.text.toString(),
          qiymati = cost.text.toString(),
        )
        startActivity(Intent(this@LandPlotDataActivity, EstateDataActivity::class.java))
        finish()
      }
    }
  }
}