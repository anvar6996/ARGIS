package uz.univer.argis

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import uz.univer.argis.databinding.ActivityMainBinding
import uz.univer.argis.domain.MainRepository

class MainActivity : AppCompatActivity() {
  private val repository = MainRepository()
  private lateinit var bind: ActivityMainBinding

  private val listCity = ArrayList<Pair<String, ArrayList<String>>>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    bind = ActivityMainBinding.inflate(layoutInflater)
    setContentView(bind.root)
    loadShaxar()

    val adapterRegions = ArrayAdapter(this, R.layout.item_product_type, repository.listRegions)
    bind.viloyat.setAdapter(adapterRegions)

    bind.viloyat.onItemClickListener =
      AdapterView.OnItemClickListener { parent, view, position, id ->

        val key = repository.listRegions[position]

        val adapterCity = ArrayAdapter(
          this, R.layout.item_product_type, getShaxar(key)
        )
        bind.shaxar.setAdapter(adapterCity)

      }

    val adapter = ArrayAdapter(this, R.layout.item_product_type, repository.listRegions)
//    bind.viloyat.setAdapter(adapterBrand)

  }

  private fun getShaxar(tuman: String): ArrayList<String> {
    var list = ArrayList<String>()
    listCity.forEach {
      if (it.first == tuman) {
        list = it.second
      }
    }
    return list
  }

  fun loadShaxar() {
    listCity.add(
      Pair(
        "Навоий", arrayListOf(
          "Навоий ш.",
          "Заpафшон ш.",
          "Конимех",
          "Қизилтепа",
          "Навбаҳор",
          "Кармана",
          "Нурота",
          "Томди",
          "Учқудуқ",
          "Хатирчи",
          "Ғозғон ш."
        )
      )
    )
    listCity.add(
      Pair(
        "Наманган", arrayListOf(
          "Косонсой",
          "Наманган ш.",
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
    listCity.add(
      Pair(
        "Самарқанд", arrayListOf(
          "Самарқанд ш.",
          "Каттақўрғон ш.",
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

    listCity.add(
      Pair(
        "Андижон", arrayListOf(
          "Андижон ш.",
          "Хонобод ш.",
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

    listCity.add(
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
    listCity.add(
      Pair(
        "Қашқадарё", arrayListOf(
          "Қарши ш.",
          "Шаҳрисабз ш.",
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
    listCity.add(
      Pair(
        "Жиззах", arrayListOf(
          "Жиззах ш.",
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
    listCity.add(
      Pair(
        "Хоразм", arrayListOf(
          "Урганч ш.",
          "Хива ш.",
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
    listCity.add(
      Pair(
        "Хоразм", arrayListOf(
          "Фарғона ш.",
          "Қўқон ш.",
          "Қувасой ш.",
          "Маpғилон ш.",
          "туманлар:",
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
    listCity.add(
      Pair(
        "Сурхондарё", arrayListOf(
          "Термиз ш.",
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
    listCity.add(
      Pair(
        "Сирдарё", arrayListOf(
          "Гулистон ш.",
          "Шиpин ш.",
          "Янгиеp ш .",
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