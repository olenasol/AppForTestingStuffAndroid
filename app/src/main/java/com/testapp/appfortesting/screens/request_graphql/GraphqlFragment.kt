package com.testapp.appfortesting.screens.request_graphql


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.lnu.integrationrecipe.RecipeAdapter
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.fragment_graphql.*
import okhttp3.OkHttpClient


class GraphqlFragment : Fragment() {

    private lateinit var client: ApolloClient


    companion object {
        const val BASE_URL = "http://1ce1dddb.ngrok.io/graphql"

        fun newInstance():GraphqlFragment{
            return GraphqlFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graphql, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecipesFromBE()
    }

    fun getRecipesFromBE(){
        client = setupApollo()
        client.query(RecipesQuery
            .builder()
            .build())
            .enqueue(object : ApolloCall.Callback<RecipesQuery.Data>() {

                override fun onFailure(e: ApolloException) {
                    Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
                }

                override fun onResponse(response: Response<RecipesQuery.Data>) {
                    activity?.runOnUiThread {
                        onRequestCompleted(response.data()?.recipes())
                    }
                }

            })
    }

    fun onRequestCompleted(list: List<RecipesQuery.Recipe>?) {
        recipesRecyclerView.layoutManager = LinearLayoutManager(context)
        recipesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context!!,
                DividerItemDecoration.VERTICAL
            )
        )
        if (list != null)
            recipesRecyclerView.adapter = RecipeAdapter(list, context!!)
    }
    private fun setupApollo(): ApolloClient {
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(),
                    original.body())
                chain.proceed(builder.build())
            }
            .build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp)
            .build()
    }
}
