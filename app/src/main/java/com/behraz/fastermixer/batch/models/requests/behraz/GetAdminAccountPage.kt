package com.behraz.fastermixer.batch.models.requests.behraz

import com.behraz.fastermixer.batch.models.Package
import com.behraz.fastermixer.batch.models.TransactionHistory

data class GetAdminAccountPage(
    val packages: List<Package>,
    val transactions: List<TransactionHistory>,
    val username: String,
    val phone: String,
    val companyName: String,
    val isSMSAlertActive: Boolean
) {
    private var _currentPackage: Package? = null

    val currentPackage
        get(): Package? {
            if (_currentPackage != null) return _currentPackage
            packages.forEach {
                if (it.isApplied) {
                    _currentPackage = it
                    return it
                }
            }
            return null
        }
}