package hr.algebra.cocktailheaven.dao

import android.content.Context

fun getCocktailHeavenRepository(context: Context?) = CocktailHeavenSqlHelper(context)