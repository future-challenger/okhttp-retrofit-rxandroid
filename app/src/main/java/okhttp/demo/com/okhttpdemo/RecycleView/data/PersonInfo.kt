package okhttp.demo.com.okhttpdemo.RecycleView.data

import okhttp.demo.com.okhttpdemo.R
import java.util.*

/**
 * Created by uncle_charlie on 27/2/16.
 */
data class PersonInfo(var name: String, var age: String, var photoId: Int) {
    init {
        this.name = name
        this.age = age
        this.photoId = photoId
    }

    companion object {
        fun createDataSource(): ArrayList<PersonInfo> {
            var persons = ArrayList<PersonInfo>()
            persons.add(PersonInfo("Emma Wilson", "23 years old", R.drawable.emma))
            persons.add(PersonInfo("Lavery Maiss", "25 years old", R.drawable.lavery))
            persons.add(PersonInfo("Lillie Watts", "35 years old", R.drawable.lillie))
            return persons
        }
    }

}
