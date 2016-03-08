package okhttp.demo.com.okhttpdemo.DebugDemo

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import okhttp.demo.com.okhttpdemo.R

class URLListActivity() : Activity() {
    private val TAG: String = URLListActivity::class.java.simpleName

    private var urlList: MutableList<String>? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_urllist)

        urlList = URLPreference.getURLList(this@URLListActivity)
        if (urlList == null)
            urlList = mutableListOf<String>()

        recyclerView = findViewById(R.id.url_list_recycler_view) as RecyclerView
        recyclerView?.layoutManager = LinearLayoutManager(this@URLListActivity)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = URLAdaper(urlList?.toMutableList())

        actionBar.title = "选择URL"
        //        actionBar.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when (item?.itemId) {
            R.id.action_add -> {
                var inflater = LayoutInflater.from(this@URLListActivity)
                var promptView = inflater.inflate(R.layout.view_prompt_input, null)

                var alertDialogBuilder = AlertDialog.Builder(this@URLListActivity)
                alertDialogBuilder.setView(promptView)

                val editText = promptView.findViewById(R.id.url_edit_text) as EditText
                //                editText.setText("https://", TextView.BufferType.EDITABLE)
                alertDialogBuilder.setCancelable(true).setPositiveButton("确定") { dialog, which ->
                    var urlInput: String = editText.text.toString()
                    Log.d(TAG, "input url: $urlInput")

                    var urlList = this@URLListActivity.urlList
                    if (urlList?.contains(urlInput) == false) {
                        urlList?.add(urlInput)
                        URLPreference.putURLList(this@URLListActivity, urlList)

                        recyclerView?.adapter = URLAdaper(urlList)
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }

                }.setNegativeButton("取消") { dialog, which ->
                    dialog.cancel()
                };

                alertDialogBuilder.create().show()
            }
        }

        return true
    }

    internal class URLAdaper(urlList: MutableList<String>?) : RecyclerView.Adapter<URLAdaper.URLViewHolder>() {
        private var urlList: MutableList<String>? = urlList

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): URLViewHolder? {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.url_list_item, parent, false)
            return URLViewHolder(view, hl = object : URLViewHolder.OnViewHolderClickListener {
                override fun onHoderClick(view: View?) {
                    var text: String = ""
                    if (view is TextView) {
                        text = view.text.toString()
                    }
                    //                    Toast.makeText(parent.context, "click " + text, Toast.LENGTH_SHORT).show()
                    if (view != null) {
                        var height = view!!.height
                        var width = view!!.width

                        var array = floatArrayOf(1.0f, 1.5f)
                        var clickAnim = ObjectAnimator.ofFloat(view, "scaleX", *array)
                        clickAnim.start()
                    }
                }
            })
        }

        override fun onBindViewHolder(holder: URLViewHolder, position: Int) {
            var urlString = urlList?.get(position)
            holder.urlTextView.text = urlString
        }

        override fun getItemCount(): Int {
            return urlList?.size ?: 0
        }

        internal class URLViewHolder(itemView: View, hl: OnViewHolderClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            var holderListener: OnViewHolderClickListener? = null
            var urlTextView: TextView
            //            var descriptionTextView: TextView

            init {
                urlTextView = itemView.findViewById(R.id.url_text_view) as TextView
                urlTextView.setOnClickListener(this)
                this.holderListener = hl
            }

            override fun onClick(view: View?) {
                if (holderListener != null) {
                    holderListener?.onHoderClick(view)
                }
            }

            interface OnViewHolderClickListener {
                fun onHoderClick(view: View?)
            }
        }
    }

    class URLPreference(val context: Context) {
        companion object {
            val PREF_NAME = "ENUMERATOR_PREFERENCES"
            val MODE = Context.MODE_PRIVATE

            fun getURLList(context: Context): MutableList<String>? {
                var urlSet: Set<String>? = getPreferences(context).getStringSet(URL_LIST_KEY, null)
                return urlSet?.toMutableList()
            }

            fun putURLList(context: Context, listVal: List<String>?): Boolean {
                if (listVal == null) {
                    return false
                }

                try {
                    var editor = getEditor(context);
                    editor.putStringSet(URL_LIST_KEY, listVal?.toSet())
                    editor.commit()

                    return true
                } catch(e: Exception) {
                    return false
                }
            }

            fun getPreferences(context: Context): SharedPreferences {
                return context.getSharedPreferences(PREF_NAME, MODE)
            }

            fun getEditor(context: Context): SharedPreferences.Editor {
                return getPreferences(context).edit()

            }
        }
    }

    override fun onBackPressed() {

        super.onBackPressed()
//        var i = Intent(this@URLListActivity, DebugDemoActivity::class.java)
//        startActivity(i)

        finish()
        overridePendingTransition(R.anim.still_anim, R.anim.exit_anim)
    }

    companion object {
        private val URL_LIST_KEY = "url_list_key"
    }
}

fun <T> convertSetToList(setVal: Set<T>): MutableList<T> {
    return setVal.toMutableList()
}

fun <T> convertListToSet(listVal: List<T>): Set<T> {
    return java.util.HashSet<T>(listVal)
}