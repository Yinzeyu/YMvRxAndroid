import androidx.lifecycle.viewModelScope
import com.yzy.baselibrary.base.BaseLiveData
import com.yzy.baselibrary.base.BaseViewModel
import com.yzy.example.repository.GankRepository
import com.yzy.example.repository.bean.DataResult
import com.yzy.example.repository.bean.GankAndroidBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class DynViewModel :BaseViewModel(){
    private var page = 1
    private var pageSize = 20
    private val ganRepository: GankRepository by lazy { GankRepository() }
    private var articleBean: MutableList<GankAndroidBean> = mutableListOf()
    private val _bannerAndArticleResult: BaseLiveData<BaseUiModel<MutableList<GankAndroidBean>>> = BaseLiveData()
    val uiState: BaseLiveData<BaseUiModel<MutableList<GankAndroidBean>>> get() = _bannerAndArticleResult
    fun getAndroidSuspend( isRefresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            emitArticleUiState(showLoading = articleBean.size <= 0)
            val tempPage = if (isRefresh) 1 else page + 1
            val result = withContext(Dispatchers.IO) { ganRepository.getAndroidSuspend(pageSize, tempPage) }
            if (result is DataResult.Success ){
                if (isRefresh){
                    articleBean.clear()
                }
                articleBean.addAll(result.data)
                emitArticleUiState(
                    showLoading = false,
                    showSuccess =articleBean
                )
                page++
            } else if (result is DataResult.Error){
//                emitArticleUiState(
//                    showLoading = false,
//                    showSuccess = BannerAndArticleBean(exception = result.exception)
//                )
            }

        }
    }

    private fun emitArticleUiState(showLoading: Boolean = false, showSuccess: MutableList<GankAndroidBean>? = null) {
        val uiModel = BaseUiModel(showLoading ,showSuccess)
        _bannerAndArticleResult.update(uiModel)
    }
}