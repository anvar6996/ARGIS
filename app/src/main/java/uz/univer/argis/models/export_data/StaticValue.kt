package uz.univer.argis.models.export_data

import uz.univer.argis.models.EstateData
import uz.univer.argis.models.LandPlotData
import uz.univer.argis.models.PlaceDate

object StaticValue {
  var placeDate: PlaceDate? = null
  var landPlotData: LandPlotData? = null
  var estateData: EstateData? = null
  val placeDataFildsName = arrayOf<String>(
    "Viloyat",
    "Shaxar",
    "Tuman",
    "Qishloq",
    "MFY/SHFY",
    "Yer va mulk foydalanuvchisi",
    "Yer toifasi",
  )
  val DataFildsName = arrayOf<String>(
    "Kadstr raqami",
    "Obyektning nomi",
    "Obyektning manzili",
    "Xuquq turi",
    "Davlat ruyxatiga olingan sana",
    "Maydon",
    "Maqsad vazifasi",
    "Soliq zonasi",
    "Qiymati (so'm)"
  )
}