package plm.patientslikeme2.utils.callback

interface CallBackListener<T> {
    fun onClick(data: T, type: String)
}