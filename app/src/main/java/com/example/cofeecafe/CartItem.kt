package com.example.cofeecafe

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val resImg: Int,
    val name: String,
    val price: Double,
    var quantity: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resImg)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem = CartItem(parcel)
        override fun newArray(size: Int): Array<CartItem?> = arrayOfNulls(size)
    }
}
