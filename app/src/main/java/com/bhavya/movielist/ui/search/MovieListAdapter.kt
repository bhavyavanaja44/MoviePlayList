package com.bhavya.movielist.ui.search


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bhavya.movielist.R
import com.bhavya.movielist.data.model.Movie
import com.bhavya.movielist.util.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MovieListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING= 1
    }

    private var movieList = ArrayList<Movie.SearchItem?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie_item, parent, false)
            MovieViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.progress, parent, false)
            LoadingViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MovieViewHolder) {
            if (movieList[position] != null) {
                holder.bindItems(movieList[position]!!)

            }
        } else if (holder is LoadingViewHolder) {
            holder.showProgress()
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (movieList[position] == null) TYPE_LOADING else TYPE_ITEM
    }

    fun setData(newMoviesList: ArrayList<Movie.SearchItem?>?) {
        if (newMoviesList != null) {
            if (movieList.isNotEmpty())
                movieList.removeAt(movieList.size - 1)
            movieList.clear()
            movieList.addAll(newMoviesList)
        } else {
            movieList.add(newMoviesList)
        }
        notifyDataSetChanged()
    }

    private fun getMovieList() = movieList

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imagePoster: ImageView = itemView.findViewById(R.id.image_poster)
        private val textTitle: TextView = itemView.findViewById(R.id.text_title)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

        fun bindItems(movie: Movie.SearchItem) {
            when {
                movie.isSelected() -> {
                    checkBox.visibility =  View.VISIBLE }
                else -> {
                    checkBox.visibility = View.GONE
                }
            }
            textTitle.text = movie.title
            Glide.with(imagePoster.context).load(movie.poster)
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePoster)

            itemView.setOnClickListener {
                movie.setSelected(!movie.isSelected())
                when {
                    movie.isSelected() -> {
                        checkBox.visibility =  View.VISIBLE }
                    else -> {
                        checkBox.visibility = View.GONE
                    }
                }
        }

        }

    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        fun showProgress() {
            progressBar.show()
        }
    }

    fun getSelected(): ArrayList<Movie.SearchItem?>? {
        val selected: ArrayList<Movie.SearchItem?> = ArrayList()
        for (i in 0 until getMovieList().size) {
            if (getMovieList()[i]?.isSelected()!!) {
                selected.add(getMovieList()[i])
            }
        }
        return selected
    }

}