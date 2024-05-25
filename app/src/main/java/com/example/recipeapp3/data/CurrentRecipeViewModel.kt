package com.example.recipeapp3.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.recipeapp3.RecipeApplication
import com.example.recipeapp3.data.model.RecipeDTO
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface CurrentRecipeUiState {
    data class Success(val recipeDTO: List<RecipeDTO>): CurrentRecipeUiState
    object Error : RecipeDetailsUiState
    object Loading : RecipeDetailsUiState
}

class CurrentRecipeViewModel (
    private val recipeRepository: RecipeRepository
): ViewModel(){

    var recipeDetailsUiState: RecipeDetailsUiState by mutableStateOf(RecipeDetailsUiState.Loading)
        private set

    init {
        getRecipeDetails("52772")
    }

    fun getRecipeDetails(query: String){
        viewModelScope.launch {
            recipeDetailsUiState = RecipeDetailsUiState.Loading
            recipeDetailsUiState = try {
                RecipeDetailsUiState.Success(recipeRepository.getRecipeDetails(query))
            }catch (e: IOException){
                RecipeDetailsUiState.Error
            }

        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RecipeApplication)
                val recipeRepository = application.container.recipeRepository
                RecipeDetailsViewModel(recipeRepository = recipeRepository)
            }
        }
    }


}