package com.behraz.fastermixer.batch.utils.fastermixer

import org.osmdroid.util.GeoPoint

object Constants {

    const val MIXER_MISSION_MAX_SECONDS_FOR_NORMAL_STATE = 10 * 60
    const val MIXER_MISSION_MAX_SECONDS_FOR_DANGER_STATE = 20 * 60

    val MAPBOX_ACCESS_TOKEN by lazy { "pk.eyJ1IjoiYW1pcm1wIiwiYSI6ImNrZmt1eDAzbzA3YW8ycm10MnBnZmx3eDYifQ.Q-uGHGQ2TV_R3rYu6REtAA" }

    val mapStartPoint by lazy { GeoPoint(31.891413345001638, 54.35357135720551) }

    const val VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_MOVING: Byte = 5
    const val VALID_DURATION_TIME_FOR_LAST_DATA_WHEN_VEHICLE_STOP: Byte = 12

    const val ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP_MIXER_ID = "miasdf"
    const val ACTION_POMP_MAP_FRAGMENT_LOCATE_MIXER_ON_MAP = "pomp-map-locate-mixer"
    private val INTENT_CHOOSE_EQ_USER by lazy { "user" }
    private val packageName by lazy { "com.behraz.fastermixer.batch" }
    val LARGE_TEST_TEXT by lazy { "لورم ایپسوم یا طرح\u200Cنما (به انگلیسی: Lorem ipsum) به متنی آزمایشی و بی\u200Cمعنی در صنعت چاپ، صفحه\u200Cآرایی و طراحی گرافیک گفته می\u200Cشود. طراح گرافیک از این متن به عنوان عنصری از ترکیب بندی برای پر کردن صفحه و ارایه اولیه شکل ظاهری و کلی طرح سفارش گرفته شده استفاده می نماید، تا از نظر گرافیکی نشانگر چگونگی نوع و اندازه فونت و ظاهر متن باشد. معمولا طراحان گرافیک برای صفحه\u200Cآرایی، نخست از متن\u200Cهای آزمایشی و بی\u200Cمعنی استفاده می\u200Cکنند تا صرفا به مشتری یا صاحب کار خود نشان دهند که صفحه طراحی یا صفحه بندی شده بعد از اینکه متن در آن قرار گیرد چگونه به نظر می\u200Cرسد و قلم\u200Cها و اندازه\u200Cبندی\u200Cها چگونه در نظر گرفته شده\u200Cاست. از آنجایی که طراحان عموما نویسنده متن نیستند و وظیفه رعایت حق تکثیر متون را ندارند و در همان حال کار آنها به نوعی وابسته به متن می\u200Cباشد آنها با استفاده از محتویات ساختگی، صفحه گرافیکی خود را صفحه\u200Cآرایی می\u200Cکنند تا مرحله طراحی و صفحه\u200Cبندی را به پایان برند." }

    val SERVER_ERROR by lazy { "خطا در برقراری ارتباط با سرور" }

    val UNAUTHORIZED_MSG by lazy { "حساب کاربری شما در دستگاه دیگری فعال است. لطفا دوباره وارد شوید" }

    const val PREF_CREDENTIAL_NAME = "credential"

    const val PREF_CREDENTIAL_USER = "username"
    const val PREF_CREDENTIAL_PASSWORD = "password"
    const val PREF_CREDENTIAL_FACTORY_ID = "factoryId"
    const val PREF_CREDENTIAL_REMEMBERED = "remembered"

    const val INTENT_UPDATE_ACTIVITY_UPDATE_RESPONSE = "ac-up-obj"
}