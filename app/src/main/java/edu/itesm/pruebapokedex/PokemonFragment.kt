package edu.itesm.pruebapokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_pokemon.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class PokemonFragment : Fragment() {

    private val BASE_URL = "https://pokeapi.co/api/v2/"
    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnPokemon.setOnClickListener {
            getPokemon(Random.nextInt(1,100))
        }

        fixedRateTimer("CambioPokemon",false,0,30000){
            activity?.runOnUiThread {
                getPokemon(Random.nextInt(1,100))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pokemon, container, false)
    }

    private fun getRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPokemon(id : Int){

        CoroutineScope(Dispatchers.IO).launch {

            val callToService = getRetrofit().create(PokemonService::class.java)
            val responseFromService = callToService.getPokemon(id)

            pokemon = responseFromService.body() as Pokemon

            activity?.runOnUiThread {

                if(responseFromService.isSuccessful){

                    //Log.i("Pokemon 1", pokemon.forms[0].name)
                    //Log.i("Pokemon 2", pokemon.sprites.front_default)

                    tvPokemon.text = pokemon.forms[0].name
                    Glide.with(this@PokemonFragment)
                        .load(pokemon.sprites.front_default)
                        .override(500,500)
                        .centerCrop()
                        .error(R.drawable.ic_launcher_background)
                        .into(ivPokemon)

                } else {
                    Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}