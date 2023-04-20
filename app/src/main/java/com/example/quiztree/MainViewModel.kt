package com.example.quiztree

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztree.data.local.QuizEntity
import com.example.quiztree.data.repository.QuizRepository
import com.example.quiztree.utils.DataResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {
    val nameInput = MutableLiveData<String>()

    val quizMLD = MediatorLiveData<DataResource<List<QuizEntity>>>()
    fun fetchQuizList() {
        quizMLD.postValue(DataResource.Loading)
        viewModelScope.launch {
            quizRepository.fetchQuizList().apply {
                quizMLD.postValue(this)
            }
        }
    }

    val quizFlow = quizRepository.quizListFlow()

    val liveScoreLD = MutableLiveData(0)
}