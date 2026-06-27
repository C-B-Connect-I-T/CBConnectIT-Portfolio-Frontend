package cbconnectit.portfolio.web.admin.pages.companies

import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.utils.MVI

interface CompaniesContract : MVI<CompaniesContract.State, CompaniesContract.Intent, CompaniesContract.Effect> {

    data class State(
        val companies: List<Company> = emptyList(),
        val isLoading: Boolean = false
    )

    sealed class Intent {
        data object LoadCompanies : Intent()
        data class NavigateToManage(val companyId: String? = null) : Intent()
    }

    sealed class Effect {
        data class NavigateToManage(val companyId: String? = null) : Effect()
    }
}
