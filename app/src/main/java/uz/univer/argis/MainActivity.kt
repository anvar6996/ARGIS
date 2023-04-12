package uz.univer.argis

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import uz.univer.argis.common.Constants
import uz.univer.argis.common.ExcelUtils
import uz.univer.argis.common.FileShareUtils
import uz.univer.argis.databinding.ActivityMainBinding
import uz.univer.argis.domain.MainRepository
import uz.univer.argis.models.PlaceDate
import uz.univer.argis.models.export_data.StaticValue
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
  private val repository = MainRepository()
  private lateinit var bind: ActivityMainBinding

  //  private val viloyatlar = ArrayList<Pair<String, ArrayList<String>>>()
  private val shaxarlar = ArrayList<Pair<String, ArrayList<String>>>()
  private val tumanlar = ArrayList<Pair<String, ArrayList<String>>>()
  private val filePath: File = File("${Environment.getExternalStorageDirectory()}/Demo.xls")

  private val locationPermissionRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    if (checkWriteFilePermissionGranted()) {
      Toast.makeText(this, "permission success", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(this, "permission died", Toast.LENGTH_SHORT).show()
    }
  }

  private fun checkWriteFilePermissionGranted() = (ActivityCompat.checkSelfPermission(
    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
  ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
    this, Manifest.permission.READ_EXTERNAL_STORAGE
  ) == PackageManager.PERMISSION_GRANTED)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    locationPermissionRequest.launch(
      arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
      )
    )

    bind = ActivityMainBinding.inflate(layoutInflater)
    setContentView(bind.root)
    loadShaxalar()
    loadTumanlar()
    val adapterRegions = ArrayAdapter(this, R.layout.item_product_type, repository.listRegions)
    bind.viloyat.setAdapter(adapterRegions)

    bind.viloyat.onItemClickListener =
      AdapterView.OnItemClickListener { parent, view, position, id ->
        val key = repository.listRegions[position]
        val adapterShaxar = ArrayAdapter(
          this, R.layout.item_product_type, viloyatlarBuyichaShaxar(key)
        )
        bind.shaxar.setAdapter(adapterShaxar)
        val adapterTuman = ArrayAdapter(
          this, R.layout.item_product_type, viloyatlarBuyichaTuman(key)
        )
        bind.tuman.setAdapter(adapterTuman)
      }


    bind.btnNext.setOnClickListener {
      loadValuePlaceData()
    }
    bind.btnBack.setOnClickListener {
      finish()
    }
  }

  private fun loadValuePlaceData() {
    bind.apply {
      if (viloyat.text.isEmpty()) {
        viloyat.error = "Viloyatni tanlang !!!"
        return
      }
      viloyat.error = null
      if (shaxar.text.isEmpty()) {
        shaxar.error = "Shaxarni tanlang !!!"
        return
      }
      shaxar.error = null
      if (tuman.text.isEmpty()) {
        Toast.makeText(this@MainActivity, "Malumotlarni tuliq kiriting", Toast.LENGTH_SHORT).show()
        tuman.error = "Tumani tanlang !!!"
        return
      }
      tuman.error = null

      if (qishloq.text.toString().isEmpty()) {
        qishloq.error = "Qishloqni tanlang !!!"
        return
      }
      qishloq.error = null

      if (MFY.text.toString().isEmpty()) {
        MFY.error = "MFY kiriting !!!"
        return
      }
      MFY.error = null

      if (yerToifasi.text.toString().isEmpty()) {
        yerToifasi.error = "Yer toifasi tanlang !!!"
        return
      }
      yerToifasi.error = null
      if (yerVaMulk.text.toString().isEmpty()) {
        yerVaMulk.error = "Yer va mulk foydalanuvchisini tanlang tanlang !!!"
        return
      } else {
        yerVaMulk.error = null
        StaticValue.placeDate = PlaceDate(
          tuman = tuman.text.toString(),
          shaxar = shaxar.text.toString(),
          viloyat = viloyat.text.toString(),
          qishloq = qishloq.text.toString(),
          MFY = MFY.text.toString(),
          yerToifasi = yerToifasi.text.toString(),
          yerMulkFoydalanuvchisi = yerVaMulk.text.toString(),
        )
        startActivity(Intent(this@MainActivity, LandPlotDataActivity::class.java))
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
      Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
      onShareButtonClicked()
    } else {
      Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
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

  fun buttonCreateExcel() {
    val hssfWorkbook = HSSFWorkbook()
    val hssfSheet = hssfWorkbook.createSheet("Custom Sheet")
    val hssfRow = hssfSheet.createRow(0)
    val hssfCell = hssfRow.createCell(0)
    hssfCell.setCellValue("ExelText")
    try {
      if (!filePath.exists()) {
        filePath.createNewFile()
      }
      val fileOutputStream = FileOutputStream(filePath)
      hssfWorkbook.write(fileOutputStream)
      if (fileOutputStream != null) {
        fileOutputStream.flush()
        fileOutputStream.close()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun loadExel() {
    var listData = ExcelUtils.readFromExcelWorkbook(this, Constants.EXCEL_FILE_NAME)
    val isExcelGenerated = ExcelUtils.exportDataIntoWorkbook(
      application,
      Constants.EXCEL_FILE_NAME,
      StaticValue.estateData,
      StaticValue.landPlotData,
      StaticValue.placeDate
    )

    if (isExcelGenerated) {
      Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
      val data = ExcelUtils.readFromExcelWorkbook(this, Constants.EXCEL_FILE_NAME)
      Toast.makeText(this, data[0].length.toString(), Toast.LENGTH_SHORT).show()
      onShareButtonClicked()
    } else {
      Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
    }
  }


  private fun loadShaxalar() {
    shaxarlar.add(
      Pair(
        "Навоий", arrayListOf(
          "Навоий ш.", "Заpафшон ш.", "Ғозғон ш."
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Наманган", arrayListOf(
          "Наманган ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Самарқанд", arrayListOf(
          "Самарқанд ш.",
          "Каттақўрғон ш.",
        )
      )
    )

    shaxarlar.add(
      Pair(
        "Андижон", arrayListOf(
          "Андижон ш.",
          "Хонобод ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Қорақалпоғистон Республикаси", arrayListOf(
          "Нукус ш."
        )
      )
    )

    shaxarlar.add(
      Pair(
        "Тошкент", arrayListOf(
          "Тошкент ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Қашқадарё", arrayListOf(
          "Қарши ш.",
          "Шаҳрисабз ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Жиззах", arrayListOf(
          "Жиззах ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Хоразм", arrayListOf(
          "Урганч ш.",
          "Хива ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Фарғона", arrayListOf(
          "Фарғона ш.",
          "Қўқон ш.",
          "Қувасой ш.",
          "Маpғилон ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Сурхондарё", arrayListOf(
          "Термиз ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Сирдарё", arrayListOf(
          "Гулистон ш.",
          "Шиpин ш.",
          "Янгиеp ш.",
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Бухоро", arrayListOf(
          "Бухоро ш.", "Когон ш."
        )
      )
    )
    shaxarlar.add(
      Pair(
        "Тошкент", arrayListOf(
          "Нурафшон ш.",
          "Олмалиқ ш.",
          "Ангрен ш.",
          "Бекобод ш.",
          "Оҳангаpон ш.",
          "Чиpчиқ ш.",
          "Янгийўл ш.",
        )
      )
    )
  }

  private fun viloyatlarBuyichaTuman(tuman: String): ArrayList<String> {
    var list = ArrayList<String>()
    tumanlar.forEach {
      if (it.first == tuman) {
        list = it.second
      }
    }
    return list
  }

  private fun viloyatlarBuyichaShaxar(tuman: String): ArrayList<String> {
    var list = ArrayList<String>()
    shaxarlar.forEach {
      if (it.first == tuman) {
        list = it.second
      }
    }
    return list
  }

  fun loadTumanlar() {
    tumanlar.add(
      Pair(
        "Навоий", arrayListOf(
          "Навоий",
          "Заpафшон",
          "Конимех",
          "Қизилтепа",
          "Навбаҳор",
          "Кармана",
          "Нурота",
          "Томди",
          "Учқудуқ",
          "Хатирчи",
          "Ғозғон "
        )
      )
    )
    tumanlar.add(
      Pair(
        "Бухоро", arrayListOf(
          "Олот",
          "Бухоро",
          "Вобкент",
          "Ғиждувон",
          "Когон",
          "Қоракўл",
          "Қоровулбозор",
          "Пешку",
          "Ромитан",
          "Жондоp",
          "Шофиркон"
        )
      )
    )
    tumanlar.add(
      Pair(
        "Наманган", arrayListOf(
          "Косонсой",
          "Наманган ",
          "Норин",
          "Поп",
          "Тўрақўрғон",
          "Уйчи",
          "Учқўрғон",
          "Чортоқ",
          "Учқўрғ",
          "Янгиқўрғон",
          "Давлатобод"
        )
      )
    )
    tumanlar.add(
      Pair(
        "Қорақалпоғистон Республикаси", arrayListOf(
          "Амударё",
          "Беруний",
          "Қораўзак",
          "Кегейли",
          "Қўнғирот",
          "Қонликўл",
          "Мўйноқ",
          "Нукус",
          "Тахиатош",
          "Тахтакўпир",
          "Тўрткўл",
          "Хўжайли",
          "Чимбой",
          "Шуманай",
          "Элликқалъа",
        )
      )
    )
    tumanlar.add(
      Pair(
        "Самарқанд", arrayListOf(
          "Самарқанд",
          "Каттақўрғон",
          "Оқдарё",
          "Булунғур",
          "Жомбой",
          "Иштихон",
          "Каттақўрғон",
          "Қўшробод",
          "Нарпай",
          "Пойариқ",
          "Пастдарғом",
          "Пахтачи",
          "Нуробод",
          "Ургут",
          "Тойлоқ",
        )
      )
    )

    tumanlar.add(
      Pair(
        "Андижон", arrayListOf(
          "Андижон ",
          "Хонобод ",
          "Олтинкўл",
          "Балиқчи",
          "Бўстон",
          "Булоқбоши",
          "Жалолқудуқ",
          "Избоскан",
          "Улуғноp",
          "Қўрғонтепа",
          "Асака",
          "Мархамат",
          "Шаҳриҳон",
          "Пахтаобод",
          "Хўжаобод",
        )
      )
    )

    tumanlar.add(
      Pair(
        "Тошкент ш.", arrayListOf(
          "Учтепа",
          "Бектемир",
          "Юнусобод",
          "Бектемир",
          "Мирзо Улуғбек",
          "Миробод",
          "Шайхонтохур",
          "Олмазор",
          "Сирғали",
          "Яккасарой",
          "Яшнобод",
          "Чилонзор",
          "Янгиҳаёт",
        )
      )
    )
    tumanlar.add(
      Pair(
        "Тошкент", arrayListOf(
          "Оққўрғон",
          "Оҳангарон",
          "Бекобод",
          "Бўстонлиқ",
          "Бўка",
          "Қуйичирчиқ",
          "Зангиота",
          "Юқоричирчиқ",
          "Қибрай",
          "Паркент",
          "Пскент",
          "Ўртачирчиқ",
          "Чиноз",
          "Янгийўл",
          "Тошкент",

          )
      )
    )
    tumanlar.add(
      Pair(
        "Қашқадарё", arrayListOf(
          "Қарши ",
          "Шаҳрисабз ",
          "Ғузор",
          "Деҳқонобод",
          "Қамаши",
          "Қарши",
          "Косон",
          "Китоб",
          "Миришкор",
          "Муборак",
          "Нишон",
          "Касби",
          "Чироқчи",
          "Шаҳрисабз",
          "Яккабоғ"
        )
      )
    )
    tumanlar.add(
      Pair(
        "Жиззах", arrayListOf(
          "Жиззах ",
          "туманлар:",
          "Арнасой",
          "Бахмал",
          "Ғаллаорол",
          "Шароф Рашидоф",
          "Дўстлик",
          "Зомин",
          "Зарбдор",
          "Мирзачўл",
          "Зафаробод",
          "Пахтакор",
          "Фориш",
          "Янгиобод",
        )
      )
    )
    tumanlar.add(
      Pair(
        "Хоразм", arrayListOf(
          "Урганч ",
          "Хива ",
          "Боғот",
          "Гурлан",
          "Қўшкўпир",
          "Урганч",
          "Хазорасп",
          "Хонқа",
          "Хива",
          "Шовот",
          "Янгиариқ",
          "Янгибозор",
          "Тупроққалъа",
        )
      )
    )
    tumanlar.add(
      Pair(
        "Фарғона", arrayListOf(
          "Олтиариқ",
          "Қўштепа",
          "Боғдод",
          "Бувайда",
          "Бешариқ",
          "Қува",
          "Учкўприк",
          "Риштон",
          "Сўх",
          "Тошлоқ",
          "Ўзбекистон",
          "Фарғона",
          "Данғара",
          "Фурқат",
          "Ёзёвон"
        )
      )
    )
    tumanlar.add(
      Pair(
        "Сурхондарё", arrayListOf(
          "Олтинсой",
          "Ангор",
          "Бойсун",
          "Музробод",
          "Денов",
          "Жарқўрғон",
          "Қумқўрғон",
          "Қизириқ",
          "Сариосиё",
          "Термиз",
          "Узун",
          "Шеробод",
          "Шўрчи",
          "Бандихон",
        )
      )
    )
    tumanlar.add(
      Pair(
        "Сирдарё", arrayListOf(
          "Оқолтин",
          "Боёвут",
          "Сайхунобод",
          "Гулистон",
          "Сардоба",
          "Мирзаобод",
          "Сирдарё",
          "Ховос",
        )
      )
    )
  }
}