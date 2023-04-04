package uz.univer.argis

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import uz.univer.argis.databinding.ActivityMainBinding
import uz.univer.argis.domain.MainRepository

class MainActivity : AppCompatActivity() {
  private val repository = MainRepository()
  private lateinit var bind: ActivityMainBinding

  //  private val viloyatlar = ArrayList<Pair<String, ArrayList<String>>>()
  private val shaxarlar = ArrayList<Pair<String, ArrayList<String>>>()
  private val tumanlar = ArrayList<Pair<String, ArrayList<String>>>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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
      startActivity(Intent(this, LandPlotDataActivity::class.java))

    }
    bind.btnBack.setOnClickListener {
      finish()
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