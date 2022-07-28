package com.scorp.casestudy.furkanyurdakul.data.model

data class Person(val id: Int, val fullName: String)
{
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Person

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int
    {
        return id
    }
}