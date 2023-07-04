package com.service.kapai.service

import com.service.kapai.jna.LibWeb3
import com.service.kapai.jna.go.GoString
import com.service.kapai.model.AuthInfoModel
import com.service.kapai.repository.entity.WalletEntity
import com.service.boot.web.security.AuthenticationToken
import com.service.boot.web.security.AuthenticationUser
import com.service.boot.web.security.AuthenticationUserService
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val walletService: WalletService,
    private val jdbcTemplate: JdbcTemplate,
) : AuthenticationUserService() {
    override fun createAuthenticationInfo(user: AuthenticationUser?): Any? {
        return user?.principal
    }

    override fun onAuthentication(token: AuthenticationToken): AuthenticationUser? {
        val session = token.getHttpServletRequest()?.session ?: return null
        val params = token.getAuthParams()
        val wallet = (params.get("wallet") as? String)?.lowercase() ?: return null
        if (session.getAttribute("sign-wallet") != wallet) {
            return null
        }
        val message = params.get("message") as? String ?: return null
        if (message != "login:${session.id}") {
            return null
        }
        val sign = params.get("sign") as? String
        if (LibWeb3.LIB_WEB_3.VerifyingSignedMessage(
                GoString.ByValue.of(wallet),
                GoString.ByValue.of(message),
                GoString.ByValue.of(sign)
            )
        ) {
            var we = walletService.findWallet(wallet)
            if (we == null) {
                we = WalletEntity().also {
                    it.wallet = wallet
                }
                walletService.registerWallet(we)
            }
            val share = params.get("share") as? String
            val isBind = if (share.isNullOrBlank() || share.equals(we.wallet, true)) {
                walletService.findRelation(we.id) != null
            } else {
                walletService.bindRelation(we.id, share.lowercase())
                true
            }
            session.removeAttribute("sign-wallet")
            val map = jdbcTemplate.queryForMap("SELECT `level_desc`, `equity_desc`  FROM `system_config` LIMIT 1")
            return AuthenticationUser(AuthInfoModel(we.id, wallet).also { model ->
                model.isBind = isBind
                map["level_desc"]?.let {
                    model.levelDesc = it as String
                }
                map["equity_desc"]?.let {
                    model.equityDesc = it as String
                }
            }, emptySet(), emptyMap())
        }
        return null
    }

}