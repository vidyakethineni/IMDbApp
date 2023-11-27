package com.example.project8

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.project8.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val OMDB_BASE_URL = "https://www.omdbapi.com/"
private const val OMDB_API_KEY = "e3769182"
/**
 * The main activity for the movie search application.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MoviesAdapter
    private val omdbMovieList:ArrayList<OmdbMovie> = ArrayList()
    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
         adapter = MoviesAdapter(onShareClicked = {shareMovieDetails(this,it)},
             onImageClicked = {openImdbPage(this,it.imdbID?:return@MoviesAdapter)})
        binding.recyclerViewMovies.adapter = adapter
        binding.buttonSearch.setOnClickListener {
            val item = binding.editTextMovieTitle.text.toString()
            if(item.isNotBlank()){
                searchMovies(item)
            }else{
                Toast.makeText(this, "Enter valid input!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    /**
     * Initializes the contents of the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Called whenever an item in your options menu is selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbarFeedback -> {
                sendFeedbackEmail()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Initiates sending a feedback email.
     */
    private fun sendFeedbackEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:imvidyakethineni@gmail.com")
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your feedback goes here.")

        try {
            startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Searches for movies based on the provided title.
     */
    private fun searchMovies(item:String) {
        val retrofit =
            Retrofit.Builder().baseUrl(OMDB_BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val yelpService = retrofit.create(OmdbService::class.java)
        yelpService.searchMovies(item, OMDB_API_KEY).enqueue(object :
            Callback<OmdbSearchResult> {
            override fun onResponse(call: Call<OmdbSearchResult>, response: Response<OmdbSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null) {
                    Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                    return
                }
                Log.i(TAG,"Result: $body")
                omdbMovieList.clear()
                omdbMovieList.addAll(body.Search)
                val detailedMovieList = ArrayList<OmdbMovie>()
                lifecycleScope.launch {
                    try {
                        for (exitingMovie in omdbMovieList){
                            val detailedMovie = yelpService.searchDetailedMovies(exitingMovie.imdbID?:continue,
                                OMDB_API_KEY)
                            detailedMovieList.add(detailedMovie)
                        }
                        adapter.submitList(detailedMovieList)
                    }catch (e:Exception){
                        Log.i(TAG, "onFailure exception ${e.localizedMessage}")
                    }
                }

            }

            override fun onFailure(call: Call<OmdbSearchResult>, t: Throwable) {
                Log.i(TAG, "onFailure $t")
            }
        })
    }
    /**
     * Opens the IMDb page for the specified movie.
     */
            private fun openImdbPage(context: Context, imdbId: String) {
            val imdbUrl = "https://www.imdb.com/title/$imdbId/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
            context.startActivity(intent)
        }
    /**
     * Shares details about the specified movie using an implicit intent.
     */
        private fun shareMovieDetails(context: Context, movie: OmdbMovie) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, movie.Title)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out ${movie.Title} on IMDb: https://www.imdb.com/title/${movie.imdbID}/")
            context.startActivity(Intent.createChooser(shareIntent, "Share Movie Details"))
        }

    companion object{
         val TAG = MainActivity::class.java.simpleName
    }
}
