package com.example.androidkaraokeapp.ulti

class HandleString {
    fun removeVietnameseUnicodeSymbol (str: String): String {
//        var temp = str.toLowerCase().trim();
//        Regex(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g)
//        str.replace(,"a");
        var temp = str.toLowerCase().trim()
        temp = temp.replace(Regex("[à,á,ạ,ả,ã,â,ầ,ấ,ậ,ẩ,ẫ,ă,ằ,ắ,ặ,ẳ,ẵ]"), "a")
        temp = temp.replace(Regex("[è,é,ẹ,ẻ,ẽ,ê,ề,ế,ệ,ể,ễ]"), "e")
        temp = temp.replace(Regex("[ì,í,ị,ỉ,ĩ]"), "i")
        temp = temp.replace(Regex("[ò,ó,ọ,ỏ,õ,ô,ồ,ố,ộ,ổ,ỗ,ơ,ờ,ớ,ợ,ở,ỡ]"), "o")
        temp = temp.replace(Regex("[ù,ú,ụ,ủ,ũ,ư,ừ,ứ,ự,ử,ữ]"), "u")
        temp = temp.replace(Regex("[ỳ,ý,ỵ,ỷ,ỹ]"), "y")
        temp = temp.replace(Regex("đ"), "d")
        temp = temp.replace(Regex("ừ"), "u")
        return temp
    }
}

