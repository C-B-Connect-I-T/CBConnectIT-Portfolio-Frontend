package cbconnectit.portfolio.web.data.repos

import cbconnectit.portfolio.web.data.NetworkingConfig
import cbconnectit.portfolio.web.data.deleteRequest
import cbconnectit.portfolio.web.data.extensions.toRepoResult
import cbconnectit.portfolio.web.data.getRequest
import cbconnectit.portfolio.web.data.models.NetworkResponse
import cbconnectit.portfolio.web.data.models.RepoResult
import cbconnectit.portfolio.web.data.models.domain.Company
import cbconnectit.portfolio.web.data.models.domain.toCompany
import cbconnectit.portfolio.web.data.models.dto.requests.company.InsertCompany
import cbconnectit.portfolio.web.data.models.dto.requests.company.UpdateCompany
import cbconnectit.portfolio.web.data.models.dto.responses.CompanyDto
import cbconnectit.portfolio.web.data.models.dto.responses.ErrorResponse
import cbconnectit.portfolio.web.data.postRequest
import cbconnectit.portfolio.web.data.putRequest

object CompanyRepo {
    private val companyUrl = "${NetworkingConfig.baseUrl}/api/v1/companies"

    suspend fun getCompanies(): RepoResult<List<Company>> {
        val response: NetworkResponse<List<CompanyDto>, ErrorResponse> = getRequest(companyUrl)

        return response.toRepoResult(
            successMapper = { companyDtos -> companyDtos.map { it.toCompany() } },
            defaultServerErrorMessage = "Server fout bij het ophalen van bedrijven",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van bedrijven"
        )
    }

    suspend fun getCompanyById(id: String): RepoResult<Company> {
        val response: NetworkResponse<CompanyDto, ErrorResponse> = getRequest("$companyUrl/$id")

        return response.toRepoResult(
            successMapper = { companyDto -> companyDto.toCompany() },
            defaultServerErrorMessage = "Server fout bij het ophalen van bedrijf",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het ophalen van bedrijf"
        )
    }

    suspend fun insertCompany(company: InsertCompany): RepoResult<Company> {
        val response: NetworkResponse<CompanyDto, ErrorResponse> = postRequest(resource = companyUrl, body = company)

        return response.toRepoResult(
            successMapper = { companyDto -> companyDto.toCompany() },
            defaultServerErrorMessage = "Server fout bij het aanmaken van bedrijf",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het aanmaken van bedrijf"
        )
    }

    suspend fun updateCompany(id: String, update: UpdateCompany): RepoResult<Company> {
        val response: NetworkResponse<CompanyDto, ErrorResponse> = putRequest(resource = "$companyUrl/$id", body = update)

        return response.toRepoResult(
            successMapper = { companyDto -> companyDto.toCompany() },
            defaultServerErrorMessage = "Server fout bij het bijwerken van bedrijf",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het bijwerken van bedrijf"
        )
    }

    suspend fun deleteCompany(id: String): RepoResult<Unit> {
        val response: NetworkResponse<Unit, ErrorResponse> = deleteRequest("$companyUrl/$id")

        return response.toRepoResult(
            successMapper = { },
            defaultServerErrorMessage = "Server fout bij het verwijderen van bedrijf",
            networkErrorMessage = "Netwerkfout: controleer je internetverbinding",
            unknownErrorMessage = "Onbekende fout bij het verwijderen van bedrijf"
        )
    }
}
