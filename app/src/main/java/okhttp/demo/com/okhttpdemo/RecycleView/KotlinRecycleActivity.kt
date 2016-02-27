package okhttp.demo.com.okhttpdemo.RecycleView

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import okhttp.demo.com.okhttpdemo.R
import okhttp.demo.com.okhttpdemo.RecycleView.data.PersonInfo
import java.util.*

class KotlinRecycleActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_recycle)
        val recyclerView = findViewById(R.id.my_recycler_view) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this@KotlinRecycleActivity)
        recyclerView.adapter = RVAdapter(PersonInfo.createDataSource())
    }

    inner class RVAdapter(val persons: ArrayList<PersonInfo>) : RecyclerView.Adapter<RVAdapter.PersonViewHolder>() {

        override fun onBindViewHolder(viewHolder: PersonViewHolder?, position: Int) {
            var person = persons[position]
            viewHolder?.personName?.text = person.name
            viewHolder?.personAge?.text = person.age
            viewHolder?.personPhoto?.setImageResource(person.photoId)
            //            var drawable = resources.getDrawable(person.photoId, this@KotlinRecycleActivity.theme)
            //            viewHolder?.personPhoto?.setImageDrawable(drawable)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup?, p1: Int): PersonViewHolder? {
            var v: View = LayoutInflater.from(viewGroup?.context)
                    .inflate(R.layout.recyeler_card_view, viewGroup, false)
            return PersonViewHolder(v)
        }

        override fun getItemCount(): Int {
            return this.persons.size
        }

        inner class PersonViewHolder(var itemCardView: View?) : RecyclerView.ViewHolder(itemCardView) {
            var cardView: CardView? = null
            var personName: TextView? = null
            var personAge: TextView? = null
            var personPhoto: ImageView? = null

            init {
                cardView = itemCardView?.findViewById(R.id.cv) as CardView
                personName = itemCardView?.findViewById(R.id.person_name) as TextView
                personAge = itemCardView?.findViewById(R.id.person_age) as TextView
                personPhoto = itemCardView?.findViewById(R.id.person_photo) as ImageView
            }
        }
    }
}
