package co.lnu.integrationrecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import com.testapp.appfortesting.R
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeAdapter(val list:List<RecipesQuery.Recipe>, val  context: Context):RecyclerView.Adapter<RecipeViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Ion.with(context)
            .load(list[position].imageLink())
            .intoImageView(holder.ivRecipeImage)
        holder.txtRecipeTitle.text = list[position].name()
    }

}

class RecipeViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val txtRecipeTitle = view.recipeTitle
    val ivRecipeImage = view.recipeImage
}