package us.smt.mylearningcentre.util


fun String.toMoneyType():String{
    var st=""
    for ((j, i) in (length-1 downTo 0).withIndex()){
        if (j%3==0){
            st= " $st"
        }
        st=get(i).toString()+st
    }
    return st.trim()
}