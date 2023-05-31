package com.example.travelassistant

data class Location(var sira:Any?=null,var not: String? = null, var latitude: Any? = null, var longitude : Any?=null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}
