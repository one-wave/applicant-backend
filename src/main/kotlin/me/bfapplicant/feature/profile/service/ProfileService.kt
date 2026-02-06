package me.bfapplicant.feature.profile.service

import me.bfapplicant.domain.repository.ApplicantUserInfoRepository
import me.bfapplicant.domain.repository.ApplicantUserRepository
import me.bfapplicant.feature.profile.dto.ChangePasswordRequest
import me.bfapplicant.feature.profile.dto.ProfileRequest
import me.bfapplicant.feature.profile.dto.ProfileResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ProfileService(
    private val userRepository: ApplicantUserRepository,
    private val userInfoRepository: ApplicantUserInfoRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun getProfile(userId: UUID): ProfileResponse {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        val info = userInfoRepository.findByUserUserId(userId)
            ?: throw IllegalArgumentException("Profile not found")
        return ProfileResponse.from(user, info)
    }

    @Transactional
    fun updateProfile(userId: UUID, req: ProfileRequest): ProfileResponse {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        val info = userInfoRepository.findByUserUserId(userId)
            ?: throw IllegalArgumentException("Profile not found")

        applyChanges(info, req)
        return ProfileResponse.from(user, info)
    }

    @Transactional
    fun changePassword(userId: UUID, req: ChangePasswordRequest) {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found")
        }
        require(passwordEncoder.matches(req.currentPassword, user.password)) {
            "Current password is incorrect"
        }
        user.password = passwordEncoder.encode(req.newPassword)!!
    }

    private fun applyChanges(info: me.bfapplicant.domain.entity.ApplicantUserInfo, req: ProfileRequest) {
        info.userPhone = req.userPhone
        info.birthDate = req.birthDate
        info.envBothHands = req.envBothHands
        info.envEyeSight = req.envEyeSight
        info.envHandWork = req.envHandWork
        info.envLiftPower = req.envLiftPower
        info.envLstnTalk = req.envLstnTalk
        info.envStndWalk = req.envStndWalk
    }
}
