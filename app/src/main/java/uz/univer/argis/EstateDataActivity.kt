package uz.univer.argis

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import uz.univer.argis.common.Constants
import uz.univer.argis.common.ExcelUtils
import uz.univer.argis.common.FileShareUtils
import uz.univer.argis.databinding.ActivityEstateDataBinding
import uz.univer.argis.domain.MapsActivity
import uz.univer.argis.models.EstateData
import uz.univer.argis.models.export_data.StaticValue
import java.text.SimpleDateFormat
import java.util.*

class EstateDataActivity : AppCompatActivity() {
  private lateinit var binding: ActivityEstateDataBinding
  private val xuquqTurlari = arrayListOf<String>(
    "Доимий фойдаланиш", "Ижара", "Мулк"
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityEstateDataBinding.inflate(layoutInflater)
    setContentView(binding.root)


    val xuquqAdapter = ArrayAdapter(this, R.layout.item_product_type, xuquqTurlari)
    binding.xuquqTuri.setAdapter(xuquqAdapter)

    binding.btnNext.setOnClickListener {
      loadEstateData()
    }
    binding.btnBack.setOnClickListener {
      finish()
    }
    binding.btnMap.setOnClickListener {
      startActivity(Intent(this@EstateDataActivity, MapsActivity::class.java))
      finish()
    }
  }

  private fun loadEstateData() {
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
        StaticValue.estateData = EstateData(
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
        generateExelFile()
      }

    }
  }

  private fun generateExelFile() {
    val isExcelGenerated = ExcelUtils.exportDataIntoWorkbook(
      application,
      getCurrentDate() + getCurrentTime() + Constants.EXCEL_FILE_NAME,
      StaticValue.estateData,
      StaticValue.landPlotData,
      StaticValue.placeDate
    )
    if (isExcelGenerated) {
      onShareButtonClicked()
    }
  }

  fun onShareButtonClicked() {
    val fileUri: Uri = initiateSharing()
    fileUri.let { launchShareFileIntent(it) }
    launchShareFileIntent(fileUri)
  }

  private fun launchShareFileIntent(uri: Uri) {
    val intent = ShareCompat.IntentBuilder.from(this).setType("application/pdf").setStream(uri)
      .setChooserTitle("Select application to share file").createChooserIntent()
      .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(intent)
  }

  fun initiateSharing(): Uri {
    return FileShareUtils.accessFile(
      application, getCurrentDate() + getCurrentTime() + Constants.EXCEL_FILE_NAME
    )
  }

  @SuppressLint("SimpleDateFormat")
  fun getCurrentDate() = SimpleDateFormat("yyyy-MM-dd").format(Date()).toString()

  @SuppressLint("SimpleDateFormat")
  fun getCurrentTime() = SimpleDateFormat("HH:mm").format(Date()).toString()

}