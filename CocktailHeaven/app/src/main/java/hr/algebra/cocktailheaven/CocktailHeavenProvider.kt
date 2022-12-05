package hr.algebra.cocktailheaven

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.cocktailheaven.dao.CocktailHeavenRepository
import hr.algebra.cocktailheaven.dao.getCocktailHeavenRepository
import hr.algebra.cocktailheaven.model.Item
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.cocktailheaven.api.provider"
private const val PATH = "items"

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)){
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

val COCKTAIL_HEAVEN_PROVIDER_URI = Uri.parse("content://$AUTHORITY/$PATH")

class CocktailHeavenProvider : ContentProvider() {

    private lateinit var cocktailHeavenRepository: CocktailHeavenRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
                ITEMS -> return cocktailHeavenRepository.delete(selection, selectionArgs)
                ITEM_ID -> {
                    uri.lastPathSegment?.let {
                        return cocktailHeavenRepository.delete("${Item::_id.name}=?", arrayOf(it))
                    }
                }
        }
        throw IllegalArgumentException("Wrong uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var id = cocktailHeavenRepository.insert(values)
        return ContentUris.withAppendedId(COCKTAIL_HEAVEN_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        cocktailHeavenRepository = getCocktailHeavenRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = cocktailHeavenRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            ITEMS -> return cocktailHeavenRepository.update(values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return cocktailHeavenRepository.update(values, "${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("Wrong uri")
    }
}