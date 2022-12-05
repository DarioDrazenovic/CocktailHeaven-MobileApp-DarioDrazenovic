package hr.algebra.cocktailheaven.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.cocktailheaven.model.Item

private const val DB_NAME = "items.db"
private const val DB_VERSION = 3
private const val ITEMS = "items"

private val CREATE = "create table $ITEMS(" +
        "${Item::_id.name} integer primary key autoincrement, " +
        "${Item::id.name} text not null, " +
        "${Item::name.name} text not null, " +
        "${Item::brewery_type.name} text not null, " +
        "${Item::city.name} text not null, " +
        "${Item::state.name} text not null, " +
        "${Item::postal_code.name} text not null, " +
        "${Item::country.name} text not null, " +
        "${Item::updated_at.name} text not null, " +
        "${Item::created_at.name} text not null, " +
        "${Item::read.name} integer not null" +
        ")"
private const val DROP = "drop table $ITEMS"

class CocktailHeavenSqlHelper(
    context: Context?)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), CocktailHeavenRepository {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?): Int
    = writableDatabase.delete(ITEMS, selection, selectionArgs)

    override fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ) = writableDatabase.update(ITEMS, values, selection, selectionArgs)

    override fun insert(values: ContentValues?)
    = writableDatabase.insert(ITEMS, null, values)

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor?
        = readableDatabase.query(
            ITEMS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
            )
}